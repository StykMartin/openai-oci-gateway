package io.martinstyk.config;

import com.oracle.bmc.generativeaiinference.model.ChatDetails;
import com.oracle.bmc.generativeaiinference.model.ServingMode;
import io.micronaut.context.annotation.Factory;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

@Factory
public class ChatDetailsConfiguration {

    @Singleton
    public ChatDetails chatDetails(
            @NonNull ServingMode servingMode, @NonNull GenAiProperties genAiProperties) {
        return ChatDetails.builder()
                .servingMode(servingMode)
                .compartmentId(genAiProperties.getCompartmentId())
                .build();
    }
}
