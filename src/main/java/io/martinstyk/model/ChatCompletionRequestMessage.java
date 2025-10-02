package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Base class for chat completion request messages. Uses Jackson annotations for polymorphic
 * deserialization based on the 'role' field.
 */
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

    /**
     * Protected no-argument constructor.
     *
     * <p>Intended for use by subclasses and serialization/deserialization frameworks.
     */
    protected ChatCompletionRequestMessage() {}

    /**
     * Creates a ChatCompletionRequestMessage with the specified role.
     *
     * @param role the message role identifying the actor (e.g., "user", "system", "assistant",
     *     "developer"); must not be null or blank
     */
    protected ChatCompletionRequestMessage(String role) {
        this.role = role;
    }

    /**
     * Retrieves the role of this chat completion request message.
     *
     * @return the role of the message, e.g. "user", "system", "assistant", or "developer"
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of this chat completion request message.
     *
     * @param role the role name; must not be null or blank
     */
    public void setRole(String role) {
        this.role = role;
    }
}
