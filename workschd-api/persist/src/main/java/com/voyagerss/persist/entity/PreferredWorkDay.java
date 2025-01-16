package com.voyagerss.persist.entity;

import com.voyagerss.persist.entity.account.AccountInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "preferred_work_days")
public class PreferredWorkDay {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_info_id")
    private AccountInfo accountInfo;

    @Column(name = "day_of_week")
    private Integer dayOfWeek; // 1(월요일) ~ 7(일요일)

    @Column(name = "preferred_start_time")
    private LocalTime preferredStartTime;

    @Column(name = "preferred_end_time")
    private LocalTime preferredEndTime;

    @Column(name = "preference_type")
    @Enumerated(EnumType.STRING)
    private PreferenceType preferenceType;

    public enum PreferenceType {
        PREFERRED,    // 선호하는 요일
        UNAVAILABLE  // 근무 불가능한 요일
    }
} 