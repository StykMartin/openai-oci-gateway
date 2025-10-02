package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Developer message in chat completion request. Used for developer-provided instructions with o1
 * models and newer.
 */
@Serdeable
public class ChatCompletionRequestDeveloperMessage extends ChatCompletionRequestMessage {

    @NotNull(message = "Content cannot be null")
    @NotBlank(message = "Content cannot be blank")
    private String content;

    /**
     * Create a developer chat message with the given content.
     *
     * @param content the message text; must not be null or blank. This value is mapped from the JSON property "content" during deserialization.
     */
    @JsonCreator
    public ChatCompletionRequestDeveloperMessage(@JsonProperty("content") String content) {
        super("developer");
        this.content = content;
    }

    /**
     * Constructs a developer chat message and sets its role to "developer".
     *
     * The `content` field is not initialized by this constructor and should be assigned before validation.
     */
    public ChatCompletionRequestDeveloperMessage() {
        super("developer");
    }

    /**
     * Gets the developer message content.
     *
     * @return the developer message content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the developer message content.
     *
     * @param content the message text; must not be null or blank
     */
    public void setContent(String content) {
        this.content = content;
    }
}
