package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.TaskDTO;
import com.voyagerss.persist.dto.TaskEmployeeDTO;
import com.voyagerss.persist.entity.Account;
import com.voyagerss.persist.entity.Task;
import com.voyagerss.persist.entity.TaskEmployee;
import com.voyagerss.persist.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.voyagerss.persist.EnumMaster.TaskEmployeeStatus.APPROVED;
import static com.voyagerss.persist.EnumMaster.TaskEmployeeStatus.PENDING;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TeamRepository teamRepository;
    private final ShopRepository shopRepository;
    private final TaskRepository taskRepository;
    private final TaskEmployeeRepository taskEmployeeRepository;
    private final AccountRepository accountRepository;

    public Task save(TaskDTO vO) {
        Task task = new Task();
        BeanUtils.copyProperties(vO, task);

        shopRepository.findById(vO.getShopId())
                .ifPresent(task::setShop);
        teamRepository.findById(vO.getTeamId())
                .ifPresent(task::setTeam);

        return taskRepository.save(task);
    }

    /**
     * Save multiple tasks at once
     * @param taskDTOs List of task DTOs to save
     * @return List of saved Task entities
     */
    @Transactional
    public List<Task> saveMultiple(List<TaskDTO> taskDTOs) {
        return taskDTOs.stream()
                .map(this::save)
                .collect(Collectors.toList());
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
        List<TaskEmployee> taskEmployees = taskEmployeeRepository.findByTask_Id(id);
        if (taskEmployees != null && !taskEmployees.isEmpty()) {
            dto.setTaskEmployees(taskEmployees.stream()
                    .map(this::toTaskEmployee)
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
        TaskEmployee taskEmployee = taskEmployeeRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Join request not found: " + requestId));
        
        // Update request status
        taskEmployee.setStatus(APPROVED);
        taskEmployee.setApprovedAt(LocalDateTime.now());
        
        // Save the updated request
        taskEmployeeRepository.save(taskEmployee);
    }
    
    /**
     * Create a new join request
     * @param taskId The task ID
     * @param accountId The account ID
     * @return The created join request as TaskDTO
     */
    @Transactional
    public TaskEmployeeDTO createJoinRequest(Long taskId, Integer accountId) {
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
        TaskEmployee taskEmployee = new TaskEmployee();
        taskEmployee.setTask(task);
        taskEmployee.setAccount(account);
        taskEmployee.setStatus(PENDING);
        taskEmployee.setRequestDate(LocalDateTime.now());
        
        // Save the request
        taskEmployee = taskEmployeeRepository.save(taskEmployee);
        
        // Return DTO
        return toTaskEmployee(taskEmployee);
    }

    public TaskDTO toDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        BeanUtils.copyProperties(task, taskDTO);
        
        // Set IDs
        taskDTO.setShopId(task.getShop().getId());
        taskDTO.setTeamId(task.getTeam().getId());
        
        // Set names
        taskDTO.setShopName(task.getShop().getName());
        taskDTO.setTeamName(task.getTeam().getName());
        
        return taskDTO;
    }
    
    private TaskEmployeeDTO toTaskEmployee(TaskEmployee request) {
        TaskEmployeeDTO taskEmployeeDTO = new TaskEmployeeDTO();
        
        // Set request fields
        taskEmployeeDTO.setId(request.getId());
        taskEmployeeDTO.setStatus(request.getStatus());
        taskEmployeeDTO.setRequestDate(request.getRequestDate());
        taskEmployeeDTO.setApprovedAt(request.getApprovedAt());
        taskEmployeeDTO.setRejectedAt(request.getRejectedAt());
        taskEmployeeDTO.setRejectionReason(request.getRejectionReason());
        
        // Set task ID
        if (request.getTask() != null) {
            taskEmployeeDTO.setTaskId(request.getTask().getId());
        }
        
        // Set account details
        if (request.getAccount() != null) {
            taskEmployeeDTO.setAccountId(request.getAccount().getAccountId());
            taskEmployeeDTO.setAccountName(request.getAccount().getUsername());
            taskEmployeeDTO.setAccountEmail(request.getAccount().getEmail());
        }
        
        return taskEmployeeDTO;
    }

    private Task requireOne(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
