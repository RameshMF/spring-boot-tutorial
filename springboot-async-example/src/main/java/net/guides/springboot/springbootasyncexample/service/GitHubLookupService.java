package net.guides.springboot.springbootasyncexample.service;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.guides.springboot.springbootasyncexample.model.User;

/**
 * BEGINNER:
 * This class is a "Service". Think of it as a helper that does specific jobs for our application.
 * Its main job here is to look up GitHub user information from the internet.
 * - `@Service`: This tells Spring that this class is a service component.
 * - `Logger`: This is used to write messages (logs) about what the service is doing, which helps in debugging.
 * - `RestTemplate`: This is a tool Spring provides to make it easier to call other services on the internet (like the GitHub API).
 * - `findUser(String user)`: This is the main method that does the lookup.
 *
 * About `@Async` on `findUser` method:
 *   Imagine you ask this service to find a user. Normally, you'd have to wait until it finishes before you can do anything else.
 *   But because of `@Async`, when you call `findUser`, it's like giving the job to a background worker.
 *   The main program can continue doing other things immediately, and the `findUser` job runs separately.
 *   This is called "asynchronous" processing and helps keep the application responsive, especially if the lookup takes a long time.
 *   The method returns a `CompletableFuture<User>`, which is like a placeholder for the `User` data that will arrive later when the background job is done.
 *
 * INTERMEDIATE:
 * This class is a Spring `@Service` component responsible for fetching user data from the GitHub API.
 * - `RestTemplate`: Used for making HTTP GET requests to the GitHub API endpoint. It's configured via `RestTemplateBuilder` injected in the constructor,
 *   which is a common Spring Boot practice for creating `RestTemplate` instances.
 * - `logger`: An SLF4J logger for logging informational messages and potential errors.
 *
 * `@Async("threadPoolTaskExecutor")` annotation on `findUser` method:
 *   - This annotation marks the `findUser` method for asynchronous execution. When this method is called from another Spring bean,
 *     Spring intercepts the call and executes the method body in a separate thread from the specified "threadPoolTaskExecutor" bean.
 *     (This executor is defined in `SpringbootAsyncApplication.class`).
 *   - This means the caller thread is not blocked waiting for the GitHub API call and the artificial `Thread.sleep()` to complete.
 *     Instead, it immediately receives a `CompletableFuture<User>`.
 *   - `CompletableFuture<User>`: This is a Java 8 feature representing a future result of an asynchronous computation.
 *     It acts as a handle to the result (a `User` object) that will be available once the asynchronous execution finishes.
 *     The client code (in `SpringbootAsyncApplication`) can use this `CompletableFuture` to wait for completion, get the result, or chain further operations.
 *   - `CompletableFuture.completedFuture(results)`: Once the `User` data (`results`) is fetched and the artificial delay is over,
 *     this static method is used to create a `CompletableFuture` that is already completed with the given `results`.
 *
 * Error Handling:
 *   - The current `findUser` method declares `throws InterruptedException` due to `Thread.sleep()`. If the sleeping thread is interrupted, this exception is thrown.
 *   - If `restTemplate.getForObject()` encounters an HTTP error (e.g., 404 Not Found, 500 Server Error from GitHub), it will throw a `RestClientException` (or a more specific subclass).
 *     This exception, if not caught within `findUser`, will cause the `CompletableFuture` to complete exceptionally. The caller can handle this using methods like `exceptionally()` or `handle()` on the `CompletableFuture`, or by catching `ExecutionException` when calling `future.get()`.
 *
 * ADVANCED:
 * This service demonstrates asynchronous interaction with an external API (GitHub).
 *
 * `@Async("threadPoolTaskExecutor")` Deep Dive:
 *   - Thread Management: Spring's `@Async` support relies on a `TaskExecutor`. When `@EnableAsync` is used in the configuration (like in `SpringbootAsyncApplication`),
 *     Spring looks for a `TaskExecutor` bean or creates a default one (a `SimpleAsyncTaskExecutor` which creates new threads per task, or a `ThreadPoolTaskExecutor` with default settings if none is found and Spring Boot auto-configuration is active).
 *     This application explicitly defines a `ThreadPoolTaskExecutor` bean named "threadPoolTaskExecutor", which is then specified in the `@Async` annotation here. This ensures that these specific async methods use that configured pool.
 *   - Proxying: For `@Async` to work, the method must be public and called from outside the class (i.e., from another Spring bean). Spring creates a proxy around the `GitHubLookupService` bean.
 *     When `findUser` is called through this proxy, the proxy intercepts the call and delegates the actual execution to the specified `TaskExecutor`. Internal calls within the same class (`this.findUser(...)`) will bypass the proxy and execute synchronously.
 *   - Return Types for `@Async`:
 *     - `void`: "Fire and forget" style. The caller cannot track completion or handle exceptions directly from the method call.
 *     - `Future<T>` or `CompletableFuture<T>` (Java 8+): Recommended for most use cases. Allows the caller to track completion, retrieve the result, and handle exceptions. `CompletableFuture` is more powerful and flexible than `Future`.
 *     - `ListenableFuture<T>` (Spring specific): An older Spring interface, `CompletableFuture` is generally preferred now.
 *
 * Error Handling Strategies for `@Async` methods:
 *   1. `CompletableFuture`'s built-in mechanisms: The caller can use `exceptionally()`, `handle()`, or `whenComplete()` on the returned `CompletableFuture`
 *      to process exceptions that occurred during the async execution. If the caller uses `future.get()`, it will throw an `ExecutionException` wrapping the original exception.
 *   2. `AsyncUncaughtExceptionHandler`: Implement `AsyncConfigurer` in your `@Configuration` class and override `getAsyncUncaughtExceptionHandler()`.
 *      This provides a global handler for exceptions thrown from `@Async` methods that return `void` or are not otherwise handled by the `Future`.
 *      This is more for "fire and forget" scenarios or unhandled exceptions propagating from Future-returning methods if not caught by the future's mechanisms.
 *   3. Try-catch within the `@Async` method: You can catch exceptions within the `findUser` method itself and then complete the `CompletableFuture` exceptionally
 *      using `future.completeExceptionally(ex)` or return a default/fallback value wrapped in a completed future.
 *
 * Context Propagation:
 *   - By default, thread-local context (e.g., `SecurityContext`, transaction context, MDC for logging) is NOT propagated from the caller thread to the thread executing the `@Async` method.
 *   - If context propagation is needed (e.g., for security information in downstream calls made by the async method):
 *     - Spring Security: `SecurityContextHolder.setStrategyName(MODE_INHERITABLETHREADLOCAL)` can be used, or manually propagate the `SecurityContext`.
 *     - Custom `TaskDecorator`: For more complex scenarios, you can provide a `TaskDecorator` to the `ThreadPoolTaskExecutor` to wrap tasks and propagate context.
 *     - Project Reactor or other reactive libraries often have built-in mechanisms for context propagation if used alongside `@Async`.
 *
 * `RestTemplate`:
 *   - `restTemplate.getForObject(url, User.class)`: This method makes an HTTP GET request to the specified `url`.
 *     Jackson (auto-configured by Spring Boot if on classpath) handles the deserialization of the JSON response into a `User` object.
 *   - `RestTemplate` is generally considered to be in maintenance mode by Spring, with `WebClient` (from `spring-webflux`) being the recommended modern alternative for non-blocking, reactive HTTP calls,
 *     even in traditional Spring MVC applications. However, `RestTemplate` is still widely used and functional for synchronous blocking calls within an `@Async` method like this.
 *
 * Artificial Delay: `Thread.sleep(1000L)`
 *   - This is purely for demonstration purposes to simulate a longer-running task, making the benefits of asynchronous execution more apparent when multiple calls are made.
 *   - In a real application, this would be the actual time taken by the network call and processing.
 *   - `throws InterruptedException`: `Thread.sleep()` can throw `InterruptedException`, so the method signature includes it. If this exception occurs, the `CompletableFuture` will complete exceptionally.
 */
