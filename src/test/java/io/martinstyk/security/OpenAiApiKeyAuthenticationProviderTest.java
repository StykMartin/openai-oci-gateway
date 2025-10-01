package io.martinstyk.security;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class OpenAiApiKeyAuthenticationProviderTest {

    private OpenAiApiKeyAuthenticationProvider authProvider;

    @BeforeEach
    void setUp() {
        authProvider = new OpenAiApiKeyAuthenticationProvider();
    }

    @Test
    void testValidApiKeyAuthentication() {
        String validApiKey = "sk-123456789012345678901234567890123456789012345678";
        
        HttpRequest<?> request = HttpRequest.GET("/test")
                .header("Authorization", "Bearer " + validApiKey)
                .header("OpenAI-Organization", "org-test")
                .header("OpenAI-Project", "proj-test");

        AuthenticationResponse result = authProvider.authenticate(request, null);
        
        assertNotNull(result);
        assertTrue(result.isAuthenticated());
        assertTrue(result.getAuthentication().isPresent());
        assertEquals("api-user", result.getAuthentication().get().getName());
    }

    @Test
    void testInvalidApiKeyFormat() {
        String invalidApiKey = "invalid-key";
        
        HttpRequest<?> request = HttpRequest.GET("/test")
                .header("Authorization", "Bearer " + invalidApiKey);

        AuthenticationResponse result = authProvider.authenticate(request, null);
        
        assertNotNull(result);
        assertFalse(result.isAuthenticated());
    }

    @Test
    void testMissingAuthorizationHeader() {
        HttpRequest<?> request = HttpRequest.GET("/test");

        AuthenticationResponse result = authProvider.authenticate(request, null);
        
        assertNotNull(result);
        assertFalse(result.isAuthenticated());
    }

    @Test
    void testInvalidBearerFormat() {
        String validApiKey = "sk-123456789012345678901234567890123456789012345678";
        
        HttpRequest<?> request = HttpRequest.GET("/test")
                .header("Authorization", validApiKey);

        AuthenticationResponse result = authProvider.authenticate(request, null);
        
        assertNotNull(result);
        assertFalse(result.isAuthenticated());
    }

    @Test
    void testApiKeyFormatValidation() {
        assertTrue(isValidApiKeyFormat("sk-123456789012345678901234567890123456789012345678"));
        
        assertFalse(isValidApiKeyFormat("sk-123"));
        assertFalse(isValidApiKeyFormat("sk-1234567890123456789012345678901234567890123456789"));
        assertFalse(isValidApiKeyFormat("pk-123456789012345678901234567890123456789012345678"));
        assertFalse(isValidApiKeyFormat("sk-12345678901234567890123456789012345678901234567!"));
    }

    @Test
    void testOrganizationAndProjectExtraction() {
        String validApiKey = "sk-123456789012345678901234567890123456789012345678";
        
        HttpRequest<?> request = HttpRequest.GET("/test")
                .header("Authorization", "Bearer " + validApiKey)
                .header("OpenAI-Organization", "org-custom")
                .header("OpenAI-Project", "proj-custom");

        AuthenticationResponse result = authProvider.authenticate(request, null);
        
        assertNotNull(result);
        assertTrue(result.isAuthenticated());
        assertTrue(result.getAuthentication().isPresent());
        
        Map<String, Object> attributes = result.getAuthentication().get().getAttributes();
        assertEquals("org-custom", attributes.get("organization"));
        assertEquals("proj-custom", attributes.get("project"));
    }

    @Test
    void testDefaultOrganizationAndProject() {
        String validApiKey = "sk-123456789012345678901234567890123456789012345678";
        
        HttpRequest<?> request = HttpRequest.GET("/test")
                .header("Authorization", "Bearer " + validApiKey);

        AuthenticationResponse result = authProvider.authenticate(request, null);
        
        assertNotNull(result);
        assertTrue(result.isAuthenticated());
        assertTrue(result.getAuthentication().isPresent());
        
        Map<String, Object> attributes = result.getAuthentication().get().getAttributes();
        assertEquals("org-default", attributes.get("organization"));
        assertEquals("proj-default", attributes.get("project"));
    }

    @Test
    void testAuthenticationAttributes() {
        String validApiKey = "sk-123456789012345678901234567890123456789012345678";
        
        HttpRequest<?> request = HttpRequest.GET("/test")
                .header("Authorization", "Bearer " + validApiKey)
                .header("OpenAI-Organization", "test-org")
                .header("OpenAI-Project", "test-proj");

        AuthenticationResponse result = authProvider.authenticate(request, null);
        
        assertNotNull(result);
        assertTrue(result.isAuthenticated());
        assertTrue(result.getAuthentication().isPresent());
        
        Map<String, Object> attributes = result.getAuthentication().get().getAttributes();
        assertEquals(validApiKey, attributes.get("apiKey"));
        assertEquals("test-org", attributes.get("organization"));
        assertEquals("test-proj", attributes.get("project"));
        assertEquals("api-key", attributes.get("authType"));
    }

    @Test
    void testEmptyBearerToken() {
        HttpRequest<?> request = HttpRequest.GET("/test")
                .header("Authorization", "Bearer ");

        AuthenticationResponse result = authProvider.authenticate(request, null);
        
        assertNotNull(result);
        assertFalse(result.isAuthenticated());
    }

    @Test
    void testApiKeyTooShort() {
        String tooShortApiKey = "sk-12345678901234567890123456789012345678901234567";
        
        HttpRequest<?> request = HttpRequest.GET("/test")
                .header("Authorization", "Bearer " + tooShortApiKey);

        AuthenticationResponse result = authProvider.authenticate(request, null);
        
        assertNotNull(result);
        assertFalse(result.isAuthenticated());
    }

    @Test
    void testApiKeyTooLong() {
        String tooLongApiKey = "sk-1234567890123456789012345678901234567890123456789";
        
        HttpRequest<?> request = HttpRequest.GET("/test")
                .header("Authorization", "Bearer " + tooLongApiKey);

        AuthenticationResponse result = authProvider.authenticate(request, null);
        
        assertNotNull(result);
        assertFalse(result.isAuthenticated());
    }

    @Test
    void testWhitespaceOnlyBearerToken() {
        HttpRequest<?> request = HttpRequest.GET("/test")
                .header("Authorization", "Bearer   ");

        AuthenticationResponse result = authProvider.authenticate(request, null);
        
        assertNotNull(result);
        assertFalse(result.isAuthenticated());
    }

    private boolean isValidApiKeyFormat(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            return false;
        }
        
        if (!apiKey.startsWith("sk-")) {
            return false;
        }
        
        if (apiKey.length() != 51) {
            return false;
        }
        
        String keyPart = apiKey.substring(3);
        return keyPart.matches("[a-zA-Z0-9]+");
    }
}

