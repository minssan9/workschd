package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.UnavailableDatesDTO;
import com.voyagerss.persist.service.UnavailableDatesService;
import jakarta.persistence.NotNull;
import jakarta.persistence.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/unavailableDates")
public class UnavailableDatesController {

    @Autowired
    private UnavailableDatesService unavailableDatesService;

    @PostMapping
    public String save(@Valid @RequestBody UnavailableDatesVO vO) {
        return unavailableDatesService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        unavailableDatesService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody UnavailableDatesUpdateVO vO) {
        unavailableDatesService.update(id, vO);
    }

    @GetMapping("/{id}")
    public UnavailableDatesDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return unavailableDatesService.getById(id);
    }

    @GetMapping
    public Page<UnavailableDatesDTO> query(@Valid UnavailableDatesQueryVO vO) {
        return unavailableDatesService.query(vO);
    }
}
