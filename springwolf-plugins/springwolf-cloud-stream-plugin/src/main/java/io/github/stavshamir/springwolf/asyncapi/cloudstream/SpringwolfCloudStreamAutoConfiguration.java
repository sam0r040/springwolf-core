// SPDX-License-Identifier: Apache-2.0
package io.github.stavshamir.springwolf.asyncapi.cloudstream;

import io.github.springwolf.core.asyncapi.scanners.beans.BeanMethodsScanner;
import io.github.springwolf.core.asyncapi.scanners.channels.payload.PayloadClassExtractor;
import io.github.springwolf.core.configuration.AsyncApiDocketService;
import io.github.springwolf.core.configuration.properties.SpringwolfConfigConstants;
import io.github.springwolf.core.schemas.ComponentsService;
import io.github.stavshamir.springwolf.asyncapi.scanners.channels.cloudstream.CloudStreamFunctionChannelsScanner;
import io.github.stavshamir.springwolf.asyncapi.scanners.channels.cloudstream.CloudStreamFunctionOperationsScanner;
import io.github.stavshamir.springwolf.asyncapi.scanners.channels.cloudstream.FunctionalChannelBeanBuilder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.context.annotation.Bean;

/**
 * Autoconfiguration for the springwolf cloudstream plugin.
 */
@AutoConfiguration
@ConditionalOnProperty(name = SpringwolfConfigConstants.SPRINGWOLF_ENABLED, havingValue = "true", matchIfMissing = true)
public class SpringwolfCloudStreamAutoConfiguration {

    @Bean
    public CloudStreamFunctionChannelsScanner cloudStreamFunctionChannelsScanner(
            AsyncApiDocketService asyncApiDocketService,
            BeanMethodsScanner beanMethodsScanner,
            ComponentsService componentsService,
            BindingServiceProperties cloudstreamBindingServiceProperties,
            FunctionalChannelBeanBuilder functionalChannelBeanBuilder) {
        return new CloudStreamFunctionChannelsScanner(
                asyncApiDocketService,
                beanMethodsScanner,
                componentsService,
                cloudstreamBindingServiceProperties,
                functionalChannelBeanBuilder);
    }

    @Bean
    public CloudStreamFunctionOperationsScanner cloudStreamFunctionOperationsScanner(
            AsyncApiDocketService asyncApiDocketService,
            BeanMethodsScanner beanMethodsScanner,
            ComponentsService componentsService,
            BindingServiceProperties cloudstreamBindingServiceProperties,
            FunctionalChannelBeanBuilder functionalChannelBeanBuilder) {
        return new CloudStreamFunctionOperationsScanner(
                asyncApiDocketService,
                beanMethodsScanner,
                componentsService,
                cloudstreamBindingServiceProperties,
                functionalChannelBeanBuilder);
    }

    @Bean
    public FunctionalChannelBeanBuilder functionalChannelBeanBuilder(PayloadClassExtractor payloadClassExtractor) {
        return new FunctionalChannelBeanBuilder(payloadClassExtractor);
    }
}
