package com.voyagerss.persist.repository;

import com.voyagerss.persist.entity.AccountWorkOffDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountWorkOffDatesRepository extends JpaRepository<AccountWorkOffDates, Long> {
    
    @Query("SELECT awd FROM AccountWorkOffDates awd WHERE awd.account.accountId = :accountId")
    List<AccountWorkOffDates> findAllByAccountId(@Param("accountId") Long accountId);
    
    @Query("SELECT awd FROM AccountWorkOffDates awd WHERE awd.account.accountId = :accountId AND awd.id = :id")
    Optional<AccountWorkOffDates> findByAccountIdAndId(@Param("accountId") Long accountId, @Param("id") Long id);
    
    @Query("SELECT awd FROM AccountWorkOffDates awd WHERE awd.account.accountId = :accountId AND awd.offDate BETWEEN :startDate AND :endDate")
    List<AccountWorkOffDates> findByAccountIdAndDateRange(
        @Param("accountId") Long accountId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    List<AccountWorkOffDates> findByAccountAccountId(Integer accountId);
}