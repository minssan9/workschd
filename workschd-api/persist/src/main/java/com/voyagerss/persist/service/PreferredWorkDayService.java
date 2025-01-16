package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.PreferredWorkDayDTO;
import java.util.List;

public interface PreferredWorkDayService {
    PreferredWorkDayDTO save(PreferredWorkDayDTO dto);
    List<PreferredWorkDayDTO> findByAccountInfoId(Long accountInfoId);
    PreferredWorkDayDTO update(Long id, PreferredWorkDayDTO dto);
    void delete(Long id);
} 