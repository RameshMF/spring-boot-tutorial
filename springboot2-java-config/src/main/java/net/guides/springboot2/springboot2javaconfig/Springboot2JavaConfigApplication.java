package net.guides.springboot2.springboot2javaconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.guides.springboot2.springboot2javaconfig.service.EmailService;
import net.guides.springboot2.springboot2javaconfig.service.MessageProcessor;
import net.guides.springboot2.springboot2javaconfig.service.MessageProcessorImpl;
import net.guides.springboot2.springboot2javaconfig.service.MessageService;
import net.guides.springboot2.springboot2javaconfig.service.SMSService;
import net.guides.springboot2.springboot2javaconfig.service.TwitterService;

@SpringBootApplication
@Configuration
public class Springboot2JavaConfigApplication {

	@Bean(name = "emailService")
	public MessageService emailService() {
		return new EmailService();
	}

	@Bean(name = "smsService")
	public MessageService smsService() {
		return new SMSService();
	}

	@Bean(name = "twitterService")
	public MessageService twitterService() {
		return new TwitterService();
	}

	@Bean
	public MessageProcessor messageProcessor() {
		return new MessageProcessorImpl(twitterService());
	}

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(Springboot2JavaConfigApplication.class, args);
		MessageProcessor userService = applicationContext.getBean(MessageProcessor.class);
		userService.processMsg("twitter message sending ");
	}
}
