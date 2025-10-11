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
                    "Creates a completion for the provided chat conversation. Compatible with OpenAI Chat Completions API format. "
                            + "Supports both streaming (with stream=true) and non-streaming responses.",
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
                                            examples = {
                                                @ExampleObject(
                                                        name = "Non-Streaming Request",
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
                    """),
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
                    """)
                                            })))
    @ApiResponse(
            responseCode = "200",
            description = "Successful chat completion (format depends on stream parameter)",
            content = {
                @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = CreateChatCompletionResponse.class),
                        examples =
                                @ExampleObject(
                                        name = "Non-Streaming Response",
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
                """)),
                @Content(
                        mediaType = "text/event-stream",
                        examples =
                                @ExampleObject(
                                        name = "Streaming Response",
                                        value =
                                                """
                data: {"id":"chatcmpl-abc123","object":"chat.completion.chunk","created":1759737038,"model":"gpt-5-nano-2025-08-07","service_tier":"default","system_fingerprint":null,"choices":[{"index":0,"delta":{"role":"assistant","content":"","refusal":null},"finish_reason":null}],"obfuscation":"hG"}
                data: {"id":"chatcmpl-abc123","object":"chat.completion.chunk","created":1759737038,"model":"gpt-5-nano-2025-08-07","service_tier":"default","system_fingerprint":null,"choices":[{"index":0,"delta":{"content":"Hi"},"finish_reason":null}],"obfuscation":"6E"}
                data: {"id":"chatcmpl-abc123","object":"chat.completion.chunk","created":1759737038,"model":"gpt-5-nano-2025-08-07","service_tier":"default","system_fingerprint":null,"choices":[{"index":0,"delta":{"content":"."},"finish_reason":null}],"obfuscation":"5Yn"}
                data: {"id":"chatcmpl-abc123","object":"chat.completion.chunk","created":1759737038,"model":"gpt-5-nano-2025-08-07","service_tier":"default","system_fingerprint":null,"choices":[{"index":0,"delta":{},"finish_reason":"stop"}],"obfuscation":"o8xbNfSakTaUNF"}
                data: [DONE]
                """))
            })
    @ExecuteOn(TaskExecutors.BLOCKING)
    public HttpResponse<?> createChatCompletion(@Valid @Body CreateChatCompletionRequest request) {

        logger.info(
                "Received chat completion request for model: {} (stream={})",
                request.getModel(),
                request.getStream());
        logger.debug("Request details: {}", request);

        if (Boolean.TRUE.equals(request.getStream())) {
            logger.info("Processing streaming chat completion request");
            Publisher<Event<String>> stream = createMockStreamingResponse(request);
            return HttpResponse.ok(stream).contentType(MediaType.TEXT_EVENT_STREAM_TYPE);
        } else {
            logger.info("Processing non-streaming chat completion request");
            CreateChatCompletionResponse response = createMockResponse(request);
            logger.info("Chat completion request processed successfully");
            return HttpResponse.ok(response).contentType(MediaType.APPLICATION_JSON_TYPE);
        }
    }

    private Publisher<Event<String>> createMockStreamingResponse(CreateChatCompletionRequest request) {
        CreateChatCompletionResponse mockResponse = createMockResponse(request);
        String content = "";
        if (mockResponse.getChoices() != null && !mockResponse.getChoices().isEmpty()) {
            ChatCompletionChoice choice = mockResponse.getChoices().getFirst();
            if (choice.getMessage() != null && choice.getMessage().getContent() != null) {
                content = choice.getMessage().getContent();
            }
        }
        List<String> chunks = getChunks(content);
        Flux<Event<String>> chunkFlux = Flux
                .fromIterable(chunks)
                .delayElements(Duration.ofMillis(500))
                .map(Event::of);

        Flux<Event<String>> doneFlux = Flux.just(Event.of("[DONE]"));
        return Flux.concat(chunkFlux, doneFlux);
    }

    private static List<String> getChunks(String content) {
        String[] words = content.split(" ");
        List<String> chunks = new java.util.ArrayList<>();
        StringBuilder chunkBuilder = new StringBuilder();
        for (String word : words) {
            if (!chunkBuilder.isEmpty()) {
                chunkBuilder.append(" ");
            }
            chunkBuilder.append(word);
            chunks.add(chunkBuilder.toString());
            chunkBuilder.setLength(0);
        }
        if (!chunkBuilder.isEmpty()) {
            chunks.add(chunkBuilder.toString());
        }
        return chunks;
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
