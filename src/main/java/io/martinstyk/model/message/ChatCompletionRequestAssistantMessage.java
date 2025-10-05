package io.martinstyk.model.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable
public class ChatCompletionRequestAssistantMessage extends ChatCompletionRequestMessage {

    @JsonProperty("content")
    @NotNull(message = "Content cannot be null")
    @NotBlank(message = "Content cannot be blank")
    private String content;

    @JsonCreator
    public ChatCompletionRequestAssistantMessage(@JsonProperty("content") String content) {
        super("assistant");
        this.content = content;
    }

    public ChatCompletionRequestAssistantMessage() {
        super("assistant");
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
