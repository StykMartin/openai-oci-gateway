package io.martinstyk.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.martinstyk.config.GenAiProperties;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ModelResolverServiceTest {

    private GenAiProperties genAiProperties;
    private ModelResolverService modelResolverService;

    @BeforeEach
    void setUp() {
        genAiProperties = new GenAiProperties();
        modelResolverService = new ModelResolverService(genAiProperties);
    }

    @Test
    void testResolveMappedModel() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("gpt-4", "cohere.command-r-plus");
        mapping.put("gpt-3.5-turbo", "cohere.command-r");
        mapping.put("claude-3", "meta.llama-3.1-70b-instruct");
        genAiProperties.setModelMapping(mapping);

        String result = modelResolverService.resolveModel("gpt-4");

        assertEquals("cohere.command-r-plus", result);
    }

    @Test
    void testResolveDirectTargetModel() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("gpt-4", "cohere.command-r-plus");
        mapping.put("gpt-3.5-turbo", "cohere.command-r");
        mapping.put("claude-3", "meta.llama-3.1-70b-instruct");
        genAiProperties.setModelMapping(mapping);

        String result = modelResolverService.resolveModel("cohere.command-r-plus");
        assertEquals("cohere.command-r-plus", result);
    }

    @ParameterizedTest
    @MethodSource("provideExceptionTestCases")
    void testUnrecognizedModelExceptions(
            String inputModel, String expectedMessageContent, String testDescription) {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("gpt-4", "cohere.command-r-plus");
        mapping.put("gpt-4-turbo", "cohere.command-r-plus");
        mapping.put("claude-3-opus", "meta.llama-3.1-70b-instruct");
        mapping.put("special-chars", "model-with-dashes_and.underscores");
        genAiProperties.setModelMapping(mapping);

        UnrecognizedModelException thrown =
                assertThrows(
                        UnrecognizedModelException.class,
                        () -> modelResolverService.resolveModel(inputModel),
                        "Expected resolveModel() to throw for " + testDescription);

        assertTrue(thrown.getMessage().contains(expectedMessageContent));
    }

    private static Stream<Arguments> provideExceptionTestCases() {
        return Stream.of(
                Arguments.of("unknown-model", "unknown-model", "unrecognized model"),
                Arguments.of(null, "null", "null input"),
                Arguments.of("", "", "empty string input"),
                Arguments.of("   ", "   ", "whitespace-only input"),
                Arguments.of("GPT-4", "GPT-4", "case-sensitive mapping with different case"));
    }

    @Test
    void testMultipleMappingsToSameTarget() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("gpt-4", "cohere.command-r-plus");
        mapping.put("gpt-4-turbo", "cohere.command-r-plus");
        mapping.put("claude-3-opus", "meta.llama-3.1-70b-instruct");
        mapping.put("special-chars", "model-with-dashes_and.underscores");
        genAiProperties.setModelMapping(mapping);

        assertEquals("cohere.command-r-plus", modelResolverService.resolveModel("gpt-4"));
        assertEquals("cohere.command-r-plus", modelResolverService.resolveModel("gpt-4-turbo"));
    }

    @Test
    void testVeryLongModelNames() {
        String longModelName =
                "very-long-model-name-with-many-characters-that-should-still-work-properly";
        Map<String, String> mapping = new HashMap<>();
        mapping.put(longModelName, "target-model");
        genAiProperties.setModelMapping(mapping);

        String result = modelResolverService.resolveModel(longModelName);
        assertEquals("target-model", result);
    }
}
