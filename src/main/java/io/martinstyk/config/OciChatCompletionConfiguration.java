package io.martinstyk.config;

import com.oracle.bmc.generativeaiinference.GenerativeAiInferenceClient;
import com.oracle.bmc.generativeaiinference.model.ChatDetails;
import io.martinstyk.mapper.ChatCompletionMapper;
import io.martinstyk.service.OciChatCompletion;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

@Factory
public class OciChatCompletionConfiguration {
    @Singleton
    @Requires(property = "chat-completion.mock.enabled", value = "false", defaultValue = "true")
    public OciChatCompletion ociChatCompletion(
            @NonNull GenerativeAiInferenceClient generativeAiInferenceClient,
            @NonNull ChatDetails chatDetails,
            @NonNull ChatCompletionMapper chatCompletionMapper) {
        return new OciChatCompletion(
                generativeAiInferenceClient, chatDetails, chatCompletionMapper);
    }
}
