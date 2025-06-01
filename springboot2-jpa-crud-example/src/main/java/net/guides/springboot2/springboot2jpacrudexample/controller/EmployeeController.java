package net.guides.springboot2.springboot2jpacrudexample.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.springboot2jpacrudexample.exception.ResourceNotFoundException;
import net.guides.springboot2.springboot2jpacrudexample.model.Employee;
import net.guides.springboot2.springboot2jpacrudexample.repository.EmployeeRepository;

/**
 * BEGINNER:
 * This class is a Controller. In Spring MVC (Model-View-Controller), a controller handles incoming web requests.
 * This specific controller manages operations related to "Employees" (e.g., creating, reading, updating, deleting employees).
 * Annotations used:
 *  - @RestController: This tells Spring that this class will handle incoming web requests and directly return data (like JSON) as a response,
 *    instead of returning a view (like an HTML page). It combines @Controller and @ResponseBody.
 *  - @RequestMapping("/api/v1"): This sets a base path for all the methods in this controller. So, any request starting with "/api/v1"
 *    will be directed here. For example, to get all employees, the full path would be "/api/v1/employees".
 *
 * INTERMEDIATE:
 * This class implements a RESTful API for managing Employee resources.
 * It uses Spring MVC annotations to map HTTP requests to specific handler methods.
 *  - @Autowired: This is used for dependency injection. Here, it injects an instance of `EmployeeRepository`
 *    so that the controller can interact with the database.
 *  - @GetMapping, @PostMapping, @PutMapping, @DeleteMapping: These are specialized versions of @RequestMapping for specific HTTP methods (GET, POST, PUT, DELETE).
 *    They make the code more readable.
 *  - @PathVariable: This extracts values from the URL path (e.g., the 'id' in "/employees/{id}").
 *  - @RequestBody: This indicates that the method parameter (e.g., `employee` in `createEmployee`) should be populated with the data sent in the request body (usually JSON).
 *  - @Valid: This annotation, when placed on `@RequestBody Employee employee`, triggers validation of the employee object based on validation annotations defined in the `Employee` class (e.g., @NotNull, @Size).
 *           If validation fails, a `MethodArgumentNotValidException` is thrown, which can be handled globally.
 *  - ResponseEntity: This class represents the entire HTTP response, including status code, headers, and body. It allows for more control over the response.
 *
 * ADVANCED:
 * This controller adheres to common REST API design principles.
 *  - Resource-based URLs: URLs are designed around resources (e.g., "/employees", "/employees/{id}").
 *  - Standard HTTP methods: Uses GET for retrieval, POST for creation, PUT for update, and DELETE for removal.
 *  - Statelessness: Each request from a client contains all the information needed to understand and process the request. The server does not store any client context between requests.
 *  - Error Handling: Uses a custom exception `ResourceNotFoundException` for cases where an employee is not found. This exception is likely handled by a global exception handler (e.g., using @ControllerAdvice)
 *    to return appropriate HTTP status codes (e.g., 404 Not Found).
 *
 * Best Practices for Controller Design:
 *  - Keep controllers lean: Controllers should primarily delegate business logic to service layers. They should be responsible for request/response handling, validation, and invoking services.
 *    In this example, for simplicity, it directly interacts with the repository. In larger applications, a service layer between controller and repository is recommended.
 *  - Versioning: The "/api/v1" in @RequestMapping is a good practice for API versioning. If breaking changes are needed in the future, a new version (e.g., "/api/v2") can be introduced.
 *  - Consistent response format: Use ResponseEntity to standardize response structures, especially for error responses.
 *  - Input Validation: Always validate input data (e.g., using @Valid) to prevent errors and ensure data integrity.
 *  - Exception Handling: Implement robust exception handling using @ControllerAdvice and @ExceptionHandler to provide meaningful error responses to the client.
 *    For example, instead of letting `ResourceNotFoundException` propagate as a generic 500 error, a global handler would convert it to a 404.
 *
 * Potential Pitfalls:
 *  - Fat Controllers: Putting too much business logic in controllers makes them hard to test and maintain.
 *  - Inconsistent Error Responses: Not having a centralized exception handling mechanism can lead to different error formats for different errors.
 *  - Security: This controller does not implement any security measures. In a real application, endpoints should be secured (e.g., using Spring Security for authentication and authorization).
 *  - Hardcoding URLs: While @RequestMapping defines base paths, avoid hardcoding full URLs elsewhere in the application. Use Spring's `LinkBuilder` or similar mechanisms if needed.
 */
@RestController
@RequestMapping("/api/v1") // Base path for all handler methods in this controller.
public class EmployeeController {

