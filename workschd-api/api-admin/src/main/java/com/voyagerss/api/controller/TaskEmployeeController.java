package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.TaskEmployeeDTO;
import com.voyagerss.persist.service.TaskEmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/task-employee")
public class TaskEmployeeController {

    @Autowired
    private TaskEmployeeService taskEmployeeService;

    @PostMapping
    public ResponseEntity<String> save(@Valid @RequestBody TaskEmployeeDTO vO) {
        return ResponseEntity.ok(taskEmployeeService.save(vO).toString());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @NotNull @PathVariable("id") Long id) {
        taskEmployeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody TaskEmployeeDTO vO) {
        taskEmployeeService.update(id, vO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskEmployeeDTO> getById(@Valid @NotNull @PathVariable("id") Long id) {
        return ResponseEntity.ok(taskEmployeeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<TaskEmployeeDTO>> query(
        @Valid TaskEmployeeDTO vO,
        @PageableDefault(page = 0, size = 10, direction = Sort.Direction.DESC) Pageable pageable
        ) {
        return ResponseEntity.ok(taskEmployeeService.query(vO));
    }
    
    /**
     * Create a task employee request
     * 
     * @param taskId The task ID
     * @param requestData The request data
     * @return The created task employee
     */
    @PostMapping("/{taskId}/request")
    public ResponseEntity<TaskEmployeeDTO> createRequest(
            @PathVariable("taskId") Long taskId,
            @Valid @RequestBody TaskEmployeeDTO requestData) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskEmployeeService.createRequest(taskId, requestData));
    }
    
    /**
     * Approve a join request
     * 
     * @param taskId The task ID
     * @param requestId The request ID
     */
    @PostMapping("/{taskId}/request/{requestId}/approve")
    public ResponseEntity<Void> approveJoinRequest(
            @PathVariable("taskId") Long taskId,
            @PathVariable("requestId") Long requestId) {
        taskEmployeeService.approveRequest(taskId, requestId);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Get all task employees for a task with pagination
     * 
     * @param taskId The task ID
     * @param pageable Pagination information
     * @return Page of TaskEmployeeDTO
     */
    @GetMapping("/{taskId}/employees")
    public ResponseEntity<Page<TaskEmployeeDTO>> getTaskEmployees(
            @PathVariable("taskId") Long taskId,
            @PageableDefault(page = 0, size = 10, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(taskEmployeeService.getTaskEmployees(taskId, pageable));
    }
}
