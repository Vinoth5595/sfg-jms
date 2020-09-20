package com.javageek.listener;

import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.javageek.config.JmsConfig;
import com.javageek.model.HelloWorldMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class HelloMessageListener {
	
	private static int send_receive_count = 0;
	private final JmsTemplate jmsTemplate;

	@JmsListener(destination = JmsConfig.MY_QUEUE)
	public void listen(@Payload HelloWorldMessage helloWorldMessage,
						@Headers MessageHeaders headers, Message message) {
		
		log.info("Listener receives the message...");
		log.info(""+helloWorldMessage);
		
	}
	
	@JmsListener(destination = JmsConfig.MY_SEND_RECEIVE_QUEUE)
	public void listenSendReceive(@Payload HelloWorldMessage helloWorldMessage,
						@Headers MessageHeaders headers, Message message) throws JmsException, JMSException {
		
		HelloWorldMessage payLoadMessage = HelloWorldMessage
				.builder()
				.id(UUID.randomUUID())
				.message("Message from Listener = "+send_receive_count)
				.build();
		
		log.info(""+helloWorldMessage);
		
		jmsTemplate.convertAndSend(message.getJMSReplyTo(), payLoadMessage);
		log.info("Listener receives and replied...");
		
	}
}
