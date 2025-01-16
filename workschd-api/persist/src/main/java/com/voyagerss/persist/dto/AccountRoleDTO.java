package com.voyagerss.persist.dto;


import com.voyagerss.persist.entity.account.AccountRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class AccountRoleDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long accountId;

    private Integer accountRoleId;

    private LocalDateTime createdAt;

    private String roleType;

    public AccountRoleDTO(AccountRole original) {
    }
}
