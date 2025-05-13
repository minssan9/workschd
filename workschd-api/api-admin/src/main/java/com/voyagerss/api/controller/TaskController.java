package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.TaskEmployeeDTO;
import com.voyagerss.persist.entity.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.voyagerss.persist.dto.TaskDTO;
import com.voyagerss.persist.service.TaskService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Validated
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * Create a new task
     * Mapped to: const createTask = (task: Task): Promise<AxiosResponse<Task>> => {
     *   return request.post('/task', task)
     * }
     */
    @PostMapping
    public ResponseEntity<TaskDTO> save(@Valid @RequestBody TaskDTO taskDTO) {
        try {
            log.info("Creating new task: {}", taskDTO);
            Task task = taskService.save(taskDTO);
            TaskDTO createdTask = taskService.toDTO(task);

            return ResponseEntity.ok(createdTask);
        } catch (Exception e) {
            log.error("Error creating task: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Create multiple new tasks
     */
    @PostMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> saveMultiple(@Valid @RequestBody List<TaskDTO> taskDTOs) {
        try {
            log.info("Creating {} new tasks", taskDTOs.size());
            List<Task> tasks = taskService.saveMultiple(taskDTOs);
            List<TaskDTO> createdTasks = tasks.stream().map(taskService::toDTO).toList();

            return ResponseEntity.ok(createdTasks);
        } catch (Exception e) {
            log.error("Error creating multiple tasks: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @NotNull @PathVariable("id") Long id) {
        try {
            taskService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody TaskDTO vO) {
        try {
            taskService.update(id, vO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getById(@Valid @NotNull @PathVariable("id") Long id) {
        try {
            TaskDTO task = taskService.getById(id);
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all tasks
     * Mapped to: const fetchTasks = (): Promise<AxiosResponse<Task[]>> => {
     *   return request.get('/task')
     * }
     */
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        try {
            log.info("Fetching all tasks");
            List<TaskDTO> tasks = taskService.getAllTasks();
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            log.error("Error fetching tasks: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Approve a join request
     * Mapped to: const approveJoinRequest = (requestId: number): Promise<AxiosResponse<void>> => {
     *   return request.post(`/task/request/${requestId}/approve`)
     * }
     */
    @PostMapping("/request/{requestId}/approve")
    @Transactional
    public ResponseEntity<Void> approveJoinRequest(
            @PathVariable Long requestId
    ) {
        try {
            log.info("Approving join request with ID: {}", requestId);
            taskService.approveJoinRequest(requestId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error approving join request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Create a join request for a task
     */
    @PostMapping("/{taskId}/request")
    @Transactional
    public ResponseEntity<TaskEmployeeDTO> createJoinRequest(
            @PathVariable Long taskId,
            @RequestParam Integer accountId) {
        try {
            log.info("Creating join request for task ID: {} and account ID: {}", taskId, accountId);
            TaskEmployeeDTO taskEmployeeDTO = taskService.createJoinRequest(taskId, accountId);
            return ResponseEntity.ok(taskEmployeeDTO);
        } catch (IllegalStateException e) {
            // Request already exists
            log.warn("Join request already exists: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (NoSuchElementException e) {
            // Task or account not found
            log.error("Error creating join request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error creating join request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
