package io.martinstyk.service;

import com.oracle.bmc.generativeaiinference.GenerativeAiInferenceClient;
import com.oracle.bmc.generativeaiinference.model.ChatDetails;
import com.oracle.bmc.generativeaiinference.model.GenericChatRequest;
import com.oracle.bmc.generativeaiinference.requests.ChatRequest;
import io.martinstyk.mapper.ChatCompletionMapper;
import io.martinstyk.model.CreateChatCompletionRequest;
import io.martinstyk.model.CreateChatCompletionResponse;
import io.micronaut.http.sse.Event;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OciChatCompletion implements ChatCompletion {

    private static final Logger logger = LoggerFactory.getLogger(OciChatCompletion.class);

    private final GenerativeAiInferenceClient client;
    private final ChatDetails chatDetails;
    private final ChatCompletionMapper chatCompletionMapper;

    public OciChatCompletion(
            GenerativeAiInferenceClient generativeAiInferenceClient,
            ChatDetails chatDetails,
            ChatCompletionMapper chatCompletionMapper) {
        this.client = generativeAiInferenceClient;
        this.chatDetails = chatDetails;
        this.chatCompletionMapper = chatCompletionMapper;
    }

    @Override
    public CreateChatCompletionResponse processChatCompletion(
            CreateChatCompletionRequest openAiRequest) {
        logger.info(
                "Processing OCI GenAI chat completion request for model: {}",
                openAiRequest.getModel());

        GenericChatRequest genericChatRequest =
                chatCompletionMapper.toGenericChatRequest(openAiRequest);

        ChatDetails.Builder chatDetailsBuilder = ChatDetails.builder();
        chatDetailsBuilder.copy(chatDetails);
        chatDetailsBuilder.chatRequest(genericChatRequest);

        ChatRequest.Builder chatRequestBuilder = ChatRequest.builder();
        chatRequestBuilder.chatDetails(chatDetailsBuilder.build());

        client.chat(chatRequestBuilder.build());

        throw new UnsupportedOperationException("Chat completion processing not yet implemented");
    }

    @Override
    public Publisher<Event<String>> processStreamingChatCompletion(
            CreateChatCompletionRequest request) {
        logger.info(
                "Processing OCI GenAI streaming chat completion request for model: {}",
                request.getModel());

        throw new UnsupportedOperationException(
                "Streaming chat completion processing not yet implemented");
    }
}
