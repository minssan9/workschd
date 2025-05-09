package com.voyagerss.api.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.voyagerss.persist.dto.KakaoAddressResponse;
import com.voyagerss.persist.dto.ShopDTO;
import com.voyagerss.persist.service.ShopService;

@RestController
@RequestMapping("/admin/shops")
public class ShopAdminController {

    @Autowired
    private ShopService shopService;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${KAKAO_REST_API_KEY}")
    private String kakaoRestApiKey;
    
    @GetMapping("/update-coordinates/{shopId}")
    public ResponseEntity<ShopDTO> updateShopCoordinates(@PathVariable Long shopId) {
        try {
            ShopDTO updatedShop = shopService.updateShopCoordinates(shopId);
            return ResponseEntity.ok(updatedShop);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/test-kakao-api")
    public ResponseEntity<?> testKakaoApi(@RequestParam String address) {
        try {
            String kakaoApiUrl = "https://dapi.kakao.com/v2/local/search/address.json";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);
            
            System.out.println("Using Kakao API Key: " + kakaoRestApiKey);
            
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(kakaoApiUrl)
                    .queryParam("query", address);
    
            HttpEntity<?> entity = new HttpEntity<>(headers);
            
            ResponseEntity<KakaoAddressResponse> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                KakaoAddressResponse.class
            );
            
            System.out.println("Response status: " + response.getStatusCode());
            
            if (response.getBody() != null) {
                System.out.println("Documents count: " + 
                    (response.getBody().documents() != null ? response.getBody().documents().size() : "null"));
            } else {
                System.out.println("Response body is null");
            }
            
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
} 