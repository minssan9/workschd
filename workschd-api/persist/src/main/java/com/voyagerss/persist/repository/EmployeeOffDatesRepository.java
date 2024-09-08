package com.voyagerss.persist.repository;

import com.voyagerss.persist.entity.EmployeeOffDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeOffDatesRepository extends JpaRepository<EmployeeOffDates, Long>, JpaSpecificationExecutor<EmployeeOffDates> {

}