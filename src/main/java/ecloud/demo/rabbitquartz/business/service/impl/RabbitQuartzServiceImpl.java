package ecloud.demo.rabbitquartz.business.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import ecloud.demo.rabbitquartz.business.dao.AccountDao;
import ecloud.demo.rabbitquartz.business.dao.OrderDao;
import ecloud.demo.rabbitquartz.business.entity.Account;
import ecloud.demo.rabbitquartz.business.entity.Order;
import ecloud.demo.rabbitquartz.business.entity.OrderProcessDetail;
import ecloud.demo.rabbitquartz.business.enums.PayStatusEnum;
import ecloud.demo.rabbitquartz.business.enums.ProcessStatusEnum;
import ecloud.demo.rabbitquartz.business.service.RabbitQuartzService;
import ecloud.demo.rabbitquartz.infra.rabbitmq.RabbitReceiver;

@Component
public class RabbitQuartzServiceImpl implements RabbitQuartzService {

	private final Logger logger = LoggerFactory.getLogger(RabbitReceiver.class);

	@Autowired
	private AccountDao accountDao;

	@Autowired
	private OrderDao orderDao;

	@Override
	@Transactional
	public Account createAccount(String name, Double balance) {
		Account account = new Account(name, balance);
		account = accountDao.save(account);
		return account;
	}

	@Override
	public List<Account> listAccout() {
		return accountDao.findAll();
	}

	@Override
	@Transactional
	public void deleteAccount(Long id) {
		accountDao.deleteById(id);
	}

	@Override
	@Transactional
	public Order createOrder(Long accountId, Double price) {
		Account account = accountDao.findOne(accountId);

		if (account == null) {
			return null;
		}

		Order order = new Order(price, account);
		return orderDao.save(order);
	}

	@Override
	public List<Order> listOrder() {
		return orderDao.findAll();
	}

	@Override
	@Transactional
	public Order payOrder(Long orderId) {
		Order order = orderDao.findOne(orderId);
		order.setPayStatus(PayStatusEnum.PAYED);
		return order;
	}

	@Override
	public List<Order> getAllOrdersToBeProcessed() {
		Order search = new Order();
		search.setPayStatus(PayStatusEnum.PAYED);
		search.setProcessStatus(ProcessStatusEnum.TO_PROCESS);
		Example<Order> example = Example.of(search);
		return orderDao.findAll(example);
	}

	@Override
	@Transactional
	public void changeOrderProcessStatus(Long orderId, ProcessStatusEnum newProcessStatus) {
		Order order = orderDao.findOne(orderId);
		order.setProcessStatus(newProcessStatus);
		orderDao.save(order);
	}

	@Override
	@Transactional
	public void processOrder(Long orderId) {
		logger.info("Processing order. ID: " + orderId);

		Order order = orderDao.findOne(orderId);
		Account account = accountDao.findOne(order.getAccount().getId());

		account.setBalance(account.getBalance() + order.getOrderPrice());
		accountDao.save(account);

		changeOrderProcessStatus(orderId, ProcessStatusEnum.PROCESSED);

		OrderProcessDetail opd = order.getOrderProcessDetail();
		if (opd == null) {
			opd = new OrderProcessDetail("Order processed " + System.currentTimeMillis(), order);
		}
		order.setOrderProcessDetail(opd);

		try {
			// Sleep 20s
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		orderDao.save(order);

		logger.info("Processed order. ID: " + orderId + "\n");
	}
}
