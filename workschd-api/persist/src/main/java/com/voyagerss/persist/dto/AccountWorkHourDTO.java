package com.voyagerss.persist.dto;

import com.voyagerss.persist.entity.AccountWorkHour;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AccountWorkHourDTO {
    private Integer accountId;
    private LocalDate date;
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean preferred;

    public AccountWorkHourDTO() {
    }

    public AccountWorkHourDTO(AccountWorkHour accountWorkHour) {
        if (accountWorkHour.getAccount() != null) {
            this.accountId = accountWorkHour.getAccount().getAccountId();
        }
        this.date = accountWorkHour.getDate();
        this.day = accountWorkHour.getDay();
        this.startTime = accountWorkHour.getStartTime();
        this.endTime = accountWorkHour.getEndTime();
        this.preferred = accountWorkHour.getPreferred();
    }
}