package com.mediverse.notificationservice.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.mediverse.notificationservice.config.RabbitMQConfig;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationProducer {

	private final RabbitTemplate rabbitTemplate;

	public void sendNotification(NotificationMessage message) {

		rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_QUEUE, message);
	}
}