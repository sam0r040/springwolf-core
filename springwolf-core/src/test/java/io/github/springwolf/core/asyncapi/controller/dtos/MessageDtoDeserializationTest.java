// SPDX-License-Identifier: Apache-2.0
package io.github.springwolf.core.asyncapi.controller.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.springwolf.core.controller.dtos.MessageDto;
import io.swagger.v3.core.util.Json;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

class MessageDtoDeserializationTest {
    private static final ObjectMapper objectMapper = Json.mapper();

    @Test
    void testCanBeSerialized() throws IOException, ClassNotFoundException {
        String content = "{" + "\"headers\": { \"some-header-key\" : \"some-header-value\" }, "
                + "\"payload\": \"{\\\"some-payload-key\\\":\\\"some-payload-value\\\"}\", "
                + "\"payloadType\": \""
                + MessageDto.class.getCanonicalName() + "\"" + "}";

        MessageDto value = objectMapper.readValue(content, MessageDto.class);

        assertThat(value).isNotNull();
        assertThat(value.getHeaders())
                .isEqualTo(singletonMap("some-header-key", new MessageDto.HeaderValue("some-header-value")));
        assertThat(value.getPayload())
                .isEqualTo(
                        new ObjectMapper().writeValueAsString(singletonMap("some-payload-key", "some-payload-value")));
        assertThat(value.getPayloadType()).isEqualTo("io.github.springwolf.core.controller.dtos.MessageDto");
    }
}
