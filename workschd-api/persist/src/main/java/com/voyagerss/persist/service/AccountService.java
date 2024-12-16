package com.voyagerss.persist.service;

import com.voyagerss.persist.EnumMaster;
import com.voyagerss.persist.dto.AccountDTO;
import com.voyagerss.persist.dto.QueryDTO;
import com.voyagerss.persist.entity.Account;
import com.voyagerss.persist.entity.AccountRole;
import com.voyagerss.persist.entity.AccountSns;
import com.voyagerss.persist.querydsl.AccountRepositorySupport;
import com.voyagerss.persist.repository.AccountInfoRepository;
import com.voyagerss.persist.repository.AccountRepository;
import com.voyagerss.persist.repository.AccountRoleRepository;
import com.voyagerss.persist.repository.AccountSnsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.voyagerss.persist.EnumMaster.RoleType.TEACHER;


@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final AccountSnsRepository accountSnsRepository;
    private final AccountInfoRepository accountInfoRepository;
    private final AccountRepositorySupport accountRepositorySupport;

    public Account signin(AccountDTO vO) {
        Account account = new Account(vO);
        return accountRepository.save(account);
    }

    public Account save(AccountDTO accountDTO) {
        boolean isNew = false;
        Account account = accountRepository.findByEmail(accountDTO.getEmail())
                .orElseGet(() -> new Account(accountDTO));
        if(account.getAccountId() == null) isNew = true;
        accountRepository.saveAndFlush(account);

        if (isNew){
            AccountRole accountRole = new AccountRole(EnumMaster.RoleType.STUDENT, account);
            accountRoleRepository.saveAndFlush(accountRole);

            AccountSns accountSns = new AccountSns(
                    accountDTO.getUsername(),
                    accountDTO.getPhone(),
                    accountDTO.getEmail(),
                    "Y",
                    accountDTO.getProfileImageUrl(),
                    accountDTO.getProviderType(),
                    "",
                    "",
                    account);
            accountSnsRepository.saveAndFlush(accountSns);

            HashMap<String, String> kakaoVariables = new HashMap<>();
            kakaoVariables.put("#{username}",       account.getUsername());

        }
        return account;
    }

    public AccountDTO update(Integer accountId, AccountDTO accountDTO) {
        Account account = requireOne(accountId);

        account.setEmail(accountDTO.getEmail());
        account.setUsername(accountDTO.getUsername()); 
        account.setPhone(accountDTO.getPhone());

        return toDTO(account);
    }


    public AccountDTO addRoleByAccountId(Integer accountId, AccountDTO vO) {
        Account account = requireOne(accountId);
        if ( !accountRoleRepository.existsByAccount_AccountIdAndRoleType(accountId, vO.getRoleType())){
            AccountRole accountRole = new AccountRole(vO.getRoleType(), account);
            accountRoleRepository.save(accountRole);
            account.getAccountRoles().add(accountRole);

            if(vO.getRoleType().equals(TEACHER)) {
//                deleteRoleByAccountRoleId(accountId, EN9DOOR_STUDENT);
//                MessageDTO messageDTO = MessageDTO.builder()
//                        .templateCode(TEACHER_WELCOME)
//                        .receiverDTO(account.toDto())
//                        .build();
//
//                messageServiceEmailImpl.sendMessageToOne(messageDTO);
            }
        }
        return toDTO(account);
    }

    public Account addNewAccountSns(Integer accountId, AccountSns accountSns) {
        Account account = requireOne(accountId);
        if ( !accountSnsRepository.existsByAccount_AccountIdAndProviderType(accountId, accountSns.getProviderType())){
            accountSnsRepository.saveAndFlush(accountSns);
            account.getAccountSnsList().add(accountSns);
        }
        return account;
    }


    public AccountDTO deleteRoleByAccountRole(Integer accountId, EnumMaster.RoleType roleType ) {
        AccountRole accountRole = accountRoleRepository.findByAccount_AccountIdAndRoleType(accountId, roleType);
        accountRoleRepository.deleteByAccountRoleId(accountRole.getAccountRoleId());
        accountRoleRepository.deleteByAccount_AccountIdAndRoleType(accountId, roleType);
        return getAccountDtoById(accountId);
    }


    public AccountDTO getAccountDtoById(Integer id) {
        Account original = requireOne(id);
        return toDTO(original);
    }

    public Account getAccountById(Integer id) {
        return requireOne(id);

    }

    private AccountDTO toDTO(Account original) {
        AccountDTO dto = new AccountDTO(original);
        BeanUtils.copyProperties(original, dto);
        return dto;
    }

    public Account requireOne(Integer id) {
        return accountRepository.findById(id).orElseThrow();
    }


    public Page<AccountDTO> getAccountDtoPage(QueryDTO queryDTO) {
        return accountRepositorySupport.getAccountDtoPage(queryDTO) ;
    }

    public List<AccountDTO> getAccountDtoList(QueryDTO queryDTO) {
        return accountRepositorySupport.getAccountDtoList(queryDTO).stream()
                .distinct()
                .collect(Collectors.toList());
    }

}
