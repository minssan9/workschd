package com.voyagerss.persist.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.voyagerss.persist.EnumMaster;
import com.voyagerss.persist.dto.AccountSnsDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "account_sns")
public class AccountSns extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "account_sns_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountSnsId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "sns_email")
    private String snsEmail;

    @Column(name = "provider_type", nullable = false)
    private EnumMaster.ProviderType providerType;

    @Column(name = "sns_phone")
    private String snsPhone;

    @Column(name = "expired")
    private LocalDateTime expired;

    @Column(name = "email_verified_yn", nullable = false)
    private String emailVerifiedYn;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "profile_video_url")
    private String profileVideoUrl;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;


    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;


    public AccountSns(
            @NotNull @Size(max = 64) String userId,
            String snsPhone,
            @NotNull String snsEmail,
            @NotNull @Size(max = 1) String emailVerifiedYn,
            @NotNull @Size(max = 512) String profileImageUrl,
            @NotNull EnumMaster.ProviderType providerType,
            @Size(max = 256) String accessToken,
            @Size(max = 256) String refreshToken,
            Account account
    ) {
        this.userId = userId;
        this.snsPhone = snsPhone;
        this.snsEmail = snsEmail;

        this.emailVerifiedYn = emailVerifiedYn;
        this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
        this.providerType = providerType;

        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.account = account;
    }

    public AccountSnsDTO toDto() {
        AccountSnsDTO bean = new AccountSnsDTO();
        BeanUtils.copyProperties(this, bean);
        return bean;
    }
}
