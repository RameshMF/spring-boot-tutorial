package net.guides.springboot.springbootasyncexample;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import net.guides.springboot.springbootasyncexample.model.User;
import net.guides.springboot.springbootasyncexample.service.GitHubLookupService;

/**
 * BEGINNER:
 * This is the main class that starts our Spring Boot application.
 * Think of it as the "engine room" for the application.
 * `@SpringBootApplication`: This is a special Spring Boot instruction that does a lot of setup automatically. It makes it easy to get a web application running.
 * `@EnableAsync`: This instruction tells Spring to allow some tasks to run in the background, separately from the main program flow. This is useful for long-running tasks that you don't want to make the user wait for.
 * `CommandLineRunner`: This interface means that the `run` method in this class will be executed automatically when the application starts up.
 *
 * INTERMEDIATE:
 * This is the main application class for a Spring Boot project demonstrating asynchronous method execution.
 * `@SpringBootApplication`: A convenience annotation that combines:
 *  - `@Configuration`: Tags the class as a source of bean definitions for the application context.
 *  - `@EnableAutoConfiguration`: Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
 *    For example, if `spring-webmvc` is on the classpath, this annotation automatically tries to configure Spring MVC.
 *  - `@ComponentScan`: Tells Spring to look for other components, configurations, and services in the current package (`net.guides.springboot.springbootasyncexample`) and its sub-packages.
 * `@EnableAsync`: This annotation enables Spring's asynchronous method execution capability. When used in conjunction with `@Async` on methods,
 *   Spring will execute these methods in a separate thread, typically from a thread pool. This is crucial for non-blocking operations.
 * The class implements `CommandLineRunner`, allowing it to execute code after the Spring application context has been initialized.
 * This is often used for initial setup, or in this case, to demonstrate the async functionality.
 *
 * ADVANCED:
 * This class serves as the entry point and primary configuration for the Spring Boot application.
 * Spring Boot Auto-configuration: `@SpringBootApplication` triggers auto-configuration. Spring Boot examines the application's classpath
 *   and property files. Based on the dependencies present (e.g., `spring-boot-starter-web` implies a web application,
 *   `spring-boot-starter-data-jpa` implies JPA setup), it automatically configures beans like `DispatcherServlet`, `DataSource`, `EntityManagerFactory`, etc.,
 *   with sensible defaults. These defaults can be overridden by explicit bean definitions or properties.
 * `@EnableAsync` Significance:
 *   - Activates Spring's asynchronous execution. Behind the scenes, Spring creates proxies for classes containing `@Async` methods.
 *   - When an `@Async` method is invoked, the proxy intercepts the call and submits it to a `TaskExecutor` to run in a separate thread.
 *   - If no `TaskExecutor` bean is explicitly defined, Spring Boot auto-configures a default `ThreadPoolTaskExecutor` (since Spring 3.0 and Spring Boot 1.0).
 *   - However, this application explicitly defines a `TaskExecutor` bean named "threadPoolTaskExecutor", which will be used by `@Async` methods
 *     if they specify this bean name (e.g., `@Async("threadPoolTaskExecutor")`) or if it's the only `TaskExecutor` available.
 * Customizing Thread Pool for `@Async`:
 *   - By default, `@Async` methods use a `SimpleAsyncTaskExecutor` (which creates new threads on demand without pooling) or a default `ThreadPoolTaskExecutor`
 *     if auto-configured by Spring Boot.
 *   - For production, it's highly recommended to define a custom `ThreadPoolTaskExecutor` bean (as done in this class with `getAsyncExecutor()`)
 *     to control thread pool characteristics:
 *     - `corePoolSize`: Minimum number of threads to keep alive.
 *     - `maxPoolSize`: Maximum number of threads that can be created.
 *     - `queueCapacity`: Size of the queue to hold tasks before they are executed (if not using a direct hand-off executor). This `ThreadPoolTaskExecutor` doesn't explicitly set it, so it uses `Integer.MAX_VALUE`.
 *     - `threadNamePrefix`: Useful for logging and debugging to identify threads from this pool.
 *     - `setWaitForTasksToCompleteOnShutdown(true)`: Ensures that pending tasks are completed during application shutdown.
 *   - To make `@Async` methods use a specific executor, you can implement `AsyncConfigurer` or provide a `TaskExecutor` bean named "taskExecutor" (default name looked up by Spring).
 *     Alternatively, as shown here, a custom named executor is defined, and `@Async("beanName")` can be used on methods to select it. If only one TaskExecutor bean exists, it's often picked up by default.
 *
 * This application demonstrates calling an asynchronous service (`GitHubLookupService`) multiple times and using `CompletableFuture`
 * to manage the results and overall completion.
 */
