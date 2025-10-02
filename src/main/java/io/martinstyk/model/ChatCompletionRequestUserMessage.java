package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** User message in chat completion request. */
@Serdeable
public class ChatCompletionRequestUserMessage extends ChatCompletionRequestMessage {

    @NotNull(message = "Content cannot be null")
    @NotBlank(message = "Content cannot be blank")
    private String content;

    /**
     * Create a chat completion message with the role set to "user" and the provided content.
     *
     * @param content the message text; must not be null or blank
     */
    @JsonCreator
    public ChatCompletionRequestUserMessage(@JsonProperty("content") String content) {
        super("user");
        this.content = content;
    }

    /**
     * Creates a new ChatCompletionRequestUserMessage with the role set to "user".
     *
     * <p>Intended for use by serialization/deserialization frameworks. </p>
     */
    public ChatCompletionRequestUserMessage() {
        super("user");
    }

    /**
     * The user's message content.
     *
     * @return the content of the user message
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the message content.
     *
     * @param content the message text; must not be null or blank (validated as "Content cannot be null" and "Content cannot be blank")
     */
    public void setContent(String content) {
        this.content = content;
    }
}
