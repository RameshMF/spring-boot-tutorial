package net.guides.springboot2.springboot2javaconfig.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class MessageProcessorImpl implements MessageProcessor {

	private MessageService messageService;

	@Autowired
	public MessageProcessorImpl(@Qualifier("TwitterService") MessageService messageService) {
		this.messageService = messageService;
	}

	public void processMsg(String message) {
		messageService.sendMsg(message);
	}
}
