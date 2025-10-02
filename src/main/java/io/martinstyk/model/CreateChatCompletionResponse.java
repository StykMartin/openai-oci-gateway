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

    /**
 * Creates a new, empty CreateChatCompletionResponse instance.
 *
 * <p>Required for frameworks that instantiate objects via a no-argument constructor (for example, serializers/deserializers).</p>
 */
public CreateChatCompletionResponse() {}

    /**
     * Constructs a CreateChatCompletionResponse populated with the provided identifier, timestamp, model, and choices.
     *
     * @param id      the response identifier
     * @param created the creation timestamp
     * @param model   the model identifier used to generate the completion
     * @param choices the list of completion choices included in the response
     */
    public CreateChatCompletionResponse(
            String id, Long created, String model, List<ChatCompletionChoice> choices) {
        this.id = id;
        this.created = created;
        this.model = model;
        this.choices = choices;
    }

    /**
     * Returns the unique identifier of the chat completion response.
     *
     * @return the response `id`
     */
    public String getId() {
        return id;
    }

    /**
     * Set the unique identifier of the chat completion response.
     *
     * @param id the unique identifier for this response
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * The response object's type identifier.
     *
     * @return the object type string (typically "chat.completion")
     */
    public String getObject() {
        return object;
    }

    /**
     * Sets the response object type identifier (for example, "chat.completion").
     *
     * @param object the object type identifier to set
     */
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * Gets the creation timestamp of the response.
     *
     * @return the creation timestamp in seconds since the Unix epoch
     */
    public Long getCreated() {
        return created;
    }

    /**
     * Sets the creation timestamp for the chat completion response.
     *
     * @param created the creation timestamp of the response
     */
    public void setCreated(Long created) {
        this.created = created;
    }

    /**
     * Gets the model identifier used to generate the chat completion.
     *
     * @return the model identifier
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model identifier for the chat completion response.
     *
     * @param model the model name or identifier used to generate the completion (for example, "gpt-4")
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Retrieves the list of chat completion choices.
     *
     * @return the list of ChatCompletionChoice objects representing available completion options
     */
    public List<ChatCompletionChoice> getChoices() {
        return choices;
    }

    /**
     * Set the list of choices included in this chat completion response.
     *
     * @param choices the list of ChatCompletionChoice objects to include; expected to be non-null and contain at least one element
     */
    public void setChoices(List<ChatCompletionChoice> choices) {
        this.choices = choices;
    }
}
