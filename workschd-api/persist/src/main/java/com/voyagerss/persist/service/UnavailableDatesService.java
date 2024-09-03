package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.UnavailableDatesDTO;
import com.voyagerss.persist.entity.UnavailableDates;
import com.voyagerss.persist.repository.UnavailableDatesRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UnavailableDatesService {

    @Autowired
    private UnavailableDatesRepository unavailableDatesRepository;

    public Long save(UnavailableDatesVO vO) {
        UnavailableDates bean = new UnavailableDates();
        BeanUtils.copyProperties(vO, bean);
        bean = unavailableDatesRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        unavailableDatesRepository.deleteById(id);
    }

    public void update(Long id, UnavailableDatesUpdateVO vO) {
        UnavailableDates bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        unavailableDatesRepository.save(bean);
    }

    public UnavailableDatesDTO getById(Long id) {
        UnavailableDates original = requireOne(id);
        return toDTO(original);
    }

    public Page<UnavailableDatesDTO> query(UnavailableDatesQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private UnavailableDatesDTO toDTO(UnavailableDates original) {
        UnavailableDatesDTO bean = new UnavailableDatesDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private UnavailableDates requireOne(Long id) {
        return unavailableDatesRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
