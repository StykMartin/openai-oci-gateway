package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "role")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ChatCompletionRequestUserMessage.class, name = "user"),
    @JsonSubTypes.Type(value = ChatCompletionRequestSystemMessage.class, name = "system"),
    @JsonSubTypes.Type(value = ChatCompletionRequestAssistantMessage.class, name = "assistant"),
    @JsonSubTypes.Type(value = ChatCompletionRequestDeveloperMessage.class, name = "developer")
})
@Serdeable
public abstract class ChatCompletionRequestMessage {

    @NotNull(message = "Role cannot be null")
    @NotBlank(message = "Role cannot be blank")
    private String role;

    protected ChatCompletionRequestMessage() {}

    protected ChatCompletionRequestMessage(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
