package net.guides.springboot2.jdbc;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.guides.springboot2.jdbc.model.Employee;
import net.guides.springboot2.jdbc.repository.EmployeeJDBCRepository;

@SpringBootApplication
public class Application implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private EmployeeJDBCRepository employeeRepository;
	
	@Override
	public void run(String... args) throws Exception {

		logger.info("Inserting -> {}", employeeRepository.insert(new Employee(10011L, "Ramesh", "Fadatare", "ramesh@gmail.com")));
		logger.info("Inserting -> {}", employeeRepository.insert(new Employee(10012L, "John", "Cena", "john@gmail.com")));
		logger.info("Inserting -> {}", employeeRepository.insert(new Employee(10013L, "tony", "stark", "stark@gmail.com")));
		
		logger.info("Employee id 10011 -> {}", employeeRepository.findById(10011L));

		logger.info("Update 10003 -> {}", employeeRepository.update(new Employee(10011L, "ram", "Stark", "ramesh123@gmail.com")));

		employeeRepository.deleteById(10013L);

		logger.info("All users -> {}", employeeRepository.findAll());
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
