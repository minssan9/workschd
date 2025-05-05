package com.voyagerss.api.controller.admin;

import com.voyagerss.persist.dto.KakaoAddressResponse;
import com.voyagerss.persist.service.KakaoAddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/kakao-webflux")
public class KakaoWebFluxController {

    private final KakaoAddressService kakaoAddressService;

    public KakaoWebFluxController(KakaoAddressService kakaoAddressService) {
        this.kakaoAddressService = kakaoAddressService;
    }

    @GetMapping("/address")
    public ResponseEntity<KakaoAddressResponse> getAddress(@RequestParam String query) {
        KakaoAddressResponse response = kakaoAddressService.getAddressInfo(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/address-raw")
    public Mono<Object> getAddressRaw(@RequestParam String query) {
        return kakaoAddressService.getAddressInfoAsMap(query);
    }
} 