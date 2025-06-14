package com.voyagerss.persist.repository;

import com.voyagerss.persist.entity.Account;
import com.voyagerss.persist.entity.AccountWorkHour;
import com.voyagerss.persist.entity.AccountWorkOffDates;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountWorkHourRepository extends JpaRepository<AccountWorkHour, Long>, JpaSpecificationExecutor<AccountWorkHour> {

    @Query("SELECT awh FROM AccountWorkHour awh WHERE awh.account.accountId = :accountId")
    List<AccountWorkHour> findAllByAccountId(@Param("accountId") Long accountId);
    
    @Query("SELECT awh FROM AccountWorkHour awh WHERE awh.account.accountId = :accountId AND awh.id = :id")
    Optional<AccountWorkHour> findByAccountIdAndId(@Param("accountId") Long accountId, @Param("id") Long id);
    
    @Query("SELECT awh FROM AccountWorkHour awh WHERE awh.account.accountId = :accountId AND awh.date BETWEEN :startDate AND :endDate")
    List<AccountWorkHour> findByAccountIdAndDateRange(
        @Param("accountId") Long accountId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Query("SELECT awh FROM AccountWorkHour awh WHERE awh.account.accountId = :accountId AND awh.day = :day")
    List<AccountWorkHour> findByAccountIdAndDay(
        @Param("accountId") Long accountId,
        @Param("day") String day
    );

    List<AccountWorkHour> findByAccountAccountId(Integer id);
}