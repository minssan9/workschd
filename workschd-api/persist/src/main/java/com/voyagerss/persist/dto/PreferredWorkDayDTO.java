package com.voyagerss.persist.dto;

import com.voyagerss.persist.entity.PreferredWorkDay.PreferenceType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class PreferredWorkDayDTO {
    private Long id;
    private Long accountInfoId;
    private Integer dayOfWeek;
    private LocalTime preferredStartTime;
    private LocalTime preferredEndTime;
    private PreferenceType preferenceType;
} 