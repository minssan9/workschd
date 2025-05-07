package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.TeamScheduleConfigDTO;
import com.voyagerss.persist.service.TeamScheduleConfigService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/team/{teamId}/schedule-config")
public class TeamScheduleController {
    private final TeamScheduleConfigService teamScheduleConfigService;

    @GetMapping
    public ResponseEntity<TeamScheduleConfigDTO> getConfig(@PathVariable Long teamId) {
        try {
            TeamScheduleConfigDTO config = teamScheduleConfigService.getConfig(teamId);
            return ResponseEntity.ok(config);
        } catch (Exception e) {
            // Error handled by global exception handler
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> saveConfig(
            @PathVariable Long teamId,
            @Valid @RequestBody TeamScheduleConfigDTO dto
    ) {
        try {
            teamScheduleConfigService.saveConfig(teamId, dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Error handled by global exception handler
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 