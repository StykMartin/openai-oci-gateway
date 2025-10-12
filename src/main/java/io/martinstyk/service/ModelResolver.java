package io.martinstyk.service;

import jakarta.inject.Singleton;

@Singleton
public class ModelResolver {
    public static final String MODEL_PREFIX = "openai.";

    public String resolveModel(String inputModelName) {
        if (inputModelName == null || inputModelName.trim().isEmpty()) {
            throw new UnrecognizedModelException("Model name cannot be null or empty");
        }

        if (inputModelName.startsWith(MODEL_PREFIX)) {
            return inputModelName;
        }

        return MODEL_PREFIX + inputModelName;
    }

    public String resolveToOpenAiModel(String genaiModelName) {
        if (genaiModelName == null || genaiModelName.trim().isEmpty()) {
            throw new UnrecognizedModelException("Model name cannot be null or empty");
        }

        if (!genaiModelName.startsWith(MODEL_PREFIX)) {
            throw new UnrecognizedModelException(
                    "GenAI model name must start with '"
                            + MODEL_PREFIX
                            + "' prefix: "
                            + genaiModelName);
        }

        String openAiModelName = genaiModelName.substring(MODEL_PREFIX.length()).trim();
        if (openAiModelName.isEmpty()) {
            throw new UnrecognizedModelException("Model name cannot be null or empty");
        }
        return openAiModelName;
    }
}
