package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.AccountWorkHourDTO;
import com.voyagerss.persist.dto.AccountWorkOffDatesDTO;
import com.voyagerss.persist.service.AccountScheduleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/account/{accountId}/schedule")
public class AccountScheduleController {

    private final AccountScheduleService accountScheduleService;

    // Work Off Dates Endpoints
    @PostMapping("/off-dates")
    public ResponseEntity<Long> saveOrUpdateOffDate(
            @Valid @NotNull @PathVariable("accountId") Long accountId,
            @Valid @RequestBody AccountWorkOffDatesDTO dto) {
        return ResponseEntity.ok(accountScheduleService.saveOrUpdateOffDate(accountId, dto));
    }

    @PostMapping("/off-dates/batch")
    public ResponseEntity<List<Long>> saveOrUpdateOffDates(
            @Valid @NotNull @PathVariable("accountId") Long accountId,
            @Valid @RequestBody List<AccountWorkOffDatesDTO> dtos) {
        return ResponseEntity.ok(accountScheduleService.saveOrUpdateOffDates(accountId, dtos));
    }

    @DeleteMapping("/off-dates/{id}")
    public ResponseEntity<Void> deleteOffDate(
            @Valid @NotNull @PathVariable("accountId") Long accountId,
            @Valid @NotNull @PathVariable("id") Long id) {
        accountScheduleService.deleteOffDate(accountId, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/off-dates/{id}")
    public ResponseEntity<AccountWorkOffDatesDTO> getOffDateById(
            @Valid @NotNull @PathVariable("accountId") Long accountId,
            @Valid @NotNull @PathVariable("id") Long id) {
        return ResponseEntity.ok(accountScheduleService.getOffDatesById(accountId, id));
    }

    @GetMapping("/off-dates/range")
    public ResponseEntity<List<AccountWorkOffDatesDTO>> getOffDatesByDateRange(
            @Valid @NotNull @PathVariable("accountId") Long accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(accountScheduleService.getOffDatesByDateRange(accountId, startDate, endDate));
    }

    @GetMapping("/off-dates")
    public ResponseEntity<Page<AccountWorkOffDatesDTO>> queryOffDates(
            @Valid @NotNull @PathVariable("accountId") Long accountId,
            @Valid AccountWorkOffDatesDTO dto) {
        return ResponseEntity.ok(accountScheduleService.queryOffDates(accountId, dto));
    }

    // Work Hours Endpoints
    @PostMapping("/work-hours")
    public ResponseEntity<Long> saveOrUpdateWorkHour(
            @Valid @NotNull @PathVariable("accountId") Long accountId,
            @Valid @RequestBody AccountWorkHourDTO dto) {
        return ResponseEntity.ok(accountScheduleService.saveOrUpdateWorkHour(accountId, dto));
    }

    @PostMapping("/work-hours/batch")
    public ResponseEntity<List<Long>> saveOrUpdateWorkHours(
            @Valid @NotNull @PathVariable("accountId") Long accountId,
            @Valid @RequestBody List<AccountWorkHourDTO> dtos) {
        return ResponseEntity.ok(accountScheduleService.saveOrUpdateWorkHours(accountId, dtos));
    }

    @DeleteMapping("/work-hours/{id}")
    public ResponseEntity<Void> deleteWorkHour(
            @Valid @NotNull @PathVariable("accountId") Long accountId,
            @Valid @NotNull @PathVariable("id") Long id) {
        accountScheduleService.deleteWorkHour(accountId, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/work-hours/{id}")
    public ResponseEntity<AccountWorkHourDTO> getWorkHourById(
            @Valid @NotNull @PathVariable("accountId") Long accountId,
            @Valid @NotNull @PathVariable("id") Long id) {
        return ResponseEntity.ok(accountScheduleService.getWorkHourById(accountId, id));
    }

    @GetMapping("/work-hours/range")
    public ResponseEntity<List<AccountWorkHourDTO>> getWorkHoursByDateRange(
            @Valid @NotNull @PathVariable("accountId") Long accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(accountScheduleService.getWorkHoursByDateRange(accountId, startDate, endDate));
    }

    @GetMapping("/work-hours/day/{day}")
    public ResponseEntity<List<AccountWorkHourDTO>> getWorkHoursByDay(
            @Valid @NotNull @PathVariable("accountId") Long accountId,
            @Valid @NotNull @PathVariable("day") String day) {
        return ResponseEntity.ok(accountScheduleService.getWorkHoursByDay(accountId, day));
    }

    @GetMapping("/work-hours")
    public ResponseEntity<Page<AccountWorkHourDTO>> queryWorkHours(
            @Valid @NotNull @PathVariable("accountId") Long accountId,
            @Valid AccountWorkHourDTO dto) {
        return ResponseEntity.ok(accountScheduleService.queryWorkHours(accountId, dto));
    }
}
