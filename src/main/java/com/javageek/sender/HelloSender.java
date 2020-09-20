package com.javageek.sender;

import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javageek.config.JmsConfig;
import com.javageek.model.HelloWorldMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class HelloSender {
	
	private static int sent_count = 0;
	private static int send_receive_count = 0;
	
	private final JmsTemplate jmsTemplate;
	private final ObjectMapper objectMapper;
	
	@Scheduled(fixedRate = 2000)
	public void sendMessage() {
		log.info("Message Sending...");
		
		HelloWorldMessage message = HelloWorldMessage
				.builder()
				.id(UUID.randomUUID())
				.message("Hello Sent only!!! "+sent_count)
				.build();
		
		jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);
		
		log.info("Message Sent");
	}
	
	@Scheduled(fixedRate = 2000)
	public void sendAndReceiveMessage() throws JMSException {
		log.info("Message Sending...");
		
		HelloWorldMessage message = HelloWorldMessage
				.builder()
				.id(UUID.randomUUID())
				.message("Hello Send and Receive!!! "+send_receive_count)
				.build();
		
		Message receivedMessage = jmsTemplate.sendAndReceive(JmsConfig.MY_QUEUE, new MessageCreator() {

			@Override
			public Message createMessage(Session session) {
				Message helloMessage = null;
				try {
					helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
					helloMessage.setStringProperty("_type", "com.javageek.model.HelloWorldMessage");
					log.info("Sending Hello..");

				} catch (Exception e) {
					e.printStackTrace();
				}
				return helloMessage;
			}
		});
		
		log.info("Message Send and Received = "+receivedMessage.getBody(String.class));
	}

}
