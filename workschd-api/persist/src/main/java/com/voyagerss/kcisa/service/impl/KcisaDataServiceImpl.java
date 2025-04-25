package com.voyagerss.kcisa.service.impl;

import com.voyagerss.kcisa.dto.KcisaDataDTO;
import com.voyagerss.kcisa.entity.KcisaData;
import com.voyagerss.kcisa.repository.KcisaDataRepository;
import com.voyagerss.kcisa.service.KcisaDataService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class KcisaDataServiceImpl implements KcisaDataService {

    @Autowired
    private KcisaDataRepository kcisaDataRepository;
    
    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Transactional
    public Long save(KcisaDataDTO dataDTO) {
        KcisaData entity = new KcisaData();
        BeanUtils.copyProperties(dataDTO, entity);
        entity = kcisaDataRepository.save(entity);
        return entity.getId();
    }

    @Override
    @Transactional
    public void saveAll(List<KcisaDataDTO> dataDTOList) {
        List<KcisaData> entities = dataDTOList.stream()
                .map(dto -> {
                    KcisaData entity = new KcisaData();
                    BeanUtils.copyProperties(dto, entity);
                    return entity;
                })
                .collect(Collectors.toList());
        
        kcisaDataRepository.saveAll(entities);
    }

    @Override
    @Transactional
    public void update(Long id, KcisaDataDTO dataDTO) {
        KcisaData entity = requireOne(id);
        BeanUtils.copyProperties(dataDTO, entity);
        kcisaDataRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        kcisaDataRepository.deleteById(id);
    }

    @Override
    public KcisaDataDTO getById(Long id) {
        KcisaData entity = requireOne(id);
        return toDTO(entity);
    }

    @Override
    public Page<KcisaDataDTO> getAll(Pageable pageable) {
        return kcisaDataRepository.findAll(pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional
    public List<KcisaDataDTO> fetchAndSaveKcisaData() {
        List<KcisaDataDTO> savedData = new ArrayList<>();
        
        try {
            // 문화정보원 API 호출
            String apiUrl = "http://api.kcisa.kr/openapi/CNV_060/request?serviceKey=8602e1b8-baf0-47be-b9b7-4a76a8511dcc&numOfRows=100&pageNo=1&dtype=%EC%A0%84%EC%8B%9C";
            
            ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);
            Map<String, Object> responseData = response.getBody();
            
            if (responseData != null && responseData.containsKey("response")) {
                Map<String, Object> responseMap = (Map<String, Object>) responseData.get("response");
                Map<String, Object> body = (Map<String, Object>) responseMap.get("body");
                Map<String, Object> items = (Map<String, Object>) body.get("items");
                List<Map<String, Object>> itemList = (List<Map<String, Object>>) items.get("item");
                
                for (Map<String, Object> item : itemList) {
                    String apiId = item.get("seq").toString();
                    
                    // 이미 저장된 데이터인지 확인
                    if (!kcisaDataRepository.existsByApiId(apiId)) {
                        KcisaDataDTO dataDTO = new KcisaDataDTO(
                                                "기본값", // 또는 다른 적절한 기본값
                                                item.get("title") != null ? item.get("title").toString() : "제목 없음",
                                                item.get("description") != null ? item.get("description").toString() : null,
                                                item.get("performanceType") != null ? item.get("performanceType").toString() : null,
                                                item.get("eventDate") != null ? item.get("eventDate").toString() : null,
                                                item.get("venue") != null ? item.get("venue").toString() : null,
                                                apiId,
                                                "기본값", // 또는 다른 적절한 기본값
                                                "기본값" // 또는 다른 적절한 기본값
                                        );

                        
                        Long savedId = save(dataDTO);
                        savedData.add(getById(savedId));
                    }
                }
            }
        } catch (Exception e) {
            // 로깅 추가
            e.printStackTrace();
        }
        
        return savedData;
    }

    private KcisaDataDTO toDTO(KcisaData entity) {
        return new KcisaDataDTO(entity        );
    }

    private KcisaData requireOne(Long id) {
        return kcisaDataRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("리소스를 찾을 수 없습니다: " + id));
    }
}
