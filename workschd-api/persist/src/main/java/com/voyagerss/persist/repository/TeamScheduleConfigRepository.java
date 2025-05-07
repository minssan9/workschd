package com.voyagerss.persist.repository;

import com.voyagerss.persist.entity.TeamScheduleConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamScheduleConfigRepository extends JpaRepository<TeamScheduleConfig, Long> {
    Optional<TeamScheduleConfig> findByTeam_Id(Long teamId);
} 