package io.martinstyk.model.tools;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable
public class FunctionTool extends Tool {
    @JsonProperty("name")
    @NotBlank(message = "Function name cannot be blank")
    private String name;

    @JsonProperty("parameters")
    @NotNull(message = "Function parameters cannot be null")
    private Object parameters;

    @JsonProperty("strict")
    @NotNull
    private Boolean strict = true;

    @JsonProperty("description")
    private String description;

    public FunctionTool() {
        super("function");
    }

    @JsonCreator
    public FunctionTool(
            @JsonProperty("name") String name,
            @JsonProperty("parameters") Object parameters,
            @JsonProperty("strict") Boolean strict,
            @JsonProperty("description") String description) {
        super("function");
        this.name = name;
        this.parameters = parameters;
        this.strict = (strict != null) ? strict : Boolean.TRUE;
        this.description = description;
    }

    public boolean isStrict() {
        return Boolean.TRUE.equals(strict);
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
