package com.voyagerss.persist.entity.account;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.voyagerss.persist.EnumMaster;
import com.voyagerss.persist.dto.AccountRoleDTO;
import com.voyagerss.persist.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "account_role")
public class AccountRole extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_role_id", nullable = false)
    private Long accountRoleId;

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
