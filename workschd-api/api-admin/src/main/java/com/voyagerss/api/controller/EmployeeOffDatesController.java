package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.UnavailableDatesDTO;
import com.voyagerss.persist.service.EmployeeOffDatesService;import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/employee/off-dates")
public class EmployeeOffDatesController {

    @Autowired
    private EmployeeOffDatesService employeeOffDatesService;

    @PostMapping
    public String save(@Valid @RequestBody UnavailableDatesDTO vO) {
        return employeeOffDatesService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        employeeOffDatesService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody UnavailableDatesDTO vO) {
        employeeOffDatesService.update(id, vO);
    }

    @GetMapping("/{id}")
    public UnavailableDatesDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return employeeOffDatesService.getById(id);
    }

    @GetMapping
    public Page<UnavailableDatesDTO> query(@Valid UnavailableDatesDTO vO) {
        return employeeOffDatesService.query(vO);
    }
}
