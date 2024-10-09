package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.EmployeeDTO;
import com.voyagerss.persist.service.EmployeeService;import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public String save(@Valid @RequestBody EmployeeDTO vO) {
        return employeeService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        employeeService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody EmployeeDTO vO) {
        employeeService.update(id, vO);
    }

    @GetMapping("/{id}")
    public EmployeeDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return employeeService.getById(id);
    }

    @GetMapping
    public Page<EmployeeDTO> query(@Valid EmployeeDTO vO) {
        return employeeService.query(vO);
    }
}
