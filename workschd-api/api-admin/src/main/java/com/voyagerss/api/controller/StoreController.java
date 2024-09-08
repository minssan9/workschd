package com.voyagerss.api.controller;

import com.voyagerss.persist.dto.StoreDTO;
import com.voyagerss.persist.service.StoreService;import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping
    public String save(@Valid @RequestBody StoreDTO vO) {
        return storeService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        storeService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody StoreDTO vO) {
        storeService.update(id, vO);
    }

    @GetMapping("/{id}")
    public StoreDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return storeService.getById(id);
    }

    @GetMapping
    public Page<StoreDTO> query(@Valid StoreDTO vO) {
        return storeService.query(vO);
    }
}
