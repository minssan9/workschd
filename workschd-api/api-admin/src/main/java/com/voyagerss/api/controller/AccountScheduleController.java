package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.AccountWorkOffDatesDTO;
import com.voyagerss.persist.service.AccountScheduleService;import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/account/schedule")
public class AccountScheduleController {

    private final AccountScheduleService accountScheduleService;

    @PostMapping
    public String save(@Valid @RequestBody AccountWorkOffDatesDTO vO) {
        return accountScheduleService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        accountScheduleService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody AccountWorkOffDatesDTO vO) {
        accountScheduleService.update(id, vO);
    }

    @GetMapping("/{id}")
    public AccountWorkOffDatesDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return accountScheduleService.getOffDatesById(id);
    }

    @GetMapping
    public Page<AccountWorkOffDatesDTO> query(@Valid AccountWorkOffDatesDTO vO) {
        return accountScheduleService.query(vO);
    }



}
