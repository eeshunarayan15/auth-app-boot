package com.eeshu.auth.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Auth App API",
                version = "v1",
                description = "Auth App API Documentation",
                contact = @Contact(
                        name = "Eeshu Narayan",
                        url = "https://portfolio-app-eeshu.netlify.app/",
                        email = "eeshunarayan15@gmail.com"),
                        summary = "Hi this eeshu narayan java developer currenlty in final semester of my b-tech"

), security = {
                @SecurityRequirement(
                        name = "bearerAuth")})
@SecurityScheme(
        name = "bearerAuth",
        type = io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"

)
public class APIDocConfig {

}
