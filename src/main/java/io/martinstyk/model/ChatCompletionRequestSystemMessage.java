package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** System message in chat completion request. */
@Serdeable
public class ChatCompletionRequestSystemMessage extends ChatCompletionRequestMessage {

    @NotNull(message = "Content cannot be null")
    @NotBlank(message = "Content cannot be blank")
    private String content;

    /**
     * Create a chat completion message with the "system" role and the provided content.
     *
     * @param content the system message text; must not be null or blank
     */
    @JsonCreator
    public ChatCompletionRequestSystemMessage(@JsonProperty("content") String content) {
        super("system");
        this.content = content;
    }

    /** Constructs a ChatCompletionRequestSystemMessage with the role set to "system". */
    public ChatCompletionRequestSystemMessage() {
        super("system");
    }

    /**
     * Retrieves the system message content.
     *
     * @return the system message content; when validated, it will be non-null and not blank
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the system message content.
     *
     * @param content the message text; must be non-null and not blank
     */
    public void setContent(String content) {
        this.content = content;
    }
}
