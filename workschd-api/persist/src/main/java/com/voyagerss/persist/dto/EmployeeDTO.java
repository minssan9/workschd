package com.voyagerss.persist.dto;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class EmployeeDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private Integer accountId;

    private Long branchId;

    private String phoneNumber;

    private Boolean manager;

    private String createdBy;

    private LocalDateTime createdAt;

    private Boolean active;

    private String lastModifiedBy;

    private LocalDateTime lastModifiedAt;

}
