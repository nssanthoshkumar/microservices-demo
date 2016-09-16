package io.pivotal.microservices.accounts;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * The accounts Spring configuration.
 * 
 * @author Paul Chapman
 */
@Configuration
@ComponentScan
@EntityScan("io.pivotal.microservices.accounts")
@EnableJpaRepositories("io.pivotal.microservices.accounts")
@PropertySource("classpath:db-config.properties")
public class AccountsConfiguration {

	protected Logger logger;
	
	@Autowired
    private Environment env;
 

	public AccountsConfiguration() {
		logger = Logger.getLogger(getClass().getName());
	}	


	@Bean
	@Profile("Dev")
	public DataSource dataSource() {
		 logger.info("dataSource() invoked");

/*		  DataSource dataSource = (new
		  EmbeddedDatabaseBuilder()).addScript("classpath:testdb/schema.sql")
		  .addScript("classpath:testdb/data.sql").build();*/
		 

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		//dataSource.setUrl("jdbc:mysql://10.63.39.49:3307/testdb");
		
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.userName"));
		dataSource.setPassword(env.getProperty("jdbc.password"));
		
		logDataSourceInfo(dataSource);

		return dataSource;
	}
	
	@Bean
	@Profile("Test")
	public DataSource testDataSource() {
		logger.info("dataSource() invoked");

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.test.url"));
		dataSource.setUsername(env.getProperty("jdbc.test.userName"));
		dataSource.setPassword(env.getProperty("jdbc.test.password"));
		
		logDataSourceInfo(dataSource);

		return dataSource;
	}
	
	public void logDataSourceInfo(DriverManagerDataSource  dataSource){
		
		logger.info("dataSource = " + dataSource);
		logger.info("url = "+ dataSource.getUrl());
        logger.info("username = "+ dataSource.getUsername());
        logger.info("password = "+dataSource.getPassword());

		// Sanity check
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> accounts = jdbcTemplate.queryForList("SELECT number FROM t_account");
		logger.info("System has " + accounts.size() + " accounts");

		// Populate with random balances
		Random rand = new Random();

		for (Map<String, Object> item : accounts) {
			String number = (String) item.get("number");
			BigDecimal balance = new BigDecimal(rand.nextInt(10000000) / 100.0).setScale(2, BigDecimal.ROUND_HALF_UP);
			jdbcTemplate.update("UPDATE t_account SET balance = ? WHERE number = ?", balance, number);
		}
	}
}
