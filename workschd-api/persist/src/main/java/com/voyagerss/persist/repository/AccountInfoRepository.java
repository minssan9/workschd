package com.voyagerss.persist.repository;

import com.voyagerss.persist.entity.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountInfoRepository extends JpaRepository<AccountInfo, Long>, JpaSpecificationExecutor<AccountInfo> {

    Optional<AccountInfo> findByAccount_AccountId(Integer id);
}