package net.guides.springboot2.springboot2junit5example;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class Springboot2Junit5ExampleApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	@DisplayName("test Message REST API ")
	void testMessage() {
		String message = this.restTemplate.getForObject("/hello", String.class);
		assertEquals("Hello World", message);
	}

}