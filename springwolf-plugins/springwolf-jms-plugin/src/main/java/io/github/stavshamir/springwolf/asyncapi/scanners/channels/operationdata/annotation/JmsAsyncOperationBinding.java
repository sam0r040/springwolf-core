// SPDX-License-Identifier: Apache-2.0
package io.github.stavshamir.springwolf.asyncapi.scanners.channels.operationdata.annotation;

import io.github.springwolf.core.asyncapi.scanners.channels.annotation.AsyncOperationBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code @JmsAsyncOperationBinding} is a method-level annotation used in combination with {@link AsyncPublisher} or {@link AsyncListener}.
 * It configures the operation binding for the JMS protocol.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@AsyncOperationBinding
public @interface JmsAsyncOperationBinding {

    String type() default "jms";
}
