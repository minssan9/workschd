package com.voyagerss.persist.dto;


import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ShopDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private String name;

    private String address;

    private String region;

    private Long branchId;
    
    // Team relationship fields
    private Long teamId;
    private String teamName;

    private String createdBy;

    private LocalDateTime createdAt;

    private Boolean active;

    private String lastModifiedBy;

    private LocalDateTime lastModifiedAt;

}
