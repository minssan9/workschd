package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.UnavailableDatesDTO;
import com.voyagerss.persist.entity.EmployeeOffDates;
import com.voyagerss.persist.repository.EmployeeOffDatesRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class EmployeeOffDatesService {

    @Autowired
    private EmployeeOffDatesRepository employeeOffDatesRepository;

    public Long save(UnavailableDatesDTO vO) {
        EmployeeOffDates bean = new EmployeeOffDates();
        BeanUtils.copyProperties(vO, bean);
        bean = employeeOffDatesRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        employeeOffDatesRepository.deleteById(id);
    }

    public void update(Long id, UnavailableDatesDTO vO) {
        EmployeeOffDates bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        employeeOffDatesRepository.save(bean);
    }

    public UnavailableDatesDTO getById(Long id) {
        EmployeeOffDates original = requireOne(id);
        return toDTO(original);
    }

    public Page<UnavailableDatesDTO> query(UnavailableDatesDTO vO) {
        throw new UnsupportedOperationException();
    }

    private UnavailableDatesDTO toDTO(EmployeeOffDates original) {
        UnavailableDatesDTO bean = new UnavailableDatesDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private EmployeeOffDates requireOne(Long id) {
        return employeeOffDatesRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
