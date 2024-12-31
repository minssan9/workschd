package com.voyagerss.persist.config.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
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
public class En9doorDatabaseConfig extends HikariConfig {

    private final Environment env;
    private final VoyagerDatabaseProperties voyagerDatabaseProperties;
//    private final MybatisProperties mybatisProperties;
    private final JpaProperties jpaProperties;

    @Bean
    public Properties en9doorDBProperties() {
        Properties en9doorProperties = new Properties();
        en9doorProperties.put("jdbcUrl", this.voyagerDatabaseProperties.getUrl());
        en9doorProperties.put("username", this.voyagerDatabaseProperties.getUsername());
        en9doorProperties.put("password", this.voyagerDatabaseProperties.getPassword());
        en9doorProperties.put("driverClassName", this.voyagerDatabaseProperties.getDriverClassName());
        return en9doorProperties;
    }

    @Bean
    @BatchDataSource
    public DataSource en9doorDataSource() {
        HikariConfig config = new HikariConfig(en9doorDBProperties());
        return new LazyConnectionDataSourceProxy(new HikariDataSource(config));
    }


    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
        EntityManagerFactoryBuilder builder) {

        return builder.dataSource(en9doorDataSource())
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


    //    mybatis config ----------------------------------------------------
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(
        ApplicationContext applicationContext
    ) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(en9doorDataSource());
        sqlSessionFactoryBean.setTypeAliasesPackage("com.voyagerss.persist.entity");
        sqlSessionFactoryBean.setMapperLocations(
                applicationContext.getResources("classpath*:/mapper/**/**.xml")
        );
        sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "en9doorSessionTemplate")
    public SqlSessionTemplate en9doorSqlSessionTemplate(
        @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(
        @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean factory) {
        return new JPAQueryFactory(Objects.requireNonNull(factory.getObject()).createEntityManager());
    }
}