@SpringBootApplication
@EnableAsync // Enables Spring's ability to run @Async methods in a background thread pool.
public class SpringbootAsyncApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(SpringbootAsyncApplication.class);

	// BEGINNER: This line tells Spring to give us an instance of GitHubLookupService.
	// INTERMEDIATE: @Autowired performs dependency injection. Spring will create and inject an instance of GitHubLookupService here.
	// ADVANCED: GitHubLookupService is a component (likely a @Service) that contains @Async methods.
	@Autowired
	private GitHubLookupService gitHubLookupService;

	/**
	 * BEGINNER:
	 * This section sets up a "pool" of workers (threads) that our application can use to do tasks in the background.
	 * Instead of creating a new worker every time, it reuses workers from this pool, which is more efficient.
	 *
	 * INTERMEDIATE:
	 * This method defines a custom `TaskExecutor` bean, specifically a `ThreadPoolTaskExecutor`.
	 * This bean will be used by Spring to execute methods annotated with `@Async`.
	 * If multiple TaskExecutor beans are present, `@Async` methods can specify which one to use by its bean name (e.g., `@Async("threadPoolTaskExecutor")`).
	 * If no specific executor is named in `@Async` and multiple are available, one might be chosen based on its bean name "taskExecutor" or if it's primary.
	 * Configuration details:
	 *  - `corePoolSize(20)`: The pool will try to keep 20 threads ready.
	 *  - `maxPoolSize(1000)`: If many tasks come in, the pool can grow up to 1000 threads.
	 *  - `setWaitForTasksToCompleteOnShutdown(true)`: When the application shuts down, it will wait for currently running async tasks to finish.
	 *  - `setThreadNamePrefix("Async-")`: Threads created by this pool will have names starting with "Async-", which helps in debugging and logging.
	 *
	 * ADVANCED:
	 * This `@Bean` method creates and configures a `ThreadPoolTaskExecutor` instance. This executor will be managed by the Spring container.
	 * By naming the bean "threadPoolTaskExecutor", methods annotated with `@Async("threadPoolTaskExecutor")` will specifically use this pool.
	 * If an `@Async` method does not specify an executor name, Spring will look for a unique `TaskExecutor` bean, or one named "taskExecutor",
	 * or one marked as `@Primary`. If this is the only `TaskExecutor` bean, it will likely be used by default for `@Async` methods.
	 * The chosen queue capacity (defaulting to `Integer.MAX_VALUE` here as `setQueueCapacity` is not called) can significantly impact behavior
	 * when `maxPoolSize` is reached. A large queue can lead to high memory consumption if tasks are produced faster than consumed.
	 * For CPU-bound tasks, `corePoolSize` is often set around the number of CPU cores. For I/O-bound tasks (like in `GitHubLookupService`),
	 * a larger `corePoolSize` and `maxPoolSize` can be beneficial as threads will spend time waiting for I/O.
	 *
	 * @return A configured TaskExecutor for asynchronous operations.
	 */
	@Bean("threadPoolTaskExecutor") // Defines a bean named "threadPoolTaskExecutor"
	public TaskExecutor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20); // Minimum number of threads to keep in the pool.
		executor.setMaxPoolSize(1000); // Maximum number of threads that can be created.
		executor.setWaitForTasksToCompleteOnShutdown(true); // Wait for tasks to complete before shutting down.
		executor.setThreadNamePrefix("Async-"); // Prefix for names of threads created by this executor.
		// executor.setQueueCapacity(100); // Optionally configure queue capacity. Default is Integer.MAX_VALUE.
		// executor.initialize(); // Not strictly necessary here as Spring calls init methods, but good practice if used outside Spring management.
		return executor;
	}

	/**
	 * BEGINNER:
	 * This is the main "door" to start the application.
	 *
	 * INTERMEDIATE:
	 * The standard main method for a Java application.
	 * `SpringApplication.run(SpringbootAsyncApplication.class, args)` bootstraps the Spring Boot application,
	 * creating the ApplicationContext and auto-configuring components.
	 *
	 * ADVANCED:
	 * Entry point of the Spring Boot application. `SpringApplication.run()` handles:
	 * 1. Creating an appropriate `ApplicationContext`.
	 * 2. Registering `CommandLinePropertySource`.
	 * 3. Loading beans, running auto-configuration.
	 * 4. Calling `CommandLineRunner` beans after the context is refreshed.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		// Launches the Spring Boot application.
		SpringApplication.run(SpringbootAsyncApplication.class, args);
	}

	/**
	 * BEGINNER:
	 * This method runs automatically after the application starts.
	 * It calls the `GitHubLookupService` multiple times to find GitHub users. Because the service is "async" (asynchronous),
	 * these calls can happen at the same time, rather than one after another. This makes the program faster.
	 * Finally, it prints out how long it took and the user details it found.
	 *
	 * INTERMEDIATE:
	 * This `run` method is executed on application startup due to implementing `CommandLineRunner`.
	 * It demonstrates the use of the asynchronous `gitHubLookupService.findUser()` method.
	 *  - `CompletableFuture<User>`: The `findUser` method returns a `CompletableFuture`. This is an object that represents a future result
	 *    of an asynchronous computation. It will eventually contain a `User` object or an exception if the lookup fails.
	 *  - Multiple calls are made to `findUser`. Because `findUser` is `@Async`, each call will likely run in a separate thread from the pool
	 *    configured by `getAsyncExecutor()`.
	 *  - `CompletableFuture.allOf(page1, page2, page3, page4).join()`: This line waits for all the `CompletableFuture` instances to complete.
	 *    The `join()` method waits for the result and will throw an exception if any of the futures completed exceptionally.
	 *  - The results are then retrieved using `pageN.get()` and printed. `get()` would block if the future is not yet complete,
	 *    but since `join()` was called, they are guaranteed to be complete here.
	 *
	 * ADVANCED:
	 * The `run` method showcases the client-side handling of asynchronous operations using `CompletableFuture`.
	 * The `GitHubLookupService.findUser()` method is annotated with `@Async`, so its execution is offloaded to the configured `TaskExecutor`.
	 * `CompletableFuture.allOf(...)` is a static helper method that creates a new `CompletableFuture` that is completed when all of the
	 * provided `CompletableFuture`s complete. `join()` is called on this aggregate future, effectively blocking the main thread
	 * (the one running this `run` method) until all asynchronous GitHub user lookups have finished.
	 * This pattern is useful for orchestrating multiple independent asynchronous tasks and then aggregating their results or waiting for their completion.
	 * Error Handling: If any of the `findUser` calls were to throw an exception within their async execution, the `CompletableFuture`
	 * would complete exceptionally. `pageN.get()` would then throw an `ExecutionException` (wrapping the original exception),
	 * and `CompletableFuture.allOf(...).join()` would also throw a `CompletionException`. Proper error handling would involve try-catch blocks
	 * around `get()` or `join()`, or using methods like `exceptionally()` on the `CompletableFuture` instances.
	 *
	 * @param args Command line arguments (not used in this example).
	 * @throws Exception if any error occurs during the execution, particularly from `get()` or `join()`.
	 */
	@Override
	public void run(String... args) throws Exception {
		// Start the clock to measure execution time.
		long start = System.currentTimeMillis();

		// Kick off multiple asynchronous lookups. Each call to findUser will be executed in a separate thread.
		CompletableFuture<User> page1 = gitHubLookupService.findUser("PivotalSoftware");
		CompletableFuture<User> page2 = gitHubLookupService.findUser("CloudFoundry");
		CompletableFuture<User> page3 = gitHubLookupService.findUser("Spring-Projects");
		CompletableFuture<User> page4 = gitHubLookupService.findUser("RameshMF"); // Example with a different user

		// Wait until all CompletableFuture instances are complete.
		// CompletableFuture.allOf() returns a new CompletableFuture that is completed when all given CompletableFutures complete.
		// .join() waits for this completion (similar to get() but throws unchecked CompletionException on failure).
		CompletableFuture.allOf(page1, page2, page3, page4).join();

		// Print results, including elapsed time.
		logger.info("Elapsed time: " + (System.currentTimeMillis() - start) + " ms");
		logger.info("--> User 1: " + page1.get()); // .get() retrieves the result of the CompletableFuture.
		logger.info("--> User 2: " + page2.get());
		logger.info("--> User 3: " + page3.get());
		logger.info("--> User 4: " + page4.get());
	}
}
