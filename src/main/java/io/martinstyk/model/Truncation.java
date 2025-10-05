package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public enum Truncation {
    AUTO("auto"),
    DISABLED("disabled");

    private final String value;

    Truncation(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Truncation fromValue(String value) {
        for (Truncation truncation : Truncation.values()) {
            if (truncation.value.equals(value)) {
                return truncation;
            }
        }
        throw new IllegalArgumentException("Unknown truncation: " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}
