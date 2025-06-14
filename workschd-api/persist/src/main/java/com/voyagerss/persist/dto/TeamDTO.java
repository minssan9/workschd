package com.voyagerss.persist.dto;

import com.voyagerss.persist.entity.Team;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Data
public class TeamDTO extends BaseDTO {
    Long id;
    String name;
    String region;
    String scheduleType;
    String invitationHash;
    LocalDateTime invitationCreatedAt;
    String location;
    List<TeamMemberDTO> teamMembers;
    Pageable pageable;

    public TeamDTO(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.region = team.getRegion();
        this.scheduleType = team.getScheduleType();
        this.invitationHash = team.getInvitationHash();
        this.invitationCreatedAt = team.getInvitationCreatedAt();
        this.location = team.getLocation();
        this.setCreatedAt(team.getCreatedAt());
        this.setLastModifiedAt(team.getLastModifiedAt());
    }
}
