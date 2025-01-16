package com.voyagerss.persist.dto;

import com.voyagerss.persist.entity.PreferredWorkDay.PreferenceType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class PreferredWorkDayDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long accountInfoId;
    private Integer dayOfWeek; // 1(월요일) ~ 7(일요일)
    private LocalTime preferredStartTime;
    private LocalTime preferredEndTime;
    private PreferenceType preferenceType;

    public void validate() {
        if (accountInfoId == null) {
            throw new IllegalArgumentException("Account Info ID cannot be null");
        }
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("Day of week cannot be null");
        }
        if (dayOfWeek < 1 || dayOfWeek > 7) {
            throw new IllegalArgumentException("Day of week must be between 1 and 7");
        }
        if (preferenceType == null) {
            throw new IllegalArgumentException("Preference type cannot be null");
        }
    }
} 