	// BEGINNER: This line allows our controller to use EmployeeRepository to talk to the database.
	// INTERMEDIATE: @Autowired injects an instance of EmployeeRepository (managed by Spring) into this controller. This is known as Dependency Injection.
	// ADVANCED: The EmployeeRepository is a Spring Data JPA repository providing CRUD operations for Employee entities.
	@Autowired
	private EmployeeRepository employeeRepository;

	/**
	 * BEGINNER: This method gets a list of all employees.
	 * When you go to "/api/v1/employees" in your web browser (or using a tool like Postman), this method will be called.
	 * It returns a list of all employees stored in the database.
	 *
	 * INTERMEDIATE: Handles HTTP GET requests to "/api/v1/employees".
	 * It fetches all `Employee` entities from the database using `employeeRepository.findAll()`
	 * and returns them directly in the response body (Spring Boot automatically converts the List<Employee> to JSON).
	 * The HTTP status will be 200 (OK) by default if successful.
	 *
	 * ADVANCED: This method implements the "retrieve all resources" endpoint for the Employee resource.
	 * `employeeRepository.findAll()` typically translates to a "SELECT * FROM employees" SQL query (or equivalent in the underlying JPA provider).
	 * For large datasets, consider pagination and sorting parameters (e.g., using `Pageable` as a method argument) to improve performance and usability.
	 *
	 * @return a List of all employees.
	 */
	@GetMapping("/employees") // Maps HTTP GET requests for "/api/v1/employees" to this method.
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll(); // Fetches all employees from the repository.
	}

	/**
	 * BEGINNER: This method gets a specific employee by their ID.
	 * For example, if you go to "/api/v1/employees/1", this will try to find the employee with ID 1.
	 * If found, it shows the employee's details. If not found, it gives an error.
	 *
	 * INTERMEDIATE: Handles HTTP GET requests to "/api/v1/employees/{id}".
	 * `@PathVariable(value = "id") Long employeeId` extracts the 'id' from the URL path and binds it to the `employeeId` parameter.
	 * It attempts to find an employee by ID. If found, it returns the employee with an HTTP 200 (OK) status.
	 * If not found, it throws a `ResourceNotFoundException`, which should be handled by a global exception handler to return an HTTP 404 (Not Found) status.
	 * `ResponseEntity<Employee>` allows for more control over the response, including setting the status code and headers.
	 *
	 * ADVANCED: This method implements the "retrieve a specific resource" endpoint.
	 * The `orElseThrow()` construct provides a concise way to handle the `Optional<Employee>` returned by `findById()`.
	 * It's crucial that `ResourceNotFoundException` is properly mapped to a 404 response status code, typically via a `@ControllerAdvice` class with an `@ExceptionHandler` method.
	 * Consider HATEOAS principles: the response could include links to related resources or actions.
	 *
	 * @param employeeId The ID of the employee to retrieve. This is extracted from the URL path.
	 * @return ResponseEntity containing the found Employee or an error status if not found.
	 * @throws ResourceNotFoundException if no employee is found with the given ID.
	 */
	@GetMapping("/employees/{id}") // Maps HTTP GET requests for "/api/v1/employees/{some_id}" to this method.
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId)
			throws ResourceNotFoundException {
		// Attempt to find the employee by ID.
		Employee employee = employeeRepository.findById(employeeId)
				// If not found, throw a ResourceNotFoundException.
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
		// If found, return the employee in the response body with an HTTP 200 OK status.
		return ResponseEntity.ok().body(employee);
	}

	/**
	 * BEGINNER: This method creates a new employee.
	 * You would send the employee's information (like name and email) to "/api/v1/employees", and this method saves it to the database.
	 *
	 * INTERMEDIATE: Handles HTTP POST requests to "/api/v1/employees".
	 * `@RequestBody Employee employee` indicates that the request body (expected to be JSON) should be converted into an `Employee` object.
	 * `@Valid` triggers validation on the `employee` object based on annotations in the `Employee` class (e.g., @NotNull, @Email).
	 * If validation fails, Spring Boot typically returns an HTTP 400 (Bad Request) response automatically.
	 * The new employee is saved using `employeeRepository.save()`, and the saved employee (including any generated ID) is returned.
	 * The HTTP status will be 200 (OK) by default. For resource creation, a 201 (Created) status is often more appropriate, which can be set using `ResponseEntity`.
	 *
	 * ADVANCED: This method implements the "create a new resource" endpoint.
	 * It's good practice to return HTTP status 201 (Created) along with a "Location" header pointing to the URL of the newly created resource.
	 * Example: `return ResponseEntity.created(URI.create("/api/v1/employees/" + savedEmployee.getId())).body(savedEmployee);`
	 * The `@Valid` annotation is crucial for input validation before attempting to save the entity.
	 * Consider what happens if an ID is provided in the request body for a POST request. Typically, it should be ignored, as the server generates the ID.
	 *
	 * @param employee The Employee object to create, passed in the request body.
	 * @return The created Employee object (including its generated ID).
	 */
	@PostMapping("/employees") // Maps HTTP POST requests for "/api/v1/employees" to this method.
	public Employee createEmployee(@Valid @RequestBody Employee employee) {
		// Saves the new employee to the database.
		// The save method will also return the saved entity, which might include a generated ID.
		return employeeRepository.save(employee);
	}

	/**
	 * BEGINNER: This method updates an existing employee's details.
	 * You provide the ID of the employee to update and the new information.
	 * For example, to update employee with ID 1, you'd send new details to "/api/v1/employees/1".
	 *
	 * INTERMEDIATE: Handles HTTP PUT requests to "/api/v1/employees/{id}".
	 * `@PathVariable` extracts the employee ID from the URL.
	 * `@Valid @RequestBody` gets the updated employee details from the request body and validates them.
	 * It first fetches the existing employee. If not found, it throws `ResourceNotFoundException` (leading to a 404).
	 * If found, it updates the employee's fields with the new details and saves the changes.
	 * Returns the updated employee with an HTTP 200 (OK) status.
	 *
	 * ADVANCED: This method implements the "update an existing resource" endpoint (full update).
	 * For partial updates, HTTP PATCH method is more appropriate, though PUT can also be used.
	 * This implementation is a full update: it replaces all fields of the existing employee with the provided `employeeDetails`.
	 * If `employeeDetails` is missing some fields, those fields in the existing `employee` might be set to null or default values, depending on the `Employee` class structure and JSON deserialization.
	 * Consider transactional safety: if multiple operations are part of the update, ensure they are atomic using `@Transactional` at the service layer.
	 * Idempotency: PUT requests should be idempotent. Calling this method multiple times with the same `employeeDetails` should result in the same state.
	 *
	 * @param employeeId The ID of the employee to update.
	 * @param employeeDetails An Employee object containing the new details for the employee.
	 * @return ResponseEntity containing the updated Employee or an error status.
	 * @throws ResourceNotFoundException if the employee with the given ID is not found.
	 */
	@PutMapping("/employees/{id}") // Maps HTTP PUT requests for "/api/v1/employees/{some_id}" to this method.
	public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId,
			@Valid @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
		// Find the existing employee by ID.
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

		// Update the employee's details with the new information.
		employee.setEmailId(employeeDetails.getEmailId());
		employee.setLastName(employeeDetails.getLastName());
		employee.setFirstName(employeeDetails.getFirstName());
		// Save the updated employee to the database.
		final Employee updatedEmployee = employeeRepository.save(employee);
		// Return the updated employee with an HTTP 200 OK status.
		return ResponseEntity.ok(updatedEmployee);
	}

	/**
	 * BEGINNER: This method deletes an employee.
	 * You provide the ID of the employee to delete, for example, by sending a DELETE request to "/api/v1/employees/1".
	 * It returns a message confirming the deletion.
	 *
	 * INTERMEDIATE: Handles HTTP DELETE requests to "/api/v1/employees/{id}".
	 * `@PathVariable` extracts the employee ID from the URL.
	 * It first fetches the employee to ensure it exists. If not found, `ResourceNotFoundException` is thrown (leading to a 404).
	 * If found, the employee is deleted using `employeeRepository.delete()`.
	 * It returns a `Map<String, Boolean>` (which becomes a JSON object like `{"deleted": true}`) to indicate success.
	 * A common practice for DELETE is to return HTTP 204 (No Content) if successful and no body, or HTTP 200 (OK) if a body is returned.
	 *
	 * ADVANCED: This method implements the "delete a resource" endpoint.
	 * Returning HTTP 204 (No Content) is often preferred for DELETE operations as there's no meaningful content to return.
	 * Example: `employeeRepository.delete(employee); return ResponseEntity.noContent().build();`
	 * Idempotency: DELETE requests should be idempotent. Deleting a resource that's already deleted should ideally return a 404 (if subsequent attempts try to fetch it first) or a 204/200 (if the system doesn't distinguish).
	 * This implementation first fetches the employee, which is good practice to confirm existence before deletion and to allow for any pre-deletion logic (e.g., authorization checks, cascading deletes if handled manually).
	 *
	 * @param employeeId The ID of the employee to delete.
	 * @return A Map indicating the result of the deletion (e.g., `{"deleted": true}`).
	 * @throws ResourceNotFoundException if the employee with the given ID is not found.
	 */
	@DeleteMapping("/employees/{id}") // Maps HTTP DELETE requests for "/api/v1/employees/{some_id}" to this method.
	public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId)
			throws ResourceNotFoundException {
		// Find the employee by ID.
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

		// Delete the employee from the database.
		employeeRepository.delete(employee);
		// Create a response map indicating successful deletion.
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		// Return the response map.
		return response;
	}
}
