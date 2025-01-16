package com.voyagerss.persist.service.impl;

import com.voyagerss.persist.dto.AccountInfoDTO;
import com.voyagerss.persist.entity.account.AccountInfo;
import com.voyagerss.persist.repository.AccountInfoRepository;
import com.voyagerss.persist.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountInfoServiceImpl implements AccountInfoService {

    @Autowired
    private AccountInfoRepository accountInfoRepository;

    @Override
    @Transactional
    public AccountInfoDTO save(AccountInfoDTO accountInfoDTO) {
        AccountInfo accountInfo = mapToEntity(accountInfoDTO);
        accountInfo = accountInfoRepository.save(accountInfo);
        return mapToDTO(accountInfo);
    }

    @Override
    public AccountInfoDTO getById(Long id) {
        return accountInfoRepository.findById(id)
            .map(this::mapToDTO)
            .orElseThrow(() -> new IllegalArgumentException("Account info not found"));
    }

    @Override
    @Transactional
    public AccountInfoDTO update(Long id, AccountInfoDTO accountInfoDTO) {
        AccountInfo accountInfo = accountInfoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Account info not found"));
        updateEntityFromDTO(accountInfo, accountInfoDTO);
        return mapToDTO(accountInfoRepository.save(accountInfo));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        accountInfoRepository.deleteById(id);
    }

    private AccountInfo mapToEntity(AccountInfoDTO dto) {
        // Implement mapping logic
        return new AccountInfo(); // TODO: implement proper mapping
    }

    private AccountInfoDTO mapToDTO(AccountInfo entity) {
        // Implement mapping logic
        return new AccountInfoDTO(); // TODO: implement proper mapping
    }

    private void updateEntityFromDTO(AccountInfo entity, AccountInfoDTO dto) {
        // Implement update logic
    }
} 