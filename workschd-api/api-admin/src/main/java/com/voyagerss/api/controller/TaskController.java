package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.TaskDTO;
import com.voyagerss.persist.service.TaskService;
import jakarta.persistence.NotNull;
import jakarta.persistence.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public String save(@Valid @RequestBody TaskVO vO) {
        return taskService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        taskService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody TaskUpdateVO vO) {
        taskService.update(id, vO);
    }

    @GetMapping("/{id}")
    public TaskDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return taskService.getById(id);
    }

    @GetMapping
    public Page<TaskDTO> query(@Valid TaskQueryVO vO) {
        return taskService.query(vO);
    }
}
