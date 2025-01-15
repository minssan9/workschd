package com.voyagerss.persist.dto;

import com.voyagerss.persist.entity.TeamMember.TeamRole;
import com.voyagerss.persist.entity.TeamMember.InvitationStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TeamMemberDTO {
    private Long id;
    private Long teamId;
    private Long accountId;
    private TeamRole role;
    private InvitationStatus invitationStatus;
    private String invitationToken;
    private LocalDateTime invitationExpiresAt;
    
    // 추가 표시용 필드
    private String accountName;
    private String accountEmail;
} 