package io.martinstyk.service;

import com.oracle.bmc.generativeaiinference.GenerativeAiInferenceClient;
import com.oracle.bmc.generativeaiinference.model.ChatDetails;
import io.martinstyk.model.CreateChatCompletionRequest;
import io.martinstyk.model.CreateChatCompletionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OciGenAiService {

    private static final Logger logger = LoggerFactory.getLogger(OciGenAiService.class);

    private final GenerativeAiInferenceClient generativeAiInferenceClient;
    private final ChatDetails chatDetails;

    public OciGenAiService(
            GenerativeAiInferenceClient generativeAiInferenceClient, ChatDetails chatDetails) {
        this.generativeAiInferenceClient = generativeAiInferenceClient;
        this.chatDetails = chatDetails;
    }

    public CreateChatCompletionResponse processChatCompletion(
            CreateChatCompletionRequest openAiRequest) {
        logger.info("Processing chat completion request for model: {}", openAiRequest.getModel());

        // TODO: Implement OpenAI -> OCI GenAI message mapping
        // TODO: Implement OCI GenAI API call logic
        throw new UnsupportedOperationException("Chat completion processing not yet implemented");
    }
}
