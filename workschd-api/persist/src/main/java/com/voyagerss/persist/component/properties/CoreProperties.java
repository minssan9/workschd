package com.voyagerss.persist.component.properties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "core")
public class CoreProperties {
    private String webAddress;



}
