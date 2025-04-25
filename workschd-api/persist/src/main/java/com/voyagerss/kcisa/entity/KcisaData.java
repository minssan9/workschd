package com.voyagerss.kcisa.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(
    name = "kcisa_data",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title", "period"})
    }
)
public class KcisaData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "performance_type")
    private String performanceType;

    @Column(name = "event_date")
    private String eventDate;

    @Column(name = "venue")
    private String venue;
    
    @Column(name = "period")
    private String period;
    
    @Column(name = "event_period")
    private Integer eventPeriod;
    
    @Column(name = "charge")
    private String charge;
    
    @Column(name = "contact_point")
    private String contactPoint;
    
    @Column(name = "url")
    private String url;
    
    @Column(name = "image_object")
    private String imageObject;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "api_id", unique = true)
    private String apiId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
