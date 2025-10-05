package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public enum ToolChoice {
    NONE("none"),
    AUTO("auto"),
    REQUIRED("required");

    private final String value;

    ToolChoice(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ToolChoice fromValue(String value) {
        for (ToolChoice choice : ToolChoice.values()) {
            if (choice.value.equals(value)) {
                return choice;
            }
        }
        throw new IllegalArgumentException("Unknown tool choice: " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}
