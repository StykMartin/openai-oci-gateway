package io.martinstyk.model.message;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class ChatCompletionRequestUserMessage extends ChatCompletionRequestMessage {

    public ChatCompletionRequestUserMessage() {
        super("user");
    }
}
