# Concepts in Spring Boot Asynchronous Programming (`springboot-async-example`)

## Introduction & High-Level Overview

This document explains the key concepts demonstrated in the `springboot-async-example` project.

**Project Purpose:**
The primary goal of this project is to illustrate how to use the `@Async` annotation in a Spring Boot application to perform operations asynchronously. This is crucial for building responsive applications that can handle long-running tasks without blocking the main application thread, such as calling external APIs.

**Brief Architecture:**
The application is a simple Spring Boot command-line runner.
1.  **`SpringbootAsyncApplication.java`**: The main application class that initializes Spring Boot and includes a `CommandLineRunner` to trigger the asynchronous operations. It also defines a custom thread pool executor.
2.  **`GitHubLookupService.java`**: A Spring service that contains a method (`findUser`) annotated with `@Async`. This method simulates a long-running task by calling the GitHub API and introducing an artificial delay.
3.  **`User.java`**: A simple Plain Old Java Object (POJO) or Data Transfer Object (DTO) used to model the data retrieved from the GitHub API.
4.  **`pom.xml`**: The Maven project configuration file, defining dependencies (like Spring Boot starters) and the build process.
5.  **`application.properties`**: Configuration file for Spring Boot (though largely unused in this specific example for async configuration, as it's done via a Java bean).

When the application starts, the `CommandLineRunner` in `SpringbootAsyncApplication` calls the `findUser` method in `GitHubLookupService` multiple times. Because `findUser` is asynchronous, these calls are executed concurrently in separate threads, and the main thread uses `CompletableFuture.allOf().join()` to wait for all of them to complete.

## Basic Concepts

### What is Spring Boot?

*   **Definition**: Spring Boot is an extension of the Spring Framework that makes it significantly easier to create stand-alone, production-grade Spring-based applications that you can "just run." It simplifies the setup and configuration of Spring applications.
*   **Benefits**:
    *   **Auto-configuration**: Automatically configures your application based on the JAR dependencies you have added. For example, if it sees `spring-webmvc` on the classpath, it automatically configures a web server and Spring MVC.
    *   **Standalone Applications**: Allows you to create applications that can be run directly (e.g., from a JAR file with an embedded server like Tomcat or Jetty) without needing to deploy them to an external web server.
    *   **Opinionated Defaults**: Provides sensible default configurations for many features, reducing the amount of boilerplate code and configuration you need to write.
    *   **Starter Dependencies**: Simplifies Maven/Gradle configuration by providing "starter" POMs/dependencies (e.g., `spring-boot-starter-web`) that bundle common dependencies for specific types of applications.
*   **Real-world Analogy**: Think of Spring Boot as a pre-packaged toolkit for building a specific type of structure, like a house. Instead of buying all the raw materials (wood, nails, cement) and tools separately and figuring out how they all fit together (like in traditional Spring Framework setup), Spring Boot gives you a well-organized kit with many parts already pre-assembled and configured to work together (e.g., the foundation, walls, and roof structure are already planned out). You can still customize it, but the basic setup is much faster.
*   **Code Reference**:
    *   `SpringbootAsyncApplication.java`: The `@SpringBootApplication` annotation is the entry point and enables auto-configuration and component scanning.
    *   `pom.xml`: The use of `spring-boot-starter-parent` and starter dependencies like `spring-boot-starter-web`.

### What is Asynchronous Programming?

*   **Definition**: Asynchronous programming is a means of parallel execution that allows a unit of work to run separately from the primary application thread. When the primary thread makes an asynchronous call, it doesn't wait for that task to complete. Instead, it receives a handle (often a `Future` or `CompletableFuture`) and can continue doing other work. The asynchronous task runs in the background, and the primary thread can check the handle later for the result or be notified when the task is finished.
*   **Benefits**:
    *   **Improved Responsiveness**: Prevents the main application thread from being blocked by long-running operations (like I/O operations, network calls, or complex computations). This is especially important for user interfaces and server applications that need to handle multiple requests concurrently.
    *   **Increased Throughput**: By utilizing multiple threads or cores, applications can perform more tasks simultaneously, leading to better resource utilization and overall performance for certain types of workloads.
*   **Real-world Analogy**: Ordering food at a busy fast-food counter.
    *   **Synchronous**: You place your order and stand at the counter, blocking the line for others, until your food is prepared and handed to you. You can't do anything else while waiting.
    *   **Asynchronous**: You place your order, and they give you a buzzer. You are now free to go find a table, talk to friends, or do other things. When the buzzer goes off (your food is ready), you go pick it up. The main "thread" (you) was not blocked waiting directly at the counter.
*   **Code Reference**:
    *   `GitHubLookupService.java`: The `findUser` method is designed to be a long-running task (simulated with `Thread.sleep` and a network call). By making it asynchronous, the calling code in `SpringbootAsyncApplication.java` can invoke it multiple times without waiting for each call to finish sequentially.

### What is the `@Async` annotation?

*   **Definition**: In Spring, the `@Async` annotation is used to mark a method for asynchronous execution. When a method annotated with `@Async` is called from another Spring bean, Spring will execute that method in a separate thread, allowing the caller to proceed without waiting for the method to complete.
*   **How it Works (Basic)**:
    1.  You enable asynchronous processing in your Spring application (usually with `@EnableAsync` on a configuration class).
    2.  You annotate a public method in a Spring bean (e.g., a `@Service` or `@Component`) with `@Async`.
    3.  When this method is called from *another bean* (not from within the same class directly), Spring creates a proxy around the bean. This proxy intercepts the method call.
    4.  Instead of executing the method synchronously, the proxy submits the method execution to a thread pool (a `TaskExecutor` in Spring's terminology).
    5.  The original caller immediately gets a return value. If the `@Async` method has a `void` return type, the caller gets `null`. If it returns a `Future` or `CompletableFuture`, the caller gets an instance of that future, which can be used to track the result of the asynchronous execution.
*   **Code Reference**:
    *   `GitHubLookupService.java`: The `findUser(String user)` method is annotated with `@Async("threadPoolTaskExecutor")`.
    *   `SpringbootAsyncApplication.java`: The `run` method calls `gitHubLookupService.findUser(...)` multiple times. These calls are executed asynchronously.

### What is `CompletableFuture`?

*   **Definition (Simple)**: A `CompletableFuture<T>` (where `T` is the type of the result) is an object that represents the future result of an asynchronous computation. It's a promise that a value will be available eventually, or an error might occur. It provides methods to check if the computation is complete, to get the result (waiting if necessary), and to handle errors.
*   **Key Features**:
    *   Can be explicitly completed (unlike traditional `Future`).
    *   Allows chaining of asynchronous operations (e.g., "when this is done, then do that").
    *   Provides better error handling mechanisms.
*   **Real-world Analogy**: The buzzer you get at a restaurant after ordering food.
    *   The buzzer itself is the `CompletableFuture`. It doesn't contain your food yet, but it *represents* your future food.
    *   You can check if it's buzzing (`isDone()`).
    *   When it buzzes, your food is ready, and you can "get" it (`get()`).
    *   If something went wrong (e.g., they ran out of an ingredient), the buzzer might signal an error, or the staff might tell you when you try to get your food (`exceptionally()`, or `get()` throwing an exception).
*   **Code Reference**:
    *   `GitHubLookupService.java`: The `findUser` method returns `CompletableFuture<User>`.
    *   `SpringbootAsyncApplication.java`: The `run` method receives these `CompletableFuture` objects and uses `CompletableFuture.allOf(...).join()` to wait for all of them to complete, and then `pageN.get()` to retrieve the actual `User` results.

## Intermediate Concepts

### Enabling Asynchronous Processing (`@EnableAsync`)

*   **Why it's Needed**: The `@EnableAsync` annotation is crucial because it signals to Spring that it should activate its asynchronous method execution capabilities. Without it, annotating methods with `@Async` will have no effect; the methods will execute synchronously within the caller's thread.
*   **How it Works**: When Spring encounters `@EnableAsync` on a configuration class (a class annotated with `@Configuration`), it triggers the necessary post-processing to find beans with `@Async` methods. For these beans, Spring creates proxies that intercept calls to the `@Async` methods and delegate their execution to a configured `TaskExecutor`.
*   **Pro Tip**: Place `@EnableAsync` on a central configuration class, typically your main application class or a dedicated configuration class for asynchronous features.
*   **Code Reference**:
    *   `SpringbootAsyncApplication.java`: This class is annotated with `@EnableAsync`.

### How Spring Manages Threads for `@Async`

*   **Default Behavior**: If you use `@EnableAsync` but do not explicitly define a `TaskExecutor` bean in your application context, Spring Boot's auto-configuration steps in.
    *   It typically creates a `ThreadPoolTaskExecutor` with default settings (e.g., core pool size of 8, max pool size of `Integer.MAX_VALUE`, queue capacity of `Integer.MAX_VALUE`, and a thread name prefix like "task-").
    *   In older Spring versions (or non-Boot Spring), the default might be a `SimpleAsyncTaskExecutor`, which creates a new thread for each task and does not reuse threads (less efficient for many short-lived tasks).
*   **Limitations of the Default Executor**:
    *   **Unbounded Threads/Queue (Potentially)**: The default `ThreadPoolTaskExecutor` settings in Spring Boot might have an unbounded queue or max pool size. If tasks are submitted faster than they can be processed, this could lead to excessive memory consumption (due to the queue) or too many threads, potentially exhausting system resources.
    *   **Lack of Fine-Grained Control**: The default settings might not be optimal for your specific application's workload (e.g., CPU-bound vs. I/O-bound tasks).
    *   **Generic Thread Names**: Default thread names might not be very descriptive for debugging or monitoring.
*   **Best Practice**: For production applications, it's highly recommended to explicitly configure a `TaskExecutor` bean to have better control over thread pool behavior, resource usage, and error handling.

### Basic Configuration of the Thread Pool

*   **Overriding the Default `TaskExecutor`**: You can customize the thread pool used by `@Async` methods by defining your own bean of type `TaskExecutor` (typically an instance of `ThreadPoolTaskExecutor`).
    *   If your bean is named `"taskExecutor"`, it becomes the default executor for `@Async` methods that don't specify an executor name.
    *   Alternatively, you can define a bean with any name and then refer to it in the `@Async` annotation (e.g., `@Async("myCustomExecutor")`).
*   **Key Configuration Parameters (`ThreadPoolTaskExecutor`)**:
    *   `corePoolSize`: The number of threads to keep in the pool, even if they are idle. These are created as tasks arrive.
    *   `maxPoolSize`: The maximum number of threads allowed in the pool. If the queue is full and `maxPoolSize` has not been reached, new threads will be created.
    *   `queueCapacity`: The number of tasks that can be queued up if all core threads are busy. Once the queue is full, new tasks might lead to thread creation up to `maxPoolSize`, or task rejection if `maxPoolSize` is also reached.
*   **Trade-offs**:
    *   Larger `corePoolSize`: Can handle more concurrent tasks immediately but consumes more resources even when idle.
    *   Larger `maxPoolSize` with a bounded queue: Allows bursting but can still lead to resource issues if not managed.
    *   Large `queueCapacity`: Can smooth out temporary bursts of tasks but might hide performance problems and increase memory usage if tasks are consistently produced faster than consumed.
*   **Code Reference**:
    *   `SpringbootAsyncApplication.java`: The `getAsyncExecutor()` method defines a `ThreadPoolTaskExecutor` bean named `"threadPoolTaskExecutor"`.
        *   `executor.setCorePoolSize(20);`
        *   `executor.setMaxPoolSize(1000);`
        *   `executor.setWaitForTasksToCompleteOnShutdown(true);`
        *   `executor.setThreadNamePrefix("Async-");`
    *   `GitHubLookupService.java`: `@Async("threadPoolTaskExecutor")` explicitly uses this configured bean.

### Using `RestTemplate`

*   **What it is**: `RestTemplate` is Spring Framework's central class for performing synchronous client-side HTTP requests. It simplifies interaction with RESTful web services by handling common concerns like request/response marshalling (converting Java objects to/from JSON/XML).
*   **How it's used here**: In `GitHubLookupService`, `RestTemplate` is used to make a GET request to the GitHub API (`https://api.github.com/users/{user}`) to fetch user details. The JSON response from the API is automatically converted (unmarshalled) into a `User` object by Jackson, which is typically auto-configured with `RestTemplate` by Spring Boot.
*   **Pro Tip**:
    *   `RestTemplate` is now in maintenance mode in Spring Framework 5+. For new development, especially if non-blocking or reactive programming is desired, Spring recommends using `WebClient` from the `spring-webflux` module. However, `RestTemplate` is still widely used and perfectly suitable for synchronous blocking calls, especially when used within an `@Async` method (as the blocking call then happens on a separate thread).
    *   It's good practice to configure `RestTemplate` using `RestTemplateBuilder` (as done in this project), which allows Spring Boot to apply auto-configuration and customizers.
*   **Code Reference**:
    *   `GitHubLookupService.java`:
        *   `private final RestTemplate restTemplate;`
        *   Constructor: `public GitHubLookupService(RestTemplateBuilder restTemplateBuilder) { this.restTemplate = restTemplateBuilder.build(); }`
        *   `findUser` method: `User results = restTemplate.getForObject(url, User.class);`

## Advanced Concepts / Mastery

### Customizing `TaskExecutor` in Detail

When configuring a `ThreadPoolTaskExecutor`, several parameters allow for fine-grained control:

*   **`corePoolSize`**: (e.g., `20` in the example) The number of threads to keep alive in the pool, even if they are idle. Threads are created lazily as tasks arrive until this number is reached.
*   **`maxPoolSize`**: (e.g., `1000` in the example) The maximum number of threads that can be created in the pool. If the queue is full and more tasks arrive, new threads are created up to this limit.
*   **`queueCapacity`**: (Default `Integer.MAX_VALUE` if not set, as in the example's `getAsyncExecutor` method) The size of the queue to hold tasks waiting for execution when all core threads are busy.
    *   Using `Integer.MAX_VALUE` (default for `ThreadPoolTaskExecutor`) can be risky as it might lead to memory exhaustion if tasks are produced much faster than consumed.
    *   A bounded queue is generally safer.
    *   A `SynchronousQueue` (capacity 0) forces direct hand-off: tasks are rejected if no thread is immediately available or if `maxPoolSize` is reached. This often means threads are created up to `maxPoolSize` more aggressively.
*   **`threadNamePrefix`**: (e.g., `"Async-"` in the example) Sets a prefix for the names of threads created by the executor. Extremely useful for logging, debugging, and thread dump analysis.
*   **`keepAliveSeconds`**: When the number of threads is greater than `corePoolSize`, this is the maximum time (in seconds) that excess idle threads will wait for new tasks before terminating. Default is 60.
*   **`allowCoreThreadTimeOut` (boolean)**: If `true`, core threads may also time out and terminate if no tasks arrive within the `keepAliveSeconds` period. Default is `false`. Useful if you want to scale down resources completely during long idle periods.
*   **Rejection Policies (`RejectedExecutionHandler`)**: Defines what happens when a task is submitted but the executor cannot accept it (e.g., queue is full and `maxPoolSize` is reached).
    *   `AbortPolicy` (default): Throws `RejectedExecutionException`.
    *   `CallerRunsPolicy`: The task is executed synchronously by the thread that submitted it.
    *   `DiscardPolicy`: The task is silently discarded.
    *   `DiscardOldestPolicy`: The oldest task in the queue is discarded, and the new task is added.
    *   Custom implementations are also possible.
*   **`setWaitForTasksToCompleteOnShutdown(boolean)`**: (e.g., `true` in the example) If `true`, the executor will wait for currently executing tasks and tasks in the queue to complete before shutting down the application.
*   **`setAwaitTerminationSeconds(int)`**: The maximum time the application will wait for tasks to complete on shutdown if `setWaitForTasksToCompleteOnShutdown(true)`.

**Importance of Sizing the Thread Pool Correctly**:
*   **CPU-Bound Tasks**: For tasks that are primarily computational and keep the CPU busy, the optimal pool size is often close to the number of CPU cores (e.g., `Runtime.getRuntime().availableProcessors()` or `N+1`). Too many threads can lead to increased context switching overhead.
*   **I/O-Bound Tasks**: For tasks that spend a lot of time waiting for external operations (like network calls, database queries, file system access – as in `GitHubLookupService`), the pool size can be significantly larger than the number of CPU cores. This is because threads will be idle (waiting for I/O) much of the time, and having more threads allows other tasks to proceed. The formula often cited is `Number of threads = Number of Cores * (1 + Wait time / Service time)`.
*   **Pro Tip**: Profile your application under realistic load to determine the optimal pool size. Monitor CPU utilization, task queue length, and response times.

**Code Reference**:
*   `SpringbootAsyncApplication.java`: The `getAsyncExecutor()` method demonstrates setting `corePoolSize`, `maxPoolSize`, `threadNamePrefix`, and `waitForTasksToCompleteOnShutdown`.

### Error Handling in `@Async` Methods

Handling exceptions in asynchronous methods requires special attention because the execution happens in a different thread from the caller.

*   **Methods returning `void`**:
    *   If an exception is thrown from an `@Async void` method, it cannot be caught by the caller directly.
    *   By default, the exception is simply logged by Spring.
    *   To customize this, you can implement the `AsyncUncaughtExceptionHandler` interface and register it by overriding the `getAsyncUncaughtExceptionHandler()` method in a class that implements `AsyncConfigurer` (often your `@Configuration` class that has `@EnableAsync`).
    *   This handler will be invoked when an unhandled exception propagates out of an `@Async void` method.
*   **Methods returning `Future` / `CompletableFuture`**:
    *   This is the recommended approach for better error handling.
    *   If an exception occurs within the `@Async` method, the `Future`/`CompletableFuture` object returned to the caller will be completed "exceptionally."
    *   The caller can then handle this:
        *   Calling `future.get()` will throw an `ExecutionException`, which wraps the original exception.
        *   `CompletableFuture` provides methods like `exceptionally(Function<Throwable, ? extends T> fn)` to provide a fallback value or `handle(BiFunction<? super T, Throwable, ? extends U> fn)` to process both successful results and exceptions.
    *   **Example Project**: In `GitHubLookupService.java`, if `restTemplate.getForObject(...)` throws a `RestClientException` or `Thread.sleep(...)` throws an `InterruptedException`, the `CompletableFuture` returned by `findUser` will complete exceptionally. The `run` method in `SpringbootAsyncApplication.java` calls `pageN.get()`, which would throw an `ExecutionException` if any of the lookups failed. (A `try-catch` block around `.get()` or `.join()` would be needed for robust error handling in the client).
*   **Why default Spring exception handlers might not catch**: Global exception handlers defined with `@ControllerAdvice` are typically for exceptions thrown from Spring MVC controller methods (within the web request processing threads). They won't directly catch exceptions from threads managed by `@Async` unless those exceptions are propagated back to the controller layer (e.g., by awaiting a `CompletableFuture` and re-throwing).

### `CompletableFuture` for Composing Asynchronous Operations

`CompletableFuture` is a powerful tool not just for getting a future result, but also for composing and coordinating multiple asynchronous operations in a non-blocking way.

*   **Chaining Operations**:
    *   `thenApply(Function<? super T,? extends U> fn)`: Transforms the result of a `CompletableFuture` when it completes successfully.
    *   `thenCompose(Function<? super T,? extends CompletionStage<U>> fn)`: Chains another asynchronous operation that depends on the result of the first. (Useful when the next step also returns a `CompletableFuture`).
    *   `thenAccept(Consumer<? super T> action)`: Performs an action with the result when it completes (no return value).
    *   `thenRun(Runnable action)`: Executes a `Runnable` after the completion of the `CompletableFuture`.
*   **Combining Multiple `CompletableFuture`s**:
    *   `allOf(CompletableFuture<?>... cfs)`: Returns a new `CompletableFuture` that completes when all of the given `CompletableFuture`s complete. Useful for waiting for a group of independent tasks. (Used in `SpringbootAsyncApplication.java`).
    *   `anyOf(CompletableFuture<?>... cfs)`: Returns a new `CompletableFuture` that completes when any one of the given `CompletableFuture`s completes, with the same result.
*   **Error Handling specific to `CompletableFuture`**:
    *   `exceptionally(Function<Throwable, ? extends T> fn)`: Provides a way to recover from an exception by returning a default value or an alternative result.
    *   `handle(BiFunction<? super T, Throwable, ? extends U> fn)`: Processes either the successful result or the exception, allowing for a more general way to complete the next stage.
*   **Note**: The `springboot-async-example` project primarily uses `CompletableFuture` to retrieve results and `allOf` to wait for group completion. It doesn't heavily showcase advanced composition, but these features are essential for more complex asynchronous workflows.

### Testing Asynchronous Methods

Testing code that involves asynchronous operations can be challenging because the execution flow is non-deterministic and involves multiple threads.

*   **Challenges**:
    *   **Timing Issues**: Test assertions might run before the asynchronous method has completed.
    *   **Thread Management**: Ensuring that threads are properly managed and shut down after tests.
    *   **Exception Handling**: Verifying that exceptions in async threads are correctly propagated or handled.
*   **Strategies**:
    *   **Using `CompletableFuture.get()` with Timeouts**: If your `@Async` method returns a `CompletableFuture`, you can call `future.get(timeout, timeUnit)` in your test. This will block until the result is available or a timeout occurs, allowing you to assert the result. This is the simplest approach for methods returning futures.
        ```java
        // In your test
        CompletableFuture<User> futureUser = gitHubLookupService.findUser("testuser");
        User user = futureUser.get(5, TimeUnit.SECONDS); // Wait up to 5 seconds
        assertNotNull(user);
        ```
    *   **`Awaitility` Library**: A popular third-party library that provides a fluent DSL for synchronizing asynchronous operations in tests. It allows you to wait until a certain condition is met.
        ```java
        // Example with Awaitility (conceptual)
        // service.triggerAsyncOperation();
        // await().atMost(5, TimeUnit.SECONDS).until(() -> service.isOperationComplete());
        ```
    *   **Mocking and Verifying**: If the `@Async` method interacts with other components, you can mock those components (e.g., using Mockito) and verify interactions. For testing the async behavior itself, you might need to configure a synchronous `TaskExecutor` for tests or use the strategies above.
    *   **Spring Test Support**: `@SpringBootTest` can be used to load the application context. You can `@Autowired` your async service and test it. For `@Async` methods, ensure your test configuration includes `@EnableAsync` and a suitable `TaskExecutor` (or rely on the production one if appropriate for the test scope).

### Potential Pitfalls with `@Async`

*   **Calling `@Async` methods from within the same class (Self-Invocation Issue)**:
    *   `@Async` (and other proxy-based Spring AOP features like `@Transactional`) works by Spring creating a proxy around your bean. When you call an `@Async` method from *another bean*, you are calling it through the proxy, which then applies the asynchronous behavior.
    *   If you call an `@Async` method from another method *within the same class* (`this.asyncMethod()`), you are bypassing the proxy. The call will be a direct Java method call and will execute *synchronously* in the same thread, not asynchronously.
    *   **Workarounds**:
        1.  Inject the bean into itself (generally discouraged due to complexity).
        2.  Refactor the `@Async` method into a separate Spring bean and inject that new bean. (Cleanest approach).
        3.  Use `((MyClass) AopContext.currentProxy()).asyncMethod();` (requires `spring-aspects` dependency and `@EnableAspectJAutoProxy(exposeProxy = true)`).
*   **Transaction Propagation**:
    *   By default, transactional context is not propagated to threads executing `@Async` methods. If an `@Async` method needs to participate in a transaction started by the caller, or start its own transaction that is visible to the caller's thread in some way, this requires careful configuration.
    *   Each `@Async` method execution will typically start a new transaction if annotated with `@Transactional` (with `Propagation.REQUIRED` or `REQUIRES_NEW`).
    *   Managing transactions across thread boundaries is complex. Consider whether the async operation truly needs to be part of the same transaction or if it can be an independent unit of work.
*   **Security Context Propagation**:
    *   Similar to transactions, the `SecurityContext` (e.g., holding information about the logged-in user) is usually thread-local and is not automatically propagated to `@Async` threads.
    *   If your `@Async` method needs to access security information or call other secured methods, you'll need to configure security context propagation.
    *   Spring Security offers mechanisms for this, such as setting `SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)` before the async call, or manually wrapping the task execution.
*   **Graceful Shutdown of Task Executors**:
    *   Ensure that your `TaskExecutor` is configured to shut down gracefully, allowing active tasks and tasks in the queue to complete before the application exits.
    *   `ThreadPoolTaskExecutor` provides `setWaitForTasksToCompleteOnShutdown(true)` and `setAwaitTerminationSeconds(int)` for this purpose. (Used in `SpringbootAsyncApplication.java`).
    *   If not handled, async tasks might be abruptly terminated during application shutdown, leading to data inconsistencies or incomplete operations.

```
