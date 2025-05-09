package com.voyagerss.persist.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyagerss.persist.dto.TeamScheduleConfigDTO;
import com.voyagerss.persist.entity.Team;
import com.voyagerss.persist.entity.TeamScheduleConfig;
import com.voyagerss.persist.repository.TeamRepository;
import com.voyagerss.persist.repository.TeamScheduleConfigRepository;
import com.voyagerss.persist.service.TeamScheduleConfigService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamScheduleConfigServiceImpl implements TeamScheduleConfigService {
    @Autowired
    private TeamScheduleConfigRepository configRepository;
    @Autowired
    private TeamRepository teamRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public TeamScheduleConfigDTO getConfig(Long teamId) {
        TeamScheduleConfig config = configRepository.findByTeam_Id(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team schedule config not found for teamId: " + teamId));
        try {
            return new TeamScheduleConfigDTO(
                objectMapper.readValue(config.getMinStaffPerDay(), objectMapper.getTypeFactory().constructMapType(java.util.Map.class, String.class, Integer.class)),
                objectMapper.readValue(config.getMaxOffDaysPerMonth(), objectMapper.getTypeFactory().constructMapType(java.util.Map.class, Integer.class, Integer.class)),
                objectMapper.readValue(config.getAdditionalOptions(), TeamScheduleConfigDTO.AdditionalOptions.class)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse schedule config JSON", e);
        }
    }

    @Override
    @Transactional
    public void saveConfig(Long teamId, TeamScheduleConfigDTO dto) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found: " + teamId));
        TeamScheduleConfig config = configRepository.findByTeam_Id(teamId).orElse(new TeamScheduleConfig());
        config.setTeam(team);
        try {
            config.setMinStaffPerDay(objectMapper.writeValueAsString(dto.minStaffPerDay()));
            config.setMaxOffDaysPerMonth(objectMapper.writeValueAsString(dto.maxOffDaysPerMonth()));
            config.setAdditionalOptions(objectMapper.writeValueAsString(dto.additionalOptions()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize schedule config JSON", e);
        }
        configRepository.save(config);
    }
} 