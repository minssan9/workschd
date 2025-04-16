package com.voyagerss.persist.dto;

import com.voyagerss.persist.EnumMaster;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;

/**
 * DTO for {@link com.voyagerss.persist.entity.TaskEmployee}
 */
@Data
@NoArgsConstructor
public class TaskEmployeeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long taskId;
    private String taskTitle;
    private EnumMaster.TaskStatus taskStatus;
    private Integer accountId;
    private String accountName;
    private String accountEmail;
    private EnumMaster.TaskEmployeeStatus status;
    private LocalDateTime requestDate;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    private String rejectionReason;
    private LocalDateTime joinedAt;
    private LocalDateTime leftAt;

    private Pageable pageable;

    // Helper methods for display
    public String getTaskStatusDisplay() {
        return taskStatus != null ? taskStatus.getDisplayName() : "";
    }
    
    public String getTaskStatusColor() {
        return taskStatus != null ? taskStatus.getColor() : "grey";
    }
    
    public String getStatusDisplay() {
        return status != null ? status.getDisplayName() : "";
    }
    
    public String getStatusColor() {
        return status != null ? status.getColor() : "grey";
    }
}
