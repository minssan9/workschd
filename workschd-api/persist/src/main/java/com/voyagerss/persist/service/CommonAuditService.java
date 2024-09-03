package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.CommonAuditDTO;
import com.voyagerss.persist.entity.CommonAudit;
import com.voyagerss.persist.repository.CommonAuditRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CommonAuditService {

    @Autowired
    private CommonAuditRepository commonAuditRepository;

    public Long save(CommonAuditVO vO) {
        CommonAudit bean = new CommonAudit();
        BeanUtils.copyProperties(vO, bean);
        bean = commonAuditRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        commonAuditRepository.deleteById(id);
    }

    public void update(Long id, CommonAuditUpdateVO vO) {
        CommonAudit bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        commonAuditRepository.save(bean);
    }

    public CommonAuditDTO getById(Long id) {
        CommonAudit original = requireOne(id);
        return toDTO(original);
    }

    public Page<CommonAuditDTO> query(CommonAuditQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private CommonAuditDTO toDTO(CommonAudit original) {
        CommonAuditDTO bean = new CommonAuditDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private CommonAudit requireOne(Long id) {
        return commonAuditRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
