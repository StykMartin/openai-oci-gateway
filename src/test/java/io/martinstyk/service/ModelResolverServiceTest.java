package io.martinstyk.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ModelResolverServiceTest {
    private ModelResolver modelResolver;

    @BeforeEach
    void setUp() {
        modelResolver = new ModelResolver();
    }

    @Test
    void testResolveOpenAiModelWithPrefix() {
        String result = modelResolver.resolveModel("gpt-4");
        assertEquals(ModelResolver.MODEL_PREFIX + "gpt-4", result);
    }

    @Test
    void testResolveModelAlreadyWithPrefix() {
        String result = modelResolver.resolveModel(ModelResolver.MODEL_PREFIX + "gpt-4");
        assertEquals(ModelResolver.MODEL_PREFIX + "gpt-4", result);
    }

    @Test
    void testResolveVariousModelNames() {
        assertEquals(
                ModelResolver.MODEL_PREFIX + "gpt-3.5-turbo",
                modelResolver.resolveModel("gpt-3.5-turbo"));
        assertEquals(
                ModelResolver.MODEL_PREFIX + "gpt-4-turbo",
                modelResolver.resolveModel("gpt-4-turbo"));
        assertEquals(
                ModelResolver.MODEL_PREFIX + "text-davinci-003",
                modelResolver.resolveModel("text-davinci-003"));
        assertEquals(
                ModelResolver.MODEL_PREFIX + "model-with-dashes_and.underscores",
                modelResolver.resolveModel("model-with-dashes_and.underscores"));
    }

    @ParameterizedTest
    @MethodSource("provideExceptionTestCases")
    void testInvalidModelNames(
            String inputModel, String expectedMessageContent, String testDescription) {
        UnrecognizedModelException thrown =
                assertThrows(
                        UnrecognizedModelException.class,
                        () -> modelResolver.resolveModel(inputModel),
                        "Expected resolveModel() to throw for " + testDescription);

        assertTrue(thrown.getMessage().contains(expectedMessageContent));
    }

    private static Stream<Arguments> provideExceptionTestCases() {
        return Stream.of(
                Arguments.of(null, "Model name cannot be null or empty", "null input"),
                Arguments.of("", "Model name cannot be null or empty", "empty string input"),
                Arguments.of("   ", "Model name cannot be null or empty", "whitespace-only input"));
    }

    @Test
    void testVeryLongModelNames() {
        String longModelName =
                "very-long-model-name-with-many-characters-that-should-still-work-properly";
        String result = modelResolver.resolveModel(longModelName);
        assertEquals(ModelResolver.MODEL_PREFIX + longModelName, result);
    }

    @Test
    void testCaseSensitiveModelNames() {
        assertEquals(ModelResolver.MODEL_PREFIX + "GPT-4", modelResolver.resolveModel("GPT-4"));
        assertEquals(ModelResolver.MODEL_PREFIX + "gpt-4", modelResolver.resolveModel("gpt-4"));
        assertEquals(
                ModelResolver.MODEL_PREFIX + "OpenAI-GPT",
                modelResolver.resolveModel("OpenAI-GPT"));
    }

    @Test
    void testResolveToOpenAiModel() {
        assertEquals(
                "gpt-4", modelResolver.resolveToOpenAiModel(ModelResolver.MODEL_PREFIX + "gpt-4"));
        assertEquals(
                "gpt-3.5-turbo",
                modelResolver.resolveToOpenAiModel(ModelResolver.MODEL_PREFIX + "gpt-3.5-turbo"));
        assertEquals(
                "text-davinci-003",
                modelResolver.resolveToOpenAiModel(
                        ModelResolver.MODEL_PREFIX + "text-davinci-003"));
    }

    @Test
    void testResolveToOpenAiModelWithSpecialCharacters() {
        assertEquals(
                "model-with-dashes_and.underscores",
                modelResolver.resolveToOpenAiModel(
                        ModelResolver.MODEL_PREFIX + "model-with-dashes_and.underscores"));
    }

    @Test
    void testResolveToOpenAiModelCaseSensitive() {
        assertEquals(
                "GPT-4", modelResolver.resolveToOpenAiModel(ModelResolver.MODEL_PREFIX + "GPT-4"));
        assertEquals(
                "gpt-4", modelResolver.resolveToOpenAiModel(ModelResolver.MODEL_PREFIX + "gpt-4"));
        assertEquals(
                "OpenAI-GPT",
                modelResolver.resolveToOpenAiModel(ModelResolver.MODEL_PREFIX + "OpenAI-GPT"));
    }

    @Test
    void testResolveToOpenAiModelVeryLongNames() {
        String longModelName =
                "very-long-model-name-with-many-characters-that-should-still-work-properly";
        assertEquals(
                longModelName,
                modelResolver.resolveToOpenAiModel(ModelResolver.MODEL_PREFIX + longModelName));
    }

    @Test
    void testResolveToOpenAiModelEmptyModel() {
        String emptyModelName = "";
        UnrecognizedModelException thrown =
                assertThrows(
                        UnrecognizedModelException.class,
                        () -> modelResolver.resolveToOpenAiModel(emptyModelName),
                        "Expected resolveToOpenAiModel() to throw for " + emptyModelName);
        assertTrue(thrown.getMessage().contains("Model name cannot be null or empty"));
    }

    @ParameterizedTest
    @MethodSource("provideReverseExceptionTestCases")
    void testResolveToOpenAiModelExceptions(
            String inputModel, String expectedMessageContent, String testDescription) {
        UnrecognizedModelException thrown =
                assertThrows(
                        UnrecognizedModelException.class,
                        () -> modelResolver.resolveToOpenAiModel(inputModel),
                        "Expected resolveToOpenAiModel() to throw for " + testDescription);

        assertTrue(thrown.getMessage().contains(expectedMessageContent));
    }

    private static Stream<Arguments> provideReverseExceptionTestCases() {
        return Stream.of(
                Arguments.of(null, "Model name cannot be null or empty", "null input"),
                Arguments.of("", "Model name cannot be null or empty", "empty string input"),
                Arguments.of("   ", "Model name cannot be null or empty", "whitespace-only input"),
                Arguments.of(
                        "gpt-4",
                        "GenAI model name must start with '"
                                + ModelResolver.MODEL_PREFIX
                                + "' prefix",
                        "missing prefix"),
                Arguments.of(
                        "cohere.command-r-plus",
                        "GenAI model name must start with '"
                                + ModelResolver.MODEL_PREFIX
                                + "' prefix",
                        "wrong prefix"));
    }
}
