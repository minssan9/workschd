package com.voyagerss.persist.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.voyagerss.persist.EnumMaster;
import com.voyagerss.persist.dto.AccountDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
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

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone; 

    @Column(name = "password")
    private String password;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumMaster.AccountStatus status;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "profile_video_url")
    private String profileVideoUrl;  

    @JsonManagedReference
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<AccountRole> accountRoles = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AccountSns> accountSnsList = new ArrayList<>();

    @JsonManagedReference
    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
    private AccountInfo accountInfo;

    public Account(AccountDTO accountDto) {
        BeanUtils.copyProperties(accountDto, this);
        this.username = accountDto.getUsername();
        this.password = accountDto.getPassword();
        this.email = accountDto.getEmail(); 
        this.phone = accountDto.getPhone(); 
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
