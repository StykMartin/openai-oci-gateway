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

    @JsonCreator
    public ChatCompletionRequestDeveloperMessage(@JsonProperty("content") String content) {
        super("developer");
        this.content = content;
    }

    public ChatCompletionRequestDeveloperMessage() {
        super("developer");
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
