package com.voyagerss.persist.dto;


import com.voyagerss.persist.EnumMaster;
import com.voyagerss.persist.entity.Account;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class AccountDTO extends BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer accountId;

    private String username;

    private String englishName;

    private String email;

    private String phone;

    private EnumMaster.RoleType roleType;

    private Double incentivePercent;

    private String password;

    private String profileImageUrl;

    private String accessToken;

    private String refreshToken;

    private String profileVideoUrl;
    private EnumMaster.ProviderType providerType;

    private String userId;
    private Integer accountSnsId;
    private LocalDateTime expired;
    private String snsAccount;

    private String firebaseToken;

    private List<AccountRoleDTO> accountRoles;
    private List<AccountSnsDTO> accountSnsList;
    private AccountInfoDTO accountInfo;


    public AccountDTO(
            @NotNull @Size(max = 100) String username,
            @NotNull @Size(max = 20) String phone,
            @NotNull @Size(max = 512) String email,
            @NotNull @Size(max = 512) String profileImageUrl,
            @NotNull EnumMaster.ProviderType providerType,
            @NotNull LocalDateTime createdAt,
            @NotNull LocalDateTime modifiedAt
    ) {
        this.username = username;
        this.englishName = username;
        this.password = "NO_PASS";
        this.phone = phone;
        this.email = email != null ? email : "NO_EMAIL";
        this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
        this.providerType = providerType;
        this.incentivePercent = 1.0;

    }


    public String getPhone() {
        return phone != null ? phone.replaceAll("-","").replace("+82 ","0") : "";
    }


    public AccountDTO(Account account) {
        BeanUtils.copyProperties(account, this);
        this.userId = account.getAccountId().toString();
        this.profileImageUrl = account.getProfileImageUrl();
        this.profileVideoUrl = account.getProfileVideoUrl();

        this.accountRoles = account.getAccountRoles().stream()
                .map(role -> new AccountRoleDTO(role))
                .collect(Collectors.toList());
        this.accountSnsList = account.getAccountSnsList().stream()
                .map(accountSns -> new AccountSnsDTO(accountSns))
                .collect(Collectors.toList());
        this.accountInfo = new AccountInfoDTO(account.getAccountInfo());
    }
}
