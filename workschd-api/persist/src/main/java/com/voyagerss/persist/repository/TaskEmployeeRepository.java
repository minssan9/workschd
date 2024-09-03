package com.voyagerss.persist.repository;

import com.voyagerss.persist.entity.TaskEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskEmployeeRepository extends JpaRepository<TaskEmployee, Long>, JpaSpecificationExecutor<TaskEmployee> {

}