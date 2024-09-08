package com.voyagerss.persist.component.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
@Setter
@Getter
public class StaticProperties {
    public static DateTimeFormatter DATE_STRING_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static DateTimeFormatter DATE_DASHED_STRING_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter TIME_STRING_FORMAT = DateTimeFormatter.ofPattern("HHmm");

    public static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    public static DateTimeFormatter DATETIME_FULL_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd a hh:mm:ss");

    public static long BASE_ORGANIZATION_ID= 87L;

}
