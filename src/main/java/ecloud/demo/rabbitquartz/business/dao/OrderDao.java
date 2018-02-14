package ecloud.demo.rabbitquartz.business.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ecloud.demo.rabbitquartz.business.entity.Order;

public interface OrderDao extends JpaRepository<Order, Long> {

}
