package ecloud.demo.rabbitquartz.business.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import ecloud.demo.rabbitquartz.business.enums.PayStatusEnum;
import ecloud.demo.rabbitquartz.business.enums.ProcessStatusEnum;

@Entity
@Table(name = "t_order")
public class Order {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "order_no")
	private String orderNo;

	@Column(name = "order_price")
	private Double orderPrice;

	@Column(name = "pay_status")
	@Enumerated(value = EnumType.STRING)
	private PayStatusEnum payStatus;

	@Column(name = "process_status")
	@Enumerated(value = EnumType.STRING)
	private ProcessStatusEnum processStatus;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_process_detail_id")
	private OrderProcessDetail orderProcessDetail;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;

	public Order(Double orderPrice, Account account) {
		this.orderNo = "OR_" + System.currentTimeMillis();
		this.orderPrice = orderPrice;
		this.payStatus = PayStatusEnum.TO_PAY;
		this.processStatus = ProcessStatusEnum.TO_PROCESS;
		this.account = account;
	}

	public Order() {

	}

	public Long getId() {
		return id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Double getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Double orderPrice) {
		this.orderPrice = orderPrice;
	}

	public PayStatusEnum getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(PayStatusEnum payStatus) {
		this.payStatus = payStatus;
	}

	public ProcessStatusEnum getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(ProcessStatusEnum processStatus) {
		this.processStatus = processStatus;
	}

	public OrderProcessDetail getOrderProcessDetail() {
		return orderProcessDetail;
	}

	public void setOrderProcessDetail(OrderProcessDetail orderProcessDetail) {
		this.orderProcessDetail = orderProcessDetail;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}
