package com.voyagerss.persist.repository;

import com.voyagerss.persist.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>, JpaSpecificationExecutor<Account> {

    Optional<Account> findByEmail(String email);

    boolean existsByRefreshToken(String refreshToken);

    boolean existsByEmail(String email);
}