package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.PreferredWorkDayDTO;
import com.voyagerss.persist.service.PreferredWorkDayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/preferred-work-days")
public class PreferredWorkDayController {
    
    private final PreferredWorkDayService preferredWorkDayService;

    @PostMapping
    public ResponseEntity<PreferredWorkDayDTO> create(@Valid @RequestBody PreferredWorkDayDTO dto) {
        return ResponseEntity.ok(preferredWorkDayService.save(dto));
    }

    @GetMapping("/account/{accountInfoId}")
    public ResponseEntity<List<PreferredWorkDayDTO>> getByAccountInfoId(@PathVariable Long accountInfoId) {
        return ResponseEntity.ok(preferredWorkDayService.findByAccountInfoId(accountInfoId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreferredWorkDayDTO> update(
            @PathVariable Long id, 
            @Valid @RequestBody PreferredWorkDayDTO dto) {
        return ResponseEntity.ok(preferredWorkDayService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        preferredWorkDayService.delete(id);
        return ResponseEntity.ok().build();
    }
} 