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
    private ModelResolverService modelResolverService;

    @BeforeEach
    void setUp() {
        modelResolverService = new ModelResolverService();
    }

    @Test
    void testResolveOpenAiModelWithPrefix() {
        String result = modelResolverService.resolveModel("gpt-4");
        assertEquals(ModelResolverService.MODEL_PREFIX + "gpt-4", result);
    }

    @Test
    void testResolveModelAlreadyWithPrefix() {
        String result =
                modelResolverService.resolveModel(ModelResolverService.MODEL_PREFIX + "gpt-4");
        assertEquals(ModelResolverService.MODEL_PREFIX + "gpt-4", result);
    }

    @Test
    void testResolveVariousModelNames() {
        assertEquals(
                ModelResolverService.MODEL_PREFIX + "gpt-3.5-turbo",
                modelResolverService.resolveModel("gpt-3.5-turbo"));
        assertEquals(
                ModelResolverService.MODEL_PREFIX + "gpt-4-turbo",
                modelResolverService.resolveModel("gpt-4-turbo"));
        assertEquals(
                ModelResolverService.MODEL_PREFIX + "text-davinci-003",
                modelResolverService.resolveModel("text-davinci-003"));
        assertEquals(
                ModelResolverService.MODEL_PREFIX + "model-with-dashes_and.underscores",
                modelResolverService.resolveModel("model-with-dashes_and.underscores"));
    }

    @ParameterizedTest
    @MethodSource("provideExceptionTestCases")
    void testInvalidModelNames(
            String inputModel, String expectedMessageContent, String testDescription) {
        UnrecognizedModelException thrown =
                assertThrows(
                        UnrecognizedModelException.class,
                        () -> modelResolverService.resolveModel(inputModel),
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
        String result = modelResolverService.resolveModel(longModelName);
        assertEquals(ModelResolverService.MODEL_PREFIX + longModelName, result);
    }

    @Test
    void testCaseSensitiveModelNames() {
        assertEquals(
                ModelResolverService.MODEL_PREFIX + "GPT-4",
                modelResolverService.resolveModel("GPT-4"));
        assertEquals(
                ModelResolverService.MODEL_PREFIX + "gpt-4",
                modelResolverService.resolveModel("gpt-4"));
        assertEquals(
                ModelResolverService.MODEL_PREFIX + "OpenAI-GPT",
                modelResolverService.resolveModel("OpenAI-GPT"));
    }

    @Test
    void testResolveToOpenAiModel() {
        assertEquals(
                "gpt-4",
                modelResolverService.resolveToOpenAiModel(
                        ModelResolverService.MODEL_PREFIX + "gpt-4"));
        assertEquals(
                "gpt-3.5-turbo",
                modelResolverService.resolveToOpenAiModel(
                        ModelResolverService.MODEL_PREFIX + "gpt-3.5-turbo"));
        assertEquals(
                "text-davinci-003",
                modelResolverService.resolveToOpenAiModel(
                        ModelResolverService.MODEL_PREFIX + "text-davinci-003"));
    }

    @Test
    void testResolveToOpenAiModelWithSpecialCharacters() {
        assertEquals(
                "model-with-dashes_and.underscores",
                modelResolverService.resolveToOpenAiModel(
                        ModelResolverService.MODEL_PREFIX + "model-with-dashes_and.underscores"));
    }

    @Test
    void testResolveToOpenAiModelCaseSensitive() {
        assertEquals(
                "GPT-4",
                modelResolverService.resolveToOpenAiModel(
                        ModelResolverService.MODEL_PREFIX + "GPT-4"));
        assertEquals(
                "gpt-4",
                modelResolverService.resolveToOpenAiModel(
                        ModelResolverService.MODEL_PREFIX + "gpt-4"));
        assertEquals(
                "OpenAI-GPT",
                modelResolverService.resolveToOpenAiModel(
                        ModelResolverService.MODEL_PREFIX + "OpenAI-GPT"));
    }

    @Test
    void testResolveToOpenAiModelVeryLongNames() {
        String longModelName =
                "very-long-model-name-with-many-characters-that-should-still-work-properly";
        assertEquals(
                longModelName,
                modelResolverService.resolveToOpenAiModel(
                        ModelResolverService.MODEL_PREFIX + longModelName));
    }

    @Test
    void testResolveToOpenAiModelEmptyModel() {
        String emptyModelName = "";
        UnrecognizedModelException thrown =
                assertThrows(
                        UnrecognizedModelException.class,
                        () -> modelResolverService.resolveToOpenAiModel(emptyModelName),
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
                        () -> modelResolverService.resolveToOpenAiModel(inputModel),
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
                                + ModelResolverService.MODEL_PREFIX
                                + "' prefix",
                        "missing prefix"),
                Arguments.of(
                        "cohere.command-r-plus",
                        "GenAI model name must start with '"
                                + ModelResolverService.MODEL_PREFIX
                                + "' prefix",
                        "wrong prefix"));
    }
}
