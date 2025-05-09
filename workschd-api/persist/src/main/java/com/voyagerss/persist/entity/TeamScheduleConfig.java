package com.voyagerss.persist.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "team_schedule_config")
public class TeamScheduleConfig implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false, unique = true)
    private Team team;

    @Column(name = "min_staff_per_day", columnDefinition = "TEXT")
    private String minStaffPerDay; // JSON string

    @Column(name = "max_off_days_per_month", columnDefinition = "TEXT")
    private String maxOffDaysPerMonth; // JSON string

    @Column(name = "additional_options", columnDefinition = "TEXT")
    private String additionalOptions; // JSON string

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }
    public String getMinStaffPerDay() { return minStaffPerDay; }
    public void setMinStaffPerDay(String minStaffPerDay) { this.minStaffPerDay = minStaffPerDay; }
    public String getMaxOffDaysPerMonth() { return maxOffDaysPerMonth; }
    public void setMaxOffDaysPerMonth(String maxOffDaysPerMonth) { this.maxOffDaysPerMonth = maxOffDaysPerMonth; }
    public String getAdditionalOptions() { return additionalOptions; }
    public void setAdditionalOptions(String additionalOptions) { this.additionalOptions = additionalOptions; }
} 