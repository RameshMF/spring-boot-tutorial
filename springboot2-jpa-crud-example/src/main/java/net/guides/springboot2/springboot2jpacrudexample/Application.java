package net.guides.springboot2.springboot2jpacrudexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * BEGINNER:
 * This is the main class for the Spring Boot application.
 * Spring Boot is a framework that makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".
 * The @SpringBootApplication annotation is a convenience annotation that adds all of the following:
 *  - @Configuration: Tags the class as a source of bean definitions for the application context.
 *  - @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
 *  - @ComponentScan: Tells Spring to look for other components, configurations, and services in the 'net.guides.springboot2.springboot2jpacrudexample' package, allowing it to find and register them.
 *
 * INTERMEDIATE:
 * This class serves as the entry point for the application.
 * The @SpringBootApplication annotation is a meta-annotation that pulls in component scanning, auto-configuration, and property support.
 * It's equivalent to using @Configuration, @EnableAutoConfiguration, and @ComponentScan with their default attributes.
 * Auto-configuration attempts to automatically configure your Spring application based on the jar dependencies that you have added.
 * For example, if H2 is on your classpath, and you have not manually configured any database connection beans, then Spring Boot auto-configures an in-memory database.
 *
 * ADVANCED:
 * This class bootstraps the Spring Boot application.
 * The @SpringBootApplication annotation enables a host of features. While convenient, in complex applications, you might want to use the individual annotations (@Configuration, @EnableAutoConfiguration, @ComponentScan)
 * for finer-grained control over the application's configuration.
 * For instance, you might want to specify different packages for component scanning or exclude certain auto-configuration classes.
 *
 * Design Patterns:
 *  - Front Controller Pattern (Implicitly via Spring MVC's DispatcherServlet): Spring Boot applications using spring-boot-starter-web will have Spring MVC configured,
 *    where DispatcherServlet acts as a single point of entry for handling requests. This `Application` class itself is not the front controller, but it sets up the environment where it operates.
 *  - Dependency Injection: Spring Framework is built around the DI pattern. Beans (components, services, etc.) are managed by the Spring container and their dependencies are injected.
 *
 * Best Practices:
 *  - Keep the main application class lean: This class should primarily be for bootstrapping the application. Business logic and configuration should be delegated to other classes.
 *  - Package structure: Ensure a logical package structure. The default component scan starts from the package of this class. It's a common practice to place this class in a root package,
 *    and all other components in sub-packages (e.g., `com.example.demo.controller`, `com.example.demo.service`, `com.example.demo.repository`).
 *  - Externalize configuration: Use `application.properties` or `application.yml` for configuration to avoid hardcoding values. Spring Boot provides a sophisticated way to manage external configurations.
 *
 * Potential Pitfalls:
 *  - Classpath hell: While auto-configuration is powerful, it can sometimes lead to unexpected behavior if conflicting dependencies are on the classpath.
 *    Carefully manage your dependencies and use tools like `mvn dependency:tree` or `gradle dependencies` to inspect the dependency graph.
 *  - Over-reliance on defaults: While Spring Boot's defaults are sensible, always understand what's happening under the hood. For production systems, explicitly configure critical aspects like database connections, security, etc.
 *  - Slow startup times: As the application grows, auto-configuration and component scanning can contribute to slower startup times. Profile your application and consider excluding unnecessary auto-configurations or using lazy initialization.
 */
@SpringBootApplication
public class Application {

	/**
	 * BEGINNER:
	 * This is the main method, which is the entry point for any Java application.
	 * It uses SpringApplication.run() to launch the Spring Boot application.
	 *
	 * INTERMEDIATE:
	 * The `main` method delegates to Spring Boot's `SpringApplication.run()` static method to bootstrap the application.
	 * `SpringApplication` will create an `ApplicationContext` (the Spring container) and perform various setup tasks.
	 * The `args` parameter can be used to pass command-line arguments to the application, which can be accessed as Spring properties.
	 *
	 * ADVANCED:
	 * The `SpringApplication.run(Application.class, args)` method performs several actions:
	 *  1. Creates an appropriate `ApplicationContext` instance (e.g., `AnnotationConfigServletWebServerApplicationContext` for web applications).
	 *  2. Registers a `CommandLinePropertySource` to expose command-line arguments as Spring properties.
	 *  3. Refreshes the application context, loading all singleton beans.
	 *  4. Triggers any `CommandLineRunner` and `ApplicationRunner` beans.
	 * Customizations to the `SpringApplication` instance can be made before calling `run()`, for example, by calling `setBannerMode(Banner.Mode.OFF)` or adding listeners.
	 *
	 * @param args command line arguments passed to the application.
	 */
	public static void main(String[] args) {
		// This line starts the entire Spring Boot application.
		// SpringApplication.run takes two arguments:
		// 1. The primary Spring component (in this case, our Application class itself).
		// 2. The command line arguments.
		SpringApplication.run(Application.class, args);
	}
}
