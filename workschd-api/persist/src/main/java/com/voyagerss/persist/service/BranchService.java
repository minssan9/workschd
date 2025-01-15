package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.BranchDTO;
import com.voyagerss.persist.entity.Branch;
import com.voyagerss.persist.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BranchService {

    private final BranchRepository branchRepository;

    @Transactional
    public BranchDTO save(BranchDTO branchDTO) {
        Branch branch = new Branch();
        BeanUtils.copyProperties(branchDTO, branch);
        branch = branchRepository.save(branch);
        BeanUtils.copyProperties(branch, branchDTO);
        return branchDTO;
    }

    public BranchDTO getById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));
        BranchDTO branchDTO = new BranchDTO();
        BeanUtils.copyProperties(branch, branchDTO);
        return branchDTO;
    }

    @Transactional
    public BranchDTO update(Long id, BranchDTO branchDTO) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));
        
        BeanUtils.copyProperties(branchDTO, branch, "id");
        branch = branchRepository.save(branch);
        BeanUtils.copyProperties(branch, branchDTO);
        return branchDTO;
    }

    @Transactional
    public void delete(Long id) {
        branchRepository.deleteById(id);
    }

    public List<BranchDTO> getAll() {
        return branchRepository.findAll().stream()
                .map(branch -> {
                    BranchDTO dto = new BranchDTO();
                    BeanUtils.copyProperties(branch, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }
} 