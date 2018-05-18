package com.canehealth.spring.ctakes.configuration;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by beapen on 2017-06-30.
 * stackoverflow.com/questions/43455869
 * With some alterations
 */
@Configuration
@EnableConfigurationProperties
public class AppConfig {
    //    @Primary
//    @Bean(name = "prodDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "prodJdbc")
//    public JdbcTemplate prodJdbcTemplate(@Qualifier("prodDataSource") DataSource prodDataSource) {
//        return new JdbcTemplate(prodDataSource);
//    }
//
//    @Bean(name = "devDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.dev")
//    public DataSource devDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "devJdbc")
//    public JdbcTemplate devJdbcTemplate(@Qualifier("devDataSource") DataSource devDataSource) {
//        return new JdbcTemplate(devDataSource);
//    }
    @Bean
    public ActorSystem actorSystem() {
        ActorSystem actorSystem = ActorSystem.create("ctakes-actor-system", akkaConfiguration());
        return actorSystem;
    }

    @Bean
    public Config akkaConfiguration() {
        return ConfigFactory.load();
    }

}