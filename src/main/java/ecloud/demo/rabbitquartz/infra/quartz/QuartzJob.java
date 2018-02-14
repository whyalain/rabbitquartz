package ecloud.demo.rabbitquartz.infra.quartz;

import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import ecloud.demo.rabbitquartz.business.entity.Order;
import ecloud.demo.rabbitquartz.business.service.RabbitQuartzService;
import ecloud.demo.rabbitquartz.infra.rabbitmq.RabbitSender;

@Component
@PersistJobDataAfterExecution
// 禁止任务并发执行
@DisallowConcurrentExecution
public class QuartzJob extends QuartzJobBean {

	private final Logger logger = LoggerFactory.getLogger(QuartzJob.class);

	@Autowired
	private RabbitQuartzService rabbitQuartzService;

	@Autowired
	private RabbitSender rabbitSender;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		List<Order> orderList = rabbitQuartzService.getAllOrdersToBeProcessed();

		if (orderList.size() > 0) {
			rabbitSender.send(orderList.get(0).getId());
		}

		logger.info("End quartz job \n");
	}

}
