package com.voyagerss.persist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task")
@Getter
@Setter
public class Task extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "description")
    private String description;

    @Column(name = "worker_count", nullable = false)
    private Integer workerCount;

    @Column(name = "start_date_time", nullable = false)
    private String startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private String endDateTime;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @OneToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskEmployee> taskEmployees = new ArrayList<>();
}
