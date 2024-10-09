package com.voyagerss.persist.dto;


import com.voyagerss.persist.EnumMaster;
import com.voyagerss.persist.entity.AccountSns;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Data
public class AccountSnsDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer accountId;

    private Integer accountSnsId;

    private String userId;

    private String snsEmail;

    private EnumMaster.ProviderType providerType;

    private String snsPhone;

    private LocalDateTime expired;

    private String emailVerifiedYn;

    private String profileImageUrl;

    private String profileVideoUrl;

    private String accessToken;

    private String refreshToken;

    private LocalDateTime createdAt;

    public AccountSnsDTO(AccountSns accountSns) {
        this.accountSnsId = accountSns.getAccountSnsId();
        this.userId = accountSns.getUserId();
        this.snsEmail = accountSns.getSnsEmail();
        this.accessToken = accountSns.getAccessToken();
        this.refreshToken = accountSns.getRefreshToken();
        this.emailVerifiedYn = accountSns.getEmailVerifiedYn();
        this.profileImageUrl = accountSns.getProfileImageUrl();
        this.providerType = accountSns.getProviderType();
        this.snsPhone = accountSns.getSnsPhone();
    }
}
