package com.voyagerss.persist.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    private LocalDateTime invitationExpireAt;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TeamMember> teamMembers;
    
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<Shop> shops;

    private String location;

}
