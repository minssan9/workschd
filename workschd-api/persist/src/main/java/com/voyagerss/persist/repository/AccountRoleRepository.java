package com.voyagerss.persist.repository;

import com.voyagerss.persist.EnumMaster;
import com.voyagerss.persist.entity.account.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, Long>, JpaSpecificationExecutor<AccountRole> {
    List<AccountRole> findByRoleType(EnumMaster.RoleType roleType);

    List<AccountRole> findByAccount_AccountId(Long accountId);

    List<AccountRole> findByAccount_Email(String email);

    Boolean existsByAccount_AccountIdAndRoleType(Long accountId, EnumMaster.RoleType roleType);

    void deleteByAccountRoleId(Long accountRoleId);

    AccountRole findByAccount_AccountIdAndRoleType(Long accountId, EnumMaster.RoleType roleType);

    void deleteByAccount_AccountIdAndRoleType(Long accountId, EnumMaster.RoleType roleType);
}
