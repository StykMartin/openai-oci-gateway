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

    public ChatCompletionResponseMessage() {}

    public ChatCompletionResponseMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRefusal() {
        return refusal;
    }

    public void setRefusal(String refusal) {
        this.refusal = refusal;
    }

    public Object getToolCalls() {
        return toolCalls;
    }

    public void setToolCalls(Object toolCalls) {
        this.toolCalls = toolCalls;
    }

    public Object getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Object annotations) {
        this.annotations = annotations;
    }
}
