package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.BranchDTO;
import com.voyagerss.persist.dto.TeamMemberDTO;
import com.voyagerss.persist.entity.Account;
import com.voyagerss.persist.entity.Team;
import com.voyagerss.persist.entity.TeamMember;
import com.voyagerss.persist.repository.AccountRepository;
import com.voyagerss.persist.repository.TeamRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private AccountRepository accountRepository;

    public Long save(BranchDTO vO) {
        Team bean = new Team();
        BeanUtils.copyProperties(vO, bean);
        bean = teamRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        teamRepository.deleteById(id);
    }

    public void update(Long id, BranchDTO vO) {
        Team bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        teamRepository.save(bean);
    }

    public BranchDTO getById(Long id) {
        Team original = requireOne(id);
        return toDTO(original);
    }

    public Page<BranchDTO> query(BranchDTO vO) {
        throw new UnsupportedOperationException();
    }

    private BranchDTO toDTO(Team original) {
        BranchDTO bean = new BranchDTO();
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
        return "https://yourapp.com/invite/" + hash;
    }



    public List<TeamMemberDTO> getTeamMembers(String teamName) {
        Team team = teamRepository.findByName(teamName)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        return team.getTeamMembers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TeamMemberDTO convertToDTO(TeamMember member) {
        TeamMemberDTO dto = new TeamMemberDTO();
        dto.setId(member.getId());
        dto.setName(member.getAccount().getUsername());
        dto.setEmail(member.getAccount().getEmail());
        dto.setJoinDate(member.getJoinDate());
        dto.setStatus(member.getStatus());
        return dto;
    }
}
