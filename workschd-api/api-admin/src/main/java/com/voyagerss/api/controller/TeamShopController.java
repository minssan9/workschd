package com.voyagerss.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.voyagerss.persist.dto.ShopDTO;
import com.voyagerss.persist.service.ShopService;
import com.voyagerss.persist.service.TeamService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/team/{teamId}/shop")
@RequiredArgsConstructor
public class TeamShopController {

    private final ShopService shopService;
    private final TeamService teamService;

    /**
     * Create a new store for a team
     * @param teamId The ID of the team
     * @param shopDTO The store data
     * @return The created store
     */
    @PostMapping
    public ResponseEntity<ShopDTO> save(
            @PathVariable Long teamId,
            @Valid @RequestBody ShopDTO shopDTO) {
        try {
            log.info("Creating new store for team {}: {}", teamId, shopDTO);
            
            // Set the team ID in the DTO
            shopDTO.setTeamId(teamId);
            
            Long id = shopService.save(shopDTO);
            ShopDTO createdStore = shopService.getById(id);
            return ResponseEntity.ok(createdStore);
        } catch (Exception e) {
            log.error("Error creating store for team {}: {}", teamId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete a store
     * @param teamId The ID of the team
     * @param id The ID of the store to delete
     * @return Empty response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long teamId,
            @Valid @NotNull @PathVariable("id") Long id) {
        try {
            log.info("Deleting store {} for team {}", id, teamId);
            
            // Verify the store belongs to the team
            ShopDTO store = shopService.getById(id);
            if (!teamId.equals(store.getTeamId())) {
                log.warn("Store {} does not belong to team {}", id, teamId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            shopService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting store {} for team {}: {}", id, teamId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update a store
     * @param teamId The ID of the team
     * @param id The ID of the store to update
     * @param shopDTO The updated store data
     * @return Empty response
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long teamId,
            @Valid @NotNull @PathVariable("id") Long id,
            @Valid @RequestBody ShopDTO shopDTO) {
        try {
            log.info("Updating store {} for team {}: {}", id, teamId, shopDTO);
            
            // Verify the store belongs to the team
            ShopDTO existingStore = shopService.getById(id);
            if (!teamId.equals(existingStore.getTeamId())) {
                log.warn("Store {} does not belong to team {}", id, teamId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            // Set the team ID in the DTO
            shopDTO.setTeamId(teamId);
            
            shopService.update(id, shopDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error updating store {} for team {}: {}", id, teamId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get a store by ID
     * @param teamId The ID of the team
     * @param id The ID of the store
     * @return The store
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShopDTO> getById(
            @PathVariable Long teamId,
            @Valid @NotNull @PathVariable("id") Long id) {
        try {
            log.info("Getting store {} for team {}", id, teamId);
            
            ShopDTO store = shopService.getById(id);
            
            // Verify the store belongs to the team
            if (!teamId.equals(store.getTeamId())) {
                log.warn("Store {} does not belong to team {}", id, teamId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            return ResponseEntity.ok(store);
        } catch (Exception e) {
            log.error("Error getting store {} for team {}: {}", id, teamId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get all stores for a team
     * @param teamId The ID of the team
     * @return List of stores for the team
     */
    @GetMapping
    public ResponseEntity<List<ShopDTO>> getAllShops(
            @PathVariable Long teamId,
            @RequestParam(required = false) String district) {
        try {
            List<ShopDTO> stores;
            
            if (district != null && !district.isEmpty()) {
                log.info("Getting stores for team {} in region: {}", teamId, district);
                stores = shopService.findByTeamIdAndDistrict(teamId, district);
            } else {
                log.info("Getting all stores for team: {}", teamId);
                stores = shopService.findByTeamId(teamId);
            }
            
            return ResponseEntity.ok(stores);
        } catch (Exception e) {
            log.error("Error getting stores for team {}: {}", teamId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get all active stores for a team
     * @param teamId The ID of the team
     * @return List of active stores for the team
     */
    @GetMapping("/active")
    public ResponseEntity<List<ShopDTO>> getActiveStores(@PathVariable Long teamId) {
        try {
            log.info("Getting active stores for team: {}", teamId);
            List<ShopDTO> stores = shopService.findActiveStoresByTeamId(teamId);
            return ResponseEntity.ok(stores);
        } catch (Exception e) {
            log.error("Error getting active stores for team {}: {}", teamId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
