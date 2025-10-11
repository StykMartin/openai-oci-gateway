package io.martinstyk.security;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.validator.TokenValidator;
import jakarta.inject.Singleton;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@Singleton
public class OpenAiFormatTokenValidator<T> implements TokenValidator<T> {
    private static final Pattern LEGACY_TOKEN_PATTERN = Pattern.compile("^sk-[a-zA-Z0-9]{48}$");

    private static final Pattern NEW_TOKEN_PATTERN =
            Pattern.compile("^sk-(proj|svcacct)-[a-zA-Z0-9_-]{40,200}$");

    @Override
    public @NonNull Publisher<Authentication> validateToken(
            @NonNull String token, @Nullable T request) {
        if (!isTokenValid(token)) {
            return Mono.empty();
        }
        String organization = extractOrganization(request);
        String project = extractProject(request);

        Map<String, Object> attributes =
                Map.of(
                        "organization", organization,
                        "project", project,
                        "authType", "token");

        return Mono.just(Authentication.build("openai-compat", attributes));
    }

    private String extractOrganization(T request) {
        if (request instanceof HttpRequest) {
            return Optional.ofNullable(
                            ((HttpRequest<?>) request).getHeaders().get("OpenAI-Organization"))
                    .orElse("org-default");
        }
        return "org-default";
    }

    private String extractProject(T request) {
        if (request instanceof HttpRequest) {
            return Optional.ofNullable(
                            ((HttpRequest<?>) request).getHeaders().get("OpenAI-Project"))
                    .orElse("proj-default");
        }
        return "proj-default";
    }

    private boolean isTokenValid(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            return false;
        }

        if (!apiKey.startsWith("sk-")) {
            return false;
        }

        if (apiKey.length() == 51 && LEGACY_TOKEN_PATTERN.matcher(apiKey).matches()) {
            return true;
        }

        return NEW_TOKEN_PATTERN.matcher(apiKey).matches();
    }
}
