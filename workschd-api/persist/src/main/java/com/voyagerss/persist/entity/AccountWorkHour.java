package com.voyagerss.persist.entity;

import com.voyagerss.persist.dto.AccountWorkHourDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "account_work_hours")
@Getter
@Setter
@NoArgsConstructor
public class AccountWorkHour extends BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "day", nullable = false)
    private String day;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "preferred", nullable = false)
    private boolean preferred;

    public void updateFromDto(AccountWorkHourDTO dto) {
        if (dto != null) {
            LocalDate newDate = dto.getDateAsLocalDate();
            if (newDate != null) {
                this.date = newDate;
            }
            if (dto.getDay() != null) {
                this.day = dto.getDay();
            }
            LocalTime newStartTime = dto.getStartTimeAsLocalTime();
            if (newStartTime != null) {
                this.startTime = newStartTime;
            }
            LocalTime newEndTime = dto.getEndTimeAsLocalTime();
            if (newEndTime != null) {
                this.endTime = newEndTime;
            }
            this.preferred = dto.isPreferred();
        }
    }

    public AccountWorkHour(AccountWorkHourDTO dto) {
        updateFromDto(dto);
    }
}