package com.voyagerss.persist.dto;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AttendanceDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private Long branchId;

    private Long taskId;

    private LocalDateTime actualStartTime;

    private LocalDateTime actualEndTime;

    private BigDecimal calculatedDailyWage;

    private String createdBy;

    private LocalDateTime createdAt;

    private Boolean active;

    private String lastModifiedBy;

    private LocalDateTime lastModifiedAt;

}
