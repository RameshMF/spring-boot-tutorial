package net.guides.springboot.springboot2jpaauditing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class TestSpringboot2JpaAuditingApplication {

    @Bean
    @ServiceConnection
    MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>("mysql:latest");
    }

    public static void main(String[] args) {
        SpringApplication.from(Springboot2JpaAuditingApplication::main).with(TestSpringboot2JpaAuditingApplication.class).run(args);
    }

}
