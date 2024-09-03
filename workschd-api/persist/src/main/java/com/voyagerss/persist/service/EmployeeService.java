package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.EmployeeDTO;
import com.voyagerss.persist.entity.Employee;
import com.voyagerss.persist.repository.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Long save(EmployeeVO vO) {
        Employee bean = new Employee();
        BeanUtils.copyProperties(vO, bean);
        bean = employeeRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    public void update(Long id, EmployeeUpdateVO vO) {
        Employee bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        employeeRepository.save(bean);
    }

    public EmployeeDTO getById(Long id) {
        Employee original = requireOne(id);
        return toDTO(original);
    }

    public Page<EmployeeDTO> query(EmployeeQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private EmployeeDTO toDTO(Employee original) {
        EmployeeDTO bean = new EmployeeDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private Employee requireOne(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
