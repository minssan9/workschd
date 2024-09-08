package com.voyagerss.persist.repository;

import com.voyagerss.persist.EnumMaster;
import com.voyagerss.persist.entity.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, Integer>, JpaSpecificationExecutor<AccountRole> {
    List<AccountRole> findByRoleType(EnumMaster.RoleType roleType);

    List<AccountRole> findByAccount_AccountId(Integer accountId);

    List<AccountRole> findByAccount_Email(String email);

    Boolean existsByAccount_AccountIdAndRoleType(Integer accountId, EnumMaster.RoleType roleType);

    void deleteByAccountRoleId(Integer accountRoleId);

    AccountRole findByAccount_AccountIdAndRoleType(Integer accountId, EnumMaster.RoleType roleType);

    void deleteByAccount_AccountIdAndRoleType(Integer accountId, EnumMaster.RoleType roleType);
}
