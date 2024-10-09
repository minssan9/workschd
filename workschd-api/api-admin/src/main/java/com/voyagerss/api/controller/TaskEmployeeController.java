package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.TaskEmployeeDTO;
import com.voyagerss.persist.service.TaskEmployeeService;import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/taskEmployee")
public class TaskEmployeeController {

    @Autowired
    private TaskEmployeeService taskEmployeeService;

    @PostMapping
    public String save(@Valid @RequestBody TaskEmployeeDTO vO) {
        return taskEmployeeService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        taskEmployeeService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody TaskEmployeeDTO vO) {
        taskEmployeeService.update(id, vO);
    }

    @GetMapping("/{id}")
    public TaskEmployeeDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return taskEmployeeService.getById(id);
    }

    @GetMapping
    public Page<TaskEmployeeDTO> query(@Valid TaskEmployeeDTO vO) {
        return taskEmployeeService.query(vO);
    }
}
