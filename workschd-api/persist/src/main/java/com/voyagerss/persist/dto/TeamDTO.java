package com.voyagerss.persist.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Data
public class TeamDTO {
    Long id;
    String name;
    String region;
    String scheduleType;
    String invitationHash;
    LocalDateTime invitationCreatedAt;
    String location;
    List<TeamMemberDTO> teamMembers;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Pageable pageable;

}
