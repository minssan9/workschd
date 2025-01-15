package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.TeamMemberDTO;
import com.voyagerss.persist.service.TeamMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team-members")
public class TeamMemberController {
    
    private final TeamMemberService teamMemberService;

    @PostMapping("/invite")
    public ResponseEntity<TeamMemberDTO> inviteMember(@Valid @RequestBody TeamMemberDTO dto) {
        return ResponseEntity.ok(teamMemberService.inviteMember(dto));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<TeamMemberDTO> acceptInvitation(
            @PathVariable Long id,
            @RequestParam String token) {
        return ResponseEntity.ok(teamMemberService.acceptInvitation(id, token));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<TeamMemberDTO> rejectInvitation(
            @PathVariable Long id,
            @RequestParam String token) {
        return ResponseEntity.ok(teamMemberService.rejectInvitation(id, token));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<TeamMemberDTO>> getTeamMembers(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamMemberService.findByTeamId(teamId));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<TeamMemberDTO> updateRole(
            @PathVariable Long id,
            @RequestBody TeamRole role) {
        return ResponseEntity.ok(teamMemberService.updateRole(id, role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeMember(@PathVariable Long id) {
        teamMemberService.delete(id);
        return ResponseEntity.ok().build();
    }
} 