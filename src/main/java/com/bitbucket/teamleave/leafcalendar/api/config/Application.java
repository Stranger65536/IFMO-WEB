package com.bitbucket.teamleave.leafcalendar.api.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author vladislav.trofimov@emc.com
 */
@Configuration
@SpringBootApplication
@EnableAutoConfiguration
@EnableTransactionManagement
@PropertySource(Application.PROPERTIES_PATH)
@ComponentScan("com.bitbucket.teamleave.leafcalendar.api")
public class Application {
    public static final String PROPERTIES_PATH = "classpath:application.properties";

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}