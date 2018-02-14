package ecloud.demo.rabbitquartz.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "ecloud.demo.rabbitquartz.business")
@EnableJpaRepositories(basePackages = "ecloud.demo.rabbitquartz.business")
public class HibernateConfig {

}
