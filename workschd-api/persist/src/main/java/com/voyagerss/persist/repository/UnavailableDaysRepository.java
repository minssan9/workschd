package com.voyagerss.persist.repository;

import com.voyagerss.persist.entity.UnavailableDays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UnavailableDaysRepository extends JpaRepository<UnavailableDays, Long>, JpaSpecificationExecutor<UnavailableDays> {

}