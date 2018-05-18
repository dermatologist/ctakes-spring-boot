package com.canehealth.spring.ctakes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class CtakesApplication {
    static {
        System.setProperty("ctakes_umlsuser", "myusername");
        System.setProperty("ctakes_umlspw", "mypassword");
    }
	public static void main(String[] args) {

        SpringApplication.run(CtakesApplication.class, args);
	}
}
