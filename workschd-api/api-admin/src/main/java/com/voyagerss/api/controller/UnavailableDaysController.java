package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.UnavailableDaysDTO;
import com.voyagerss.persist.service.UnavailableDaysService;
import jakarta.persistence.NotNull;
import jakarta.persistence.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/unavailableDays")
public class UnavailableDaysController {

    @Autowired
    private UnavailableDaysService unavailableDaysService;

    @PostMapping
    public String save(@Valid @RequestBody UnavailableDaysVO vO) {
        return unavailableDaysService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        unavailableDaysService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody UnavailableDaysUpdateVO vO) {
        unavailableDaysService.update(id, vO);
    }

    @GetMapping("/{id}")
    public UnavailableDaysDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return unavailableDaysService.getById(id);
    }

    @GetMapping
    public Page<UnavailableDaysDTO> query(@Valid UnavailableDaysQueryVO vO) {
        return unavailableDaysService.query(vO);
    }
}
