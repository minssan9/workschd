package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.BranchDTO;
import com.voyagerss.persist.entity.Branch;
import com.voyagerss.persist.repository.BranchRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    public Long save(BranchVO vO) {
        Branch bean = new Branch();
        BeanUtils.copyProperties(vO, bean);
        bean = branchRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        branchRepository.deleteById(id);
    }

    public void update(Long id, BranchUpdateVO vO) {
        Branch bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        branchRepository.save(bean);
    }

    public BranchDTO getById(Long id) {
        Branch original = requireOne(id);
        return toDTO(original);
    }

    public Page<BranchDTO> query(BranchQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private BranchDTO toDTO(Branch original) {
        BranchDTO bean = new BranchDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private Branch requireOne(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
