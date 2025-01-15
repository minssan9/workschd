package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.AccountInfoDTO;
import com.voyagerss.persist.service.AccountInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account-info")
public class AccountInfoController {
    
    private final AccountInfoService accountInfoService;

    @PostMapping
    public ResponseEntity<AccountInfoDTO> create(@Valid @RequestBody AccountInfoDTO accountInfoDTO) {
        return ResponseEntity.ok(accountInfoService.save(accountInfoDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountInfoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(accountInfoService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountInfoDTO> update(@PathVariable Long id, @Valid @RequestBody AccountInfoDTO accountInfoDTO) {
        return ResponseEntity.ok(accountInfoService.update(id, accountInfoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountInfoService.delete(id);
        return ResponseEntity.ok().build();
    }
} 