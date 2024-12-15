package com.voyagerss.persist.component.util;

import com.voyagerss.persist.component.properties.CoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

import static com.voyagerss.persist.component.properties.StaticProperties.*;

@Slf4j
@Component
public class DateUtil {

    private static ApplicationContext applicationContext;
    private static CoreProperties coreProperties;

    public static LocalDateTime toDateFrom8digitString(String dateString){
        dateString = dateString.substring(0,4) + "-" + dateString.substring(4,6)+ "-" + dateString.substring(6,8);

        return LocalDateTime.of(
            LocalDate.parse(dateString, DATE_FORMAT),
            LocalDateTime.now().withHour(17).withMinute(0).withSecond(0).withNano(0).toLocalTime()) ;
    }

    public static LocalDateTime toDateFromFormatString(String dateString){
        return LocalDateTime.of(
                LocalDate.parse(dateString, DATE_FORMAT),
                LocalDateTime.now().withHour(17).withMinute(0).withSecond(0).withNano(0).toLocalTime()) ;
    }

    public static String  toStringFromDate( LocalDateTime date){
        return date.format(DATE_STRING_FORMAT);
    }

    public static String  toDashedStringFromDate( LocalDateTime date){
        return date.format(DATE_DASHED_STRING_FORMAT);
    }

    public static LocalDate getPreviousWorkingDay(LocalDate date) {
        DayOfWeek dayOfWeek = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));
        switch (dayOfWeek) {
            case MONDAY:
                return date.minus(3, ChronoUnit.DAYS);
            case SUNDAY:
                return date.minus(2, ChronoUnit.DAYS);
            default:
                return date.minus(1, ChronoUnit.DAYS);
        }
    }

    public static LocalDateTime getNowbyString() {
    	return LocalDateTime.parse(LocalDateTime.now().format(DATETIME_FULL_FORMAT), DATETIME_FULL_FORMAT);
    }

    public static LocalDateTime getLocalDateTime(String dateTimeString, String dateTimeFormatString) {
//        Assert.hasText(dateTimeString, MessageConst.NOT_FOUND.getFormatMessage("일시가"));
//        Assert.hasText(dateTimeFormatString, MessageConst.NOT_FOUND.getFormatMessage("일시 유형이"));
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(dateTimeFormatString));
    }

    public static LocalDateTime getLocalDateTime(String dateTimeString, DateTimeFormatter dateTimeFormat) {
//        Assert.hasText(dateTimeString, MessageConst.NOT_FOUND.getFormatMessage("일시가"));
//        Assert.notNull(dateTimeFormat, MessageConst.NOT_FOUND.getFormatMessage("일시 유형이"));
        return LocalDateTime.parse(dateTimeString, dateTimeFormat);
    }


    public static boolean isGreaterThan(String stndDateTime, String compDateTime, String dateTimeFormat) {
//        Assert.isTrue(isValid(stndDateTime, dateTimeFormat), MessageConst.NOT_FOUND.getFormatMessage("기준일시가 잘못되었거나"));
//        Assert.isTrue(isValid(compDateTime, dateTimeFormat), MessageConst.NOT_FOUND.getFormatMessage("비교일시가 잘못되었거나"));

        String standardDate = stndDateTime;
        String compareDate = compDateTime;
        String format = dateTimeFormat;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        int result = LocalDateTime.parse(standardDate, dtf).compareTo(LocalDateTime.parse(compareDate, dtf));

        if(log.isTraceEnabled()) {
            if(result < 0) {
                log.trace("기준일이 작습니다.");
            }
            else if(result > 0) {
                log.trace("기준일이 큽니다.");
            }
            else {
                log.trace("두 일시가 동일합니다.");
            }
        }

        return result > 0;
    }

    public static int compare(String stndDateTime, String compDateTime, String dateTimeFormat) {
        Assert.isTrue(isValid(stndDateTime, dateTimeFormat), "기준일시가 잘못되었거나");
        Assert.isTrue(isValid(compDateTime, dateTimeFormat), "비교일시가 잘못되었거나");

        String standardDate = stndDateTime;
        String compareDate = compDateTime;
        String format = dateTimeFormat;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(standardDate, dtf).compareTo(LocalDateTime.parse(compareDate, dtf));
    }

    public static boolean isValid(String dateString, String... dateFormat) {
        Assert.hasText(dateString, "일시가");
        Assert.notEmpty(dateFormat, "일시 유형이");

        String date = dateString;
        String[] dateFormats = dateFormat;
        DateTimeFormatter dtf = null;
        for(String format : dateFormats) {

            String confirmFormat = null;
            try {
                dtf = DateTimeFormatter.ofPattern(format);
                confirmFormat = LocalDateTime.parse(date, dtf).format(dtf);
            } catch (Exception e) {
                log.error("isValidDate Fail {}", (Object) e.getStackTrace());
            }

            if(date.equals(confirmFormat)) {
                return true;
            }
        }

        return false;
    }
}
