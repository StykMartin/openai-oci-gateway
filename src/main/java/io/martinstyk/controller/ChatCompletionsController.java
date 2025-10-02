package io.martinstyk.controller;

import io.martinstyk.model.ChatCompletionChoice;
import io.martinstyk.model.ChatCompletionResponseMessage;
import io.martinstyk.model.CreateChatCompletionRequest;
import io.martinstyk.model.CreateChatCompletionResponse;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.sse.Event;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * Controller for handling chat completion requests. This is a gateway that proxies requests to the
 * actual AI service.
 */
@Controller("/v1")
@Secured(SecurityRule.IS_ANONYMOUS)
@Validated
public class ChatCompletionsController {

    private static final Logger logger = LoggerFactory.getLogger(ChatCompletionsController.class);

    /**
     * Creates a chat completion. This endpoint proxies the request to the actual AI service.
     *
     * @param request The chat completion request
     * @return The chat completion response
     */
    @Post(value = "/chat/completions", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse<CreateChatCompletionResponse> createChatCompletion(
            @Valid @Body CreateChatCompletionRequest request) {

        logger.info("Received chat completion request for model: {}", request.getModel());
        logger.debug("Request details: {}", request);

        try {
            // TODO: Implement actual proxy logic to forward request to AI service
            CreateChatCompletionResponse response = createMockResponse(request);

            logger.info("Chat completion request processed successfully");
            return HttpResponse.ok(response);

        } catch (Exception e) {
            logger.error("Error processing chat completion request", e);
            return HttpResponse.serverError();
        }
    }

    @Post(value = "/chat/completions", produces = MediaType.TEXT_EVENT_STREAM)
    public Publisher<Event<String>> createChatCompletionSse(
            @Valid @Body CreateChatCompletionRequest request) {
        logger.info("Received chat completion request for model: {}", request.getModel());
        logger.debug("Request details: {}", request);

        return Mono.just(Event.of("This is a SSE mock response from the gateway"));
    }

    private CreateChatCompletionResponse createMockResponse(CreateChatCompletionRequest request) {
        ChatCompletionResponseMessage message = new ChatCompletionResponseMessage();
        message.setContent("This is a mock response from the gateway");

        ChatCompletionChoice choice = new ChatCompletionChoice();
        choice.setIndex(0);
        choice.setMessage(message);
        choice.setFinishReason("stop");

        CreateChatCompletionResponse response = new CreateChatCompletionResponse();
        response.setId("chatcmpl-" + UUID.randomUUID().toString().substring(0, 8));
        response.setCreated(Instant.now().getEpochSecond());
        response.setModel(request.getModel());
        response.setChoices(List.of(choice));

        return response;
    }
}
