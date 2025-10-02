package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** Assistant message in chat completion request. */
@Serdeable
public class ChatCompletionRequestAssistantMessage extends ChatCompletionRequestMessage {

    @NotNull(message = "Content cannot be null")
    @NotBlank(message = "Content cannot be blank")
    private String content;

    /**
     * Create a new chat message with the assistant role using the provided content.
     *
     * The content must be non-null and not blank; validation constraints enforce this.
     *
     * @param content the assistant message text; must not be `null` or empty/blank
     */
    @JsonCreator
    public ChatCompletionRequestAssistantMessage(@JsonProperty("content") String content) {
        super("assistant");
        this.content = content;
    }

    /**
     * Constructs a ChatCompletionRequestAssistantMessage with the role set to "assistant".
     *
     * The message content is not initialized by this constructor and must be set separately;
     * the content is validated to be non-null and non-blank.
     */
    public ChatCompletionRequestAssistantMessage() {
        super("assistant");
    }

    /**
     * The assistant message content.
     *
     * @return the assistant message content; will be a non-null, non-blank string as validated by the class constraints
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the assistant message content.
     *
     * @param content the message content; must not be null or blank
     */
    public void setContent(String content) {
        this.content = content;
    }
}
