package net.guides.springboot2.springboot2swagger3.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger3Config {

  @Bean
  public OpenAPI springOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Spring Boot REST API")
                .description("Employee Management REST API")
                .contact(
                    new Contact()
                        .name("Ramesh Fadatare")
                        .url("www.javaguides.net")
                        .email("ramesh24fadatare@gmail.com"))
                .license(
                    new License()
                        .name("Apache 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                .version("1.0.0"));
  }
}
