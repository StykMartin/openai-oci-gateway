package io.martinstyk.config;

import com.oracle.bmc.generativeaiinference.GenerativeAiInferenceClient;
import com.oracle.bmc.generativeaiinference.model.ChatDetails;
import io.martinstyk.service.OciGenAiService;
import io.micronaut.context.annotation.Factory;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

@Factory
public class OciGenAiConfiguration {
    @Singleton
    public OciGenAiService service(
            @NonNull GenerativeAiInferenceClient generativeAiInferenceClient,
            @NonNull ChatDetails chatDetails) {
        return new OciGenAiService(generativeAiInferenceClient, chatDetails);
    }
}
