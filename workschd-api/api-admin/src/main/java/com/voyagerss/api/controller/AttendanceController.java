package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.AttendanceDTO;
import com.voyagerss.persist.service.AttendanceService;
import jakarta.persistence.NotNull;
import jakarta.persistence.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping
    public String save(@Valid @RequestBody AttendanceVO vO) {
        return attendanceService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        attendanceService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody AttendanceUpdateVO vO) {
        attendanceService.update(id, vO);
    }

    @GetMapping("/{id}")
    public AttendanceDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return attendanceService.getById(id);
    }

    @GetMapping
    public Page<AttendanceDTO> query(@Valid AttendanceQueryVO vO) {
        return attendanceService.query(vO);
    }
}
