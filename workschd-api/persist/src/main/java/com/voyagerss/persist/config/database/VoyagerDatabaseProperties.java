package com.voyagerss.persist.config.database;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("db-voyager")
@Data
@Component
public class VoyagerDatabaseProperties {

    String url;
    String username;
    String password;
    String driverClassName;
}
