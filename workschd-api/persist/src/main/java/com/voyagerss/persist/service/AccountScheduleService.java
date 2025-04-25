package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.AccountWorkHourDTO;
import com.voyagerss.persist.dto.AccountWorkOffDatesDTO;
import com.voyagerss.persist.entity.AccountInfo;
import com.voyagerss.persist.entity.AccountWorkHour;
import com.voyagerss.persist.entity.AccountWorkOffDates;
import com.voyagerss.persist.repository.AccountInfoRepository;
import com.voyagerss.persist.repository.AccountWorkHourRepository;
import com.voyagerss.persist.repository.AccountWorkOffDatesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AccountScheduleService {

    private final AccountInfoRepository accountInfoRepository;
    private final AccountWorkOffDatesRepository accountWorkOffDatesRepository;
    private final AccountWorkHourRepository accountWorkHourRepository;

    public Long save(AccountWorkOffDatesDTO vO) {
        AccountWorkOffDates bean = new AccountWorkOffDates();
        BeanUtils.copyProperties(vO, bean);
        bean = accountWorkOffDatesRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        accountWorkOffDatesRepository.deleteById(id);
    }


    public Page<AccountWorkOffDatesDTO> query(AccountWorkOffDatesDTO vO) {
        throw new UnsupportedOperationException();
    }

    public List<AccountWorkOffDatesDTO> getOffDatesByAccountId(Integer id) {
        return  requireOffDatesByAccountId(id).stream()
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
        AccountInfo accountInfo = accountInfoRepository.findByAccount_AccountId(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        return accountWorkOffDatesRepository.findByAccountInfoId(accountInfo.getId());
    }

    public AccountWorkOffDatesDTO getOffDatesById(@Valid @NotNull Long id) {
        return accountWorkOffDatesRepository.findById(id)
                .map(this::toOffDatesDTO)
                .orElseThrow(() -> new NoSuchElementException("No schedule found with id: " + id));
    }
    public void update(@Valid @NotNull Long id, @Valid AccountWorkOffDatesDTO vO) {
        AccountWorkOffDates accountWorkOffDates = accountWorkOffDatesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
        BeanUtils.copyProperties(vO, accountWorkOffDates);
        accountWorkOffDatesRepository.save(accountWorkOffDates);
    }





    public AccountWorkHourDTO create(AccountWorkHourDTO accountWorkHourDTO) {
        AccountWorkHour accountWorkHour = new AccountWorkHour(accountWorkHourDTO);
        AccountInfo accountInfo = accountInfoRepository.findByAccount_AccountId(accountWorkHourDTO.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        accountWorkHour.setAccountInfo(accountInfo);
        accountWorkHourRepository.save(accountWorkHour);
        return new AccountWorkHourDTO(accountWorkHour);
    }

    public AccountWorkHourDTO update(Long id, AccountWorkHourDTO accountWorkHourDTO) {
        AccountWorkHour accountWorkHour = accountWorkHourRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
        AccountInfo accountInfo = accountInfoRepository.findByAccount_AccountId(accountWorkHourDTO.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        accountWorkHour.setAccountInfo(accountInfo);
        accountWorkHourRepository.save(accountWorkHour);
        return new AccountWorkHourDTO(accountWorkHour);
    }


    public List<AccountWorkHourDTO> getWorkHourByAccountId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
        AccountInfo accountInfo = accountInfoRepository.findByAccount_AccountId(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        return requireWorkHourByAccountId(accountInfo.getId()).stream()
                .map(this::toWorkHourDTO)
                .toList();
    }
    private AccountWorkHourDTO toWorkHourDTO(AccountWorkHour accountWorkHour) {
        AccountWorkHourDTO accountWorkHourDTO = new AccountWorkHourDTO();
        BeanUtils.copyProperties(accountWorkHour, accountWorkHourDTO);
        return accountWorkHourDTO;
    }

    private List<AccountWorkHour> requireWorkHourByAccountId(Long id) {
        return accountWorkHourRepository.findByAccountInfoId(id);
    }

    public void deleteSchedule(Long id) {
        accountWorkHourRepository.deleteById(id);
    }

}
