package com.khrystoforov.onlinestore.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Configuration
public class SwaggerConfig {

    static {
        io.swagger.v3.core.jackson.ModelResolver.enumsAsRef = true;
        Schema<?> schema = new Schema<LocalTime>().example(
                LocalTime.now().format(DateTimeFormatter.ISO_TIME)).type("string").format("time");
        SpringDocUtils.getConfig().replaceWithSchema(LocalTime.class, schema);
    }

    @Bean
    public OpenAPI api(
            final Optional<BuildProperties> build,
            final ServerProperties serverProperties
    ) {
        String securityName = "Auth";
        SecurityScheme securityScheme = new SecurityScheme()
                .name(securityName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("oauth2-client").in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION).flows(
                        new OAuthFlows().implicit(
                                new OAuthFlow().scopes(new Scopes().addString("global", "accessEverything"))
                        )
                );

        Info apiInfo = new Info().title("Online Store")
                .version(build.map(BuildProperties::getVersion).orElse("unknown"));
        return new OpenAPI()
                .info(apiInfo)
                .components(new Components().addSecuritySchemes(securityName, securityScheme))
                .addSecurityItem(new SecurityRequirement().addList(securityName))
                .addServersItem(
                        new Server().url(serverProperties.getServlet().getContextPath())
                                .description("Default server url")
                );
    }
}

