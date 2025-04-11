package com.voyagerss.persist.dto;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class TaskDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Task fields
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
    
    // Task join request fields
    private Long requestId;
    private Long taskId;
    private Integer accountId;
    private String accountName;
    private String accountEmail;
    private String status;  // "PENDING", "APPROVED", "REJECTED"
    private LocalDateTime requestDate;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    private String rejectionReason;
    
    // Fields for pagination
    private Pageable pageable;
    
    // List of associated join requests (when retrieving a task)
    private List<TaskDTO> joinRequests;
}
