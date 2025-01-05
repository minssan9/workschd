package com.voyagerss.persist.config.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(
    entityManagerFactoryRef = "entityManagerFactory",
    transactionManagerRef = "transactionManager",
    basePackages = {"com.voyagerss.persist.repository"}
)
@MapperScan(basePackages = "com.voyagerss.persist",
        annotationClass = Mapper.class,
        sqlSessionFactoryRef = "sqlSessionFactory")
public class VoyagerDatabaseConfig extends HikariConfig {

    private final Environment env;
    private final VoyagerDatabaseProperties voyagerDatabaseProperties;
//    private final MybatisProperties mybatisProperties;
    private final JpaProperties jpaProperties;

    @Bean
    public Properties voyagerDBProperties() {
        Properties en9doorProperties = new Properties();
        en9doorProperties.put("jdbcUrl", this.voyagerDatabaseProperties.getUrl());
        en9doorProperties.put("username", this.voyagerDatabaseProperties.getUsername());
        en9doorProperties.put("password", this.voyagerDatabaseProperties.getPassword());
        en9doorProperties.put("driverClassName", this.voyagerDatabaseProperties.getDriverClassName());
        return en9doorProperties;
    }

    @Bean
    @BatchDataSource
    public DataSource voyagerDataSource() {
        HikariConfig config = new HikariConfig(voyagerDBProperties());
        return new LazyConnectionDataSourceProxy(new HikariDataSource(config));
    }


    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
        EntityManagerFactoryBuilder builder) {

        return builder.dataSource(voyagerDataSource())
            .properties(jpaProperties.getProperties())
            .packages(new String[]{"com.voyagerss.persist.entity"})
            .persistenceUnit("en9doorEntityManager")
            .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(
        EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(
            Objects.requireNonNull(entityManagerFactory(builder).getObject()));
    }


    @Bean
    public JPAQueryFactory jpaQueryFactory(
        @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean factory) {
        return new JPAQueryFactory(Objects.requireNonNull(factory.getObject()).createEntityManager());
    }
}


