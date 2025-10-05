package io.martinstyk.model.message;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class ChatCompletionRequestDeveloperMessage extends ChatCompletionRequestMessage {

    public ChatCompletionRequestDeveloperMessage() {
        super("developer");
    }
}
