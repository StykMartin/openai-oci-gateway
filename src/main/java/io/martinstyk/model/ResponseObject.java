package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public enum ResponseObject {
    CHAT_COMPLETION("chat.completion"),
    CHAT_COMPLETION_CHUNK("chat.completion.chunk");

    private final String value;

    ResponseObject(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ResponseObject fromValue(String value) {
        for (ResponseObject obj : ResponseObject.values()) {
            if (obj.value.equals(value)) {
                return obj;
            }
        }
        throw new IllegalArgumentException("Unknown response object: " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}
