// SPDX-License-Identifier: Apache-2.0
package io.github.springwolf.bindings.jms.configuration;

import io.github.springwolf.bindings.jms.scanners.messages.JmsMessageBindingProcessor;
import io.github.springwolf.bindings.jms.scanners.operations.JmsOperationBindingProcessor;
import io.github.springwolf.core.asyncapi.scanners.bindings.BindingProcessorPriority;
import io.github.springwolf.core.asyncapi.scanners.common.utils.StringValueResolverProxy;
import io.github.springwolf.core.configuration.properties.SpringwolfConfigConstants;
import io.github.springwolf.core.standalone.StandaloneConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * Autoconfiguration for the springwolf JMS Binding.
 */
@AutoConfiguration
@ConditionalOnProperty(name = SpringwolfConfigConstants.SPRINGWOLF_ENABLED, havingValue = "true", matchIfMissing = true)
@StandaloneConfiguration
public class SpringwolfJmsBindingAutoConfiguration {

    @Bean
    @Order(value = BindingProcessorPriority.PROTOCOL_BINDING)
    @ConditionalOnMissingBean
    public JmsOperationBindingProcessor jmsOperationBindingProcessor(StringValueResolverProxy stringValueResolver) {
        return new JmsOperationBindingProcessor(stringValueResolver);
    }

    @Bean
    @Order(value = BindingProcessorPriority.PROTOCOL_BINDING)
    @ConditionalOnMissingBean
    public JmsMessageBindingProcessor jmsMessageBindingProcessor() {
        return new JmsMessageBindingProcessor();
    }
}
