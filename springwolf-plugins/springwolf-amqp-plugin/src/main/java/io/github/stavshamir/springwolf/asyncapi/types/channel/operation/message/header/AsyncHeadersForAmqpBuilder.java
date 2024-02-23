// SPDX-License-Identifier: Apache-2.0
package io.github.stavshamir.springwolf.asyncapi.types.channel.operation.message.header;

import io.github.springwolf.core.asyncapi.types.channel.operation.message.header.AsyncHeaders;
import io.github.springwolf.core.asyncapi.types.channel.operation.message.header.AsyncHeadersBuilder;

public class AsyncHeadersForAmqpBuilder implements AsyncHeadersBuilder {

    private static final AsyncHeaders headers = new AsyncHeaders("SpringRabbitListenerDefaultHeaders");

    @Override
    public AsyncHeaders buildHeaders(Class<?> payloadType) {
        return headers;
    }
}
