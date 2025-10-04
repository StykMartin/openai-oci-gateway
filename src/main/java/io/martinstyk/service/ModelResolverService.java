package io.martinstyk.service;

import io.martinstyk.config.GenAiProperties;
import jakarta.inject.Singleton;
import java.util.Map;

@Singleton
public class ModelResolverService {
    private final GenAiProperties properties;

    public ModelResolverService(GenAiProperties properties) {
        this.properties = properties;
    }

    public String resolveModel(String inputModelName) {
        Map<String, String> mapping = properties.getModelMapping();

        if (mapping.containsKey(inputModelName)) {
            return mapping.get(inputModelName);
        }

        if (mapping.containsValue(inputModelName)) {
            return inputModelName;
        }

        throw new UnrecognizedModelException("Unrecognized model name: " + inputModelName);
    }
}
