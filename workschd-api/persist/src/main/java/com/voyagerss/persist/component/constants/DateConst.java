package com.voyagerss.persist.component.constants;


import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public abstract class DateConst {

    //Timezone ID
    public static final String TIME_ZONE_ID_ASIS_SEOUL;

    //TimeZone
    public static final TimeZone TIME_ZONE_ASIS_SEOUL;

    //ZoneId
    public static final ZoneId TIME_ZONE_ID_OBJECT_ASIS_SEOUL;

    //Default DateFormat (Annotation에서 사용하는 상수로 선언부에서 값을 바로 대입한다.)
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    public static final String DEFAULT_DATE_TIME_MILLISECOND_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DEFAULT_TIMEMILLISECOND_FORMAT = "HHmmssSSS";
    public static final String DEFAULT_DATE_TIMESTAMP_FORMAT = "yyyyMMddHHmmssSSS";
    public static final String DEFAULT_YYYYMM_FORMAT = "yyyy-MM";
    public static final String DEFAULT_TEMP_DATE = "9999-12-31 00:00:00";

    public static final String MIN_TIME = "00:00:00";
    public static final String MAX_TIME = "23:59:59";

    public static final String MIN_TIME_START_WITH_WHITESPACE = " ".concat(MIN_TIME);
    public static final String MAX_TIME_START_WITH_WHITESPACE = " ".concat(MAX_TIME);

    //Default DateTimeFormatter
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER;
    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER;
    public static final DateTimeFormatter DEFAULT_TIME_FORMATTER;
    public static final DateTimeFormatter DEFAULT_DATE_TIME_MILLISECOND_FORMATTER;
    public static final DateTimeFormatter DEFAULT_DATE_TIMESTAMP_FORMATTER;

    static {
        TIME_ZONE_ID_ASIS_SEOUL = "Asia/Seoul";
        TIME_ZONE_ASIS_SEOUL = TimeZone.getTimeZone(TIME_ZONE_ID_ASIS_SEOUL);
        TIME_ZONE_ID_OBJECT_ASIS_SEOUL = ZoneId.of(TIME_ZONE_ID_ASIS_SEOUL);

        //Java8 LocalDate(Time) DateTimeFormatter
        DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
        DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT);
        DEFAULT_DATE_TIME_MILLISECOND_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_MILLISECOND_FORMAT);
        DEFAULT_DATE_TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIMESTAMP_FORMAT);
    }
}
