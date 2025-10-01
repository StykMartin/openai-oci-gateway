package io.martinstyk.security;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.provider.AuthenticationProvider;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.regex.Pattern;

@Singleton
public class OpenAiApiKeyAuthenticationProvider implements AuthenticationProvider {
    private static final Logger LOG = LoggerFactory.getLogger(OpenAiApiKeyAuthenticationProvider.class);
    
    private static final Pattern API_KEY_PATTERN = Pattern.compile("^sk-[a-zA-Z0-9]{48}$");

    public OpenAiApiKeyAuthenticationProvider() {
    }

    public AuthenticationResponse authenticate(HttpRequest<?> httpRequest, 
                                               AuthenticationRequest<?, ?> authenticationRequest) {
        String apiKey = extractApiKey(httpRequest);

        if (!isValidApiKeyFormat(apiKey)) {
            return AuthenticationResponse.failure("Invalid API key format");
        }

        String organization = extractOrganization(httpRequest);
        String project = extractProject(httpRequest);
        
        Map<String, Object> attributes = Map.of(
            "apiKey", apiKey,
            "organization", organization,
            "project", project,
            "authType", "api-key"
        );
        
        return AuthenticationResponse.success("api-user", attributes);
    }

    private String extractApiKey(HttpRequest<?> httpRequest) {
        String authHeader = httpRequest.getHeaders().get("Authorization");
        
        if (authHeader == null || authHeader.isEmpty()) {
            return null;
        }
        
        if (!authHeader.startsWith("Bearer ")) {
            return null;
        }
        
        return authHeader.substring(7).trim();
    }

    private String extractOrganization(HttpRequest<?> httpRequest) {
        String orgHeader = httpRequest.getHeaders().get("OpenAI-Organization");
        return orgHeader != null ? orgHeader : "org-default";
    }

    private String extractProject(HttpRequest<?> httpRequest) {
        String projHeader = httpRequest.getHeaders().get("OpenAI-Project");
        return projHeader != null ? projHeader : "proj-default";
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
        
        return API_KEY_PATTERN.matcher(apiKey).matches();
    }

    @Override
    public AuthenticationResponse authenticate(Object httpRequest, AuthenticationRequest authenticationRequest) {
        if (httpRequest instanceof HttpRequest<?>) {
            return authenticate((HttpRequest<?>) httpRequest, authenticationRequest);
        }
        return AuthenticationResponse.failure("Invalid request type");
    }
}

