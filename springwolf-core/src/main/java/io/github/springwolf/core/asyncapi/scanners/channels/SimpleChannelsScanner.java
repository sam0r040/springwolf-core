// SPDX-License-Identifier: Apache-2.0
package io.github.springwolf.core.asyncapi.scanners.channels;

import io.github.springwolf.core.asyncapi.scanners.classes.ClassScanner;
import io.github.springwolf.asyncapi.v3.model.channel.ChannelObject;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class SimpleChannelsScanner implements ChannelsScanner {

    private final ClassScanner classScanner;

    private final ClassProcessor classProcessor;

    @Override
    public Map<String, ChannelObject> scan() {
        Set<Class<?>> components = classScanner.scan();

        List<Map.Entry<String, ChannelObject>> channels = mapToChannels(components);

        return ChannelMerger.mergeChannels(channels);
    }

    private List<Map.Entry<String, ChannelObject>> mapToChannels(Set<Class<?>> components) {
        return components.stream().flatMap(classProcessor::process).toList();
    }

    public interface ClassProcessor {
        Stream<Map.Entry<String, ChannelObject>> process(Class<?> clazz);
    }
}