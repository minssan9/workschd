package com.voyagerss.persist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "task_employee")
public class TaskEmployee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Id
    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_active")
    private Boolean active;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

}
