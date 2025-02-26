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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.voyagerss.persist.EnumMaster.RoleType.SCHEDULER;
import static com.voyagerss.persist.EnumMaster.RoleType.WORKER;



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

    @Transactional
    public Account save(AccountDTO accountDTO) {
        // Create new Account entity from DTO
        Account account = new Account();
        account.setEmail(accountDTO.getEmail());
        account.setUsername(accountDTO.getUsername());
        account.setStatus(EnumMaster.AccountStatus.ACTIVE); // Set default status as ACTIVE
        account.setCreatedAt(LocalDateTime.now());

        // Save and return the new account
        accountRepository.save(account);

        accountDTO.setRoleType(WORKER);
        addRoleByAccountId(account.getAccountId(), accountDTO);
        return account;
    }

    public AccountDTO update(Integer accountId, AccountDTO accountDTO) {
        Account account = requireOne(accountId);

        account.setEmail(accountDTO.getEmail());
        account.setUsername(accountDTO.getUsername()); 
        account.setPhone(accountDTO.getPhone());

        return toDTO(account);
    }


//    public AccountDTO updateImage(Integer accountId, MultipartFile profileImage)  {
//        Account account = requireOne(accountId);
//
//        String storedFileName = s3Service.uploadFile(
//                profileImage,
//                account.getAccountId().toString() + "_" + account.getEnglishName(),
//                EnumMaster.FileFolder.TEACHER_PROFILE_IMAGES
//        );
//        account.setProfileImageUrl(storedFileName);
//
//        accountRepository.save(account);
//        return toDTO(account);
//    }


    public AccountDTO addRoleByAccountId(Integer accountId, AccountDTO vO) {
        Account account = requireOne(accountId);
        if ( !accountRoleRepository.existsByAccount_AccountIdAndRoleType(accountId, vO.getRoleType())){
            AccountRole accountRole = new AccountRole(vO.getRoleType(), account);
            accountRoleRepository.save(accountRole);
            account.getAccountRoles().add(accountRole);

            if(vO.getRoleType().equals(SCHEDULER)) {
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

    public AccountDTO getAccountDtoByEmail(String email) {
        Account account = accountRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return toDTO(account);
    }

    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

}
