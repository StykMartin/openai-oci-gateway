package io.martinstyk.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.validation.Validated;
import jakarta.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties("genai")
@Validated
public class GenAiProperties {

    @NotBlank(message = "Compartment ID cannot be blank")
    private String compartmentId;

    private Map<String, String> modelMapping = new HashMap<>();

    public String getCompartmentId() {
        return compartmentId;
    }

    public void setCompartmentId(String compartmentId) {
        this.compartmentId = compartmentId;
    }

    public Map<String, String> getModelMapping() {
        return modelMapping;
    }

    public void setModelMapping(@NonNull Map<String, String> modelMapping) {
        this.modelMapping = modelMapping;
    }
}
