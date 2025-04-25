package com.voyagerss.kcisa.service;

import com.voyagerss.kcisa.dto.KcisaDataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KcisaDataService {
    
    Long save(KcisaDataDTO dataDTO);
    
    void saveAll(List<KcisaDataDTO> dataDTOList);
    
    void update(Long id, KcisaDataDTO dataDTO);
    
    void delete(Long id);
    
    KcisaDataDTO getById(Long id);
    
    Page<KcisaDataDTO> getAll(Pageable pageable);
    
    List<KcisaDataDTO> fetchAndSaveKcisaData();
}
