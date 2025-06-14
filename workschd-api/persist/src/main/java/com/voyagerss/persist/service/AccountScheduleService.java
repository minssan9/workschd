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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

public interface AccountScheduleService {
    // Work Off Dates
    Long saveOrUpdateOffDate(Long accountId, AccountWorkOffDatesDTO dto);
    List<Long> saveOrUpdateOffDates(Long accountId, List<AccountWorkOffDatesDTO> dtos);
    void deleteOffDate(Long accountId, Long id);
    AccountWorkOffDatesDTO getOffDatesById(Long accountId, @Valid @NotNull Long id);
    List<AccountWorkOffDatesDTO> getOffDatesByDateRange(Long accountId, LocalDate startDate, LocalDate endDate);
    Page<AccountWorkOffDatesDTO> queryOffDates(Long accountId, AccountWorkOffDatesDTO dto);
    List<AccountWorkOffDatesDTO> getOffDatesByAccountId(Integer id);

    // Work Hours
    Long saveOrUpdateWorkHour(Long accountId, AccountWorkHourDTO dto);
    List<Long> saveOrUpdateWorkHours(Long accountId, List<AccountWorkHourDTO> dtos);
    void deleteWorkHour(Long accountId, Long id);
    AccountWorkHourDTO getWorkHourById(Long accountId, Long id);
    List<AccountWorkHourDTO> getWorkHoursByDateRange(Long accountId, LocalDate startDate, LocalDate endDate);
    List<AccountWorkHourDTO> getWorkHoursByDay(Long accountId, String day);
    Page<AccountWorkHourDTO> queryWorkHours(Long accountId, AccountWorkHourDTO dto);
    List<AccountWorkHourDTO> getWorkHourByAccountId(Integer id);
    void deleteSchedule(Long id);
}
