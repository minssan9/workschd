package com.voyagerss.persist.repository;

import com.voyagerss.persist.entity.TaskEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskEmployeeRepository extends JpaRepository<TaskEmployee, Long>, JpaSpecificationExecutor<TaskEmployee> {

}