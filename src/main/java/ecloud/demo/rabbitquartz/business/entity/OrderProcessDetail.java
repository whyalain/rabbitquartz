package ecloud.demo.rabbitquartz.business.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "t_order_process_detail")
public class OrderProcessDetail {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "detail")
	private String detail;

	@JsonBackReference
	@OneToOne(mappedBy = "orderProcessDetail")
	private Order order;

	public OrderProcessDetail() {
		super();
	}

	public OrderProcessDetail(String detail, Order order) {
		super();
		this.detail = detail;
		this.order = order;
	}

	public Long getId() {
		return id;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}
