package com.voyagerss.persist.repository;

import com.voyagerss.persist.entity.PreferredWorkDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferredWorkDayRepository extends JpaRepository<PreferredWorkDay, Long> {
    
    @Query("SELECT pwd FROM PreferredWorkDay pwd WHERE pwd.accountInfo.id = :accountInfoId")
    List<PreferredWorkDay> findByAccountInfoId(Long accountInfoId);
} 