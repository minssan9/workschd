package com.voyagerss.persist.dto;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AccountWorkOffDatesDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private Long accountId;

    private LocalDate date;

    private String createdBy;

    private LocalDateTime createdAt;

    private Boolean active;

    private String lastModifiedBy;

    private LocalDateTime lastModifiedAt;

}
