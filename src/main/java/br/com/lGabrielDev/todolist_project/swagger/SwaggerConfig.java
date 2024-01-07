package br.com.lGabrielDev.todolist_project.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition(
    info = @Info(
        title = "Todo-List API",
        description = "Todolist aplication using Java + Spring",
        version = "1.0",
        license = @License(
            name = "MIT license",
            url = "https://opensource.org/license/mit/"
        ),
        contact = @Contact(
            name = "lGabrielDev"
        )
    ),
    servers = {
        @Server(
            description = "Local Server",
            url = "http://localhost:8080"
        )
    },
    tags = {
        @Tag(name = "admin", description = "admin authority needed"),
        @Tag(name = "person", description = "regular users, without authentication"),
        @Tag(name = "category", description = "from the authenticated person"),
        @Tag(name = "task", description = "from the authenticated person"),
    },
    security = {
        @SecurityRequirement(name = "simpleBasicAuth") //we pass the SecurityScheme 'name'
    }
    
)
@SecurityScheme(
    name = "simpleBasicAuth", //you can choose whatever you want
    scheme = "basic",
    type = SecuritySchemeType.HTTP,
    in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {}