package com.voyagerss.persist.dto;


import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class AttendanceDTO extends BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;


    private Long branchId;


    private Long taskId;


    private BigDecimal calculatedDailyWage;



    private Long employeeId;


    private LocalDate attendanceDate;


    private String dayOfWeek;


    private LocalTime startTime;


    private LocalTime endTime;


    private LocalDateTime actualStartTime;


    private LocalDateTime actualEndTime;

}
