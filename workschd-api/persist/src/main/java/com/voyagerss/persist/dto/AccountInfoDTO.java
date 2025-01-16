package com.voyagerss.persist.dto;

import com.voyagerss.persist.entity.account.AccountInfo;
import com.voyagerss.persist.entity.account.AccountInfo.EmployeeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class AccountInfoDTO {
    private Long id;
    private Long accountId;
    private EmployeeType employeeType;

    public AccountInfoDTO(AccountInfo accountInfo) {
        this.id = accountInfo.getId();
        this.employeeType = accountInfo.getEmployeeType();
    }
}
