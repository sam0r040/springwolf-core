// SPDX-License-Identifier: Apache-2.0
package io.github.springwolf.plugins.jms.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.springwolf.asyncapi.v3.model.schema.SchemaObject;
import io.github.springwolf.core.asyncapi.components.ComponentsService;
import io.github.springwolf.core.asyncapi.components.DefaultComponentsService;
import io.github.springwolf.core.asyncapi.components.examples.SchemaWalkerProvider;
import io.github.springwolf.core.asyncapi.components.examples.walkers.DefaultSchemaWalker;
import io.github.springwolf.core.asyncapi.components.examples.walkers.json.ExampleJsonValueGenerator;
import io.github.springwolf.core.asyncapi.scanners.common.payload.internal.PayloadExtractor;
import io.github.springwolf.core.asyncapi.scanners.common.payload.internal.PayloadService;
import io.github.springwolf.core.asyncapi.scanners.common.payload.internal.TypeExtractor;
import io.github.springwolf.core.asyncapi.schemas.SwaggerSchemaService;
import io.github.springwolf.core.asyncapi.schemas.SwaggerSchemaUtil;
import io.github.springwolf.core.configuration.properties.SpringwolfConfigProperties;
import io.github.springwolf.core.controller.PublishingPayloadCreator;
import io.github.springwolf.core.controller.dtos.MessageDto;
import io.github.springwolf.plugins.jms.producer.SpringwolfJmsProducer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SpringwolfJmsController.class)
@ContextConfiguration(
        classes = {
            SpringwolfJmsController.class,
            PublishingPayloadCreator.class,
            SpringwolfJmsProducer.class,
            PayloadExtractor.class,
            TypeExtractor.class,
            DefaultComponentsService.class,
            SwaggerSchemaService.class,
            PayloadService.class,
            SwaggerSchemaUtil.class,
            DefaultSchemaWalker.class,
            SchemaWalkerProvider.class,
            ExampleJsonValueGenerator.class,
            SpringwolfConfigProperties.class,
        })
@TestPropertySource(
        properties = {
            "springwolf.docket.base-package=io.github.springwolf.plugins.jms",
            "springwolf.docket.info.title=Title",
            "springwolf.docket.info.version=1.0",
            "springwolf.docket.servers.jms.protocol=jms",
            "springwolf.docket.servers.jms.host=127.0.0.1",
            "springwolf.plugin.jms.publishing.enabled=true",
        })
class SpringwolfJmsControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ComponentsService componentsService;

    @MockitoBean
    private SpringwolfJmsProducer springwolfJmsProducer;

    @Captor
    private ArgumentCaptor<PayloadDto> payloadCaptor;

    @Captor
    private ArgumentCaptor<Map<String, MessageDto.HeaderValue>> headerCaptor;

    @BeforeEach
    void setup() {
        when(springwolfJmsProducer.isEnabled()).thenReturn(true);

        componentsService.registerSchema(SchemaObject.builder()
                .title(PayloadDto.class.getName())
                .properties(new HashMap<>())
                .build());
    }

    @Nested
    class CanPublish {
        @Test
        void testControllerShouldReturnOkWhenPublishingIsEnabled() throws Exception {
            mvc.perform(get("/springwolf/jms/publish").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        void testControllerShouldReturnNotFoundWhenPublishingIsDisabled() throws Exception {
            when(springwolfJmsProducer.isEnabled()).thenReturn(false);

            mvc.perform(get("/springwolf/jms/publish").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError());
        }
    }

    @Test
    void testControllerShouldReturnBadRequestIfPayloadIsEmpty() throws Exception {
        String content =
                """
                            {
                              "bindings": null,
                              "headers": null,
                              "payload": ""
                            }""";
        mvc.perform(post("/springwolf/jms/publish?topic=test-topic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is2xxSuccessful());

        verify(springwolfJmsProducer).send(eq("test-topic"), isNull(), eq(null));
    }

    @Test
    void testControllerShouldAcceptIfPayloadIsNotSet() throws Exception {
        String content =
                """
                            {
                              "bindings": null,
                              "headers": null
                            }""";
        mvc.perform(post("/springwolf/jms/publish?topic=test-topic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is2xxSuccessful());

        verify(springwolfJmsProducer).send(eq("test-topic"), isNull(), eq(null));
    }

    @Test
    void testControllerShouldReturnNotFoundIfNoJmsProducerIsEnabled() throws Exception {
        when(springwolfJmsProducer.isEnabled()).thenReturn(false);

        String content =
                """
                        {
                          "bindings": null,
                          "headers": null,
                          "payload": "{ \\"some-payload-key\\" : \\"some-payload-value\\" }"
                        }""";
        mvc.perform(post("/springwolf/jms/publish?topic=test-topic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isNotFound());
    }

    @Test
    void testControllerShouldCallJmsProducerIfOnlyPayloadIsSend() throws Exception {
        when(springwolfJmsProducer.isEnabled()).thenReturn(true);

        String content =
                """
                        {
                          "bindings": null,
                          "headers": null,
                          "payload": "{ \\"some-payload-key\\" : \\"some-payload-value\\" }",
                          "payloadType": "io.github.springwolf.plugins.jms.controller.SpringwolfJmsControllerIntegrationTest$PayloadDto"
                        }""";

        mvc.perform(post("/springwolf/jms/publish")
                        .param("topic", "test-topic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());

        verify(springwolfJmsProducer).send(eq("test-topic"), isNull(), payloadCaptor.capture());

        assertThat(payloadCaptor.getValue()).isEqualTo(new PayloadDto("some-payload-value"));
    }

    @Test
    void testControllerShouldCallJmsProducerIfPayloadAndHeadersAreSend() throws Exception {
        when(springwolfJmsProducer.isEnabled()).thenReturn(true);

        String content =
                """
                        {
                          "bindings": null,
                          "headers": {
                            "some-header-key": "some-header-value"
                          },
                          "payload": "{ \\"some-payload-key\\" : \\"some-payload-value\\" }",
                          "payloadType": "io.github.springwolf.plugins.jms.controller.SpringwolfJmsControllerIntegrationTest$PayloadDto"
                        }
                        """;

        mvc.perform(post("/springwolf/jms/publish?topic=test-topic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());

        verify(springwolfJmsProducer).send(eq("test-topic"), headerCaptor.capture(), payloadCaptor.capture());

        assertThat(headerCaptor.getValue())
                .isEqualTo(singletonMap("some-header-key", new MessageDto.HeaderValue("some-header-value")));
        assertThat(payloadCaptor.getValue()).isEqualTo(new PayloadDto("some-payload-value"));
    }

    @Data
    @AllArgsConstructor
    @Jacksonized
    @Builder
    public static class PayloadDto {
        @JsonProperty("some-payload-key")
        private String field;
    }
}
