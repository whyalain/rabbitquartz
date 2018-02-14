package ecloud.demo.rabbitquartz.infra.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ecloud.demo.rabbitquartz.business.service.RabbitQuartzService;
import ecloud.demo.rabbitquartz.config.RabbitConfig;

@Component
@RabbitListener(queues = RabbitConfig.ORDER_QUEUE)
public class RabbitReceiver {

	private final Logger logger = LoggerFactory.getLogger(RabbitReceiver.class);

	@Autowired
	private RabbitQuartzService rabbitQuartzService;

	@RabbitHandler
	public void process(String message) {
		logger.info("Received order. ID: " + message);

		Long orderId = Long.valueOf(message);
		rabbitQuartzService.processOrder(orderId);
	}
}
