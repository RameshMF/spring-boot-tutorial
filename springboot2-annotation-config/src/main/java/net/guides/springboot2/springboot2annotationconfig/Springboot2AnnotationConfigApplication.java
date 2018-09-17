package net.guides.springboot2.springboot2annotationconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import net.guides.springboot2.springboot2annotationconfig.service.UserService;

@SpringBootApplication
public class Springboot2AnnotationConfigApplication {

	private static Logger LOG = LoggerFactory.getLogger(Springboot2AnnotationConfigApplication.class);

	public static void main(String[] args) {
		LOG.info("STARTING THE APPLICATION");
		ApplicationContext applicationContext = SpringApplication.run(Springboot2AnnotationConfigApplication.class,
				args);
		UserService userService = applicationContext.getBean(UserService.class);
		userService.processMsg("twitter message sending ");
		LOG.info("APPLICATION FINISHED");
	}
}
