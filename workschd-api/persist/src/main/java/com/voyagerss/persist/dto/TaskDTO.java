package com.voyagerss.persist.dto;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class TaskDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private Long branchId;

    private Long storeId;

    private String additionalInfo;

    private LocalDateTime taskDatetime;

    private LocalTime startTime;

    private LocalTime endTime;

    private BigDecimal dailyWage;

    private String createdBy;

    private LocalDateTime createdAt;

    private Boolean active;

    private String lastModifiedBy;

    private LocalDateTime lastModifiedAt;

}
