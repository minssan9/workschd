package com.voyagerss.persist.entity;

import com.voyagerss.persist.dto.AccountWorkHourDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@RequiredArgsConstructor
@Getter
@Setter
@Entity
public class AccountWorkHour extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String day;
    private String startTime;
    private String endTime;
    private Boolean preferred;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_info_id")
    private AccountInfo accountInfo;

    public AccountWorkHour(AccountWorkHourDTO accountWorkHourDTO) {
        this.date = accountWorkHourDTO.getDate();
        this.day = accountWorkHourDTO.getDay();
        this.startTime = accountWorkHourDTO.getStartTime();
        this.endTime = accountWorkHourDTO.getEndTime();
        this.preferred = accountWorkHourDTO.getPreferred();
    }

    public void updateFromDto(AccountWorkHourDTO accountWorkHourDTO) {
        this.date = accountWorkHourDTO.getDate();
        this.day = accountWorkHourDTO.getDay();
        this.startTime = accountWorkHourDTO.getStartTime();
        this.endTime = accountWorkHourDTO.getEndTime();
        this.preferred = accountWorkHourDTO.getPreferred();
    }
}