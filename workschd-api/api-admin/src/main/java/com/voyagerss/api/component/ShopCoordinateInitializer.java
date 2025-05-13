package com.voyagerss.api.component;

import com.voyagerss.persist.entity.Shop;
import com.voyagerss.persist.repository.ShopRepository;
import com.voyagerss.persist.service.KakaoAddressService;
import com.voyagerss.persist.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShopCoordinateInitializer {
    
    private static final Logger log = LoggerFactory.getLogger(ShopCoordinateInitializer.class);
    
    @Autowired
    private ShopRepository shopRepository;
    
    @Autowired
    private ShopService shopService;

    @Autowired
    private KakaoAddressService kakaoAddressService;
    
    @EventListener(ApplicationReadyEvent.class)
    public void initializeShopCoordinates() {
        log.info("Starting shop coordinates initialization...");
        
        List<Shop> shopsWithoutCoordinates = shopRepository.findByLatitudeIsNullOrLongitudeIsNull();
        
        for (Shop shop : shopsWithoutCoordinates) {
            try {
                shopService.updateShopCoordinates(shop.getId());
                log.info("Updated coordinates for shop: {}", shop.getName());
                // Add a small delay to avoid hitting rate limits
                Thread.sleep(500);
            } catch (Exception e) {
                log.error("Failed to update coordinates for shop {}: {}", shop.getName(), e.getMessage());
            }
        }
        
        log.info("Completed shop coordinates initialization");
    }
} 