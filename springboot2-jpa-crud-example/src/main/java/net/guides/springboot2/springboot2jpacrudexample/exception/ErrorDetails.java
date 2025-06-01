package net.guides.springboot2.springboot2jpacrudexample.exception;

import java.util.Date;

/**
 * BEGINNER:
 * This class is a simple blueprint for creating error messages that our API (Application Programming Interface) will send back
 * when something goes wrong. Think of it like a form that always has the same fields: when the error happened,
 * a short error message, and a more detailed explanation.
 * This helps anyone using our API to understand what the problem was in a consistent way.
 *
 * INTERMEDIATE:
 * This class, `ErrorDetails`, is a Plain Old Java Object (POJO) used as a Data Transfer Object (DTO)
 * specifically for conveying structured error information in API responses.
 * When an exception occurs that needs to be communicated to an API client (like a web browser or another application),
 * instances of this class are typically created, populated, and then serialized to JSON or XML to form the response body.
 * This standardized error format is crucial for API consumers to parse and handle errors programmatically.
 *
 * ADVANCED:
 * `ErrorDetails` serves as a standardized schema for error responses within the application's RESTful services.
 * Adopting a consistent error response structure is a key aspect of robust API design. It improves client-side error handling
 * by providing predictable fields.
 * This class could be further enhanced, for example, by adding:
 *  - `errorCode`: A unique application-specific error code that clients can use for more precise error handling or localization.
 *  - `path`: The API path that was requested when the error occurred.
 *  - `validationErrors`: A list of more specific error details if the error is due to input validation failures (e.g., field-specific messages).
 * Such enhancements make the API more developer-friendly and easier to debug and integrate with.
 * This POJO is typically used in conjunction with global exception handlers (`@ControllerAdvice` classes)
 * that catch exceptions and transform them into `ResponseEntity<ErrorDetails>`.
 *
 * Importance of Structured Error Responses in APIs:
 *  1. Consistency: Clients can rely on a consistent format for all errors, simplifying parsing and handling logic.
 *  2. Clarity: Provides clear, actionable information to the client (and developers debugging the client) about what went wrong.
 *  3. Debuggability: Detailed error messages, timestamps, and potentially error codes make it easier to diagnose issues.
 *  4. Client-Side Handling: Allows clients to build more robust error handling, potentially displaying user-friendly messages
 *     or attempting specific recovery actions based on the error details.
 *  5. Monitoring and Logging: Standardized error structures can be more easily parsed and ingested by logging and monitoring systems,
 *     aiding in tracking error rates and types.
 */
public class ErrorDetails {
	// BEGINNER: Stores the exact date and time when the error occurred.
	// INTERMEDIATE: A `java.util.Date` object representing the instant the error was recorded.
	// ADVANCED: This timestamp is useful for correlating logs and understanding the temporal context of an error.
	//           Consider using `java.time.Instant` or `OffsetDateTime` for better time zone handling and precision in modern Java applications.
	private Date timestamp;

	// BEGINNER: A short, human-readable message explaining the error. For example, "Employee not found".
	// INTERMEDIATE: A concise summary of the error. This is often the primary message displayed to a user or logged for quick diagnostics.
	// ADVANCED: This field should provide a succinct error message. It might be sourced from exception messages or be a predefined string based on the error type.
	private String message;

	// BEGINNER: More detailed information about the error, like which specific employee ID was not found.
	// INTERMEDIATE: Provides more specific context or details about the error, such as the specific resource or condition that caused it.
	// ADVANCED: This field can contain more granular information, such as stack trace snippets (though generally not recommended for client-facing APIs due to security/ verbosity),
	//           or specific context from the request (e.g., "Request: GET /api/v1/employees/999").
	private String details;

	/**
	 * BEGINNER: This is a constructor, a special method used to create new `ErrorDetails` objects.
	 * When we create an error detail, we need to give it the timestamp, message, and details.
	 *
	 * INTERMEDIATE: Constructor for the `ErrorDetails` class.
	 * It initializes a new instance with the provided timestamp, general message, and specific details of the error.
	 * The `super()` call is a call to the constructor of the parent class (`Object` in this case), which is implicitly done
	 * if not written, but sometimes included for clarity or by coding conventions.
	 *
	 * ADVANCED: Standard constructor for initializing all fields of the `ErrorDetails` immutable object (once created, its state doesn't change if setters are not provided, which is good practice for DTOs).
	 * Consider using a builder pattern if the number of fields grows, or if some fields are optional, to improve readability and ease of construction.
	 *
	 * @param timestamp The time at which the error occurred.
	 * @param message A general message describing the error.
	 * @param details Specific details about the error.
	 */
	public ErrorDetails(Date timestamp, String message, String details) {
		super(); // Calls the constructor of the superclass (Object).
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	/**
	 * BEGINNER: This method lets other parts of the code get the timestamp of the error.
	 * INTERMEDIATE: Standard getter method for the `timestamp` field.
	 * ADVANCED: Accessor for the `timestamp` field. Part of the JavaBean convention for properties.
	 * @return The timestamp of the error.
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * BEGINNER: This method lets other parts of the code get the short error message.
	 * INTERMEDIATE: Standard getter method for the `message` field.
	 * ADVANCED: Accessor for the `message` field.
	 * @return The general error message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * BEGINNER: This method lets other parts of the code get the detailed error explanation.
	 * INTERMEDIATE: Standard getter method for the `details` field.
	 * ADVANCED: Accessor for the `details` field.
	 * @return The specific details of the error.
	 */
	public String getDetails() {
		return details;
	}
}
