package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable
public class ChatCompletionRequestSystemMessage extends ChatCompletionRequestMessage {

    @NotNull(message = "Content cannot be null")
    @NotBlank(message = "Content cannot be blank")
    private String content;

    @JsonCreator
    public ChatCompletionRequestSystemMessage(@JsonProperty("content") String content) {
        super("system");
        this.content = content;
    }

    public ChatCompletionRequestSystemMessage() {
        super("system");
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
