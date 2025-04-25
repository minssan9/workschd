package com.voyagerss.persist.dto;

import com.voyagerss.persist.entity.AccountWorkHour;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AccountWorkHourDTO {
    private Integer accountId;
    private LocalDate date;
    private String day;
    private String startTime;
    private String endTime;
    private Boolean preferred;

    public AccountWorkHourDTO() {
    }

    public AccountWorkHourDTO(AccountWorkHour accountWorkHour) {
        if (accountWorkHour.getAccountInfo() != null) {
            this.accountId = accountWorkHour.getAccountInfo().getAccount().getAccountId();
        }
        this.date = accountWorkHour.getDate();
        this.day = accountWorkHour.getDay();
        this.startTime = accountWorkHour.getStartTime();
        this.endTime = accountWorkHour.getEndTime();
        this.preferred = accountWorkHour.getPreferred();
    }
}