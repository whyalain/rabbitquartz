package ecloud.demo.rabbitquartz.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ecloud.demo.rabbitquartz.business.service.RabbitQuartzService;

/*
 * Spring Boot中消息接受处理完后，若无异常，则由Spring Boot发送消息接受确认。
 * spring.rabbitmq.listener.prefetch控制QoS。
 */
@Configuration
@EnableRabbit
public class RabbitConfig {

	private final Logger logger = LoggerFactory.getLogger(RabbitConfig.class);

	public static final String ORDER_ROUTING_KEY = "order_key";

	public static final String ORDER_QUEUE = "order_queue";

	public static final String ORDER_EXCHANGE = "order_exchange";

	@Bean
	Queue orderQueue() {
		return new Queue(ORDER_QUEUE);
	}

	@Bean
	DirectExchange orderExchange() {
		return new DirectExchange(ORDER_EXCHANGE);
	}

	@Bean
	Binding bindingExangeQueue() {
		return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(ORDER_ROUTING_KEY);
	}

	@Bean
	RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, RabbitQuartzService rabbitQuartzService) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

		// 发送确认的回调方法，配合rabbitmq.publisher-confirms=true
		rabbitTemplate.setConfirmCallback((correlationData, b, s) -> {
			if (b) {
				logger.info("Sending OK");
			} else {
				logger.error("Sending failed, cause: " + s);
			}
		});

		rabbitTemplate.setMessageConverter(messageConverter());

		return rabbitTemplate;
	}

	@Bean
	Jackson2JsonMessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
