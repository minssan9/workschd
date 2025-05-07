package com.voyagerss.persist.dto;

import java.io.Serializable;
import java.util.Map;

public record TeamScheduleConfigDTO(
    Map<String, Integer> minStaffPerDay,
    Map<Integer, Integer> maxOffDaysPerMonth,
    AdditionalOptions additionalOptions
) implements Serializable {
    public record AdditionalOptions(
        boolean allowWeekendWork,
        boolean enforceMinimumRest,
        int maxConsecutiveWorkDays,
        String scheduleGenerationFrequency
    ) implements Serializable {}
} 