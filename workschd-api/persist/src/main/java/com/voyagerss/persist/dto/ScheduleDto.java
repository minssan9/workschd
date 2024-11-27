package com.voyagerss.persist.dto;

import com.voyagerss.persist.entity.Schedule;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ScheduleDto {
    private String employeeName;
    private LocalDate date;
    private String shift;

    // Getters, Setters, Constructors

    public ScheduleDto(Schedule schedule) {
        this.employeeName = schedule.getEmployeeName();
        this.date = schedule.getDate();
        this.shift = schedule.getShift();
    }
}