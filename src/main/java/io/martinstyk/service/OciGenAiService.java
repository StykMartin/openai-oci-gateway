package io.martinstyk.service;

import com.oracle.bmc.generativeaiinference.GenerativeAiInferenceClient;
import com.oracle.bmc.generativeaiinference.model.ChatDetails;
import com.oracle.bmc.generativeaiinference.model.GenericChatRequest;
import com.oracle.bmc.generativeaiinference.requests.ChatRequest;
import io.martinstyk.mapper.ChatCompletionMapper;
import io.martinstyk.model.CreateChatCompletionRequest;
import io.martinstyk.model.CreateChatCompletionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OciGenAiService {

    private static final Logger logger = LoggerFactory.getLogger(OciGenAiService.class);

    private final GenerativeAiInferenceClient client;
    private final ChatDetails chatDetails;
    private final ChatCompletionMapper chatCompletionMapper;

    public OciGenAiService(
            GenerativeAiInferenceClient generativeAiInferenceClient,
            ChatDetails chatDetails,
            ChatCompletionMapper chatCompletionMapper) {
        this.client = generativeAiInferenceClient;
        this.chatDetails = chatDetails;
        this.chatCompletionMapper = chatCompletionMapper;
    }

    public CreateChatCompletionResponse processChatCompletion(
            CreateChatCompletionRequest openAiRequest) {
        logger.info("Processing chat completion request for model: {}", openAiRequest.getModel());

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
}
