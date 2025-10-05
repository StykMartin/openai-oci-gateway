package io.martinstyk.model.message;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class ChatCompletionRequestSystemMessage extends ChatCompletionRequestMessage {

    public ChatCompletionRequestSystemMessage() {
        super("system");
    }
}
