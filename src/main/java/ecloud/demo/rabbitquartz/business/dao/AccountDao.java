package ecloud.demo.rabbitquartz.business.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ecloud.demo.rabbitquartz.business.entity.Account;

@Repository
public interface AccountDao extends JpaRepository<Account, Long> {

	public List<Account> findByName(String name);

	public void deleteById(Long id);
}
