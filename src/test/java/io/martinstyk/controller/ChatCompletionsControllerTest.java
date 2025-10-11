package io.martinstyk.controller;

import io.martinstyk.model.CreateChatCompletionRequest;
import io.martinstyk.model.message.ChatCompletionRequestMessage;
import io.martinstyk.model.message.ChatCompletionRequestUserMessage;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@MicronautTest
class ChatCompletionsControllerTest {
    private static final String VALID_TOKEN = "sk-123456789012345678901234567890123456789012345678";
    private static final String ENDPOINT = "/v1/chat/completions";
    private static final String DEFAULT_MODEL = "gpt-4";
    private static final String DEFAULT_CONTENT = "Hello";

    private RequestSpecification spec;

    @BeforeEach
    void setUp(RequestSpecification spec) {
        this.spec =
                spec.auth()
                        .oauth2(VALID_TOKEN)
                        .contentType("application/json")
                        .accept("application/json");
    }

    @Test
    void testCreateChatCompletionMissingModel() {
        ChatCompletionRequestUserMessage message = createUserMessage(DEFAULT_CONTENT);
        CreateChatCompletionRequest request = new CreateChatCompletionRequest();
        request.setMessages(List.of(message));

        spec.when()
                .body(request)
                .post(ENDPOINT)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }

    @Test
    void testCreateChatCompletionEmptyModel() {
        CreateChatCompletionRequest request = createBasicRequest("");

        spec.when()
                .body(request)
                .post(ENDPOINT)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }

    @Test
    void testCreateChatCompletionMissingMessages() {
        CreateChatCompletionRequest request = new CreateChatCompletionRequest();
        request.setModel(DEFAULT_MODEL);

        spec.when()
                .body(request)
                .post(ENDPOINT)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }

    @Test
    void testCreateChatCompletionEmptyMessages() {
        CreateChatCompletionRequest request = new CreateChatCompletionRequest();
        request.setModel(DEFAULT_MODEL);
        request.setMessages(List.of());

        spec.when()
                .body(request)
                .post(ENDPOINT)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, 3.0})
    void testCreateChatCompletionInvalidTemperature(double temperature) {
        CreateChatCompletionRequest request = createBasicRequest(DEFAULT_MODEL);
        request.setTemperature(temperature);

        spec.when()
                .body(request)
                .post(ENDPOINT)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, 2.0})
    void testCreateChatCompletionInvalidTopP(double topP) {
        CreateChatCompletionRequest request = createBasicRequest(DEFAULT_MODEL);
        request.setTopP(topP);

        spec.when()
                .body(request)
                .post(ENDPOINT)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-3.0, 3.0})
    void testCreateChatCompletionInvalidFrequencyPenalty(double penalty) {
        CreateChatCompletionRequest request = createBasicRequest(DEFAULT_MODEL);
        request.setFrequencyPenalty(penalty);

        spec.when()
                .body(request)
                .post(ENDPOINT)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-3.0, 3.0})
    void testCreateChatCompletionInvalidPresencePenalty(double penalty) {
        CreateChatCompletionRequest request = createBasicRequest(DEFAULT_MODEL);
        request.setPresencePenalty(penalty);

        spec.when()
                .body(request)
                .post(ENDPOINT)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }

    @Test
    void testCreateChatCompletionInvalidMaxCompletionTokens() {
        CreateChatCompletionRequest request = createBasicRequest(DEFAULT_MODEL);
        request.setMaxCompletionTokens(0);

        spec.when()
                .body(request)
                .post(ENDPOINT)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }

    @Test
    void testCreateChatCompletionMessageWithoutContent() {
        ChatCompletionRequestUserMessage message = createUserMessage(null);
        CreateChatCompletionRequest request = createRequest(DEFAULT_MODEL, List.of(message));

        spec.when()
                .body(request)
                .post(ENDPOINT)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }

    private CreateChatCompletionRequest createBasicRequest(String model) {
        ChatCompletionRequestUserMessage message = createUserMessage(DEFAULT_CONTENT);
        return createRequest(model, List.of(message));
    }

    private CreateChatCompletionRequest createRequest(
            String model, List<ChatCompletionRequestMessage> messages) {
        CreateChatCompletionRequest request = new CreateChatCompletionRequest();
        request.setModel(model);
        request.setMessages(messages);
        return request;
    }

    private ChatCompletionRequestUserMessage createUserMessage(String content) {
        ChatCompletionRequestUserMessage message = new ChatCompletionRequestUserMessage();
        message.setRole("user");
        message.setContent(content);
        return message;
    }
}
