package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Serdeable
public class ChatCompletionChoice {

    @NotNull(message = "Index cannot be null")
    private Integer index;

    @NotNull(message = "Message cannot be null")
    @Valid
    private ChatCompletionResponseMessage message;

    @NotNull(message = "Finish reason cannot be null")
    @JsonProperty("finish_reason")
    private FinishReason finishReason;

    @JsonProperty("logprobs")
    @Valid
    private LogProbs logprobs;

    public ChatCompletionChoice() {}

    public ChatCompletionChoice(
            Integer index, ChatCompletionResponseMessage message, FinishReason finishReason) {
        this.index = index;
        this.message = message;
        this.finishReason = finishReason;
    }

    public ChatCompletionChoice(
            Integer index,
            ChatCompletionResponseMessage message,
            FinishReason finishReason,
            LogProbs logprobs) {
        this.index = index;
        this.message = message;
        this.finishReason = finishReason;
        this.logprobs = logprobs;
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

    public FinishReason getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(FinishReason finishReason) {
        this.finishReason = finishReason;
    }

    public LogProbs getLogprobs() {
        return logprobs;
    }

    public void setLogprobs(LogProbs logprobs) {
        this.logprobs = logprobs;
    }
}
