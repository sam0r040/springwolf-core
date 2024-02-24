// SPDX-License-Identifier: Apache-2.0
package io.github.springwolf.plugins.kafka.configuration;

import io.github.springwolf.core.asyncapi.scanners.bindings.BindingProcessorPriority;
import io.github.springwolf.core.asyncapi.scanners.channels.ChannelPriority;
import io.github.springwolf.core.asyncapi.scanners.channels.SimpleChannelsScanner;
import io.github.springwolf.core.asyncapi.scanners.channels.SimpleOperationsScanner;
import io.github.springwolf.core.asyncapi.scanners.channels.annotation.ClassLevelAnnotationChannelsScanner;
import io.github.springwolf.core.asyncapi.scanners.channels.annotation.ClassLevelAnnotationOperationsScanner;
import io.github.springwolf.core.asyncapi.scanners.channels.annotation.MethodLevelAnnotationChannelsScanner;
import io.github.springwolf.core.asyncapi.scanners.channels.annotation.MethodLevelAnnotationOperationsScanner;
import io.github.springwolf.core.asyncapi.scanners.channels.payload.PayloadClassExtractor;
import io.github.springwolf.core.asyncapi.scanners.classes.SpringwolfClassScanner;
import io.github.springwolf.core.schemas.ComponentsService;
import io.github.springwolf.plugins.kafka.scanners.bindings.KafkaBindingFactory;
import io.github.springwolf.plugins.kafka.scanners.bindings.processor.KafkaMessageBindingProcessor;
import io.github.springwolf.plugins.kafka.scanners.bindings.processor.KafkaOperationBindingProcessor;
import io.github.springwolf.plugins.kafka.types.channel.operation.message.header.AsyncHeadersForKafkaBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;

import static io.github.springwolf.plugins.kafka.properties.SpringwolfKafkaConfigConstants.SPRINGWOLF_SCANNER_KAFKA_LISTENER_ENABLED;

/**
 * spring configuration defining the scanner beans for the kafka plugin
 */
@Configuration(proxyBeanMethods = false)
public class SpringwolfKafkaScannerConfiguration {

    @Bean
    @ConditionalOnProperty(
            name = SPRINGWOLF_SCANNER_KAFKA_LISTENER_ENABLED,
            havingValue = "true",
            matchIfMissing = true)
    public KafkaBindingFactory kafkaBindingBuilder() {
        return new KafkaBindingFactory();
    }

    @Bean
    @ConditionalOnProperty(
            name = SPRINGWOLF_SCANNER_KAFKA_LISTENER_ENABLED,
            havingValue = "true",
            matchIfMissing = true)
    public AsyncHeadersForKafkaBuilder kafkaAsyncHeadersBuilder() {
        return new AsyncHeadersForKafkaBuilder();
    }

    @Bean
    @ConditionalOnProperty(
            name = SPRINGWOLF_SCANNER_KAFKA_LISTENER_ENABLED,
            havingValue = "true",
            matchIfMissing = true)
    @Order(value = ChannelPriority.AUTO_DISCOVERED)
    public SimpleChannelsScanner simpleKafkaClassLevelListenerAnnotationChannelsScanner(
            SpringwolfClassScanner classScanner,
            KafkaBindingFactory kafkaBindingBuilder,
            AsyncHeadersForKafkaBuilder asyncHeadersForKafkaBuilder,
            PayloadClassExtractor payloadClassExtractor,
            ComponentsService componentsService) {
        ClassLevelAnnotationChannelsScanner<KafkaListener, KafkaHandler> strategy =
                new ClassLevelAnnotationChannelsScanner<>(
                        KafkaListener.class,
                        KafkaHandler.class,
                        kafkaBindingBuilder,
                        asyncHeadersForKafkaBuilder,
                        payloadClassExtractor,
                        componentsService);

        return new SimpleChannelsScanner(classScanner, strategy);
    }

    @Bean
    @ConditionalOnProperty(
            name = SPRINGWOLF_SCANNER_KAFKA_LISTENER_ENABLED,
            havingValue = "true",
            matchIfMissing = true)
    @Order(value = ChannelPriority.AUTO_DISCOVERED)
    public SimpleOperationsScanner simpleKafkaClassLevelListenerAnnotationOperationScanner(
            SpringwolfClassScanner classScanner,
            KafkaBindingFactory kafkaBindingBuilder,
            AsyncHeadersForKafkaBuilder asyncHeadersForKafkaBuilder,
            PayloadClassExtractor payloadClassExtractor,
            ComponentsService componentsService) {
        ClassLevelAnnotationOperationsScanner<KafkaListener, KafkaHandler> strategy =
                new ClassLevelAnnotationOperationsScanner<>(
                        KafkaListener.class,
                        KafkaHandler.class,
                        kafkaBindingBuilder,
                        asyncHeadersForKafkaBuilder,
                        payloadClassExtractor,
                        componentsService);

        return new SimpleOperationsScanner(classScanner, strategy);
    }

    @Bean
    @ConditionalOnProperty(
            name = SPRINGWOLF_SCANNER_KAFKA_LISTENER_ENABLED,
            havingValue = "true",
            matchIfMissing = true)
    @Order(value = ChannelPriority.AUTO_DISCOVERED)
    public SimpleChannelsScanner simpleKafkaMethodLevelListenerAnnotationChannelsScanner(
            SpringwolfClassScanner classScanner,
            KafkaBindingFactory kafkaBindingBuilder,
            PayloadClassExtractor payloadClassExtractor,
            ComponentsService componentsService) {
        MethodLevelAnnotationChannelsScanner<KafkaListener> strategy = new MethodLevelAnnotationChannelsScanner<>(
                KafkaListener.class, kafkaBindingBuilder, payloadClassExtractor, componentsService);

        return new SimpleChannelsScanner(classScanner, strategy);
    }

    @Bean
    @ConditionalOnProperty(
            name = SPRINGWOLF_SCANNER_KAFKA_LISTENER_ENABLED,
            havingValue = "true",
            matchIfMissing = true)
    @Order(value = ChannelPriority.AUTO_DISCOVERED)
    public SimpleOperationsScanner simpleKafkaMethodLevelListenerAnnotationOperationsScanner(
            SpringwolfClassScanner classScanner,
            KafkaBindingFactory kafkaBindingBuilder,
            PayloadClassExtractor payloadClassExtractor,
            ComponentsService componentsService) {
        MethodLevelAnnotationOperationsScanner<KafkaListener> strategy = new MethodLevelAnnotationOperationsScanner<>(
                KafkaListener.class, kafkaBindingBuilder, payloadClassExtractor, componentsService);

        return new SimpleOperationsScanner(classScanner, strategy);
    }

    @Bean
    @Order(value = BindingProcessorPriority.PROTOCOL_BINDING)
    @ConditionalOnMissingBean
    public KafkaMessageBindingProcessor kafkaMessageBindingProcessor() {
        return new KafkaMessageBindingProcessor();
    }

    @Bean
    @Order(value = BindingProcessorPriority.PROTOCOL_BINDING)
    @ConditionalOnMissingBean
    public KafkaOperationBindingProcessor kafkaOperationBindingProcessor() {
        return new KafkaOperationBindingProcessor();
    }
}
