package io.martinstyk.model;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Response model for chat completion creation. Based on OpenAI's CreateChatCompletionResponse
 * schema.
 */
@Serdeable
public class CreateChatCompletionResponse {

    @NotNull(message = "ID cannot be null")
    private String id;

    @NotNull(message = "Object cannot be null")
    private String object = "chat.completion";

    @NotNull(message = "Created timestamp cannot be null")
    private Long created;

    @NotNull(message = "Model cannot be null")
    private String model;

    @NotNull(message = "Choices cannot be null")
    @NotEmpty(message = "Choices cannot be empty")
    @Valid
    private List<ChatCompletionChoice> choices;

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

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
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
}
