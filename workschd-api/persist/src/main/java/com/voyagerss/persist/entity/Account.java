package com.voyagerss.persist.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.voyagerss.persist.dto.AccountDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "account")
public class Account extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "account_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "english_name")
    private String englishName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "incentive_percent", nullable = false)
    private Double incentivePercent;

    @Column(name = "password")
    private String password;

    @Column(name = "profile_image_url", nullable = false)
    private String profileImageUrl;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "profile_video_url")
    private String profileVideoUrl;

    @Column(name = "firebase_token")
    private String firebaseToken;

    @OneToMany(mappedBy = "account")
    private List<TeamMember> teamMembers;

    @JsonManagedReference
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<AccountRole> accountRoles;

    @JsonManagedReference
    @OneToMany(mappedBy = "account" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AccountSns> accountSnsList;

    @JsonManagedReference
    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
    private AccountInfo accountInfo;



    public Account(AccountDTO accountDto) {
        BeanUtils.copyProperties(accountDto, this);
        this.username = accountDto.getUsername();
        this.password = accountDto.getPassword();
        this.email = accountDto.getEmail();
        this.englishName = accountDto.getEnglishName();
        this.phone = accountDto.getPhone();
        this.incentivePercent = accountDto.getIncentivePercent();
        this.profileVideoUrl = accountDto.getProfileVideoUrl() != null ? accountDto.getProfileVideoUrl() : "";
    }

    public Account(
            @NotNull @Size(max = 100) String username,
            @NotNull @Size(max = 20) String phone,
            @NotNull @Size(max = 512) String email,
            @NotNull @Size(max = 512) String profileImageUrl,
            @NotNull LocalDateTime createdAt,
            @NotNull LocalDateTime modifiedAt
    ) {
        this.username = username;
        this.password = "NO_PASS";
        this.phone = phone;
        this.email = email != null ? email : "NO_EMAIL";
        this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
        this.profileVideoUrl = "";
        this.englishName = username;
        this.incentivePercent = 1.0;
    }

    public Account(Integer id) {
        this.accountId = id;
    }

    public AccountDTO toDto() {
        AccountDTO bean = new AccountDTO();
        BeanUtils.copyProperties(this, bean);
        return bean;
    }
}
