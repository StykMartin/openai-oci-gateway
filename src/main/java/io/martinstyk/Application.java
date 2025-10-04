package io.martinstyk;

import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.context.ApplicationContextConfigurer;
import io.micronaut.context.annotation.ContextConfigurer;
import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info =
                @Info(
                        title =
                                "OpenAI API Gateway for Oracle Cloud Infrastructure (OCI) Generative AI",
                        description =
                                "This API gateway makes OCI Generative AI services compatible with the OpenAI API format.",
                        contact =
                                @Contact(
                                        name = "OpenAI OCI Gateway",
                                        url = "https://github.com/StykMartin/openai-oci-gateway",
                                        email = "mart.styk@gmail.com"),
                        license =
                                @License(
                                        name = "Apache 2.0 License",
                                        url = "https://opensource.org/license/apache-2-0")),
        security = @SecurityRequirement(name = "OpenAI-API-Key"))
@SecurityScheme(
        name = "OpenAI-API-Key",
        type = io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "OpenAI API Key",
        description = "OpenAI-compatible API key authentication. Use format: Bearer sk-...")
public class Application {
    @ContextConfigurer
    public static class Configurer implements ApplicationContextConfigurer {
        @Override
        public void configure(@NonNull ApplicationContextBuilder builder) {
            builder.defaultEnvironments(Environment.DEVELOPMENT);
            builder.eagerInitConfiguration(true);
        }
    }

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
