package com.voyagerss.persist.config.database;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("db-en9door")
@Data
@Component
public class En9doorDatabaseProperties {

    String url;
    String username;
    String password;
    String driverClassName;
}
