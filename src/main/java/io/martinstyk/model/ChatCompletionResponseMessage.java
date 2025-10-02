package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

/** Response message from chat completion. */
@Serdeable
public class ChatCompletionResponseMessage {

    private String content;
    private String refusal;

    @JsonProperty("tool_calls")
    private Object toolCalls;

    private Object annotations;

    /** Constructs a ChatCompletionResponseMessage with all fields unset. */
    public ChatCompletionResponseMessage() {}

    /**
     * Creates a ChatCompletionResponseMessage with the specified content.
     *
     * @param content the message content
     */
    public ChatCompletionResponseMessage(String content) {
        this.content = content;
    }

    /**
     * The textual content of the chat completion message.
     *
     * @return the message content, or null if not set
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the message content.
     *
     * @param content the message text to store
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Retrieve the refusal message associated with this chat response.
     *
     * @return the refusal message, or `null` if none is present
     */
    public String getRefusal() {
        return refusal;
    }

    /**
     * Set the refusal message associated with this response.
     *
     * @param refusal the refusal message or rationale; may be {@code null} to clear any existing
     *     refusal
     */
    public void setRefusal(String refusal) {
        this.refusal = refusal;
    }

    /**
     * Retrieves the tool calls attached to this message.
     *
     * @return the tool calls mapped from the JSON property "tool_calls", or {@code null} if not
     *     present
     */
    public Object getToolCalls() {
        return toolCalls;
    }

    /**
     * Sets the tool call payload mapped from the JSON property "tool_calls".
     *
     * @param toolCalls the tool call data to assign to this message
     */
    public void setToolCalls(Object toolCalls) {
        this.toolCalls = toolCalls;
    }

    /**
     * Gets the annotations attached to this response message.
     *
     * @return the annotations object, or {@code null} if none
     */
    public Object getAnnotations() {
        return annotations;
    }

    /**
     * Set additional metadata or tooling annotations associated with this message.
     *
     * @param annotations a metadata object (for example a Map or List) representing the
     *     `annotations` field from the serialized response; may be null
     */
    public void setAnnotations(Object annotations) {
        this.annotations = annotations;
    }
}
