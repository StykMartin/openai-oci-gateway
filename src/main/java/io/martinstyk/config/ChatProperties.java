package io.martinstyk.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.validation.Validated;
import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties("chat")
@Validated
public class ChatProperties {

    @NotBlank(message = "Compartment ID cannot be blank")
    private String compartmentId;

    public String getCompartmentId() {
        return compartmentId;
    }

    public void setCompartmentId(String compartmentId) {
        this.compartmentId = compartmentId;
    }
}
