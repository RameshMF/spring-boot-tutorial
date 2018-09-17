package net.guides.springboot2.springboot2javaconfig.service;

public class SMSService implements MessageService{

	public void sendMsg(String message) {
		System.out.println(message);
	}

}
