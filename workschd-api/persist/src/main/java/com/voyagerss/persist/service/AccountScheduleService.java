package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.AccountWorkHourDTO;
import com.voyagerss.persist.dto.AccountWorkOffDatesDTO;
import com.voyagerss.persist.entity.Account;
import com.voyagerss.persist.entity.AccountWorkHour;
import com.voyagerss.persist.entity.AccountWorkOffDates;
import com.voyagerss.persist.repository.AccountRepository;
import com.voyagerss.persist.repository.AccountWorkHourRepository;
import com.voyagerss.persist.repository.AccountWorkOffDatesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AccountScheduleService {

    private final AccountRepository accountRepository;
    private final AccountWorkOffDatesRepository accountWorkOffDatesRepository;
    private final AccountWorkHourRepository accountWorkHourRepository;

    public Long save(Long accountId, AccountWorkOffDatesDTO vO) {
        AccountWorkOffDates accountWorkOffDates = new AccountWorkOffDates();
        BeanUtils.copyProperties(vO, accountWorkOffDates);
        
        Account account = getAccountById(accountId.intValue());
        accountWorkOffDates.setAccount(account);
        
        accountWorkOffDates = accountWorkOffDatesRepository.save(accountWorkOffDates);
        return accountWorkOffDates.getId();
    }

    public void delete(Long accountId, Long id) {
        // Verify the schedule belongs to the account before deletion
        Account account = getAccountById(accountId.intValue());
        AccountWorkOffDates workOffDates = accountWorkOffDatesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
        
        if (!workOffDates.getAccount().getAccountId().equals(account.getAccountId())) {
            throw new EntityNotFoundException("Schedule not found for this account");
        }
        
        accountWorkOffDatesRepository.deleteById(id);
    }

    public Page<AccountWorkOffDatesDTO> query(Long accountId, AccountWorkOffDatesDTO vO) {
        // Get account info first
        Account account = getAccountById(accountId.intValue());
        
        // Find all work off dates for this account
        List<AccountWorkOffDates> offDates = accountWorkOffDatesRepository.findByAccountAccountId(account.getAccountId());
        
        // Convert to DTOs
        List<AccountWorkOffDatesDTO> dtos = offDates.stream()
                .map(this::toOffDatesDTO)
                .toList();
                
        // Create a Page object (simple implementation)
        return new PageImpl<>(dtos);
    }

    public List<AccountWorkOffDatesDTO> getOffDatesByAccountId(Integer id) {
        return requireOffDatesByAccountId(id).stream()
                .map(this::toOffDatesDTO)
                .toList();
    }
    
    private AccountWorkOffDatesDTO toOffDatesDTO(AccountWorkOffDates original) {
        AccountWorkOffDatesDTO bean = new AccountWorkOffDatesDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private List<AccountWorkOffDates> requireOffDatesByAccountId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        return accountWorkOffDatesRepository.findByAccountAccountId(account.getAccountId());
    }
    
    private Account getAccountById(Integer accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }

    public AccountWorkOffDatesDTO getOffDatesById(Long accountId, @Valid @NotNull Long id) {
        // Verify the schedule belongs to the account
        Account account = getAccountById(accountId.intValue());
        AccountWorkOffDates workOffDates = accountWorkOffDatesRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No schedule found with id: " + id));
        
        if (!workOffDates.getAccount().getAccountId().equals(account.getAccountId())) {
            throw new EntityNotFoundException("Schedule not found for this account");
        }
        
        return toOffDatesDTO(workOffDates);
    }
    
    public void update(Long accountId, @Valid @NotNull Long id, @Valid AccountWorkOffDatesDTO vO) {
        // Verify the schedule belongs to the account
        Account account = getAccountById(accountId.intValue());
        AccountWorkOffDates accountWorkOffDates = accountWorkOffDatesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
        
        if (!accountWorkOffDates.getAccount().getAccountId().equals(account.getAccountId())) {
            throw new EntityNotFoundException("Schedule not found for this account");
        }
        
        BeanUtils.copyProperties(vO, accountWorkOffDates);
        accountWorkOffDatesRepository.save(accountWorkOffDates);
    }

    public AccountWorkHourDTO create(AccountWorkHourDTO accountWorkHourDTO) {
        AccountWorkHour accountWorkHour = new AccountWorkHour(accountWorkHourDTO);
        Account account = accountRepository.findById(accountWorkHourDTO.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        accountWorkHour.setAccount(account);
        accountWorkHourRepository.save(accountWorkHour);
        return new AccountWorkHourDTO(accountWorkHour);
    }

    public AccountWorkHourDTO update(Long id, AccountWorkHourDTO accountWorkHourDTO) {
        AccountWorkHour accountWorkHour = accountWorkHourRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
        Account account = accountRepository.findById(accountWorkHourDTO.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        accountWorkHour.setAccount(account);
        accountWorkHourRepository.save(accountWorkHour);
        return new AccountWorkHourDTO(accountWorkHour);
    }

    public List<AccountWorkHourDTO> getWorkHourByAccountId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        return requireWorkHourByAccountId(account.getAccountId()).stream()
                .map(this::toWorkHourDTO)
                .toList();
    }
    
    private AccountWorkHourDTO toWorkHourDTO(AccountWorkHour accountWorkHour) {
        AccountWorkHourDTO accountWorkHourDTO = new AccountWorkHourDTO();
        BeanUtils.copyProperties(accountWorkHour, accountWorkHourDTO);
        return accountWorkHourDTO;
    }

    private List<AccountWorkHour> requireWorkHourByAccountId(Integer id) {
        return accountWorkHourRepository.findByAccountAccountId(id);
    }

    public void deleteSchedule(Long id) {
        accountWorkHourRepository.deleteById(id);
    }
}
