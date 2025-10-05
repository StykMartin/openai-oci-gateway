package io.martinstyk.mapper;

import com.oracle.bmc.generativeaiinference.model.AssistantMessage;
import com.oracle.bmc.generativeaiinference.model.ChatContent;
import com.oracle.bmc.generativeaiinference.model.FunctionDefinition;
import com.oracle.bmc.generativeaiinference.model.GenericChatRequest;
import com.oracle.bmc.generativeaiinference.model.Message;
import com.oracle.bmc.generativeaiinference.model.SystemMessage;
import com.oracle.bmc.generativeaiinference.model.TextContent;
import com.oracle.bmc.generativeaiinference.model.ToolChoice;
import com.oracle.bmc.generativeaiinference.model.ToolChoiceAuto;
import com.oracle.bmc.generativeaiinference.model.ToolChoiceNone;
import com.oracle.bmc.generativeaiinference.model.ToolChoiceRequired;
import com.oracle.bmc.generativeaiinference.model.ToolDefinition;
import com.oracle.bmc.generativeaiinference.model.UserMessage;
import io.martinstyk.model.CreateChatCompletionRequest;
import io.martinstyk.model.message.ChatCompletionRequestMessage;
import io.martinstyk.model.tools.FunctionTool;
import io.martinstyk.model.tools.Tool;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapper(componentModel = "jsr330", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ChatCompletionMapper {

    Logger logger = LoggerFactory.getLogger(ChatCompletionMapper.class);

    @Mapping(target = "messages", source = "messages", qualifiedByName = "mapMessages")
    @Mapping(target = "toolChoice", source = "toolChoice", qualifiedByName = "mapToolChoice")
    @Mapping(target = "tools", source = "tools", qualifiedByName = "mapTools")
    @Mapping(target = "maxTokens", source = "maxCompletionTokens")
    @Mapping(target = "isStream", source = "stream")
    @Mapping(target = "numGenerations", source = "n")
    @Mapping(target = "logProbs", source = "logprobs")
    @Mapping(target = "seed", ignore = true)
    @Mapping(target = "isEcho", ignore = true)
    @Mapping(target = "topK", ignore = true)
    @Mapping(target = "copy", ignore = true)
    @Mapping(target = "logitBias", ignore = true)
    GenericChatRequest toGenericChatRequest(CreateChatCompletionRequest openAiRequest);

    @Named("mapMessages")
    default List<Message> mapMessages(List<ChatCompletionRequestMessage> openAiMessages) {
        return openAiMessages.stream()
                .map(this::mapToSpecificMessage)
                .filter(Objects::nonNull)
                .toList();
    }

    default Message mapToSpecificMessage(ChatCompletionRequestMessage openAiMessage) {
        String content = openAiMessage.getContent();
        if (content == null) {
            logger.warn("Skipping message with missing content: {}", openAiMessage.getRole());
            return null;
        }

        TextContent textContent = TextContent.builder().text(content).build();

        List<ChatContent> contentList = List.of(textContent);

        return switch (openAiMessage.getRole().toLowerCase()) {
            case "user" -> UserMessage.builder().content(contentList).build();
            case "assistant" -> AssistantMessage.builder().content(contentList).build();
            case "system", "developer" -> SystemMessage.builder().content(contentList).build();
            default -> {
                logger.warn("Skipping message with unsupported role: {}", openAiMessage.getRole());
                yield null;
            }
        };
    }

    @Named("mapToolChoice")
    default ToolChoice mapToolChoice(io.martinstyk.model.ToolChoice openAiToolChoice) {
        if (openAiToolChoice == null) {
            return null;
        }

        return switch (openAiToolChoice) {
            case NONE -> ToolChoiceNone.builder().build();
            case AUTO -> ToolChoiceAuto.builder().build();
            case REQUIRED -> ToolChoiceRequired.builder().build();
        };
    }

    @Named("mapTools")
    default List<ToolDefinition> mapTools(List<Tool> openAiTools) {
        if (openAiTools == null) {
            return List.of();
        }

        List<ToolDefinition> tools = new ArrayList<>();

        for (Tool openAiTool : openAiTools) {
            if (openAiTool instanceof FunctionTool fnTool) {
                FunctionDefinition.Builder functionBuilder =
                        FunctionDefinition.builder()
                                .name(fnTool.getName())
                                .parameters(fnTool.getParameters());

                if (fnTool.getDescription() != null && !fnTool.getDescription().trim().isEmpty()) {
                    functionBuilder.description(fnTool.getDescription());
                }
                tools.add(functionBuilder.build());
            } else {
                logger.warn("Unsupported tool type: {}", openAiTool.getType());
            }
        }

        return tools;
    }
}
