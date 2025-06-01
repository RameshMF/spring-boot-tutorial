package net.guides.springboot.springbootasyncexample.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * BEGINNER:
 * This class is like a template or a blueprint for storing information about a GitHub user.
 * It has "fields" (variables) to hold the user's name and their blog URL.
 * - `private String name;`: This is a field to store the user's name as text. "private" means only code within this User class can directly access it.
 * - `private String blog;`: This is a field to store the user's blog URL as text.
 * It also has "methods" (functions) to get and set these values:
 * - `getName()`: A "getter" method to retrieve the user's name.
 * - `setName(String name)`: A "setter" method to change or set the user's name.
 * - `getBlog()`: A "getter" method to retrieve the user's blog URL.
 * - `setBlog(String blog)`: A "setter" method to change or set the user's blog URL.
 * - `toString()`: A special method that provides a simple text representation of the User object, useful for printing or logging.
 *
 * This `User` class is used by the `GitHubLookupService` to hold the data fetched from the GitHub API.
 *
 * INTERMEDIATE:
 * This class is a Plain Old Java Object (POJO) that acts as a Data Transfer Object (DTO).
 * Its primary purpose is to model the structure of user data retrieved from the GitHub API.
 * `@JsonIgnoreProperties(ignoreUnknown=true)`: This is a Jackson annotation. Jackson is a library used for converting Java objects to JSON (text format) and vice-versa.
 *   This particular annotation tells Jackson that if it finds properties in the JSON data from GitHub that are not defined as fields in this `User` class,
 *   it should ignore them instead of throwing an error. This makes the DTO more resilient to changes in the API response (e.g., if GitHub adds new fields).
 * The class follows JavaBean conventions with private fields and public getter/setter methods.
 * The `toString()` method is overridden to provide a developer-friendly string representation of the object, which is helpful for debugging and logging.
 * In the context of `GitHubLookupService`, when the service calls the GitHub API, the JSON response is mapped (deserialized) into an instance of this `User` class.
 *
 * ADVANCED:
 * This `User` class serves as a DTO for deserializing responses from the GitHub API.
 * Considerations:
 *  - Immutability: This class is currently mutable (its state can be changed after creation via setters). For DTOs that represent a snapshot of data,
 *    especially in concurrent environments or when used as keys in maps, immutability is often preferred.
 *    An immutable version would have final fields, a constructor to initialize all fields, and only getter methods (no setters).
 *    Example of immutable approach:
 *    ```java
 *    public class User {
 *        private final String name;
 *        private final String blog;
 *
 *        public User(String name, String blog) {
 *            this.name = name;
 *            this.blog = blog;
 *        }
 *        // ... getters only ...
 *    }
 *    ```
 *    Libraries like Lombok (`@Value` or `@Builder` with `@AllArgsConstructor`) can significantly reduce boilerplate for immutable objects.
 *  - Thread Safety: As a mutable POJO, this class is not inherently thread-safe if instances are shared and modified by multiple threads.
 *    However, in its typical use case within `GitHubLookupService` (where a new `User` object is likely created for each API response and then passed along,
 *    often within a `CompletableFuture`), thread-safety concerns for the `User` object itself are minimal unless explicitly shared and mutated across threads.
 *  - Builder Pattern: If the `User` object had many fields, especially optional ones, the Builder pattern could be used to improve the readability and
 *    flexibility of object creation compared to a constructor with many parameters. Lombok's `@Builder` annotation is a common way to achieve this.
 *  - `serialVersionUID`: This class does not declare a `serialVersionUID`. While not strictly necessary for a DTO that is primarily serialized to JSON,
 *    if this class were to be used with Java's binary serialization mechanism (e.g., in RMI or some caching strategies), explicitly declaring a `serialVersionUID`
 *    is a best practice for version control during deserialization. However, for JSON serialization with Jackson, this is not typically a concern.
 *  - Validation: For DTOs representing data to be sent *to* an API or processed internally, validation annotations (e.g., from `jakarta.validation.constraints` like `@NotNull`, `@NotBlank`)
 *    could be added to fields to ensure data integrity.
 *
 * This class is instantiated by Jackson when `RestTemplate` (used in `GitHubLookupService`) converts the JSON response from the GitHub API into a Java object.
 * The `@JsonIgnoreProperties(ignoreUnknown = true)` annotation is crucial here because the actual GitHub API response for a user contains many more fields
 * than just `name` and `blog`. Without this annotation, Jackson would throw an exception by default upon encountering these unknown properties.
 */
@JsonIgnoreProperties(ignoreUnknown=true) // Tells Jackson to ignore any properties in the JSON input that are not mapped to fields in this class.
public class User {

    // BEGINNER: This variable will hold the user's name, like "John Doe".
    // INTERMEDIATE: A private field to store the 'name' attribute from the GitHub user JSON response.
    // ADVANCED: This field will be populated by Jackson during JSON deserialization from the GitHub API response.
    private String name;

    // BEGINNER: This variable will hold the user's blog website address, like "johndoe.com".
    // INTERMEDIATE: A private field to store the 'blog' attribute from the GitHub user JSON response.
    // ADVANCED: This field will also be populated by Jackson. If the 'blog' field is missing or null in the JSON, this will be null.
    private String blog;

    /**
     * BEGINNER: This is a "getter" method. It's how other parts of the code can ask for the user's name.
     * INTERMEDIATE: Standard JavaBean getter for the `name` property.
     * ADVANCED: Accessor method for the `name` field.
     * @return the name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * BEGINNER: This is a "setter" method. It's how other parts of the code can set or change the user's name.
     * INTERMEDIATE: Standard JavaBean setter for the `name` property.
     * ADVANCED: Mutator method for the `name` field. Allows modification of the object's state after creation.
     * @param name the new name to set for the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * BEGINNER: This is a "getter" method for the user's blog URL.
     * INTERMEDIATE: Standard JavaBean getter for the `blog` property.
     * ADVANCED: Accessor method for the `blog` field.
     * @return the blog URL of the user.
     */
    public String getBlog() {
        return blog;
    }

    /**
     * BEGINNER: This is a "setter" method for the user's blog URL.
     * INTERMEDIATE: Standard JavaBean setter for the `blog` property.
     * ADVANCED: Mutator method for the `blog` field.
     * @param blog the new blog URL to set for the user.
     */
    public void setBlog(String blog) {
        this.blog = blog;
    }

    /**
     * BEGINNER: This method gives a text description of the User object. If you print a User object, this is what you'll see.
     * For example: "User [name=John Doe, blog=johndoe.com]"
     * INTERMEDIATE: Overrides the default `toString()` method from the `Object` class to provide a more informative
     * string representation of the `User` instance, which is useful for logging and debugging.
     * ADVANCED: A standard override of `toString()` for DTOs. For more complex objects or for debugging,
     * libraries like Apache Commons Lang `ToStringBuilder` or Lombok's `@ToString` can generate this automatically
     * and offer more formatting options.
     * @return a string representation of the User object.
     */
    @Override
    public String toString() {
        return "User [name=" + name + ", blog=" + blog + "]";
    }

}