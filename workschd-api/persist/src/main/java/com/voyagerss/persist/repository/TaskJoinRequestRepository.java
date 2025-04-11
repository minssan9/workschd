package com.voyagerss.persist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.voyagerss.persist.entity.TaskJoinRequest;

/**
 * Repository for the TaskJoinRequest entity
 */
@Repository
public interface TaskJoinRequestRepository extends JpaRepository<TaskJoinRequest, Long>, JpaSpecificationExecutor<TaskJoinRequest> {
    
    /**
     * Find all requests by task ID
     * @param taskId The task ID
     * @return List of TaskJoinRequest
     */
    List<TaskJoinRequest> findByTask_Id(Long taskId);
    
    /**
     * Find all requests by account ID
     * @param accountId The account ID
     * @return List of TaskJoinRequest
     */
    List<TaskJoinRequest> findByAccount_AccountId(Integer accountId);
    
    /**
     * Find all requests by task ID and status
     * @param taskId The task ID
     * @param status The request status
     * @return List of TaskJoinRequest
     */
    List<TaskJoinRequest> findByTask_IdAndStatus(Long taskId, String status);
    
    /**
     * Find all requests by account ID and status
     * @param accountId The account ID
     * @param status The request status
     * @return List of TaskJoinRequest
     */
    List<TaskJoinRequest> findByAccount_AccountIdAndStatus(Integer accountId, String status);
    
    /**
     * Find all requests by task ID and account ID
     * @param taskId The task ID
     * @param accountId The account ID
     * @return List of TaskJoinRequest
     */
    List<TaskJoinRequest> findByTask_IdAndAccount_AccountId(Long taskId, Integer accountId);
} 