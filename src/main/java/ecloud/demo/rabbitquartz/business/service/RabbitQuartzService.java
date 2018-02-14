package ecloud.demo.rabbitquartz.business.service;

import java.util.List;

import ecloud.demo.rabbitquartz.business.entity.Account;
import ecloud.demo.rabbitquartz.business.entity.Order;
import ecloud.demo.rabbitquartz.business.enums.ProcessStatusEnum;

public interface RabbitQuartzService {

	public List<Account> listAccout();

	public Account createAccount(String name, Double balance);

	public void deleteAccount(Long id);

	public Order createOrder(Long accountId, Double price);

	public List<Order> listOrder();

	public Order payOrder(Long orderId);

	public List<Order> getAllOrdersToBeProcessed();

	public void processOrder(Long orderId);

	public void changeOrderProcessStatus(Long orderId, ProcessStatusEnum newProcessStatus);
}
