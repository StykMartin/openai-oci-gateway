package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.serde.annotation.Serdeable;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Serdeable
public enum FinishReason {
    STOP("stop"),
    LENGTH("length"),
    CONTENT_FILTER("content_filter"),
    TOOL_CALLS("tool_calls"),
    FUNCTION_CALL("function_call");

    private final String value;

    FinishReason(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    private static final Map<String, FinishReason> VALUE_MAP;

    static {
        VALUE_MAP =
                Arrays.stream(FinishReason.values())
                        .collect(Collectors.toMap(FinishReason::getValue, reason -> reason));
    }

    @JsonCreator
    public static FinishReason fromValue(String value) {
        FinishReason reason = VALUE_MAP.get(value);
        if (reason == null) {
            throw new IllegalArgumentException("Unknown finish reason: " + value);
        }
        return reason;
    }

    @Override
    public String toString() {
        return value;
    }
}
