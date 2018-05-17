package com.canehealth.spring.ctakes.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by beapen on 2017-06-30.
 * stackoverflow.com/questions/43455869
 * With some alterations
 */
@Configuration
@EnableConfigurationProperties
public class AppConfig {


}