package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.TeamScheduleConfigDTO;

public interface TeamScheduleConfigService {
    TeamScheduleConfigDTO getConfig(Long teamId);
    void saveConfig(Long teamId, TeamScheduleConfigDTO dto);
} 