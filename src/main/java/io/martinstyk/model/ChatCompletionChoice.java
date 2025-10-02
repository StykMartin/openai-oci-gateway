package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/** Represents a choice in the chat completion response. */
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

    /**
 * Creates a new, empty ChatCompletionChoice instance.
 *
 * <p>Fields are initialized to null and should be set via the available setters or the
 * parameterized constructor.</p>
 */
public ChatCompletionChoice() {}

    /**
     * Create a ChatCompletionChoice with the specified index, message, and finish reason.
     *
     * @param index the position of this choice within the completion response
     * @param message the message associated with this choice
     * @param finishReason the reason the completion finished (serialized as `finish_reason`)
     */
    public ChatCompletionChoice(
            Integer index, ChatCompletionResponseMessage message, String finishReason) {
        this.index = index;
        this.message = message;
        this.finishReason = finishReason;
    }

    /**
     * Returns the position of this choice within the completion's choice list.
     *
     * @return the index of the choice
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * Set the position of this choice within the completion response.
     *
     * @param index the position of the choice within the response
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * Gets the message associated with this chat completion choice.
     *
     * @return the ChatCompletionResponseMessage for this choice
     */
    public ChatCompletionResponseMessage getMessage() {
        return message;
    }

    /**
     * Sets the message associated with this chat completion choice.
     *
     * @param message the ChatCompletionResponseMessage for this choice; must not be null
     */
    public void setMessage(ChatCompletionResponseMessage message) {
        this.message = message;
    }

    /**
     * Retrieve the finish reason for this chat completion choice.
     *
     * @return the finish reason for this choice
     */
    public String getFinishReason() {
        return finishReason;
    }

    /**
     * Sets the reason the completion finished.
     *
     * @param finishReason the completion finish reason (corresponds to JSON property `finish_reason`)
     */
    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }
}
