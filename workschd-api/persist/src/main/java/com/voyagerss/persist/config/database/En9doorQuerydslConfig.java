package com.voyagerss.persist.config.database;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class En9doorQuerydslConfig {

    @PersistenceContext(unitName = "en9doorEntityManager")
    private EntityManager en9doorEntityManager;


    @Bean
    public JPAQueryFactory en9doorJpaQueryFactory() {
        return new JPAQueryFactory(en9doorEntityManager);
    }

}
