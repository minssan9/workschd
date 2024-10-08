package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.AccountRoleDTO;
import com.voyagerss.persist.entity.AccountRole;
import com.voyagerss.persist.repository.AccountRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountRoleService {

    private final AccountRoleRepository accountRoleRepository;

    public Integer save(AccountRoleDTO vO) {
        AccountRole bean = new AccountRole();
        BeanUtils.copyProperties(vO, bean);
        bean = accountRoleRepository.save(bean);
        return bean.getAccountRoleId();
    }

    public void delete(Integer id) {
        accountRoleRepository.deleteByAccountRoleId(id);
    }

    public void update(Integer id, AccountRoleDTO vO) {
        AccountRole bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        accountRoleRepository.save(bean);
    }

    public AccountRoleDTO getById(Integer id) {
        AccountRole original = requireOne(id);
        return toDTO(original);
    }

    public Page<AccountRoleDTO> query(AccountRoleDTO vO) {
        throw new UnsupportedOperationException();
    }

    private AccountRoleDTO toDTO(AccountRole original) {
        AccountRoleDTO bean = new AccountRoleDTO(original);
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private AccountRole requireOne(Integer id) {
        return accountRoleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }


}
