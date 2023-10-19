package ru.practicum.shareit.gateway.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "\"ShareIt\" API",
                description = "RESTful platform for item exchange: find, share, rent",
                version = "1.0",
                contact = @Contact(
                        name = "Ivan Marakanov (BucketOnHead)",
                        email = "marakanovivan@gmail.com",
                        url = "https://github.com/BucketOnHead"
                )
        )
)
public class OpenApiConfig {
}
