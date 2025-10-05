package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;

@Serdeable
public class ChatCompletionResponseMessage {

    @JsonProperty("role")
    @NotNull(message = "Role cannot be null")
    private String role;

    @JsonProperty("content")
    @NotNull(message = "Content cannot be null")
    private String content;

    public ChatCompletionResponseMessage() {}

    public ChatCompletionResponseMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
