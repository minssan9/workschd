package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.AttendanceDTO;
import com.voyagerss.persist.service.AttendanceService;import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public String save(@Valid @RequestBody AttendanceDTO vO) {
        return attendanceService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        attendanceService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody AttendanceDTO vO) {
        attendanceService.update(id, vO);
    }

    @GetMapping("/{id}")
    public AttendanceDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return attendanceService.getById(id);
    }

    @GetMapping
    public Page<AttendanceDTO> query(@Valid AttendanceDTO vO) {
        return attendanceService.query(vO);
    }
}
