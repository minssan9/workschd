package com.voyagerss.persist.dto;

import com.voyagerss.persist.EnumMaster;
import com.voyagerss.persist.entity.TaskEmployee;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
public class TaskDTO extends BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Task fields
    private Long id;
    private String title;
    private String description;
    private Integer workerCount;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private EnumMaster.TaskStatus status;
    private Long teamId;
    private Long shopId;
    
    // Added fields for names
    private String shopName;
    private String teamName;

    List<TaskEmployeeDTO> taskEmployees;

    
    // Fields for pagination
    private Pageable pageable;
    
    // Helper methods for display
    public String getStatusDisplay() {
        return status != null ? status.getDisplayName() : "";
    }
    
    public String getStatusColor() {
        return status != null ? status.getColor() : "grey";
    }
}
