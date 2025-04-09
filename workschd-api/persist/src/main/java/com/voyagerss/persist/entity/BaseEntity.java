package com.voyagerss.persist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    
    @Column(name = "is_active")
    private Boolean active = true;

    @CreatedBy
    @Column(name = "created_by", columnDefinition = "varchar(255) default 'SYSTEM'")
    private String createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @UpdateTimestamp
    @Column(name = "last_modified_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime lastModifiedAt;

    @PrePersist
    public void prePersist() {
        if (active == null) {
            active = true;
        }
        if (createdBy == null) {
            createdBy = "SYSTEM";
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (lastModifiedBy == null) {
            lastModifiedBy = createdBy;
        }
        if (lastModifiedAt == null) {
            lastModifiedAt = createdAt;
        }
    }
}
