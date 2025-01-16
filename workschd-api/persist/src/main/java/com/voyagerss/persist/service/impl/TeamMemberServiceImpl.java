package com.voyagerss.persist.service.impl;

import com.voyagerss.persist.dto.TeamMemberDTO;
import com.voyagerss.persist.entity.TeamMember;
import com.voyagerss.persist.enums.TeamRole;
import com.voyagerss.persist.repository.TeamMemberRepository;
import com.voyagerss.persist.service.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamMemberServiceImpl implements TeamMemberService {
    
    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Override
    @Transactional
    public TeamMemberDTO inviteMember(TeamMemberDTO dto) {
        TeamMember member = mapToEntity(dto);
        member = teamMemberRepository.save(member);
        return mapToDTO(member);
    }

    @Override
    @Transactional
    public TeamMemberDTO acceptInvitation(Long id, String token) {
        TeamMember member = teamMemberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Team member not found"));
        // Add token validation logic here
        member.setAccepted(true);
        return mapToDTO(teamMemberRepository.save(member));
    }

    @Override
    @Transactional
    public TeamMemberDTO rejectInvitation(Long id, String token) {
        TeamMember member = teamMemberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Team member not found"));
        // Add token validation logic here
        member.setRejected(true);
        return mapToDTO(teamMemberRepository.save(member));
    }

    @Override
    public List<TeamMemberDTO> findByTeamId(Long teamId) {
        return teamMemberRepository.findByTeamId(teamId).stream()
            .map(this::mapToDTO)
            .toList();
    }

    @Override
    @Transactional
    public TeamMemberDTO updateRole(Long id, TeamRole role) {
        TeamMember member = teamMemberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Team member not found"));
        member.setRole(role);
        return mapToDTO(teamMemberRepository.save(member));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        teamMemberRepository.deleteById(id);
    }

    private TeamMember mapToEntity(TeamMemberDTO dto) {
        TeamMember entity = new TeamMember();
        entity.setId(dto.id());
        entity.setRole(dto.role());
        entity.setAccepted(dto.accepted());
        entity.setRejected(dto.rejected());
        entity.setInvitationToken(dto.invitationToken());
        // Set team and account using their repositories
        return entity;
    }

    private TeamMemberDTO mapToDTO(TeamMember entity) {
        return new TeamMemberDTO(
            entity.getId(),
            entity.getTeam().getId(),
            entity.getAccount().getAccountId(),
            entity.getRole(),
            entity.isAccepted(),
            entity.isRejected(),
            entity.getInvitationToken()
        );
    }
} 