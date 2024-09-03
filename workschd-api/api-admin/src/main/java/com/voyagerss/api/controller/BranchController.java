package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.BranchDTO;
import com.voyagerss.persist.service.BranchService;
import jakarta.persistence.NotNull;
import jakarta.persistence.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/branch")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @PostMapping
    public String save(@Valid @RequestBody BranchVO vO) {
        return branchService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        branchService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody BranchUpdateVO vO) {
        branchService.update(id, vO);
    }

    @GetMapping("/{id}")
    public BranchDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return branchService.getById(id);
    }

    @GetMapping
    public Page<BranchDTO> query(@Valid BranchQueryVO vO) {
        return branchService.query(vO);
    }
}
