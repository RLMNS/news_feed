package com.news.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;
import java.util.List;

@Configuration
@OpenAPIDefinition(info = @Info(title = "News Network API", version = "v1"))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@RequiredArgsConstructor
public class OpenApiConfig {
    @Value("news")
    private String applicationName;
    @Value("http://localhost:8081")
    private String serverUrl;

    @Bean
    public OpenAPI openAPI() throws URISyntaxException {
        URIBuilder domainUriBuilder = new URIBuilder(this.serverUrl);

        return new OpenAPI().addServersItem(new Server().url(domainUriBuilder.toString())
                .description("Greeting from " + this.applicationName));
    }
}