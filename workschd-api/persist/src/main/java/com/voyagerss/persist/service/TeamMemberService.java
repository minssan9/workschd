package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.TeamMemberDTO;
import com.voyagerss.persist.enums.TeamRole;
import java.util.List;

public interface TeamMemberService {
    TeamMemberDTO inviteMember(TeamMemberDTO dto);
    TeamMemberDTO acceptInvitation(Long id, String token);
    TeamMemberDTO rejectInvitation(Long id, String token);
    List<TeamMemberDTO> findByTeamId(Long teamId);
    TeamMemberDTO updateRole(Long id, TeamRole role);
    void delete(Long id);
} 