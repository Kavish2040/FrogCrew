package com.frogcrew.frogcrew.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("FrogCrew API")
                                                .description("This is the FrogCrew API")
                                                .version("1.0.0")
                                                .termsOfService("http://swagger.io/terms/")
                                                .contact(new Contact()
                                                                .name("Madhavam Shahi")
                                                                .email("madhavam.p.shahi@tcu.edu"))
                                                .license(new License()
                                                                .name("Apache 2.0")
                                                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                                .servers(List.of(new Server().url("https://localhost:8080")))
                                .tags(Arrays.asList(
                                                new Tag().name("Availability").description(
                                                                "Endpoints related to crew member availability"),
                                                new Tag().name("Crewed User")
                                                                .description("Endpoints related to crewed users"),
                                                new Tag().name("Crew List")
                                                                .description("Endpoints related to crew lists"),
                                                new Tag().name("Crew Schedule")
                                                                .description("Endpoints related to crew schedules"),
                                                new Tag().name("Game").description("Endpoints related to games"),
                                                new Tag().name("Schedule")
                                                                .description("Endpoints related to schedules"),
                                                new Tag().name("Game Type Properties").description(
                                                                "Endpoints related to game type properties"),
                                                new Tag().name("Notification")
                                                                .description("Endpoints related to notifications"),
                                                new Tag().name("Report").description("Endpoints related to reports"),
                                                new Tag().name("Scheduled Games")
                                                                .description("Endpoints related to scheduled games"),
                                                new Tag().name("Templates")
                                                                .description("Endpoints related to templates"),
                                                new Tag().name("Position")
                                                                .description("Endpoints related to positions"),
                                                new Tag().name("User").description("Endpoints related to users")))
                                .components(new Components());
        }
}
