package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.AccountInfoDTO;

public interface AccountInfoService {
    AccountInfoDTO save(AccountInfoDTO accountInfoDTO);
    AccountInfoDTO getById(Long id);
    AccountInfoDTO update(Long id, AccountInfoDTO accountInfoDTO);
    void delete(Long id);
}
