package com.voyagerss.persist.dto;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class StoreDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private String name;

    private String address;

    private String region;

    private Long branchId;

    private String createdBy;

    private LocalDateTime createdAt;

    private Boolean active;

    private String lastModifiedBy;

    private LocalDateTime lastModifiedAt;

}
