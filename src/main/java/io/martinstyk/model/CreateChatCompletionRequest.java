package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Serdeable
public class CreateChatCompletionRequest {

    @NotNull(message = "Messages cannot be null")
    @NotEmpty(message = "Messages cannot be empty")
    @Valid
    private List<ChatCompletionRequestMessage> messages;

    @NotNull(message = "Model cannot be null")
    @Size(min = 1, message = "Model cannot be empty")
    private String model;

    @JsonProperty("max_completion_tokens")
    @Min(value = 1, message = "Max completion tokens must be at least 1")
    private Integer maxCompletionTokens;

    @JsonProperty("frequency_penalty")
    @Min(value = -2, message = "Frequency penalty must be at least -2")
    @Max(value = 2, message = "Frequency penalty must be at most 2")
    private Double frequencyPenalty;

    @Min(value = 0, message = "Temperature must be at least 0")
    @Max(value = 2, message = "Temperature must be at most 2")
    private Double temperature;

    private Boolean stream = false;

    public CreateChatCompletionRequest() {}

    public CreateChatCompletionRequest(List<ChatCompletionRequestMessage> messages, String model) {
        this.messages = messages;
        this.model = model;
    }

    public List<ChatCompletionRequestMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatCompletionRequestMessage> messages) {
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getMaxCompletionTokens() {
        return maxCompletionTokens;
    }

    public void setMaxCompletionTokens(Integer maxCompletionTokens) {
        this.maxCompletionTokens = maxCompletionTokens;
    }

    public Double getFrequencyPenalty() {
        return frequencyPenalty;
    }

    public void setFrequencyPenalty(Double frequencyPenalty) {
        this.frequencyPenalty = frequencyPenalty;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Boolean getStream() {
        return stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }
}
