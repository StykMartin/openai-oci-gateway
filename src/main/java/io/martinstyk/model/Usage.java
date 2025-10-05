package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;

@Serdeable
public class Usage {

    @JsonProperty("prompt_tokens")
    @NotNull(message = "Prompt tokens cannot be null")
    private Integer promptTokens;

    @JsonProperty("completion_tokens")
    @NotNull(message = "Completion tokens cannot be null")
    private Integer completionTokens;

    @JsonProperty("total_tokens")
    @NotNull(message = "Total tokens cannot be null")
    private Integer totalTokens;

    public Usage() {}

    public Usage(Integer promptTokens, Integer completionTokens, Integer totalTokens) {
        this.promptTokens = promptTokens;
        this.completionTokens = completionTokens;
        this.totalTokens = totalTokens;
    }

    public Integer getPromptTokens() {
        return promptTokens;
    }

    public void setPromptTokens(Integer promptTokens) {
        this.promptTokens = promptTokens;
    }

    public Integer getCompletionTokens() {
        return completionTokens;
    }

    public void setCompletionTokens(Integer completionTokens) {
        this.completionTokens = completionTokens;
    }

    public Integer getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(Integer totalTokens) {
        this.totalTokens = totalTokens;
    }
}
