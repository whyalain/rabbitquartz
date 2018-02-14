package ecloud.demo.rabbitquartz.infra.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ecloud.demo.rabbitquartz.business.enums.ProcessStatusEnum;
import ecloud.demo.rabbitquartz.business.service.RabbitQuartzService;
import ecloud.demo.rabbitquartz.config.RabbitConfig;

@Component
public class RabbitSender {

	private final Logger logger = LoggerFactory.getLogger(RabbitSender.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private RabbitQuartzService rabbitQuartzService;

	public void send(Long orderId) {
		logger.info("Start sending");

		rabbitQuartzService.changeOrderProcessStatus(orderId, ProcessStatusEnum.PROCESSING);
		this.rabbitTemplate.convertAndSend(RabbitConfig.ORDER_EXCHANGE, RabbitConfig.ORDER_ROUTING_KEY,
				orderId.toString());

		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("End sending \n");
	}
}
