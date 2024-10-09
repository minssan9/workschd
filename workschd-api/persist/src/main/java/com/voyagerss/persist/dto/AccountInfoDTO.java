package com.voyagerss.persist.dto;


import com.voyagerss.persist.entity.Account;
import com.voyagerss.persist.entity.AccountInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class AccountInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private Integer accountId;

    private Long branchId;

    private String phoneNumber;

    private String email;

    private String name;

    private Integer preferredBranchId;

    private String preferredDay;

    private String unavailableDaysOfWeek;

    private Boolean manager;

    private String createdBy;

    private LocalDateTime createdAt;

    private Boolean active;

    private String lastModifiedBy;

    private LocalDateTime lastModifiedAt;


    public AccountInfoDTO(Account account){
        BeanUtils.copyProperties(account, this);
    }

    public AccountInfoDTO(AccountDTO account){
        BeanUtils.copyProperties(account, this);
    }


    public AccountInfoDTO(AccountInfo accountInfo){
        if (accountInfo != null) {
            BeanUtils.copyProperties(accountInfo, this);
        }
    }

}
