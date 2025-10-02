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

/**
 * Request model for creating chat completions. Based on OpenAI's CreateChatCompletionRequest
 * schema.
 */
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

    /**
 * Creates a new CreateChatCompletionRequest with default field values.
 *
 * Fields are initialized to their default values; the `stream` field defaults to `false`.
 */
public CreateChatCompletionRequest() {}

    /**
     * Creates a request initialized with the provided messages and model.
     *
     * @param messages the chat messages to include in the request; must not be null or empty
     * @param model    the identifier of the model to use for completion; must not be null or empty
     */
    public CreateChatCompletionRequest(List<ChatCompletionRequestMessage> messages, String model) {
        this.messages = messages;
        this.model = model;
    }

    /**
     * Gets the chat messages included in this completion request.
     *
     * @return the list of {@link ChatCompletionRequestMessage} objects in the request
     */
    public List<ChatCompletionRequestMessage> getMessages() {
        return messages;
    }

    /**
     * Set the messages to include in the chat completion request.
     *
     * @param messages the list of messages; must not be null or empty. Each element is validated as a {@code ChatCompletionRequestMessage}.
     */
    public void setMessages(List<ChatCompletionRequestMessage> messages) {
        this.messages = messages;
    }

    /**
     * The model identifier used for the chat completion request.
     *
     * @return the model identifier
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model identifier for the completion request.
     *
     * @param model the model identifier to use; must not be null or empty
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * The maximum number of tokens to generate for the completion.
     *
     * @return the maximum completion tokens, or `null` if not set
     */
    public Integer getMaxCompletionTokens() {
        return maxCompletionTokens;
    }

    /**
     * Set the maximum number of tokens to generate in the completion.
     *
     * @param maxCompletionTokens the maximum number of tokens to generate; must be at least 1
     */
    public void setMaxCompletionTokens(Integer maxCompletionTokens) {
        this.maxCompletionTokens = maxCompletionTokens;
    }

    /**
     * Gets the frequency penalty applied to the generated completion.
     *
     * The value adjusts likelihood of repeated tokens; permitted range is -2 to 2.
     *
     * @return the frequency penalty between -2 and 2, or `null` if not set
     */
    public Double getFrequencyPenalty() {
        return frequencyPenalty;
    }

    /**
     * Set the frequency penalty used to penalize new tokens based on their existing frequency.
     *
     * @param frequencyPenalty the penalty value between -2.0 and 2.0 (inclusive), or `null` to unset
     */
    public void setFrequencyPenalty(Double frequencyPenalty) {
        this.frequencyPenalty = frequencyPenalty;
    }

    /**
     * Gets the sampling temperature used for completion generation.
     *
     * @return the temperature value, where 0.0 produces deterministic output and higher values (up to 2.0) increase randomness; may be {@code null} if unset
     */
    public Double getTemperature() {
        return temperature;
    }

    /**
     * Sets the sampling temperature used to control randomness of generated completions.
     *
     * @param temperature the temperature value in the range 0 to 2 inclusive; higher values increase randomness
     */
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    /**
     * Indicates whether the chat completion should be streamed.
     *
     * @return `true` if the response should be streamed, `false` otherwise.
     */
    public Boolean getStream() {
        return stream;
    }

    /**
     * Enable or disable streaming responses for the completion request.
     *
     * @param stream `true` to enable streaming responses, `false` to disable them
     */
    public void setStream(Boolean stream) {
        this.stream = stream;
    }
}
