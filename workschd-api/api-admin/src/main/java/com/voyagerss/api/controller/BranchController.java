package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.BranchDTO;
import com.voyagerss.persist.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/branch")
public class BranchController {
    
    private final BranchService branchService;

    @PostMapping
    public ResponseEntity<BranchDTO> create(@Valid @RequestBody BranchDTO branchDTO) {
        return ResponseEntity.ok(branchService.save(branchDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(branchService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchDTO> update(@PathVariable Long id, @Valid @RequestBody BranchDTO branchDTO) {
        return ResponseEntity.ok(branchService.update(id, branchDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        branchService.delete(id);
        return ResponseEntity.ok().build();
    }
} 