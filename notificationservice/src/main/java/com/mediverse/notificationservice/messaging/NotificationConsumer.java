package com.mediverse.notificationservice.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.mediverse.notificationservice.config.RabbitMQConfig;
import com.mediverse.notificationservice.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

	private final NotificationService notificationService;

	@RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
	public void consumeNotification(NotificationMessage message) {
		notificationService.processNotificationEvent(message);
	}
}