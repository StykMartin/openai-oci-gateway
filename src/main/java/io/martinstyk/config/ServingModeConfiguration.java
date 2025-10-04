package io.martinstyk.config;

import com.oracle.bmc.generativeaiinference.model.OnDemandServingMode;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class ServingModeConfiguration {

    @Singleton
    public OnDemandServingMode servingMode() {
        return OnDemandServingMode.builder().build();
    }
}
