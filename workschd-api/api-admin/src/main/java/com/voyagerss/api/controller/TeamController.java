package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.BranchDTO;
import com.voyagerss.persist.entity.Team;
import com.voyagerss.persist.repository.TeamRepository;
import com.voyagerss.persist.service.TeamService;import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Validated
@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamRepository teamRepository;

    @PostMapping
    public String save(@Valid @RequestBody BranchDTO vO) {
        return teamService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        teamService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody BranchDTO vO) {
        teamService.update(id, vO);
    }

    @GetMapping("/{id}")
    public BranchDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return teamService.getById(id);
    }

    @GetMapping
    public Page<BranchDTO> query(@Valid BranchDTO vO) {
        return teamService.query(vO);
    }




    @PostMapping("/team/join")
    public String joinTeam(@RequestParam Long teamId, @RequestParam String username) {
        teamService.joinTeam(teamId, username);
        return "redirect:/team/success";  // 가입 성공 후 리디렉션
    }

    @GetMapping("/team/join/{hash}")
    public ResponseEntity handleInvite(@PathVariable String hash) {
        Team team = teamRepository.findByInvitationHash(hash)
                .orElseThrow(() -> new RuntimeException("Invalid or expired invitation link"));

        // 초대 생성일이 7일이 넘었는지 확인 (예시)
        if (team.getInvitationCreatedAt().isBefore(LocalDateTime.now().minusDays(7))) {
            throw new RuntimeException("Invitation link has expired");
        }

        return ResponseEntity.ok(team);
    }

}
