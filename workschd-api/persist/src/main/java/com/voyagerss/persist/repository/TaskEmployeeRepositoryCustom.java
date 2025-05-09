package com.voyagerss.persist.repository;

import com.voyagerss.persist.dto.TaskEmployeeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Custom repository interface for TaskEmployee
 */
public interface TaskEmployeeRepositoryCustom {
 
    /**
     * Find TaskEmployees using a DTO parameter for filtering
     * @param searchParams DTO containing search parameters
     * @param pageable Pagination information
     * @return Page of TaskEmployeeDTO
     */
    Page<TaskEmployeeDTO> findBySearchParams(TaskEmployeeDTO searchParams, Pageable pageable);
} 