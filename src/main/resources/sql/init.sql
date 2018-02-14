DROP TABLE IF EXISTS t_order;
DROP TABLE IF EXISTS t_order_process_detail;
DROP TABLE IF EXISTS t_account;

CREATE TABLE t_order(
	id int(20) NOT NULL AUTO_INCREMENT,
	account_id int(20),
	order_process_detail_id int(20),
	order_no varchar(40) NOT NULL,
	order_price decimal(10,2),
	pay_status varchar(20),
	process_status varchar(20),
	primary KEY (id)
)ENGINE=InnoDB;

CREATE TABLE t_order_process_detail(
	id int(20) NOT NULL AUTO_INCREMENT,
	detail varchar(500),
	primary KEY (id)
)ENGINE=InnoDB;

CREATE TABLE t_account(
	id int(20) NOT NULL AUTO_INCREMENT,
	name varchar(200),
	balance decimal(10,2),
	primary KEY (id)
)ENGINE=InnoDB;