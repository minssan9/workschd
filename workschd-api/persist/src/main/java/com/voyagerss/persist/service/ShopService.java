package com.voyagerss.persist.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.voyagerss.persist.entity.Shop;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Value;

import com.voyagerss.persist.dto.ShopDTO;
import com.voyagerss.persist.entity.Team;
import com.voyagerss.persist.repository.ShopRepository;
import com.voyagerss.persist.repository.TeamRepository;
import com.voyagerss.persist.dto.KakaoAddressResponse;
import com.voyagerss.persist.dto.KakaoAddressDocument;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;
    
    @Autowired
    private TeamRepository teamRepository;

    @Value("${KAKAO_REST_API_KEY}")
    private String kakaoRestApiKey;

    @Autowired
    private RestTemplate restTemplate;

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
     * Find all stores by team ID and district
     * @param teamId The team ID
     * @param district The district
     * @return List of stores belonging to the team and in the specified district
     */
    @Transactional(readOnly = true)
    public List<ShopDTO> findByTeamIdAndDistrict(Long teamId, String district) {
        List<Shop> shops = shopRepository.findByTeam_IdAndDistrict(teamId, district);
        
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

    /**
     * Updates shop coordinates (latitude, longitude) and postal code using Kakao API
     * @param shopId The shop ID to update
     * @return Updated ShopDTO
     */
    @Transactional
    public ShopDTO updateShopCoordinates(Long shopId) {
        Shop shop = requireOne(shopId);
        if (shop.getAddress() == null || shop.getAddress().trim().isEmpty()) {
            throw new IllegalStateException("Shop address is required for coordinate update");
        }

        String kakaoApiUrl = "https://dapi.kakao.com/v2/local/search/address.json";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(kakaoApiUrl)
                .queryParam("query", shop.getAddress());

        HttpEntity<?> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<KakaoAddressResponse> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                KakaoAddressResponse.class
            );

            if (response.getBody() != null && !response.getBody().documents().isEmpty()) {
                KakaoAddressDocument document = response.getBody().documents().get(0);
                shop.setLatitude(Double.parseDouble(document.y()));
                shop.setLongitude(Double.parseDouble(document.x()));
                shop.setPostalCode(document.road_address() != null ? 
                    document.road_address().zone_no() : null);
                
                shopRepository.save(shop);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update shop coordinates: " + e.getMessage(), e);
        }

        return toDTO(shop);
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
