package com.voyagerss.persist.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BaseDTO {
    private String createdBy;

    private LocalDateTime createdAt;

    private Boolean active;

    private String lastModifiedBy;

    private LocalDateTime lastModifiedAt;
}
