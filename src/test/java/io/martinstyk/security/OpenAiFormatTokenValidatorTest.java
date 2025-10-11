package io.martinstyk.security;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class OpenAiFormatTokenValidatorTest {

    private OpenAiFormatTokenValidator<HttpRequest<?>> validator;

    @BeforeEach
    void setUp() {
        validator = new OpenAiFormatTokenValidator<>();
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "sk-123456789012345678901234567890123456789012345678",
                "sk-abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUV",
                "sk-ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijkl",
                "sk-proj-1234567890123456789012345678901234567890",
                "sk-proj-abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                "sk-proj-abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_1234567890-abcdefghijklmnopqrstuvwxyz",
                "sk-proj-test_1234567890123456789012345678901234567890",
                "sk-svcacct-1234567890123456789012345678901234567890",
                "sk-svcacct-abc_def_ghi-jkl_mno-pqr_123456789012345678901234"
            })
    void testValidateValidTokens(String token) {
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

    @ParameterizedTest
    @ValueSource(
            strings = {
                "pk-123456789012345678901234567890123456789012345678",
                "ak-123456789012345678901234567890123456789012345678",
                "sk-12345678901234567890123456789012345678901234567! ",
                "sk-test@invalid#chars$1234567890123456789012345678901234567890",
                "sk-proj-abc",
                "sk-abc",
                "sk-1234567890",
                "sk-proj-abc def@#$%123456789012345678901234567890",
                "sk-test!@#$%^&*()123456789012345678901234567890",
                "sk-proj abc-def-ghi-jkl-mno-123456789012345678901234567890",
                "sk-",
                "sk-12345",
                "sk-1234567890123456789012345678901234567890123456",
                "sk-12345678901234567890123456789012345678901234567",
                "sk-1234567890abcdefghijklmnopqrstuvwxyz0123456789",
                "sk-org-1234567890123456789012345678901234567890abcd",
                "sk-proj-12345678901234567890123456789012345678",
                "sk-svcacct-123456789012345678901234567890123",
                "sk-1234567890123456789012345678901234567890123456789",
                ""
            })
    void testValidateInvalidTokens(String token) {
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
    void testValidateValidTokenWithCustomHeaders() {
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
    void testValidateTokenWithSpecialCharactersInHeaders() {
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
