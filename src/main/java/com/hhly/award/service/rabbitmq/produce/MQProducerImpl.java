package com.hhly.award.service.rabbitmq.produce;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQProducerImpl implements MQProducer {
	private static final Logger LOGGER = Logger.getLogger(MQProducerImpl.class);
    @Autowired
	private AmqpTemplate amqpTemplate;
    
	@Override
	public void sendDataToQueue(String queueKey, String message) {
		LOGGER.info(new StringBuilder("发送mq信息：queue:").append(queueKey).append(",messge:").append(message).toString());
		byte [] body= message.getBytes();
		MessageProperties properties = new MessageProperties();
		properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
		Message message2 = new Message(body,properties );
		amqpTemplate.send(queueKey,message2);
	}
	
	@Override
	public void sendDataToRouting(String exchange, String routing, String message) {
		LOGGER.info(new StringBuilder("发送mq信息：exchange:").append(exchange).append(",routing:").append(routing).append(",messge:").append(message).toString());
		byte [] body= message.getBytes();
		MessageProperties properties = new MessageProperties();
		properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
		Message message2 = new Message(body,properties );
		amqpTemplate.send(exchange,routing, message2);
	}
}
