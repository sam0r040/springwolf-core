// SPDX-License-Identifier: Apache-2.0
package io.github.stavshamir.springwolf.addons.json_schema.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.springwolf.core.asyncapi.AsyncApiCustomizer;
import io.github.stavshamir.springwolf.addons.json_schema.JsonSchemaCustomizer;
import io.github.stavshamir.springwolf.addons.json_schema.JsonSchemaGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringwolfJsonSchemaAutoConfiguration {

    @Bean
    public JsonSchemaGenerator jsonSchemaGenerator(ObjectMapper objectMapper) {
        return new JsonSchemaGenerator(objectMapper);
    }

    @Bean
    public AsyncApiCustomizer jsonSchemaCustomizer(JsonSchemaGenerator jsonSchemaGenerator) {
        return new JsonSchemaCustomizer(jsonSchemaGenerator);
    }
}
