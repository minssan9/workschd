package com.voyagerss.persist.dto;

import com.voyagerss.persist.entity.AccountInfo.EmployeeType;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
public class AccountInfoDTO {
    private Long id;
    private Long accountId;
    private EmployeeType employeeType;
    private String preferredDay;
    private Set<Integer> unavailableDaysOfWeek;
    private LocalTime preferredStartTime;
    private LocalTime preferredEndTime;
}
