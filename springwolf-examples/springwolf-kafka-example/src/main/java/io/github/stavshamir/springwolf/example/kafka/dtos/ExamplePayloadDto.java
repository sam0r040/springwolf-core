// SPDX-License-Identifier: Apache-2.0
package io.github.stavshamir.springwolf.example.kafka.dtos;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "Example payload model", contentMediaType = "yaml")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamplePayloadDto {
    @Schema(description = "Some string field", example = "some string value", requiredMode = REQUIRED, contentMediaType = "yml")
    @JacksonXmlProperty(localName = "TEST111")
    @XmlAttribute(name = "TEST789")
    private String someString;

    @Schema(description = "Some long field", example = "5")
    private long someLong;

    @Schema(description = "Some enum field", example = "FOO2", requiredMode = REQUIRED)
    @JacksonXmlProperty(localName = "TEST456")
    private ExampleEnum someEnum;

    public enum ExampleEnum {
        FOO1,
        FOO2,
        FOO3
    }
}
