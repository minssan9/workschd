package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.TeamDTO;
import com.voyagerss.persist.dto.TeamMemberDTO;
import com.voyagerss.persist.entity.Team;
import com.voyagerss.persist.repository.TeamRepository;
import com.voyagerss.persist.service.TeamService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamRepository teamRepository;

    @PostMapping
    public TeamDTO save(@Valid @RequestBody TeamDTO vO) {
        return teamService.createTeam(vO) ;
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        teamService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody TeamDTO vO) {
        teamService.update(id, vO);
    }

    @GetMapping("/{id}")
    public TeamDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return teamService.getById(id);
    }

    @GetMapping
    public Page<TeamDTO> query(@Valid TeamDTO vO) {
        return teamService.query(vO);
    }

    @PostMapping("/team/join")
    public ResponseEntity<?> joinTeam(@RequestParam Long teamId, @RequestParam String username) {
        try {
            teamService.joinTeam(teamId, username);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/team/join/{hash}")
    public ResponseEntity<?> handleInvite(@PathVariable String hash) {
        try {
            Team team = teamRepository.findByInvitationHash(hash)
                    .orElseThrow(() -> new RuntimeException("Invalid or expired invitation link"));

            if (team.getInvitationCreatedAt().isBefore(LocalDateTime.now().minusDays(7))) {
                throw new RuntimeException("Invitation link has expired");
            }

            return ResponseEntity.ok(team);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/generate-invite")
    public ResponseEntity<?> generateInviteLink(@RequestBody Map<String, String> request) {
        try {
            String teamName = request.get("teamName");
            String location = request.get("location");
            
            if (teamName == null || location == null) {
                return ResponseEntity.badRequest().body("Team name and location are required");
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
            return ResponseEntity.badRequest().body(e.getMessage());
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
