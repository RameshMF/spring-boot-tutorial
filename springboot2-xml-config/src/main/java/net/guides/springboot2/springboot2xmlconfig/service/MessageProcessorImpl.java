package net.guides.springboot2.springboot2xmlconfig.service;

public class MessageProcessorImpl implements MessageProcessor {

	private MessageService messageService;

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public void processMsg(String message) {
		messageService.sendMsg(message);
	}
}
