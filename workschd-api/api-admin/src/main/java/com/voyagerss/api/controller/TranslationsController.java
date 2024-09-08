package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.TranslationsDTO;
import com.voyagerss.persist.service.TranslationsService;import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/translations")
public class TranslationsController {

    @Autowired
    private TranslationsService translationsService;

    @PostMapping
    public String save(@Valid @RequestBody TranslationsDTO vO) {
        return translationsService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        translationsService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody TranslationsDTO vO) {
        translationsService.update(id, vO);
    }

    @GetMapping("/{id}")
    public TranslationsDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return translationsService.getById(id);
    }

    @GetMapping
    public Page<TranslationsDTO> query(@Valid TranslationsDTO vO) {
        return translationsService.query(vO);
    }
}
