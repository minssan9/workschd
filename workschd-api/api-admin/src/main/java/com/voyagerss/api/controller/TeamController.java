package com.voyagerss.api.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.voyagerss.persist.dto.TeamDTO;
import com.voyagerss.persist.dto.TeamMemberDTO;
import com.voyagerss.persist.entity.Account;
import com.voyagerss.persist.entity.Team;
import com.voyagerss.persist.entity.TeamMember;
import com.voyagerss.persist.repository.AccountRepository;
import com.voyagerss.persist.repository.TeamRepository;
import com.voyagerss.persist.service.ShopService;
import com.voyagerss.persist.service.TeamService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final TeamRepository teamRepository;
    private final AccountRepository accountRepository;
    private final ShopService shopService;

    @PostMapping
    public ResponseEntity<TeamDTO> save(@Valid @RequestBody TeamDTO vO) {
        try {
            TeamDTO createdTeam = teamService.createTeam(vO);
            return ResponseEntity.ok(createdTeam);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @NotNull @PathVariable("id") Long id) {
        try {
            teamService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody TeamDTO vO) {
        try {
            teamService.update(id, vO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getById(@Valid @NotNull @PathVariable("id") Long id) {
        try {
            TeamDTO team = teamService.getById(id);
            return ResponseEntity.ok(team);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<TeamDTO>> query(
            @Valid TeamDTO vO,
            @PageableDefault(page = 0, size = 10, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        try {
            vO.setPageable(pageable);
            Page<TeamDTO> teams = teamService.query(vO);
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
  
    /**
     * Join team by invitation hash - combines fetching and joining in one step
     * 
     * @param hash The invitation hash from the URL
     * @param accountId The ID of the account to add to the team
     * @return Response with the team information
     */
    @GetMapping("/join/{hash}")
    @Transactional
    public ResponseEntity<TeamDTO> joinTeamByInvitationHash(
            @PathVariable String hash, 
            @RequestParam Integer accountId) {
        
        try {
            log.info("Processing join request for hash: {} and accountId: {}", hash, accountId);
            
            // Find team by invitation hash
            Team team = teamRepository.findByInvitationHash(hash)
                    .orElseThrow(() -> new RuntimeException("Invalid or expired invitation link"));

            // Check if invitation has expired (7 days)
            if (team.getInvitationCreatedAt().isBefore(LocalDateTime.now().minusDays(7))) {
                throw new RuntimeException("Invitation link has expired");
            }
            
            // Find account by accountId
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            
            // Check if user is already a member
            boolean isMember = team.getTeamMembers().stream()
                    .anyMatch(member -> member.getAccount() != null && 
                             member.getAccount().getAccountId().equals(accountId));
            
            if (!isMember) {
                // Create new team member
                TeamMember newMember = new TeamMember();
                newMember.setTeam(team);
                newMember.setAccount(account);
                newMember.setJoinDate(LocalDateTime.now());
                newMember.setStatus("Active");
                
                // Add to team members
                if (team.getTeamMembers() == null) {
                    team.setTeamMembers(List.of(newMember));
                } else {
                    team.getTeamMembers().add(newMember);
                }
                
                // Save team
                team = teamRepository.save(team);
                log.info("Successfully added account {} to team {}", accountId, team.getId());
            } else {
                log.info("Account {} is already a member of team {}", accountId, team.getId());
            }
            
            // Convert to DTO and return
            TeamDTO teamDTO = teamService.getById(team.getId());
            return ResponseEntity.ok(teamDTO);
                
        } catch (RuntimeException e) {
            log.error("Error joining team with hash {}: {}", hash, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/generate-invite")
    public ResponseEntity<Map<String, String>> generateInviteLink(@RequestBody Map<String, String> request) {
        try {
            String teamName = request.get("teamName");
            String location = request.get("location");
            
            if (teamName == null || location == null) {
                return ResponseEntity.badRequest().build();
            }
            
            String hash = generateUniqueHash(teamName, location);
            
            Team team = teamRepository.findByName(teamName)
                    .orElseThrow(() -> new RuntimeException("Team not found"));
            
            team.setInvitationHash(hash);
            team.setInvitationCreatedAt(LocalDateTime.now());
            teamRepository.save(team);
            
            Map<String, String> response = new HashMap<>();
            response.put("inviteToken", hash);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{teamName}/members")
    public ResponseEntity<List<TeamMemberDTO>> getTeamMembers(@PathVariable String teamName) {
        List<TeamMemberDTO> members = teamService.getTeamMembers(teamName);
        return ResponseEntity.ok(members);
    }
    

    private String generateUniqueHash(String teamName, String location) {
        String input = teamName + location + System.currentTimeMillis();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            
            // Convert to hex string and take first 16 characters
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString().substring(0, 16);
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate hash", e);
        }
    }
}
