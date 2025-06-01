package net.guides.springboot2.springboot2jpacrudexample.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
//ResponseEntityExceptionHandler is a convenient base class for @ControllerAdvice classes that wish to provide centralized exception handling across all @RequestMapping methods through @ExceptionHandler methods.
//This class is commented out as the current GlobalExceptionHandler directly handles specific exceptions.
//If you need to handle Spring-specific exceptions (e.g., MethodArgumentNotValidException, HttpRequestMethodNotSupportedException)
//in a customized way, you would typically extend ResponseEntityExceptionHandler and override its methods.
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * BEGINNER:
 * Imagine this class as the application's "emergency response team."
 * Normally, if an error happens in one of the controllers (like `EmployeeController`), the application might crash or show a generic error page.
 * This `GlobalExceptionHandler` steps in and catches those errors before they cause a big problem.
 * It then decides what kind of "error report" (like the `ErrorDetails` object) to send back to the user or another computer program,
 * making the error messages consistent and more helpful.
 * The `@ControllerAdvice` annotation tells Spring Boot that this class is special and should watch out for errors across all controllers.
 *
 * INTERMEDIATE:
 * This class provides centralized exception handling for the entire application.
 * `@ControllerAdvice`: This annotation makes the class a global exception handler. Spring will detect this class
 * and use its methods to handle exceptions thrown by any controller (`@Controller` or `@RestController`).
 * `@ExceptionHandler`: This annotation is used on methods within a `@ControllerAdvice` class to specify which
 * type of exception that method will handle. When an exception of that type (or its subtypes) is thrown from any controller,
 * the annotated method will be invoked to process the exception and return an appropriate HTTP response.
 *
 * For example, if `ResourceNotFoundException` is thrown anywhere in a controller, the `resourceNotFoundException` method here will catch it.
 * Similarly, the `globleExcpetionHandler` method catches any other `Exception` that isn't specifically handled by another `@ExceptionHandler`.
 *
 * ADVANCED:
 * `GlobalExceptionHandler` (often extending `ResponseEntityExceptionHandler` for more comprehensive Spring MVC exception coverage, though not strictly necessary if only custom exceptions are handled)
 * centralizes cross-cutting concerns like exception handling, promoting cleaner controller logic.
 *
 * Key Concepts & Best Practices:
 *  - Separation of Concerns: Controllers focus on request handling, while this class focuses on error response generation.
 *  - Consistent Error Responses: Ensures that clients receive error messages in a uniform format (`ErrorDetails` in this case),
 *    regardless of where the error originated in the application.
 *  - Specificity: Define handlers for specific custom exceptions (like `ResourceNotFoundException`) to provide tailored responses
 *    (e.g., HTTP 404 Not Found). A general `ExceptionHandler(Exception.class)` acts as a catch-all for unexpected errors (HTTP 500 Internal Server Error).
 *  - Logging: It's crucial to log exceptions within these handlers, especially for the generic `Exception.class` handler.
 *    This helps in diagnosing and fixing unexpected issues. (Logging is not explicitly shown in this example but is a best practice).
 *    Example: `log.error("Unhandled exception: ", ex);`
 *  - HTTP Status Codes: Map exceptions to appropriate HTTP status codes to provide meaningful feedback to REST clients.
 *  - Extensibility: `ResponseEntityExceptionHandler` (if used) provides handlers for many standard Spring MVC exceptions (e.g., validation errors, method not supported).
 *    You can override these methods to customize the response format to match your `ErrorDetails` structure. For instance, overriding `handleMethodArgumentNotValid`
 *    to handle `@Valid` annotation failures.
 *  - Error Codes: For more complex APIs, consider adding application-specific error codes to the `ErrorDetails` object to allow clients
 *    to programmatically handle errors more robustly.
 *  - User-Friendly Messages: For client-facing applications, ensure that error messages are user-friendly and do not expose sensitive system information.
 *    The `details` field in `ErrorDetails` can be used for debugging information (e.g., `request.getDescription(false)`), while the `message`
 *    can be more generic for the end-user.
 *
 * Potential Pitfalls:
 *  - Overly Broad Catch-All: Relying too much on the generic `ExceptionHandler(Exception.class)` can mask specific issues that should be handled differently.
 *  - Swallowing Exceptions: Ensure that exceptions are logged and that appropriate error responses are returned. Don't just catch an exception and do nothing.
 *  - Exposing Sensitive Information: Be careful not to include sensitive details (like full stack traces or internal system state) in error responses sent to clients,
 *    especially in production environments. `request.getDescription(false)` typically provides the URI, which is safe.
 */
