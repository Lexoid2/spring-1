package com.javarush.kostenko.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Properties;

/**
 * Main configuration class for the Spring application.
 * This class configures the Spring MVC, JPA, and Thymeleaf components.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.javarush.kostenko")
@EnableJpaRepositories(basePackages = "com.javarush.kostenko.dao")
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class AppConfig {

    @Value("${spring.datasource.driver-class-name}")
    String driverClassName;

    @Value("${spring.datasource.url}")
    String url;

    @Value("${spring.datasource.username}")
    String username;

    @Value("${spring.datasource.password}")
    String password;

    /**
     * Configures the DataSource bean for database access.
     *
     * @return a configured DataSource bean
     */
    @Bean
    public DataSource dataSource() {
        log.info("Initializing DataSource with configuration properties.");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("useUnicode", "true");
        connectionProperties.setProperty("characterEncoding", "UTF-8");
        dataSource.setConnectionProperties(connectionProperties);

        return dataSource;
    }

    /**
     * Configures the EntityManagerFactory bean for JPA.
     *
     * @return a configured LocalContainerEntityManagerFactoryBean
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        log.info("Configuring EntityManagerFactory.");

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("com.javarush.kostenko.domain.entity", "com.javarush.kostenko.domain.enums");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        jpaProperties.setProperty("hibernate.connection.useUnicode", "true");
        jpaProperties.setProperty("hibernate.connection.characterEncoding", "UTF-8");
        jpaProperties.setProperty("hibernate.show_sql", "true");
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");

        factoryBean.setJpaProperties(jpaProperties);

        return factoryBean;
    }

    /**
     * Configures the PlatformTransactionManager bean for transaction management.
     *
     * @param entityManagerFactory the EntityManagerFactory to be used for transactions
     * @return a configured JpaTransactionManager
     */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        log.info("Initializing JpaTransactionManager.");
        return new JpaTransactionManager(entityManagerFactory);
    }

    /**
     * Configures the Thymeleaf template resolver for view resolution.
     *
     * @return a configured ClassLoaderTemplateResolver for Thymeleaf
     */
    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        log.info("Setting up Thymeleaf template resolver.");

        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");

        log.info("Thymeleaf template resolver set with prefix: templates/ and suffix: .html");
        return resolver;
    }

    /**
     * Configures the Thymeleaf template engine.
     *
     * @return a configured SpringTemplateEngine
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
        log.info("Configuring Thymeleaf template engine.");

        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());

        log.info("Thymeleaf template engine configured.");
        return engine;
    }

    /**
     * Configures the Thymeleaf view resolver.
     *
     * @return a configured ThymeleafViewResolver with UTF-8 encoding
     */
    @Bean
    public ThymeleafViewResolver viewResolver() {
        log.info("Setting up Thymeleaf view resolver.");

        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        resolver.setContentType("text/html; charset=UTF-8");

        log.info("Thymeleaf view resolver set with UTF-8 encoding.");
        return resolver;
    }
}
