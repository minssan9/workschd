package com.voyagerss.persist.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "team")
public class Team extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "region", nullable = false)
    private String region;

    private String scheduleType;  // Monthly, Weekly, Daily

    private String invitationHash;

    private LocalDateTime invitationCreatedAt;

    @OneToMany(mappedBy = "team")
    private List<TeamMember> teamMembers;


}