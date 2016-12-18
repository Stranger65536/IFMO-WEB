package com.bitbucket.teamleave.leafcalendar.api.dao;

import com.bitbucket.teamleave.leafcalendar.api.config.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "ProhibitedExceptionThrown"})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class UserDaoTest {

    @Autowired
    @Qualifier("testEnvironment")
    private Environment env;

    @Before
    public void setUp() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.test.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.test.password"));
        final UserDao userDao = new UserDaoImpl();
        userDao.setDataSource(dataSource);
    }

    @Test(expected = Exception.class)
    public void createUserTest() throws Exception {
        throw new Exception("");
    }

    @Configuration
    @PropertySource(Application.PROPERTIES_PATH)
    static class ContextConfiguration {

        @Autowired
        @Qualifier("environment")
        Environment env;

        @Bean
        public Environment testEnvironment() {
            return env;
        }

    }

}
