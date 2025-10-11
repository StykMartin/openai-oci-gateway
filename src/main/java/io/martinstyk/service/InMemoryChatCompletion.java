package io.martinstyk.service;

import io.martinstyk.model.ChatCompletionChoice;
import io.martinstyk.model.ChatCompletionResponseMessage;
import io.martinstyk.model.CreateChatCompletionRequest;
import io.martinstyk.model.CreateChatCompletionResponse;
import io.martinstyk.model.FinishReason;
import io.martinstyk.model.ResponseObject;
import io.martinstyk.model.Usage;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.http.sse.Event;
import jakarta.inject.Singleton;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

@Singleton
@Primary
@Requires(env = Environment.DEVELOPMENT)
public class InMemoryChatCompletion implements ChatCompletion {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryChatCompletion.class);

    @Override
    public CreateChatCompletionResponse processChatCompletion(CreateChatCompletionRequest request) {
        logger.info("Processing mock chat completion request for model: {}", request.getModel());

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

    @Override
    public Publisher<Event<String>> processStreamingChatCompletion(
            CreateChatCompletionRequest request) {
        logger.info(
                "Processing mock streaming chat completion request for model: {}",
                request.getModel());

        CreateChatCompletionResponse mockResponse = processChatCompletion(request);
        String content = "";
        if (mockResponse.getChoices() != null && !mockResponse.getChoices().isEmpty()) {
            ChatCompletionChoice choice = mockResponse.getChoices().getFirst();
            if (choice.getMessage() != null && choice.getMessage().getContent() != null) {
                content = choice.getMessage().getContent();
            }
        }

        List<String> chunks = splitIntoChunks(content);
        Flux<Event<String>> chunkFlux =
                Flux.fromIterable(chunks).delayElements(Duration.ofMillis(500)).map(Event::of);

        Flux<Event<String>> doneFlux = Flux.just(Event.of("[DONE]"));
        return Flux.concat(chunkFlux, doneFlux);
    }

    private List<String> splitIntoChunks(String content) {
        String[] words = content.split(" ");
        List<String> chunks = new ArrayList<>();
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
}
