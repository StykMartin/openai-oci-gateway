package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.serde.annotation.Serdeable;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Serdeable
public enum ServiceTier {
    AUTO("auto"),
    DEFAULT("default"),
    FLEX("flex"),
    PRIORITY("priority");

    private final String value;

    ServiceTier(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    private static final Map<String, ServiceTier> VALUE_MAP;

    static {
        VALUE_MAP =
                Arrays.stream(ServiceTier.values())
                        .collect(Collectors.toMap(ServiceTier::getValue, tier -> tier));
    }

    @JsonCreator
    public static ServiceTier fromValue(String value) {
        ServiceTier tier = VALUE_MAP.get(value);
        if (tier == null) {
            throw new IllegalArgumentException("Unknown service tier: " + value);
        }
        return tier;
    }

    @Override
    public String toString() {
        return value;
    }
}
