package net.guides.springboot.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.guides.springboot.jpa.model.Instructor;
import net.guides.springboot.jpa.model.InstructorDetail;
import net.guides.springboot.jpa.repository.InstructorRepository;

@SpringBootApplication
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private InstructorRepository instructorRepository;

	@Override
	public void run(String... args) throws Exception {

		Instructor instructor = new Instructor("Ramesh", "Fadatare", "ramesh@gmail.com");

		InstructorDetail instructorDetail = new InstructorDetail("Java Guides", "Cricket Playing");

		// associate the objects
		instructor.setInstructorDetail(instructorDetail);

		instructorRepository.save(instructor);
	}
}
