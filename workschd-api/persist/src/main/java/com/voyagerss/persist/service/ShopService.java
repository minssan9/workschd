package com.voyagerss.persist.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.voyagerss.persist.entity.Shop;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voyagerss.persist.dto.ShopDTO;
import com.voyagerss.persist.entity.Team;
import com.voyagerss.persist.repository.ShopRepository;
import com.voyagerss.persist.repository.TeamRepository;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;
    
    @Autowired
    private TeamRepository teamRepository;

    public Long save(ShopDTO vO) {
        Shop bean = new Shop();
        BeanUtils.copyProperties(vO, bean);
        
        // Set team if teamId is provided
        if (vO.getTeamId() != null) {
            Team team = teamRepository.findById(vO.getTeamId())
                    .orElseThrow(() -> new NoSuchElementException("Team not found: " + vO.getTeamId()));
            bean.setTeam(team);
        }
        
        bean = shopRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        shopRepository.deleteById(id);
    }

    public void update(Long id, ShopDTO vO) {
        Shop bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        
        // Set team if teamId is provided
        if (vO.getTeamId() != null) {
            Team team = teamRepository.findById(vO.getTeamId())
                    .orElseThrow(() -> new NoSuchElementException("Team not found: " + vO.getTeamId()));
            bean.setTeam(team);
        }
        
        shopRepository.save(bean);
    }

    public ShopDTO getById(Long id) {
        Shop original = requireOne(id);
        return toDTO(original);
    }

    public Page<ShopDTO> query(ShopDTO vO) {
        throw new UnsupportedOperationException("Query method not implemented");
    }
    
    /**
     * Find all stores by team ID
     * @param teamId The team ID
     * @return List of stores belonging to the team
     */
    @Transactional(readOnly = true)
    public List<ShopDTO> findByTeamId(Long teamId) {
        // Verify team exists
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team not found: " + teamId));
        
        // Find stores by team
        List<Shop> shops = shopRepository.findByTeam(team);
        
        // Convert to DTOs
        return shops.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Find all stores by team ID and region
     * @param teamId The team ID
     * @param region The region
     * @return List of stores belonging to the team and in the specified region
     */
    @Transactional(readOnly = true)
    public List<ShopDTO> findByTeamIdAndRegion(Long teamId, String region) {
        List<Shop> shops = shopRepository.findByTeam_IdAndRegion(teamId, region);
        
        return shops.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Find active stores by team ID
     * @param teamId The team ID
     * @return List of active stores belonging to the team
     */
    @Transactional(readOnly = true)
    public List<ShopDTO> findActiveStoresByTeamId(Long teamId) {
        List<Shop> shops = shopRepository.findActiveStoresByTeamId(teamId);
        
        return shops.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ShopDTO toDTO(Shop original) {
        ShopDTO bean = new ShopDTO();
        BeanUtils.copyProperties(original, bean);
        
        // Set teamId if team is present
        if (original.getTeam() != null) {
            bean.setTeamId(original.getTeam().getId());
            bean.setTeamName(original.getTeam().getName());
        }
        
        return bean;
    }

    private Shop requireOne(Long id) {
        return shopRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
