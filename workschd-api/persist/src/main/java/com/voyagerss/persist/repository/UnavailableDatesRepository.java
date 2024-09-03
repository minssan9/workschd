package com.voyagerss.persist.repository;

import com.voyagerss.persist.entity.UnavailableDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UnavailableDatesRepository extends JpaRepository<UnavailableDates, Long>, JpaSpecificationExecutor<UnavailableDates> {

}