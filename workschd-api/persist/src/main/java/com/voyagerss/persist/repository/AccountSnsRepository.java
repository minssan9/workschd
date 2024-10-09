package com.voyagerss.persist.repository;

import com.voyagerss.persist.EnumMaster;
import com.voyagerss.persist.entity.AccountSns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountSnsRepository extends JpaRepository<AccountSns, Long> {
    Optional<AccountSns> findFirstBySnsEmail(String snsEmail);

    AccountSns findByProviderTypeAndSnsEmail(EnumMaster.ProviderType providerType, String snsEmail);   //    Optional<AccountSns> findByProviderTypeAndSnsEmail(EnumMaster.ProviderType providerType, String snsEmail);

    AccountSns findBySnsEmailAndRefreshToken(String SnsEmail, String refreshToken);

    boolean existsByAccount_AccountIdAndProviderType(Integer accountId, EnumMaster.ProviderType providerType);
}
