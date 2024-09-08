package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.AccountInfoDTO;
import com.voyagerss.persist.entity.Account;
import com.voyagerss.persist.entity.AccountInfo;
import com.voyagerss.persist.repository.AccountInfoRepository;
import com.voyagerss.persist.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class AccountInfoService {

    private final AccountRepository accountRepository;
    private final AccountInfoRepository accountInfoRepository;

    public AccountInfoDTO save(AccountInfoDTO vO) {
        AccountInfo accountInfo = requireOne(vO.getAccountId())
                .orElse(new AccountInfo());
        accountInfo.setAccountInfo(vO);

        Account account = accountRepository.getById(vO.getAccountId());
        accountInfo.setAccount(account);

        accountInfo = accountInfoRepository.save(accountInfo);
        return toDTO(accountInfo);
    }

    public void delete(Long id) {
        accountInfoRepository.deleteById(id);
    }


    public AccountInfoDTO getById(Integer id) {
        AccountInfo original = requireOne(id)
                .orElse(new AccountInfo());
//                .orElseThrow(() -> new CommonException(CommonExceptionType.NOTEXIST_ACCOUNT_EXCEPTION_MSG));
        return toDTO(original);
    }

    public Page<AccountInfoDTO> query(AccountInfoDTO vO) {
        throw new UnsupportedOperationException();
    }

    private AccountInfoDTO toDTO(AccountInfo original) {
        AccountInfoDTO bean = new AccountInfoDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    public Optional<AccountInfo> requireOne(Integer id) {
        return accountInfoRepository.findByAccount_AccountId(id);
    }
}
