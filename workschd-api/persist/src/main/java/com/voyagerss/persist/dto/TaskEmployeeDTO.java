package com.voyagerss.persist.dto;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TaskEmployeeDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long taskId;

    private Long employeeId;

    private String createdBy;

    private LocalDateTime createdAt;

    private Boolean active;

    private String lastModifiedBy;

    private LocalDateTime lastModifiedAt;

}
