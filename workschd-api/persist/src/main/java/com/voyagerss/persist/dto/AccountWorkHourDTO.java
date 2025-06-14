package com.voyagerss.persist.dto;

import com.voyagerss.persist.entity.AccountWorkHour;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class AccountWorkHourDTO extends BaseDTO {
    private Long id;
    private String date;  // ISO format date string
    private String day;   // Day of week
    private String startTime; // ISO format time string
    private String endTime;   // ISO format time string
    private boolean preferred;

    public AccountWorkHourDTO(AccountWorkHour entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.date = entity.getDate() != null ? entity.getDate().toString() : null;
            this.day = entity.getDay();
            this.startTime = entity.getStartTime() != null ? entity.getStartTime().toString() : null;
            this.endTime = entity.getEndTime() != null ? entity.getEndTime().toString() : null;
            this.preferred = entity.isPreferred();
        }
    }

    public LocalDate getDateAsLocalDate() {
        return date != null ? LocalDate.parse(date) : null;
    }

    public LocalTime getStartTimeAsLocalTime() {
        return startTime != null ? LocalTime.parse(startTime) : null;
    }

    public LocalTime getEndTimeAsLocalTime() {
        return endTime != null ? LocalTime.parse(endTime) : null;
    }

    // Additional getter methods needed for entity updates
    public String getDay() {
        return day;
    }

    public boolean isPreferred() {
        return preferred;
    }
}