package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Serdeable
public class ChatCompletionChoice {

    @NotNull(message = "Index cannot be null")
    private Integer index;

    @NotNull(message = "Message cannot be null")
    @Valid
    private ChatCompletionResponseMessage message;

    @NotNull(message = "Finish reason cannot be null")
    @JsonProperty("finish_reason")
    private String finishReason;

    public ChatCompletionChoice() {}

    public ChatCompletionChoice(
            Integer index, ChatCompletionResponseMessage message, String finishReason) {
        this.index = index;
        this.message = message;
        this.finishReason = finishReason;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public ChatCompletionResponseMessage getMessage() {
        return message;
    }

    public void setMessage(ChatCompletionResponseMessage message) {
        this.message = message;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }
}
