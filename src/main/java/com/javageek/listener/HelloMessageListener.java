package com.javageek.listener;

import javax.jms.Message;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.javageek.config.JmsConfig;
import com.javageek.model.HelloWorldMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HelloMessageListener {

	@JmsListener(destination = JmsConfig.MY_QUEUE)
	public void listen(@Payload HelloWorldMessage helloWorldMessage,
						@Headers MessageHeaders headers, Message message) {
		
		log.info("Listener receives the message...");
		log.info(""+helloWorldMessage);
		
	}
}
