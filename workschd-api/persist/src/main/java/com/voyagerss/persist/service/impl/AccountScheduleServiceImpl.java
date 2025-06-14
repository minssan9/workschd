package com.voyagerss.persist.service.impl;

import com.voyagerss.persist.dto.AccountWorkHourDTO;
import com.voyagerss.persist.dto.AccountWorkOffDatesDTO;
import com.voyagerss.persist.entity.Account;
import com.voyagerss.persist.entity.AccountWorkHour;
import com.voyagerss.persist.entity.AccountWorkOffDates;
import com.voyagerss.persist.repository.AccountRepository;
import com.voyagerss.persist.repository.AccountWorkHourRepository;
import com.voyagerss.persist.repository.AccountWorkOffDatesRepository;
import com.voyagerss.persist.service.AccountScheduleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountScheduleServiceImpl implements AccountScheduleService {

    private final AccountRepository accountRepository;
    private final AccountWorkOffDatesRepository workOffDatesRepository;
    private final AccountWorkHourRepository workHourRepository;

    // Work Off Dates Implementation
    @Override
    @Transactional
    public Long saveOrUpdateOffDate(Long accountId, AccountWorkOffDatesDTO dto) {
        Account account = accountRepository.findById(accountId.intValue())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        AccountWorkOffDates workOffDates;
        if (dto.getId() != null) {
            workOffDates = workOffDatesRepository.findByAccountIdAndId(accountId, dto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Work off date not found"));
            workOffDates.setOffDate(dto.getOffDateAsLocalDate());
        } else {
            workOffDates = new AccountWorkOffDates();
            workOffDates.setAccount(account);
            workOffDates.setOffDate(dto.getOffDate());
        }

        return workOffDatesRepository.save(workOffDates).getId();
    }

    @Override
    @Transactional
    public void deleteOffDate(Long accountId, Long id) {
        AccountWorkOffDates workOffDates = workOffDatesRepository.findByAccountIdAndId(accountId, id)
                .orElseThrow(() -> new EntityNotFoundException("Work off date not found"));
        workOffDatesRepository.delete(workOffDates);
    }

    @Override
    public AccountWorkOffDatesDTO getOffDatesById(Long accountId, @Valid @NotNull Long id) {
        AccountWorkOffDates workOffDates = workOffDatesRepository.findByAccountIdAndId(accountId, id)
                .orElseThrow(() -> new EntityNotFoundException("Work off date not found"));
        return new AccountWorkOffDatesDTO(workOffDates);
    }

    @Override
    public List<AccountWorkOffDatesDTO> getOffDatesByDateRange(Long accountId, LocalDate startDate, LocalDate endDate) {
        return workOffDatesRepository.findByAccountIdAndDateRange(accountId, startDate, endDate)
                .stream()
                .map(AccountWorkOffDatesDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AccountWorkOffDatesDTO> queryOffDates(Long accountId, AccountWorkOffDatesDTO dto) {
        List<AccountWorkOffDates> workOffDates = workOffDatesRepository.findAllByAccountId(accountId);
        List<AccountWorkOffDatesDTO> dtos = workOffDates.stream()
                .map(AccountWorkOffDatesDTO::new)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos);
    }

    @Override
    public List<AccountWorkOffDatesDTO> getOffDatesByAccountId(Integer id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        return workOffDatesRepository.findByAccountAccountId(id).stream()
                .map(AccountWorkOffDatesDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Long> saveOrUpdateOffDates(Long accountId, List<AccountWorkOffDatesDTO> dtos) {
        Account account = accountRepository.findById(accountId.intValue())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        return dtos.stream()
                .map(dto -> {
                    AccountWorkOffDates workOffDates;
                    if (dto.getId() != null) {
                        workOffDates = workOffDatesRepository.findByAccountIdAndId(accountId, dto.getId())
                                .orElseThrow(() -> new EntityNotFoundException("Work off date not found"));
                        workOffDates.setOffDate(dto.getOffDate());
                    } else {
                        workOffDates = new AccountWorkOffDates();
                        workOffDates.setAccount(account);
                        workOffDates.setOffDate(dto.getOffDate());
                    }
                    return workOffDatesRepository.save(workOffDates).getId();
                })
                .collect(Collectors.toList());
    }

    // Work Hours Implementation
    @Override
    @Transactional
    public Long saveOrUpdateWorkHour(Long accountId, AccountWorkHourDTO dto) {
        Account account = accountRepository.findById(accountId.intValue())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        AccountWorkHour workHour;
        if (dto.getId() != null) {
            workHour = workHourRepository.findByAccountIdAndId(accountId, dto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Work hour not found"));
            workHour.updateFromDto(dto);
        } else {
            workHour = new AccountWorkHour(dto);
            workHour.setAccount(account);
        }

        return workHourRepository.save(workHour).getId();
    }

    @Override
    @Transactional
    public void deleteWorkHour(Long accountId, Long id) {
        AccountWorkHour workHour = workHourRepository.findByAccountIdAndId(accountId, id)
                .orElseThrow(() -> new EntityNotFoundException("Work hour not found"));
        workHourRepository.delete(workHour);
    }

    @Override
    public AccountWorkHourDTO getWorkHourById(Long accountId, Long id) {
        AccountWorkHour workHour = workHourRepository.findByAccountIdAndId(accountId, id)
                .orElseThrow(() -> new EntityNotFoundException("Work hour not found"));
        return new AccountWorkHourDTO(workHour);
    }

    @Override
    public List<AccountWorkHourDTO> getWorkHoursByDateRange(Long accountId, LocalDate startDate, LocalDate endDate) {
        return workHourRepository.findByAccountIdAndDateRange(accountId, startDate, endDate)
                .stream()
                .map(AccountWorkHourDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountWorkHourDTO> getWorkHoursByDay(Long accountId, String day) {
        return workHourRepository.findByAccountIdAndDay(accountId, day)
                .stream()
                .map(AccountWorkHourDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AccountWorkHourDTO> queryWorkHours(Long accountId, AccountWorkHourDTO dto) {
        List<AccountWorkHour> workHours = workHourRepository.findAllByAccountId(accountId);
        List<AccountWorkHourDTO> dtos = workHours.stream()
                .map(AccountWorkHourDTO::new)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos);
    }

    @Override
    public List<AccountWorkHourDTO> getWorkHourByAccountId(Integer id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        return workHourRepository.findByAccountAccountId(id).stream()
                .map(AccountWorkHourDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSchedule(Long id) {
        workHourRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<Long> saveOrUpdateWorkHours(Long accountId, List<AccountWorkHourDTO> dtos) {
        Account account = accountRepository.findById(accountId.intValue())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        return dtos.stream()
                .map(dto -> {
                    AccountWorkHour workHour;
                    if (dto.getId() != null) {
                        workHour = workHourRepository.findByAccountIdAndId(accountId, dto.getId())
                                .orElseThrow(() -> new EntityNotFoundException("Work hour not found"));
                        workHour.updateFromDto(dto);
                    } else {
                        workHour = new AccountWorkHour(dto);
                        workHour.setAccount(account);
                    }
                    return workHourRepository.save(workHour).getId();
                })
                .collect(Collectors.toList());
    }
} 