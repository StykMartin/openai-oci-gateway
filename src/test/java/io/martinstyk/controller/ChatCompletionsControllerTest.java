package io.martinstyk.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.http.StreamResponse;
import com.openai.errors.BadRequestException;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionChunk;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.services.blocking.chat.ChatCompletionService;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@MicronautTest
class ChatCompletionsControllerTest {
    private static final String VALID_TOKEN = "sk-123456789012345678901234567890123456789012345678";
    private static final String DEFAULT_CONTENT = "Hello";

    private final EmbeddedServer server;
    private OpenAIClient client;

    ChatCompletionsControllerTest(EmbeddedServer server) {
        this.server = server;
    }

    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:" + server.getPort() + "/v1";

        client =
                OpenAIOkHttpClient.builder()
                        .baseUrl(baseUrl)
                        .apiKey(VALID_TOKEN)
                        .responseValidation(false)
                        .build();
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, 3.0})
    void testCreateChatCompletionInvalidTemperature(double temperature) {
        ChatCompletionCreateParams params =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4)
                        .addUserMessage(DEFAULT_CONTENT)
                        .temperature(temperature)
                        .build();
        ChatCompletionService completions = client.chat().completions();

        assertThrows(
                BadRequestException.class,
                () -> {
                    completions.create(params);
                });
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, 2.0})
    void testCreateChatCompletionInvalidTopP(double topP) {
        ChatCompletionCreateParams params =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4)
                        .addUserMessage(DEFAULT_CONTENT)
                        .topP(topP)
                        .build();

        ChatCompletionService completions = client.chat().completions();

        assertThrows(
                BadRequestException.class,
                () -> {
                    completions.create(params);
                });
    }

    @ParameterizedTest
    @ValueSource(doubles = {-3.0, 3.0})
    void testCreateChatCompletionInvalidFrequencyPenalty(double penalty) {
        ChatCompletionCreateParams params =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4)
                        .addUserMessage(DEFAULT_CONTENT)
                        .frequencyPenalty(penalty)
                        .build();

        ChatCompletionService completions = client.chat().completions();

        assertThrows(
                BadRequestException.class,
                () -> {
                    completions.create(params);
                });
    }

    @ParameterizedTest
    @ValueSource(doubles = {-3.0, 3.0})
    void testCreateChatCompletionInvalidPresencePenalty(double penalty) {
        ChatCompletionCreateParams params =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4)
                        .addUserMessage(DEFAULT_CONTENT)
                        .presencePenalty(penalty)
                        .build();

        ChatCompletionService completions = client.chat().completions();

        assertThrows(
                BadRequestException.class,
                () -> {
                    completions.create(params);
                });
    }

    @Test
    void testCreateChatCompletionInvalidMaxCompletionTokens() {
        ChatCompletionCreateParams params =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4)
                        .addUserMessage(DEFAULT_CONTENT)
                        .maxCompletionTokens(0L)
                        .build();

        ChatCompletionService completions = client.chat().completions();

        assertThrows(
                BadRequestException.class,
                () -> {
                    completions.create(params);
                });
    }

    @Test
    void testCreateChatCompletionSuccess() {
        ChatCompletionCreateParams params =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4)
                        .addSystemMessage("You are a helpful assistant.")
                        .addUserMessage("Hello!")
                        .build();

        ChatCompletionService completions = client.chat().completions();
        ChatCompletion completion = completions.create(params);

        assertNotNull(completion);
        assertNotNull(completion.id());
        assertNotNull(completion.model());
        assertFalse(completion.choices().isEmpty());
        assertTrue(completion.choices().getFirst().message().content().isPresent());
        assertTrue(completion.usage().isPresent());
        assertTrue(completion.usage().get().totalTokens() > 0);
    }

    @Test
    void testCreateChatCompletionWithParameters() {
        ChatCompletionCreateParams params =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4)
                        .addUserMessage("Tell me a joke")
                        .temperature(0.7)
                        .topP(0.9)
                        .frequencyPenalty(0.5)
                        .presencePenalty(0.5)
                        .maxCompletionTokens(100L)
                        .build();

        ChatCompletionService completions = client.chat().completions();
        ChatCompletion completion = completions.create(params);

        assertNotNull(completion);
        assertFalse(completion.choices().isEmpty());
        assertTrue(completion.usage().isPresent());
        assertTrue(completion.usage().get().completionTokens() <= 100);
    }

    @Disabled("Streaming is not working properly yet")
    @Test
    void testCreateChatCompletionStreaming() {
        /*
        data: {"id":"chatcmpl-CPpEy6iz6xHypBlhQAPfkhWuf92G1","object":"chat.completion.chunk","created":1760271204,"model":"gpt-5-nano-2025-08-07","service_tier":"default","system_fingerprint":null,"choices":[{"index":0,"delta":{"content":" like"},"finish_reason":null}],"obfuscation":"wUK7UHzyp8bCPnY"}
        data: {"id":"chatcmpl-CPpEy6iz6xHypBlhQAPfkhWuf92G1","object":"chat.completion.chunk","created":1760271204,"model":"gpt-5-nano-2025-08-07","service_tier":"default","system_fingerprint":null,"choices":[{"index":0,"delta":{"content":" to"},"finish_reason":null}],"obfuscation":"w"}
        data: {"id":"chatcmpl-CPpEy6iz6xHypBlhQAPfkhWuf92G1","object":"chat.completion.chunk","created":1760271204,"model":"gpt-5-nano-2025-08-07","service_tier":"default","system_fingerprint":null,"choices":[{"index":0,"delta":{"content":" work"},"finish_reason":null}],"obfuscation":"6GULP3RfjIOkgOz"}
        data: {"id":"chatcmpl-CPpEy6iz6xHypBlhQAPfkhWuf92G1","object":"chat.completion.chunk","created":1760271204,"model":"gpt-5-nano-2025-08-07","service_tier":"default","system_fingerprint":null,"choices":[{"index":0,"delta":{"content":" on"},"finish_reason":null}],"obfuscation":"C"}
        data: {"id":"chatcmpl-CPpEy6iz6xHypBlhQAPfkhWuf92G1","object":"chat.completion.chunk","created":1760271204,"model":"gpt-5-nano-2025-08-07","service_tier":"default","system_fingerprint":null,"choices":[{"index":0,"delta":{"content":"."},"finish_reason":null}],"obfuscation":"vuk"}
        data: {"id":"chatcmpl-CPpEy6iz6xHypBlhQAPfkhWuf92G1","object":"chat.completion.chunk","created":1760271204,"model":"gpt-5-nano-2025-08-07","service_tier":"default","system_fingerprint":null,"choices":[{"index":0,"delta":{},"finish_reason":"stop"}],"obfuscation":"YYC7ECFitApIox"}
        data: [DONE]
         */
        ChatCompletionCreateParams params =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4)
                        .addUserMessage("Hello!")
                        .build();

        List<ChatCompletionChunk> chunks = new ArrayList<>();
        ChatCompletionService completions = client.chat().completions();

        try (StreamResponse<ChatCompletionChunk> streamResponse =
                completions.createStreaming(params)) {
            streamResponse.stream().forEach(chunks::add);

            assertFalse(chunks.isEmpty(), "Stream should contain at least one chunk");

            ChatCompletionChunk firstChunk = chunks.getFirst();
            assertNotNull(firstChunk.id());

            boolean hasContent =
                    chunks.stream()
                            .anyMatch(
                                    chunk ->
                                            !chunk.choices().isEmpty()
                                                    && chunk.choices()
                                                            .getFirst()
                                                            .delta()
                                                            .content()
                                                            .isPresent());
            assertTrue(hasContent, "Stream should contain content chunks");

            ChatCompletionChunk lastChunk = chunks.getLast();
            assertTrue(
                    lastChunk.choices().isEmpty()
                            || lastChunk.choices().getFirst().finishReason().isPresent(),
                    "Last chunk should have finish reason");
        }
    }

    @Test
    void testCreateChatCompletionMultipleMessages() {
        ChatCompletionCreateParams params =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4)
                        .addUserMessage("What is 2+2?")
                        .addAssistantMessage("4")
                        .addUserMessage("What is 3+3?")
                        .build();

        ChatCompletionService completions = client.chat().completions();
        ChatCompletion completion = completions.create(params);

        assertNotNull(completion);
        assertFalse(completion.choices().isEmpty());
        assertTrue(completion.choices().getFirst().message().content().isPresent());
    }

    @Test
    void testCreateChatCompletionWithDeveloperMessage() {
        ChatCompletionCreateParams params =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4)
                        .addDeveloperMessage("You are a helpful assistant.")
                        .addUserMessage("Hello!")
                        .build();

        ChatCompletionService completions = client.chat().completions();
        ChatCompletion completion = completions.create(params);

        // Validate output
        assertNotNull(completion);
        assertFalse(completion.choices().isEmpty());
    }

    @Test
    void testCreateChatCompletionWithPenalties() {
        ChatCompletionCreateParams params =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4)
                        .addUserMessage("Hello!")
                        .frequencyPenalty(0.5)
                        .presencePenalty(0.5)
                        .build();
        ChatCompletionService completions = client.chat().completions();
        ChatCompletion completion = completions.create(params);

        assertNotNull(completion);
        assertFalse(completion.choices().isEmpty());
    }
}
