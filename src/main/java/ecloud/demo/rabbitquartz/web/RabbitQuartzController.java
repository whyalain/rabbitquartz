package ecloud.demo.rabbitquartz.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecloud.demo.rabbitquartz.business.entity.Account;
import ecloud.demo.rabbitquartz.business.entity.Order;
import ecloud.demo.rabbitquartz.business.service.RabbitQuartzService;

@RestController
public class RabbitQuartzController {

	@Autowired
	private RabbitQuartzService rabbitQuartzService;

	@RequestMapping(value = "/listaccount")
	public List<Account> getAccountList() {
		return rabbitQuartzService.listAccout();
	}

	@RequestMapping(value = "/createaccount/{name}/{balance}")
	public Account createAccount(@PathVariable String name, @PathVariable Double balance) {
		return rabbitQuartzService.createAccount(name, balance);
	}

	@RequestMapping(value = "/deleteaccount/{id}")
	public void deleteAccount(@PathVariable Long id) {
		rabbitQuartzService.deleteAccount(id);
	}

	@RequestMapping(value = "/createorder/{accountId}/{price}")
	public Order createOrder(@PathVariable Long accountId, @PathVariable Double price) {
		return rabbitQuartzService.createOrder(accountId, price);
	}

	@RequestMapping(value = "/listorder")
	public List<Order> getOrderList() {
		return rabbitQuartzService.listOrder();
	}

	@RequestMapping(value = "/listorder/toprocess")
	public List<Order> getAllOrdersToBeProcessed() {
		return rabbitQuartzService.getAllOrdersToBeProcessed();
	}

	@RequestMapping(value = "/payorder/{orderId}")
	public Order payOrder(@PathVariable Long orderId) {
		return rabbitQuartzService.payOrder(orderId);
	}
}
