package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.martinstyk.model.message.ChatCompletionRequestMessage;
import io.martinstyk.model.tools.Tool;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @JsonProperty("presence_penalty")
    @Min(value = -2, message = "Presence penalty must be at least -2")
    @Max(value = 2, message = "Presence penalty must be at most 2")
    private Double presencePenalty;

    @Min(value = 0, message = "Temperature must be at least 0")
    @Max(value = 2, message = "Temperature must be at most 2")
    private Double temperature;

    @JsonProperty("top_p")
    @Schema(defaultValue = "1.0")
    @Min(value = 0, message = "Top_p must be at least 0")
    @Max(value = 1, message = "Top_p must be at most 1")
    private Double topP = 1.0;

    @JsonProperty("stream")
    @Schema(defaultValue = "false")
    private Boolean stream = false;

    @JsonProperty("service_tier")
    @Schema(defaultValue = "AUTO")
    private ServiceTier serviceTier = ServiceTier.AUTO;

    @JsonProperty("tools")
    @Valid
    private List<Tool> tools;

    @JsonProperty("tool_choice")
    private ToolChoice toolChoice;

    @JsonProperty("truncation")
    @Schema(defaultValue = "DISABLED")
    private Truncation truncation = Truncation.DISABLED;

    @JsonProperty("stream_options")
    @Valid
    private StreamOptions streamOptions;

    @JsonProperty("n")
    @Schema(defaultValue = "1")
    @Min(1)
    private Integer n = 1;

    @JsonProperty("stop")
    private List<String> stop;

    @JsonProperty("logprobs")
    @Min(1)
    private Integer logprobs;

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

    public Double getPresencePenalty() {
        return presencePenalty;
    }

    public void setPresencePenalty(Double presencePenalty) {
        this.presencePenalty = presencePenalty;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getTopP() {
        return topP;
    }

    public void setTopP(Double topP) {
        this.topP = topP;
    }

    public Boolean getStream() {
        return stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }

    public ServiceTier getServiceTier() {
        return serviceTier;
    }

    public void setServiceTier(ServiceTier serviceTier) {
        this.serviceTier = serviceTier;
    }

    public List<Tool> getTools() {
        return tools;
    }

    public void setTools(List<Tool> tools) {
        this.tools = tools;
    }

    public ToolChoice getToolChoice() {
        return toolChoice;
    }

    public void setToolChoice(ToolChoice toolChoice) {
        this.toolChoice = toolChoice;
    }

    public Truncation getTruncation() {
        return truncation;
    }

    public void setTruncation(Truncation truncation) {
        this.truncation = truncation;
    }

    public StreamOptions getStreamOptions() {
        return streamOptions;
    }

    public void setStreamOptions(StreamOptions streamOptions) {
        this.streamOptions = streamOptions;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public List<String> getStop() {
        return stop;
    }

    public void setStop(List<String> stop) {
        this.stop = stop;
    }

    public Integer getLogprobs() {
        return logprobs;
    }

    public void setLogprobs(Integer logprobs) {
        this.logprobs = logprobs;
    }
}
