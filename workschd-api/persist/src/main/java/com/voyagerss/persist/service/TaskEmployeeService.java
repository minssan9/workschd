package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.TaskEmployeeDTO;
import com.voyagerss.persist.entity.Task;
import com.voyagerss.persist.entity.TaskEmployee;
import com.voyagerss.persist.repository.TaskEmployeeRepository;
import com.voyagerss.persist.repository.TaskEmployeeRepositoryCustom;
import com.voyagerss.persist.repository.TaskRepository;
import com.voyagerss.persist.repository.AccountRepository;
import com.voyagerss.persist.EnumMaster;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskEmployeeService {

    private final TaskEmployeeRepository taskEmployeeRepository;
    private final TaskEmployeeRepositoryCustom taskEmployeeRepositoryCustom;
    private final TaskRepository taskRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public Task save(TaskEmployeeDTO vO) {
        TaskEmployee bean = new TaskEmployee();
        BeanUtils.copyProperties(vO, bean);
        bean = taskEmployeeRepository.save(bean);
        return bean.getTask();
    }

    @Transactional
    public void delete(Long id) {
        taskEmployeeRepository.deleteById(id);
    }

    @Transactional
    public void update(Long id, TaskEmployeeDTO vO) {
        TaskEmployee bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        taskEmployeeRepository.save(bean);
    }

    @Transactional(readOnly = true)
    public TaskEmployeeDTO getById(Long id) {
        TaskEmployee original = requireOne(id);
        return toDTO(original);
    }

    @Transactional(readOnly = true)
    public Page<TaskEmployeeDTO> query(TaskEmployeeDTO vO) {
        return taskEmployeeRepositoryCustom.findBySearchParams(vO, vO.getPageable());
    }
    
    /**
     * Create a new task employee request
     * 
     * @param taskId The task ID
     * @param requestDTO The request data
     * @return The created task employee request
     */
    @Transactional
    public TaskEmployeeDTO createRequest(Long taskId, TaskEmployeeDTO requestDTO) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + taskId));
                
        // Check if the user already has a request for this task
        if (taskEmployeeRepository.findByTask_IdAndAccount_AccountId(taskId, requestDTO.getAccountId()).size() > 0) {
            throw new IllegalStateException("User already has a request for this task");
        }
        
        TaskEmployee taskEmployee = new TaskEmployee();
        taskEmployee.setTask(task);
        taskEmployee.setAccount(accountRepository.findById(requestDTO.getAccountId())
                .orElseThrow(() -> new NoSuchElementException("Account not found: " + requestDTO.getAccountId())));
        taskEmployee.setStatus(EnumMaster.TaskEmployeeStatus.PENDING);
        taskEmployee.setRequestDate(LocalDateTime.now());
        
        taskEmployee = taskEmployeeRepository.save(taskEmployee);
        return toDTO(taskEmployee);
    }
    
    /**
     * Approve a join request
     * 
     * @param taskId The task ID
     * @param requestId The request ID
     */
    @Transactional
    public void approveRequest(Long taskId, Long requestId) {
        TaskEmployee request = taskEmployeeRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Request not found: " + requestId));
                
        // Verify the request belongs to the specified task
        if (!request.getTask().getId().equals(taskId)) {
            throw new IllegalArgumentException("Request does not belong to the specified task");
        }
        
        // Update request status
        request.setStatus(EnumMaster.TaskEmployeeStatus.APPROVED);
        request.setApprovedAt(LocalDateTime.now());
        taskEmployeeRepository.save(request);
    }
    
    /**
     * Get all task employees for a task with pagination
     * 
     * @param taskId The task ID
     * @param pageable Pagination information
     * @return Page of TaskEmployeeDTO
     */
    @Transactional(readOnly = true)
    public Page<TaskEmployeeDTO> getTaskEmployees(Long taskId, Pageable pageable) {
        TaskEmployeeDTO searchParams = new TaskEmployeeDTO();
        searchParams.setTaskId(taskId);
        searchParams.setPageable(pageable);
        return taskEmployeeRepositoryCustom.findBySearchParams(searchParams, pageable);
    }
    
    /**
     * Get all task requests for a specific account
     * 
     * @param accountId The account ID
     * @return List of TaskEmployeeDTO for the account's requests
     */
    @Transactional(readOnly = true)
    public List<TaskEmployeeDTO> getTaskRequestsByAccountId(Integer accountId) {
        List<TaskEmployee> requests = taskEmployeeRepository.findByAccount_AccountId(accountId);
        return requests.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private TaskEmployeeDTO toDTO(TaskEmployee original) {
        TaskEmployeeDTO bean = new TaskEmployeeDTO();
        BeanUtils.copyProperties(original, bean);
        
        // Set task fields
        if (original.getTask() != null) {
            bean.setTaskId(original.getTask().getId());
            bean.setTaskTitle(original.getTask().getTitle());
            bean.setTaskStatus(original.getTask().getStatus());
        }
        
        // Set account fields
        if (original.getAccount() != null) {
            bean.setAccountId(original.getAccount().getAccountId());
            bean.setAccountName(original.getAccount().getUsername());
            bean.setAccountEmail(original.getAccount().getEmail());
        }
        
        return bean;
    }

    private TaskEmployee requireOne(Long id) {
        return taskEmployeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
