package io.martinstyk.service;

import io.martinstyk.model.CreateChatCompletionRequest;
import io.martinstyk.model.CreateChatCompletionResponse;
import io.micronaut.http.sse.Event;
import org.reactivestreams.Publisher;

public interface ChatCompletion {
    CreateChatCompletionResponse processChatCompletion(CreateChatCompletionRequest request);

    Publisher<Event<String>> processStreamingChatCompletion(CreateChatCompletionRequest request);
}
