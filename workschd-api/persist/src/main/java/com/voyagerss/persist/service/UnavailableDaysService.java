package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.UnavailableDaysDTO;
import com.voyagerss.persist.entity.UnavailableDays;
import com.voyagerss.persist.repository.UnavailableDaysRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UnavailableDaysService {

    @Autowired
    private UnavailableDaysRepository unavailableDaysRepository;

    public Long save(UnavailableDaysVO vO) {
        UnavailableDays bean = new UnavailableDays();
        BeanUtils.copyProperties(vO, bean);
        bean = unavailableDaysRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        unavailableDaysRepository.deleteById(id);
    }

    public void update(Long id, UnavailableDaysUpdateVO vO) {
        UnavailableDays bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        unavailableDaysRepository.save(bean);
    }

    public UnavailableDaysDTO getById(Long id) {
        UnavailableDays original = requireOne(id);
        return toDTO(original);
    }

    public Page<UnavailableDaysDTO> query(UnavailableDaysQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private UnavailableDaysDTO toDTO(UnavailableDays original) {
        UnavailableDaysDTO bean = new UnavailableDaysDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private UnavailableDays requireOne(Long id) {
        return unavailableDaysRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
