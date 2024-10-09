package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.CommonAuditDTO;
import com.voyagerss.persist.service.CommonAuditService;import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/commonAudit")
public class CommonAuditController {

    @Autowired
    private CommonAuditService commonAuditService;

    @PostMapping
    public String save(@Valid @RequestBody CommonAuditDTO vO) {
        return commonAuditService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        commonAuditService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody CommonAuditDTO vO) {
        commonAuditService.update(id, vO);
    }

    @GetMapping("/{id}")
    public CommonAuditDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return commonAuditService.getById(id);
    }

    @GetMapping
    public Page<CommonAuditDTO> query(@Valid CommonAuditDTO vO) {
        return commonAuditService.query(vO);
    }
}
