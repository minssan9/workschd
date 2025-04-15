package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.TaskDTO;
import com.voyagerss.persist.entity.Account;
import com.voyagerss.persist.entity.Task;
import com.voyagerss.persist.entity.TaskEmployee;
import com.voyagerss.persist.repository.AccountRepository;
import com.voyagerss.persist.repository.TaskEmployeeRepository;
import com.voyagerss.persist.repository.TaskRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private TaskEmployeeRepository taskEmployeeRepository;
    
    @Autowired
    private AccountRepository accountRepository;

    public Long save(TaskDTO vO) {
        Task bean = new Task();
        BeanUtils.copyProperties(vO, bean);
        bean = taskRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public void update(Long id, TaskDTO vO) {
        Task bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        taskRepository.save(bean);
    }

    public TaskDTO getById(Long id) {
        Task original = requireOne(id);
        TaskDTO dto = toDTO(original);
        
        // Load join requests for this task
        List<TaskEmployee> joinRequests = taskEmployeeRepository.findByTask_Id(id);
        if (joinRequests != null && !joinRequests.isEmpty()) {
            dto.setJoinRequests(joinRequests.stream()
                    .map(this::toJoinRequestDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }

    public Page<TaskDTO> query(TaskDTO vO) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Get all tasks
     * @return List of all tasks
     */
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Approve a join request
     * @param requestId The ID of the join request to approve
     */
    @Transactional
    public void approveJoinRequest(Long requestId) {
        TaskEmployee request = taskEmployeeRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Join request not found: " + requestId));
        
        // Update request status
        request.setStatus("APPROVED");
        request.setApprovedAt(LocalDateTime.now());
        
        // Save the updated request
        taskEmployeeRepository.save(request);
    }
    
    /**
     * Create a new join request
     * @param taskId The task ID
     * @param accountId The account ID
     * @return The created join request as TaskDTO
     */
    @Transactional
    public TaskDTO createJoinRequest(Long taskId, Integer accountId) {
        // Check if task exists
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + taskId));
        
        // Check if account exists
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Account not found: " + accountId));
        
        // Check if request already exists
        List<TaskEmployee> existingRequests = taskEmployeeRepository
                .findByTask_IdAndAccount_AccountId(taskId, accountId);
        
        if (!existingRequests.isEmpty()) {
            throw new IllegalStateException("A join request already exists for this task and account");
        }
        
        // Create new request
        TaskEmployee request = new TaskEmployee();
        request.setTask(task);
        request.setAccount(account);
        request.setStatus("PENDING");
        request.setRequestDate(LocalDateTime.now());
        
        // Save the request
        request = taskEmployeeRepository.save(request);
        
        // Return DTO
        return toJoinRequestDTO(request);
    }

    private TaskDTO toDTO(Task original) {
        TaskDTO bean = new TaskDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }
    
    private TaskDTO toJoinRequestDTO(TaskEmployee request) {
        TaskDTO dto = new TaskDTO();
        
        // Set request fields
        dto.setRequestId(request.getId());
        dto.setStatus(request.getStatus());
        dto.setRequestDate(request.getRequestDate());
        dto.setApprovedAt(request.getApprovedAt());
        dto.setRejectedAt(request.getRejectedAt());
        dto.setRejectionReason(request.getRejectionReason());
        
        // Set task ID
        if (request.getTask() != null) {
            dto.setTaskId(request.getTask().getId());
        }
        
        // Set account details
        if (request.getAccount() != null) {
            dto.setAccountId(request.getAccount().getAccountId());
            dto.setAccountName(request.getAccount().getUsername());
            dto.setAccountEmail(request.getAccount().getEmail());
        }
        
        return dto;
    }

    private Task requireOne(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
