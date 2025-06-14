package com.voyagerss.persist.service;

import com.voyagerss.persist.component.properties.CoreProperties;
import com.voyagerss.persist.dto.TeamDTO;
import com.voyagerss.persist.dto.TeamMemberDTO;
import com.voyagerss.persist.entity.Team;
import com.voyagerss.persist.entity.TeamMember;
import com.voyagerss.persist.querydsl.TeamRepositorySupport;
import com.voyagerss.persist.repository.TeamMemberRepository;
import com.voyagerss.persist.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TeamService {

    private final CoreProperties coreProperties;
    private final TeamRepository teamRepository;
    private final TeamRepositorySupport teamRepositorySupport;
    private final TeamMemberRepository teamMemberRepository;

    public TeamDTO createTeam(TeamDTO vO) {
        Team team = new Team();
        BeanUtils.copyProperties(vO, team);
        team.setInvitationCreatedAt(LocalDateTime.now());
        team.setInvitationHash(UUID.randomUUID().toString());
        team.setInvitationExpireAt(LocalDateTime.now().plusDays(7)); // 7일 후 만료

        TeamDTO teamDto =  toDTO(teamRepository.save(team));
        return teamDto;
    }

    public void delete(Long id) {
        teamRepository.deleteById(id);
    }

    public void update(Long id, TeamDTO vO) {
        Team bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        teamRepository.save(bean);
    }

    public TeamDTO getById(Long id) {
        Team original = requireOne(id);
        return toDTO(original);
    }

    public Page<TeamDTO> query(TeamDTO teamDTO) {
        if (teamDTO.getPageable() == null) {
            throw new IllegalArgumentException("Pageable must not be null");
        }
        
        return teamRepositorySupport.getTeamDtoPage(teamDTO);
    }

    private TeamDTO toDTO(Team original) {
        TeamDTO bean = new TeamDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private Team requireOne(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }

    @Transactional
    public void joinTeam(Long teamId, String username) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Check if user is already a member
        boolean isMember = team.getTeamMembers().stream()
                .anyMatch(member -> member.getAccount().getUsername().equals(username));

        if (isMember) {
            throw new RuntimeException("User is already a member of this team");
        }

        // Create new team member
        TeamMember member = new TeamMember();
        member.setTeam(team);
        member.setJoinDate(LocalDateTime.now());
        member.setStatus("Active");

        team.getTeamMembers().add(member);
        teamRepository.save(team);
    }

    public String generateInvitationLink(Long teamId) {
        // UUID를 이용한 랜덤 해시값 생성
        String hash = UUID.randomUUID().toString();

        // 팀 정보 가져오기
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));

        // 팀에 해시값 저장
        team.setInvitationHash(hash);
        teamRepository.save(team);

        // 초대 링크 반환
        return coreProperties.getWebAddress() + "/invite/" + hash;
    }



    @Transactional(readOnly = true)
    public List<TeamMemberDTO> getTeamMembers(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        if (team.getTeamMembers().isEmpty()) {
            throw new RuntimeException("No members found for this team");
        }
        return team.getTeamMembers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TeamMemberDTO convertToDTO(TeamMember member) {
        TeamMemberDTO dto = new TeamMemberDTO();
        dto.setId(member.getId());
        dto.setAccountId(member.getAccount().getAccountId());
        dto.setName(member.getAccount().getUsername());
        dto.setEmail(member.getAccount().getEmail());
        dto.setJoinDate(member.getJoinDate());
        dto.setStatus(member.getStatus());
        dto.setPriority(member.getPriority());
        return dto;
    }
}
