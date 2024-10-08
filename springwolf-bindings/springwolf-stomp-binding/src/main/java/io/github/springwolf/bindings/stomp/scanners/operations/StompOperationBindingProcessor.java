// SPDX-License-Identifier: Apache-2.0
package io.github.springwolf.bindings.stomp.scanners.operations;

import io.github.springwolf.asyncapi.v3.bindings.stomp.StompOperationBinding;
import io.github.springwolf.bindings.stomp.annotations.StompAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.scanners.bindings.operations.AbstractOperationBindingProcessor;
import io.github.springwolf.core.asyncapi.scanners.bindings.operations.ProcessedOperationBinding;
import org.springframework.util.StringValueResolver;

public class StompOperationBindingProcessor extends AbstractOperationBindingProcessor<StompAsyncOperationBinding> {

    public StompOperationBindingProcessor(StringValueResolver stringValueResolver) {
        super(stringValueResolver);
    }

    @Override
    protected ProcessedOperationBinding mapToOperationBinding(StompAsyncOperationBinding bindingAnnotation) {
        var operationBinding = StompOperationBinding.builder().build();
        return new ProcessedOperationBinding("stomp", operationBinding);
    }
}
