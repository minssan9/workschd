package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.AccountWorkOffDatesDTO;
import com.voyagerss.persist.service.AccountScheduleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/account/{accountId}/schedule")
public class AccountScheduleController {

    private final AccountScheduleService accountScheduleService;

    @PostMapping
    public ResponseEntity<String> save(
            @Valid @NotNull @PathVariable("accountId") Long accountId,
            @Valid @RequestBody AccountWorkOffDatesDTO vO) {
        return ResponseEntity.ok(accountScheduleService.save(accountId, vO).toString());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Valid @NotNull @PathVariable("accountId") Long accountId,
            @Valid @NotNull @PathVariable("id") Long id) {
        accountScheduleService.delete(accountId, id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @Valid @NotNull @PathVariable("accountId") Long accountId,
            @Valid @NotNull @PathVariable("id") Long id,
            @Valid @RequestBody AccountWorkOffDatesDTO vO) {
        accountScheduleService.update(accountId, id, vO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountWorkOffDatesDTO> getById(
            @Valid @NotNull @PathVariable("accountId") Long accountId,
            @Valid @NotNull @PathVariable("id") Long id) {
        AccountWorkOffDatesDTO result = accountScheduleService.getOffDatesById(accountId, id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<Page<AccountWorkOffDatesDTO>> query(
            @Valid @NotNull @PathVariable("accountId") Long accountId,
            @Valid AccountWorkOffDatesDTO vO) {
        Page<AccountWorkOffDatesDTO> result = accountScheduleService.query(accountId, vO);
        return ResponseEntity.ok(result);
    }
}
