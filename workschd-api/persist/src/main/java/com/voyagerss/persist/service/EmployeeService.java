package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.EmployeeDTO;
import com.voyagerss.persist.entity.AccountInfo;
import com.voyagerss.persist.repository.AccountInfoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmployeeService {

    @Autowired
    private AccountInfoRepository accountInfoRepository;

    public Long save(EmployeeDTO vO) {
        AccountInfo bean = new AccountInfo();
        BeanUtils.copyProperties(vO, bean);
        bean = accountInfoRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        accountInfoRepository.deleteById(id);
    }

    public void update(Long id, EmployeeDTO vO) {
        AccountInfo bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        accountInfoRepository.save(bean);
    }

    public EmployeeDTO getById(Long id) {
        AccountInfo original = requireOne(id);
        return toDTO(original);
    }

    public Page<EmployeeDTO> query(EmployeeDTO vO) {
        throw new UnsupportedOperationException();
    }

    private EmployeeDTO toDTO(AccountInfo original) {
        EmployeeDTO bean = new EmployeeDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private AccountInfo requireOne(Long id) {
        return accountInfoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }

    public List<AccountInfo> getEmployeesByBranch(Long branchId) {
        return accountInfoRepository.getEmployeesByBranchId(branchId);
    }
}
