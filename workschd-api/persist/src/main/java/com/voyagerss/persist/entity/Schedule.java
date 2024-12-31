package com.voyagerss.persist.entity;

import com.voyagerss.persist.dto.ScheduleDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@RequiredArgsConstructor
@Getter
@Setter
@Entity
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeName;
    private LocalDate date;
    private String shift;

    // Getters, Setters, Constructors

    public Schedule(ScheduleDto scheduleDto) {
        this.employeeName = scheduleDto.getEmployeeName();
        this.date = scheduleDto.getDate();
        this.shift = scheduleDto.getShift();
    }

    public void updateFromDto(ScheduleDto scheduleDto) {
        this.employeeName = scheduleDto.getEmployeeName();
        this.date = scheduleDto.getDate();
        this.shift = scheduleDto.getShift();
    }
}