package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Serdeable
public class CreateChatCompletionResponse {

    @NotNull(message = "ID cannot be null")
    private String id;

    @NotNull(message = "Object cannot be null")
    private ResponseObject object = ResponseObject.CHAT_COMPLETION;

    @NotNull(message = "Created timestamp cannot be null")
    private Long created;

    @NotNull(message = "Model cannot be null")
    private String model;

    @NotNull(message = "Choices cannot be null")
    @NotEmpty(message = "Choices cannot be empty")
    @Valid
    private List<ChatCompletionChoice> choices;

    @JsonProperty("usage")
    @Valid
    private Usage usage;

    public CreateChatCompletionResponse() {}

    public CreateChatCompletionResponse(
            String id, Long created, String model, List<ChatCompletionChoice> choices) {
        this.id = id;
        this.created = created;
        this.model = model;
        this.choices = choices;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ResponseObject getObject() {
        return object;
    }

    public void setObject(ResponseObject object) {
        this.object = object;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ChatCompletionChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<ChatCompletionChoice> choices) {
        this.choices = choices;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }
}
