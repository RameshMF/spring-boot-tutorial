package net.guides.springboot2.springboot2swagger3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class TestSpringboot2Swagger3Application {
    
    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
      return new PostgreSQLContainer<>("postgres:latest");
    }
  
    public static void main(String[] args) {
      SpringApplication.from(Springboot2Swagger3Application::main).with(TestSpringboot2Swagger3Application.class).run(args);
    }
  
}
