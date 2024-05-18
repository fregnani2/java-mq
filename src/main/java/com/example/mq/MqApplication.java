package com.example.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableJms
@SpringBootApplication
public class MqApplication {

	@Autowired
	private JmsTemplate jmsTemplate;

	public static void main(String[] args) {
		SpringApplication.run(MqApplication.class, args);
	}


	@GetMapping("send")
	String send(){
		try{
			jmsTemplate.convertAndSend("DEV.QUEUE.1", "Hello World!");
			return "OK";
		}catch(JmsException ex){
			ex.printStackTrace();
			return "FAIL";
		}
	}

	@GetMapping("recv")
	String recv(){
		try{
			return jmsTemplate.receiveAndConvert("DEV.QUEUE.1").toString();
		}catch(JmsException ex){
			ex.printStackTrace();
			return "FAIL";
		}
	}

}
