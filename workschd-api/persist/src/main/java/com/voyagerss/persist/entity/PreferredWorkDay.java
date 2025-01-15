package com.voyagerss.persist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "preferred_work_day")
public class PreferredWorkDay extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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