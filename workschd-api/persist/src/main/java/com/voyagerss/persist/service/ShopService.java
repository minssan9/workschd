package com.voyagerss.persist.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.voyagerss.persist.dto.KakaoAddressDocument;
import com.voyagerss.persist.dto.KakaoAddressResponse;
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
import com.voyagerss.persist.service.KakaoAddressService;

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
    
    @Autowired
    private KakaoAddressService kakaoAddressService;

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

        System.out.println("Updating coordinates for shop: " + shop.getName() + ", address: " + shop.getAddress());
        
        // Try using the WebFlux implementation first
        try {
            KakaoAddressResponse response = kakaoAddressService.getAddressInfo(shop.getAddress());
            
            if (response != null && response.documents() != null && !response.documents().isEmpty()) {
                KakaoAddressDocument document = response.documents().get(0);
                System.out.println("WebFlux: Found address match: " + document.address_name());
                
                shop.setLatitude(Double.parseDouble(document.y()));
                shop.setLongitude(Double.parseDouble(document.x()));
                
                if (document.road_address() != null) {
                    shop.setPostalCode(document.road_address().zone_no());

                }
                
                shopRepository.save(shop);
                System.out.println("Updated shop coordinates successfully using WebFlux");
                return toDTO(shop);
            } else {
                System.out.println("WebFlux: No results found, falling back to RestTemplate");
            }
        } catch (Exception e) {
            System.out.println("Error using WebFlux implementation: " + e.getMessage());
            System.out.println("Falling back to RestTemplate implementation");
        }
        
        // Fall back to the original RestTemplate implementation
        String kakaoApiUrl = "https://dapi.kakao.com/v2/local/search/address.json";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(kakaoApiUrl)
                .queryParam("query", shop.getAddress());

        HttpEntity<?> entity = new HttpEntity<>(headers);
        
        try {
            System.out.println("Calling Kakao API using RestTemplate for shop: " + shop.getName());
            
            ResponseEntity<KakaoAddressResponse> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                KakaoAddressResponse.class
            );
            
            System.out.println("Response status: " + response.getStatusCode());
            
            if (response.getBody() == null) {
                System.out.println("Response body is null");
                throw new RuntimeException("Empty response body from Kakao API");
            }
            
            if (response.getBody().documents() == null) {
                System.out.println("Documents list is null");
                throw new RuntimeException("Empty documents list in Kakao API response");
            }
            
            if (response.getBody().documents().isEmpty()) {
                System.out.println("No documents found for address: " + shop.getAddress());
                return toDTO(shop);
            }
            
            System.out.println("Found " + response.getBody().documents().size() + " address matches");
            
            KakaoAddressDocument document = response.getBody().documents().get(0);
            System.out.println("First match: " + document.address_name() + ", x: " + document.x() + ", y: " + document.y());
            
            shop.setLatitude(Double.parseDouble(document.y()));
            shop.setLongitude(Double.parseDouble(document.x()));
            
            if (document.road_address() != null) {
                System.out.println("Road address found, postal code: " + document.road_address().zone_no());
                shop.setPostalCode(document.road_address().zone_no());
            } else {
                System.out.println("No road address found");
            }
            
            shopRepository.save(shop);
            System.out.println("Shop coordinates updated successfully");
        } catch (Exception e) {
            System.out.println("Exception during Kakao API call: " + e.getMessage());
            e.printStackTrace();
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
