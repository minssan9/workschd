package com.voyagerss.persist.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.voyagerss.persist.EnumMaster;
import com.voyagerss.persist.dto.AccountRoleDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.management.relation.Role;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(name = "account_role")
public class AccountRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_role_id", nullable = false)
    private Integer accountRoleId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "role_type", nullable = false)
    private EnumMaster.RoleType roleType;



    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;


    public AccountRole(EnumMaster.RoleType roleType, Account account) {
        this.roleType = roleType;
        this.account = account;
    }

    public AccountRoleDTO toDto() {
        AccountRoleDTO bean = new AccountRoleDTO();
        BeanUtils.copyProperties(this, bean);
        return bean;
    }
}