@ControllerAdvice
// By not extending ResponseEntityExceptionHandler, this handler is simpler but might miss out on handling some built-in Spring exceptions
// unless explicitly added. For this example, it's focused on custom and generic exceptions.
public class GlobalExceptionHandler /* extends ResponseEntityExceptionHandler */ {

	/**
	 * BEGINNER:
	 * This method specifically handles errors where something was not found (like an employee with a specific ID).
	 * If a `ResourceNotFoundException` error happens, this method creates an `ErrorDetails` object with the current time,
	 * the error message from the exception, and details about the web request that caused the error.
	 * It then sends this `ErrorDetails` back with a "404 Not Found" status, which is the standard way for websites to say "I couldn't find that."
	 *
	 * INTERMEDIATE:
	 * This method is an exception handler specifically for `ResourceNotFoundException`.
	 * When a `ResourceNotFoundException` is thrown by any controller, this method is invoked.
	 * It constructs an `ErrorDetails` object containing the timestamp, the exception's message, and a description of the web request (e.g., the URI).
	 * It then wraps this `ErrorDetails` object in a `ResponseEntity` with HTTP status `HttpStatus.NOT_FOUND` (404).
	 * This ensures that clients receive a standardized error response with the correct semantics for "resource not found" errors.
	 *
	 * ADVANCED:
	 * This `@ExceptionHandler` method targets the custom `ResourceNotFoundException`.
	 * It demonstrates best practice by translating a domain-specific exception into a client-friendly REST response with an appropriate HTTP status code (404).
	 * `WebRequest request`: This parameter, provided by Spring, gives access to request metadata (like headers, parameters, and the request URI via `request.getDescription(false)`).
	 * The `request.getDescription(false)` typically returns the request URI without query parameters. If query parameters are important for context, `request.getDescription(true)` can be used, but be mindful of sensitive data.
	 * Consider logging the exception `ex` here for monitoring and debugging purposes, e.g., `log.warn("ResourceNotFoundException: {} on URI: {}", ex.getMessage(), request.getDescription(false));`
	 *
	 * @param ex The `ResourceNotFoundException` instance that was thrown.
	 * @param request The `WebRequest` context for the current request.
	 * @return A `ResponseEntity` containing the `ErrorDetails` and HTTP status 404.
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	/**
	 * BEGINNER:
	 * This is a "catch-all" error handler. If any error happens that isn't a `ResourceNotFoundException` (which has its own handler above),
	 * this method will take care of it.
	 * It creates an `ErrorDetails` object similar to the other handler but sends it back with a "500 Internal Server Error" status.
	 * This status means "something went wrong on the server, but it's not a specific 'not found' issue."
	 *
	 * INTERMEDIATE:
	 * This method is a generic exception handler that catches any `Exception` not handled by more specific `@ExceptionHandler` methods in this class.
	 * It's a fallback mechanism to ensure that unhandled exceptions still result in a structured error response.
	 * It creates an `ErrorDetails` object and returns it with HTTP status `HttpStatus.INTERNAL_SERVER_ERROR` (500).
	 * This is crucial for preventing stack traces or default server error pages from being sent to the client.
	 *
	 * ADVANCED:
	 * This `@ExceptionHandler(Exception.class)` method serves as a global fallback for any exceptions not caught by more specific handlers.
	 * It's essential for robust API design, ensuring that even unexpected errors are translated into a consistent JSON error response format.
	 * **Crucially, all exceptions caught by this handler should be logged with high severity (e.g., ERROR level), including their stack traces,
	 * as they often represent unforeseen issues or bugs in the application.**
	 * Example: `log.error("Unhandled global exception on URI: {}", request.getDescription(false), ex);`
	 * For security reasons, the message from a generic `Exception` (`ex.getMessage()`) might sometimes contain internal details not suitable for external clients.
	 * In such cases, a more generic error message like "An unexpected internal error occurred" might be preferable for the `message` field in `ErrorDetails`,
	 * while `ex.getMessage()` and the stack trace are logged internally.
	 * The `request.getDescription(false)` provides context about the request that triggered the error.
	 *
	 * @param ex The `Exception` instance that was thrown.
	 * @param request The `WebRequest` context for the current request.
	 * @return A `ResponseEntity` containing the `ErrorDetails` and HTTP status 500.
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
		// It's highly recommended to log the full exception here for debugging purposes.
		// logger.error("An unexpected error occurred: " + ex.getMessage(), ex);
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
