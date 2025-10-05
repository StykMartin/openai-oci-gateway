package io.martinstyk.controller;

import io.martinstyk.model.ChatCompletionChoice;
import io.martinstyk.model.ChatCompletionResponseMessage;
import io.martinstyk.model.CreateChatCompletionRequest;
import io.martinstyk.model.CreateChatCompletionResponse;
import io.martinstyk.model.FinishReason;
import io.martinstyk.model.ResponseObject;
import io.martinstyk.model.Usage;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.sse.Event;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

@Controller("/v1")
@Secured(SecurityRule.IS_AUTHENTICATED)
@Validated
@Tag(name = "Chat Completions", description = "OpenAI-compatible chat completion endpoints")
public class ChatCompletionsController {

    private static final Logger logger = LoggerFactory.getLogger(ChatCompletionsController.class);

    @Post(value = "/chat/completions", consumes = MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Create chat completion",
            description =
                    "Creates a completion for the provided chat conversation. Compatible with OpenAI Chat Completions API format.",
            requestBody =
                    @RequestBody(
                            description = "Chat completion request",
                            content =
                                    @Content(
                                            mediaType = "application/json",
                                            schema =
                                                    @Schema(
                                                            implementation =
                                                                    CreateChatCompletionRequest
                                                                            .class),
                                            examples =
                                                    @ExampleObject(
                                                            name = "Example Request",
                                                            value =
                                                                    """
                    {
                      "model": "gpt-5",
                      "messages": [
                        {
                          "role": "developer",
                          "content": "You are a helpful assistant."
                        },
                        {
                          "role": "user",
                          "content": "Hello!"
                        }
                      ]
                    }
                    """))))
    @ApiResponse(
            responseCode = "200",
            description = "Successful chat completion",
            content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateChatCompletionResponse.class),
                            examples =
                                    @ExampleObject(
                                            name = "Example Response",
                                            value =
                                                    """
                {
                  "id": "chatcmpl-abc123",
                  "object": "chat.completion",
                  "created": 1234567890,
                  "model": "gpt-5",
                  "choices": [
                    {
                      "index": 0,
                      "message": {
                        "role": "assistant",
                        "content": "Hello! How can I help you today?"
                      },
                      "finish_reason": "stop"
                    }
                  ],
                  "usage": {
                    "prompt_tokens": 10,
                    "completion_tokens": 5,
                    "total_tokens": 15
                  }
                }
                """)))
    @ExecuteOn(TaskExecutors.BLOCKING)
    public HttpResponse<CreateChatCompletionResponse> createChatCompletion(
            @Valid @Body CreateChatCompletionRequest request) {

        logger.info("Received chat completion request for model: {}", request.getModel());
        logger.debug("Request details: {}", request);

        try {
            CreateChatCompletionResponse response = createMockResponse(request);

            logger.info("Chat completion request processed successfully");
            return HttpResponse.ok(response);

        } catch (Exception e) {
            logger.error("Error processing chat completion request", e);
            return HttpResponse.serverError();
        }
    }

    @Post(
            value = "/chat/completions",
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.TEXT_EVENT_STREAM)
    @Operation(
            hidden = true,
            summary = "Create streaming chat completion",
            description =
                    "Creates a streaming completion using Server-Sent Events (SSE). Compatible with OpenAI streaming format.",
            requestBody =
                    @RequestBody(
                            description = "Chat completion request with stream=true",
                            content =
                                    @Content(
                                            mediaType = "application/json",
                                            schema =
                                                    @Schema(
                                                            implementation =
                                                                    CreateChatCompletionRequest
                                                                            .class),
                                            examples =
                                                    @ExampleObject(
                                                            name = "Streaming Request",
                                                            value =
                                                                    """
                    {
                      "model": "gpt-5",
                      "stream": true,
                      "messages": [
                        {
                          "role": "developer",
                          "content": "You are a helpful assistant."
                        },
                        {
                          "role": "user",
                          "content": "Hello!"
                        }
                      ]
                    }
                    """))))
    @ApiResponse(
            responseCode = "200",
            description = "Streaming chat completion via SSE",
            content =
                    @Content(
                            mediaType = "text/event-stream",
                            examples =
                                    @ExampleObject(
                                            name = "SSE Response",
                                            value =
                                                    """
                data: {"id":"chatcmpl-abc123","object":"chat.completion.chunk","created":1234567890,"model":"gpt-5","choices":[{"index":0,"delta":{"content":"Hello"},"finish_reason":null}]}
                data: {"id":"chatcmpl-abc123","object":"chat.completion.chunk","created":1234567890,"model":"gpt-5","choices":[{"index":0,"delta":{"content":" there!"},"finish_reason":null}]}

                """)))
    @ExecuteOn(TaskExecutors.BLOCKING)
    public Publisher<Event<String>> createChatCompletionSse(
            @Valid @Body CreateChatCompletionRequest request) {
        logger.info("Received chat completion request for model: {}", request.getModel());
        logger.debug("Request details: {}", request);

        return Flux.interval(Duration.ofSeconds(1)).take(10).map(i -> Event.of("Chunk " + (i + 1)));
    }

    private CreateChatCompletionResponse createMockResponse(CreateChatCompletionRequest request) {
        ChatCompletionResponseMessage message = new ChatCompletionResponseMessage();
        message.setRole("assistant");
        message.setContent("This is a mock response from the gateway");

        ChatCompletionChoice choice = new ChatCompletionChoice();
        choice.setIndex(0);
        choice.setMessage(message);
        choice.setFinishReason(FinishReason.STOP);

        CreateChatCompletionResponse response = new CreateChatCompletionResponse();
        response.setId("chatcmpl-" + UUID.randomUUID().toString().substring(0, 8));
        response.setObject(ResponseObject.CHAT_COMPLETION);
        response.setCreated(Instant.now().getEpochSecond());
        response.setModel(request.getModel());
        response.setChoices(List.of(choice));

        Usage usage = new Usage();
        usage.setPromptTokens(10);
        usage.setCompletionTokens(5);
        usage.setTotalTokens(15);
        response.setUsage(usage);

        return response;
    }
}
