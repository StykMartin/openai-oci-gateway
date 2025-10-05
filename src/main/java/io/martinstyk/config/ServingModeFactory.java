package io.martinstyk.config;

import com.oracle.bmc.generativeaiinference.model.DedicatedServingMode;
import com.oracle.bmc.generativeaiinference.model.OnDemandServingMode;
import com.oracle.bmc.generativeaiinference.model.ServingMode;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class ServingModeFactory {

    @Singleton
    public ServingMode createServingMode(GenAiProperties genAiProperties) {
        ServingModeType servingModeConfig = genAiProperties.getServingMode();

        if (servingModeConfig == ServingModeType.DEDICATED) {
            String endpointId = genAiProperties.getEndpointId();
            return DedicatedServingMode.builder().endpointId(endpointId).build();
        } else {
            return OnDemandServingMode.builder().build();
        }
    }
}
