package com.voyagerss.persist.config.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(
    entityManagerFactoryRef = "en9doorEntityManagerFactory",
    transactionManagerRef = "en9doorTransactionManager",
    basePackages = {"com.persist.*.repository"}
)
@MapperScan(basePackages = "com.persist",
        annotationClass = Mapper.class,
        sqlSessionFactoryRef = "en9doorSqlSessionFactory")
public class En9doorDatabaseConfig extends HikariConfig {

    private final Environment env;
    private final En9doorDatabaseProperties en9doorDatabaseProperties;
    private final MybatisProperties mybatisProperties;
    private final JpaProperties jpaProperties;

    @Bean
    public Properties en9doorDBProperties() {
        Properties en9doorProperties = new Properties();
        en9doorProperties.put("jdbcUrl", this.en9doorDatabaseProperties.getUrl());
        en9doorProperties.put("username", this.en9doorDatabaseProperties.getUsername());
        en9doorProperties.put("password", this.en9doorDatabaseProperties.getPassword());
        en9doorProperties.put("driverClassName", this.en9doorDatabaseProperties.getDriverClassName());
        return en9doorProperties;
    }

    @Bean
    @BatchDataSource
    public DataSource en9doorDataSource() {
        HikariConfig config = new HikariConfig(en9doorDBProperties());
        return new LazyConnectionDataSourceProxy(new HikariDataSource(config));
    }


    @Bean(name = "en9doorEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean en9doorEntityManagerFactory(
        EntityManagerFactoryBuilder builder) {

        return builder.dataSource(en9doorDataSource())
            .properties(jpaProperties.getProperties())
            .packages(new String[]{"com.persist.*.domain"})
            .persistenceUnit("en9doorEntityManager")
            .build();
    }

    @Bean
    public PlatformTransactionManager en9doorTransactionManager(
        EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(
            Objects.requireNonNull(en9doorEntityManagerFactory(builder).getObject()));
    }


    //    mybatis config ----------------------------------------------------
    @Bean(name = "en9doorSqlSessionFactory")
    public SqlSessionFactory en9doorSqlSessionFactory(
        ApplicationContext applicationContext
    ) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(en9doorDataSource());
        sqlSessionFactoryBean.setTypeAliasesPackage("com.persist.*.domain");
        sqlSessionFactoryBean.setMapperLocations(
                applicationContext.getResources("classpath*:/mapper/**/**.xml")
        );
        sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "en9doorSessionTemplate")
    public SqlSessionTemplate en9doorSqlSessionTemplate(
        @Qualifier("en9doorSqlSessionFactory") SqlSessionFactory en9doorSqlSessionFactory) {
        return new SqlSessionTemplate(en9doorSqlSessionFactory);
    }
}


