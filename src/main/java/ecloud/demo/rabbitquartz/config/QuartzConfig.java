package ecloud.demo.rabbitquartz.config;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executor;

import javax.sql.DataSource;

import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import ecloud.demo.rabbitquartz.infra.quartz.AutowiringJobFactory;
import ecloud.demo.rabbitquartz.infra.quartz.QuartzJob;

@Configuration
public class QuartzConfig {

	// TODO 多台服务器的时间同步问题

	@Autowired
	private DataSource dataSource;

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();

		factory.setSchedulerName("Cluster_Scheduler");
		factory.setDataSource(dataSource);
		factory.setApplicationContextSchedulerContextKey("applicationContext");
		factory.setTaskExecutor(schedulerThreadPool());
		factory.setTriggers(quartzTrigger().getObject());
		factory.setQuartzProperties(quartzProperties());
		factory.setJobFactory(autowiringJobFactory());
		factory.setOverwriteExistingJobs(true);
		factory.setStartupDelay(15);
		return factory;
	}

	@Bean
	public JobFactory autowiringJobFactory() {
		return new AutowiringJobFactory();
	}

	@Bean
	public Scheduler scheduler() throws Exception {
		Scheduler scheduler = schedulerFactoryBean().getScheduler();
		scheduler.start();
		return scheduler;
	}

	@Bean
	public Properties quartzProperties() throws IOException {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));

		// 在quartz.properties中的属性被读取并注入后再初始化对象
		propertiesFactoryBean.afterPropertiesSet();
		return propertiesFactoryBean.getObject();
	}

	@Bean
	public JobDetailFactoryBean quartzJob() {
		JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();

		jobDetailFactoryBean.setJobClass(QuartzJob.class);
		jobDetailFactoryBean.setDurability(true);
		jobDetailFactoryBean.setGroup("order");
		jobDetailFactoryBean.setName("order_job");
		jobDetailFactoryBean.setRequestsRecovery(true);

		return jobDetailFactoryBean;
	}

	@Bean
	public CronTriggerFactoryBean quartzTrigger() {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();

		cronTriggerFactoryBean.setJobDetail(quartzJob().getObject());
		cronTriggerFactoryBean.setCronExpression("0/15 * * * * ?");

		return cronTriggerFactoryBean;
	}

	@Bean
	public Executor schedulerThreadPool() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

		executor.setCorePoolSize(15);
		executor.setMaxPoolSize(25);
		executor.setQueueCapacity(100);

		return executor;
	}
}
