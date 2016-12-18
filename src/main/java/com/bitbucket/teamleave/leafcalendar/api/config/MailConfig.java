package com.bitbucket.teamleave.leafcalendar.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
@Configuration
@PropertySource(Application.PROPERTIES_PATH)
public class MailConfig {
    @Autowired
    @Qualifier("environment")
    Environment env;

    @Bean
    public JavaMailSenderImpl mailSender() {
        final JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(env.getProperty("spring.mail.host"));
        sender.setPort(env.getProperty("spring.mail.port", Integer.class));
        sender.setUsername(env.getProperty("spring.mail.username"));
        sender.setPassword(env.getProperty("spring.mail.password"));
        final Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", env.getProperty("spring.mail.smtp.auth"));
        properties.setProperty("mail.smtp.starttls.enable", env.getProperty("spring.mail.smtp.starttls.enable"));
        sender.setJavaMailProperties(properties);
        return sender;
    }
}