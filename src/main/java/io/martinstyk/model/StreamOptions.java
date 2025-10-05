package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class StreamOptions {

    @JsonProperty("include_obfuscation")
    private Boolean includeObfuscation;

    public StreamOptions() {}

    public StreamOptions(Boolean includeObfuscation) {
        this.includeObfuscation = includeObfuscation;
    }

    public Boolean getIncludeObfuscation() {
        return includeObfuscation;
    }

    public void setIncludeObfuscation(Boolean includeObfuscation) {
        this.includeObfuscation = includeObfuscation;
    }
}
