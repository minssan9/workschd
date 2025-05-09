package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.KakaoAddressResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class KakaoAddressService {
    
    private static final Logger log = LoggerFactory.getLogger(KakaoAddressService.class);
    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/search/address.json";
    
    private final WebClient webClient;
    
    public KakaoAddressService(@Value("${KAKAO_REST_API_KEY}") String kakaoRestApiKey) {
        this.webClient = WebClient.builder()
                .baseUrl(KAKAO_API_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "KakaoAK " + kakaoRestApiKey)
                .build();
                
        log.info("KakaoAddressService initialized with API key: {}", maskApiKey(kakaoRestApiKey));
    }
    
    /**
     * Retrieve coordinates for an address using Kakao Maps API
     * @param address The address to geocode
     * @return A KakaoAddressResponse with location data, or null if not found
     */
    public KakaoAddressResponse getAddressInfo(String address) {
        log.info("Querying Kakao API for address: {}", address);
        
        try {
            KakaoAddressResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("query", address)
                            .build())
                    .retrieve()
                    .bodyToMono(KakaoAddressResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .doOnSuccess(result -> {
                        if (result != null && result.documents() != null) {
                            log.info("Received {} results for address: {}", 
                                    result.documents().size(), address);
                        } else {
                            log.warn("No results found for address: {}", address);
                        }
                    })
                    .doOnError(error -> log.error("Error querying Kakao API: {}", error.getMessage()))
                    .block();
                    
            return response;
        } catch (Exception e) {
            log.error("Exception while calling Kakao API: {}", e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Alternate implementation using raw Map for flexibility
     */
    public Mono<Object> getAddressInfoAsMap(String address) {
        log.info("Querying Kakao API for address as Map: {}", address);
        
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", address)
                        .build())
                .retrieve()
                .bodyToMono(Object.class)
                .doOnSuccess(result -> {
                    log.info("Received raw result object: {}", result != null ? result.getClass().getName() : "null");
                    log.debug("Result content: {}", result);
                })
                .doOnError(error -> log.error("Error querying Kakao API: {}", error.getMessage()));
    }
    
    // Mask API key for safe logging
    private String maskApiKey(String apiKey) {
        if (apiKey == null || apiKey.length() < 8) {
            return "****";
        }
        return apiKey.substring(0, 4) + "****" + apiKey.substring(apiKey.length() - 4);
    }
} 