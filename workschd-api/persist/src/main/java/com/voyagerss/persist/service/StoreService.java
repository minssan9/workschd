package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.StoreDTO;
import com.voyagerss.persist.entity.Store;
import com.voyagerss.persist.repository.StoreRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    public Long save(StoreVO vO) {
        Store bean = new Store();
        BeanUtils.copyProperties(vO, bean);
        bean = storeRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        storeRepository.deleteById(id);
    }

    public void update(Long id, StoreUpdateVO vO) {
        Store bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        storeRepository.save(bean);
    }

    public StoreDTO getById(Long id) {
        Store original = requireOne(id);
        return toDTO(original);
    }

    public Page<StoreDTO> query(StoreQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private StoreDTO toDTO(Store original) {
        StoreDTO bean = new StoreDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private Store requireOne(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
