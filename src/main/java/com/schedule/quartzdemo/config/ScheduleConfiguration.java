package com.schedule.quartzdemo.config;

import com.schedule.quartzdemo.ContextAwareSpringJobFactory;
import com.schedule.quartzdemo.QuartzDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.transaction.PlatformTransactionManager;


import javax.sql.DataSource;

import java.util.Properties;


@Configuration
public class ScheduleConfiguration {

    @Autowired
    private QuartzDataSource quartzDataSource;
    //
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(quartzDataSource.getDriver());
        dataSourceBuilder.url(quartzDataSource.getUrl());
        dataSourceBuilder.username(quartzDataSource.getUser());
        dataSourceBuilder.password(quartzDataSource.getPassword());
        return dataSourceBuilder.build();
    }


    @Bean
    public SpringBeanJobFactory jdbcFactory() {
        ContextAwareSpringJobFactory jobFactory = new ContextAwareSpringJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }


    @Bean("quartzSchedulerFactory")
    public SchedulerFactoryBean scheduler(PlatformTransactionManager transactionManager, QuartzProperties quartzProperties) {
        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());
        DataSource dataSource = dataSource();

        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setSchedulerName("scheduler");
        schedulerFactory.setDataSource(dataSource);
        schedulerFactory.setTransactionManager(transactionManager);
        schedulerFactory.setJobFactory(jdbcFactory());
        schedulerFactory.setQuartzProperties(properties);

        return schedulerFactory;

    }
}
