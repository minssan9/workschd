package com.voyagerss.persist.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Getter
@Setter
@Entity
@Table(name = "team_member")
public class TeamMember extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "is_active")
    private Boolean active;

    @Column
    private String status;
    @Column(name = "join_date")
    private LocalDateTime joinDate;
    
    @Column(name = "priority")
    private Integer priority = 0; // Default priority is 0

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

}
