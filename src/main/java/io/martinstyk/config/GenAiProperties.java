package io.martinstyk.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.validation.Validated;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties("genai")
@Validated
public class GenAiProperties {

    @NotBlank(message = "Compartment ID cannot be blank")
    private String compartmentId;

    private ServingModeType servingMode = ServingModeType.ON_DEMAND;
    private String endpointId;

    public String getCompartmentId() {
        return compartmentId;
    }

    public void setCompartmentId(String compartmentId) {
        this.compartmentId = compartmentId;
    }

    public ServingModeType getServingMode() {
        return servingMode;
    }

    public void setServingMode(ServingModeType servingMode) {
        this.servingMode = servingMode;
    }

    public String getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(String endpointId) {
        this.endpointId = endpointId;
    }

    @AssertTrue(message = "Endpoint ID is required when serving mode is DEDICATED")
    public boolean isEndpointIdValidForDedicatedMode() {
        if (servingMode == ServingModeType.DEDICATED) {
            return endpointId != null && !endpointId.trim().isEmpty();
        }
        return true;
    }
}
