package com.voyagerss.kcisa;

import com.voyagerss.kcisa.dto.KcisaDataDTO;
import com.voyagerss.kcisa.service.KcisaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class Scheduler {

    @Autowired
    private KcisaDataService kcisaDataService;
    
    /**
     * 매일 자정에 한국문화정보원 API 데이터를 가져와 저장합니다.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void fetchAndSaveKcisaData() {
        List<KcisaDataDTO> savedData = kcisaDataService.fetchAndSaveKcisaData();
        System.out.println("한국문화정보원 API에서 " + savedData.size() + "개의 새로운 데이터를 저장했습니다.");
    }
    
    /**
     * 어플리케이션 시작 시 한 번 실행됩니다.
     */
    @Scheduled(initialDelay = 10000, fixedDelay = Long.MAX_VALUE)
    public void fetchAndSaveKcisaDataOnStartup() {
        List<KcisaDataDTO> savedData = kcisaDataService.fetchAndSaveKcisaData();
        System.out.println("어플리케이션 시작 시 한국문화정보원 API에서 " + savedData.size() + "개의 새로운 데이터를 저장했습니다.");
    }
}
