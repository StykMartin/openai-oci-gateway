package io.martinstyk.security;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class OpenAiTokenValidatorTest {

    private OpenAiTokenValidator<HttpRequest<?>> validator;

    @BeforeEach
    void setUp() {
        validator = new OpenAiTokenValidator<>();
    }

    @Test
    void testValidateValidToken() {
        String token = "sk-123456789012345678901234567890123456789012345678";

        HttpRequest<?> request =
                HttpRequest.GET("/test")
                        .header("OpenAI-Organization", "org-test")
                        .header("OpenAI-Project", "proj-test");

        Mono<Authentication> result =
                (Mono<Authentication>) validator.validateToken(token, request);

        StepVerifier.create(result)
                .expectNextMatches(
                        auth -> {
                            Map<String, Object> attributes = auth.getAttributes();
                            return "openai-compat".equals(auth.getName())
                                    && "org-test".equals(attributes.get("organization"))
                                    && "proj-test".equals(attributes.get("project"))
                                    && "token".equals(attributes.get("authType"));
                        })
                .verifyComplete();
    }

    @Test
    void testValidateValidTokenWithDefaultHeaders() {
        String token = "sk-abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUV";

        HttpRequest<?> request = HttpRequest.GET("/test");

        Mono<Authentication> result =
                (Mono<Authentication>) validator.validateToken(token, request);

        StepVerifier.create(result)
                .expectNextMatches(
                        auth -> {
                            Map<String, Object> attributes = auth.getAttributes();
                            return "openai-compat".equals(auth.getName())
                                    && "org-default".equals(attributes.get("organization"))
                                    && "proj-default".equals(attributes.get("project"))
                                    && "token".equals(attributes.get("authType"));
                        })
                .verifyComplete();
    }

    @Test
    void testValidateInvalidTokenTooShort() {
        String token = "sk-12345678901234567890123456789012345678901234567";

        HttpRequest<?> request = HttpRequest.GET("/test");

        Mono<Authentication> result =
                (Mono<Authentication>) validator.validateToken(token, request);

        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void testValidateInvalidTokenTooLong() {
        String token = "sk-1234567890123456789012345678901234567890123456789";

        HttpRequest<?> request = HttpRequest.GET("/test");

        Mono<Authentication> result =
                (Mono<Authentication>) validator.validateToken(token, request);

        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void testValidateInvalidTokenWrongPrefix() {
        String token = "pk-123456789012345678901234567890123456789012345678";

        HttpRequest<?> request = HttpRequest.GET("/test");

        Mono<Authentication> result =
                (Mono<Authentication>) validator.validateToken(token, request);

        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void testValidateInvalidTokenWrongCharacters() {
        String token = "sk-12345678901234567890123456789012345678901234567! ";

        HttpRequest<?> request = HttpRequest.GET("/test");

        Mono<Authentication> result =
                (Mono<Authentication>) validator.validateToken(token, request);

        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void testValidateNullToken() {
        HttpRequest<?> request = HttpRequest.GET("/test");

        Mono<Authentication> result = (Mono<Authentication>) validator.validateToken(null, request);

        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void testValidateEmptyToken() {
        HttpRequest<?> request = HttpRequest.GET("/test");

        Mono<Authentication> result = (Mono<Authentication>) validator.validateToken("", request);

        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void testValidateTokenWithOnlyOrganizationHeader() {
        String token = "sk-123456789012345678901234567890123456789012345678";

        HttpRequest<?> request =
                HttpRequest.GET("/test").header("OpenAI-Organization", "org-specific");

        Mono<Authentication> result =
                (Mono<Authentication>) validator.validateToken(token, request);

        StepVerifier.create(result)
                .expectNextMatches(
                        auth -> {
                            Map<String, Object> attributes = auth.getAttributes();
                            return "openai-compat".equals(auth.getName())
                                    && "org-specific".equals(attributes.get("organization"))
                                    && "proj-default".equals(attributes.get("project"))
                                    && "token".equals(attributes.get("authType"));
                        })
                .verifyComplete();
    }

    @Test
    void testValidateTokenWithOnlyProjectHeader() {
        String token = "sk-123456789012345678901234567890123456789012345678";

        HttpRequest<?> request = HttpRequest.GET("/test").header("OpenAI-Project", "proj-specific");

        Mono<Authentication> result =
                (Mono<Authentication>) validator.validateToken(token, request);

        StepVerifier.create(result)
                .expectNextMatches(
                        auth -> {
                            Map<String, Object> attributes = auth.getAttributes();
                            return "openai-compat".equals(auth.getName())
                                    && "org-default".equals(attributes.get("organization"))
                                    && "proj-specific".equals(attributes.get("project"))
                                    && "token".equals(attributes.get("authType"));
                        })
                .verifyComplete();
    }

    @Test
    void testValidateTokenWithSpecialCharacters() {
        String token = "sk-ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijkl";

        HttpRequest<?> request =
                HttpRequest.GET("/test")
                        .header("OpenAI-Organization", "Org-With-Special-Chars")
                        .header("OpenAI-Project", "proj_with_underscores");

        Mono<Authentication> result =
                (Mono<Authentication>) validator.validateToken(token, request);

        StepVerifier.create(result)
                .expectNextMatches(
                        auth -> {
                            Map<String, Object> attributes = auth.getAttributes();
                            return "openai-compat".equals(auth.getName())
                                    && "Org-With-Special-Chars"
                                            .equals(attributes.get("organization"))
                                    && "proj_with_underscores".equals(attributes.get("project"))
                                    && "token".equals(attributes.get("authType"));
                        })
                .verifyComplete();
    }
}
