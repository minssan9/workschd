package com.voyagerss.persist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "team_member")
public class TeamMember extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private TeamRole role;

    @Column(name = "invitation_status")
    @Enumerated(EnumType.STRING)
    private InvitationStatus invitationStatus;

    @Column(name = "invitation_token")
    private String invitationToken;

    @Column(name = "invitation_expires_at")
    private LocalDateTime invitationExpiresAt;

    public enum TeamRole {
        OWNER,      // 팀 소유자
        MANAGER,    // 관리자
        MEMBER      // 일반 구성원
    }

    public enum InvitationStatus {
        PENDING,    // 초대 대기중
        ACCEPTED,   // 초대 수락됨
        REJECTED    // 초대 거절됨
    }
}
