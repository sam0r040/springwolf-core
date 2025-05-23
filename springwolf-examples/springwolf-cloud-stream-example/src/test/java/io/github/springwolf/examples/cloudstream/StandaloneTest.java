// SPDX-License-Identifier: Apache-2.0
package io.github.springwolf.examples.cloudstream;

import io.github.springwolf.asyncapi.v3.jackson.AsyncApiSerializerService;
import io.github.springwolf.asyncapi.v3.jackson.DefaultAsyncApiSerializerService;
import io.github.springwolf.asyncapi.v3.model.AsyncAPI;
import io.github.springwolf.core.standalone.DefaultStandaloneFactory;
import io.github.springwolf.core.standalone.StandaloneFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.test.EmbeddedKafkaKraftBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StandaloneTest {

    /**
     * When using {@link EmbeddedKafka} (specifically {@link EmbeddedKafkaKraftBroker}),
     * the spring.kafka.bootstrap-servers property is set globally, which leaks into this unrelated test.
     * The property is reset, to keep the test isolated/simple.
     */
    @BeforeAll
    public static void beforeAll() {
        System.clearProperty("spring.kafka.bootstrap-servers");
    }

    @Test
    public void asyncApiStandaloneArtifactTest() throws IOException {
        // given
        String basePackage = "io.github.springwolf.examples.cloudstream";
        StandaloneFactory standaloneFactory = new DefaultStandaloneFactory(basePackage);

        // when
        AsyncAPI asyncApi = standaloneFactory.getAsyncApiService().getAsyncAPI();

        // then
        assertThat(asyncApi).isNotNull();

        AsyncApiSerializerService serializerService = new DefaultAsyncApiSerializerService();
        String actual = serializerService.toJsonString(asyncApi);
        String actualPatched = actual.replace("localhost:9095", "kafka:29092");
        Files.writeString(Path.of("src", "test", "resources", "asyncapi.standalone.json"), actualPatched);

        // then
        InputStream s = this.getClass().getResourceAsStream("/asyncapi.json");
        String expected = new String(s.readAllBytes(), StandardCharsets.UTF_8).trim();
        assertEquals(expected, actualPatched);
    }
}
