package com.voyagerss.persist.repository;

import com.voyagerss.persist.entity.TaskEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskEmployeeRepository extends 
    JpaRepository<TaskEmployee, Long>  {

  /**
   * Find all requests by task ID
   * @param taskId The task ID
   * @return List of TaskEmployee
   */
  List<TaskEmployee> findByTask_Id(Long taskId);

  /**
   * Find all requests by account ID
   * @param accountId The account ID
   * @return List of TaskEmployee
   */
  List<TaskEmployee> findByAccount_AccountId(Integer accountId);

  /**
   * Find all requests by task ID and status
   * @param taskId The task ID
   * @param status The request status
   * @return List of TaskEmployee
   */
  List<TaskEmployee> findByTask_IdAndStatus(Long taskId, String status);

  /**
   * Find all requests by account ID and status
   * @param accountId The account ID
   * @param status The request status
   * @return List of TaskEmployee
   */
  List<TaskEmployee> findByAccount_AccountIdAndStatus(Integer accountId, String status);

  /**
   * Find all requests by task ID and account ID
   * @param taskId The task ID
   * @param accountId The account ID
   * @return List of TaskEmployee
   */
  List<TaskEmployee> findByTask_IdAndAccount_AccountId(Long taskId, Integer accountId);
}