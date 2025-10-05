package io.martinstyk.model.message;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class ChatCompletionRequestAssistantMessage extends ChatCompletionRequestMessage {

    public ChatCompletionRequestAssistantMessage() {
        super("assistant");
    }
}