@Service // Marks this class as a Spring service component.
public class GitHubLookupService {

	// BEGINNER: This is a tool for writing log messages (like status updates or errors) that developers can look at.
	// INTERMEDIATE: SLF4J Logger instance for logging within this service. LoggerFactory helps create this logger.
	// ADVANCED: Using a static final logger is a common practice. SLF4J provides an abstraction over various logging frameworks (Logback, Log4j2, etc.).
	private static final Logger logger = LoggerFactory.getLogger(GitHubLookupService.class);

	// BEGINNER: This is a tool that helps our service talk to other websites (like GitHub).
    // INTERMEDIATE: RestTemplate is Spring's classic way to make HTTP requests to external services.
    // ADVANCED: 'final' indicates that 'restTemplate' will be assigned once, typically in the constructor. This is good for immutability of the service's dependencies.
    private final RestTemplate restTemplate;

    /**
     * BEGINNER: When a `GitHubLookupService` is created, this constructor sets up the `RestTemplate` tool.
     * INTERMEDIATE: Constructor injection of `RestTemplateBuilder`. Spring Boot provides a `RestTemplateBuilder` that can be used to configure and create `RestTemplate` instances.
     * This is the recommended way to obtain a `RestTemplate` as it allows for easy customization and auto-configuration (e.g., message converters).
     * ADVANCED: Injecting `RestTemplateBuilder` promotes testability (can mock the builder) and leverages Spring Boot's auto-configuration for `RestTemplate`.
     * The builder might apply customizers (e.g., error handlers, interceptors) defined elsewhere in the application.
     * @param restTemplateBuilder The Spring Boot auto-configured RestTemplateBuilder.
     */
    public GitHubLookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build(); // Builds the RestTemplate instance.
    }

    /**
     * BEGINNER: This method finds a GitHub user. It's marked `@Async`, so it runs in the background.
     * It logs that it's starting, then uses `RestTemplate` to get data from GitHub's API for the given 'user'.
     * It then pretends to wait for 1 second (Thread.sleep) to simulate a slow process.
     * Finally, it returns a `CompletableFuture` which will eventually hold the `User` data.
     *
     * INTERMEDIATE: This method asynchronously fetches a GitHub user's profile.
     * `@Async("threadPoolTaskExecutor")`: Executes this method on a thread from the "threadPoolTaskExecutor" bean.
     * The method constructs the GitHub API URL, calls `restTemplate.getForObject()` to fetch and deserialize the user data into a `User` object.
     * `Thread.sleep(1000L)` introduces an artificial delay to simulate network latency or long computation, making async benefits clearer.
     * Returns `CompletableFuture.completedFuture(results)`: Wraps the fetched `User` object in an already completed `CompletableFuture`.
     *
     * ADVANCED:
     * This method is the core of the asynchronous operation.
     * - `@Async("threadPoolTaskExecutor")`: Explicitly specifies the custom executor bean. If this name were omitted and only one TaskExecutor bean existed,
     *   that one would be used. If multiple exist and none is specified, Spring looks for one named "taskExecutor".
     * - Exception Handling:
     *   - `InterruptedException`: If `Thread.sleep` is interrupted, this checked exception is thrown. The `CompletableFuture` will complete exceptionally.
     *   - `RestClientException`: If `restTemplate.getForObject` fails (e.g., 404, network error), it throws a subclass of `RestClientException`.
     *     This runtime exception will also cause the `CompletableFuture` to complete exceptionally.
     *     Callers of this method should use `CompletableFuture`'s error handling mechanisms (e.g., `exceptionally()`, `handle()`, or try-catch `ExecutionException` on `get()`).
     * - Logging: `logger.info` provides insight into the execution flow, crucial for async operations.
     * - `String.format`: Used for constructing the API URL.
     * - `CompletableFuture.completedFuture()`: This is used because the actual blocking I/O (`getForObject` and `sleep`) happens *within* the async method.
     *   Once these blocking operations are done, the result is available, so a pre-completed `CompletableFuture` is returned.
     *   If `restTemplate` itself were non-blocking (e.g., `WebClient`), its async result could be directly adapted into the returned `CompletableFuture`.
     *
     * @param user The GitHub username to look up (e.g., "PivotalSoftware").
     * @return A `CompletableFuture<User>` which will hold the `User` object when the lookup is complete.
     * @throws InterruptedException if the thread is interrupted during the artificial sleep. This will cause the CompletableFuture to complete exceptionally.
     */
    @Async("threadPoolTaskExecutor") // Specifies that this method should run on a thread from the "threadPoolTaskExecutor" pool.
    public CompletableFuture<User> findUser(String user) throws InterruptedException {
        // Log the start of the lookup for this user.
        logger.info("Looking up " + user);
        // Construct the URL for the GitHub API.
        String url = String.format("https://api.github.com/users/%s", user);
        // Make the HTTP GET request. RestTemplate handles converting the JSON response to a User object.
        // This is a blocking call within this async thread.
        User results = restTemplate.getForObject(url, User.class);

        // Artificial delay of 1 second to simulate a long-running task.
        // In a real application, this would be the actual time taken by the network call or computation.
        Thread.sleep(1000L);

        // Wrap the results in a CompletableFuture that is already completed.
        // This is because the work (API call + sleep) is done synchronously within this @Async method.
        return CompletableFuture.completedFuture(results);
    }
}
