package com.voyagerss.persist.service.impl;

import com.voyagerss.persist.dto.PreferredWorkDayDTO;
import com.voyagerss.persist.entity.PreferredWorkDay;
import com.voyagerss.persist.repository.PreferredWorkDayRepository;
import com.voyagerss.persist.service.PreferredWorkDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PreferredWorkDayServiceImpl implements PreferredWorkDayService {

    @Autowired
    private PreferredWorkDayRepository preferredWorkDayRepository;

    @Override
    @Transactional
    public PreferredWorkDayDTO save(PreferredWorkDayDTO dto) {
        PreferredWorkDay preferredWorkDay = mapToEntity(dto);
        preferredWorkDay = preferredWorkDayRepository.save(preferredWorkDay);
        return mapToDTO(preferredWorkDay);
    }

    @Override
    public List<PreferredWorkDayDTO> findByAccountInfoId(Long accountInfoId) {
        return preferredWorkDayRepository.findByAccountInfoId(accountInfoId).stream()
            .map(this::mapToDTO)
            .toList();
    }

    @Override
    @Transactional
    public PreferredWorkDayDTO update(Long id, PreferredWorkDayDTO dto) {
        PreferredWorkDay preferredWorkDay = preferredWorkDayRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Preferred work day not found"));
        updateEntityFromDTO(preferredWorkDay, dto);
        return mapToDTO(preferredWorkDayRepository.save(preferredWorkDay));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        preferredWorkDayRepository.deleteById(id);
    }

    private PreferredWorkDay mapToEntity(PreferredWorkDayDTO dto) {
        PreferredWorkDay entity = new PreferredWorkDay();
        entity.setId(dto.getId());
//        entity.setDayOfWeek(dto.dayOfWeek());
//        entity.setPreferred(dto.preferred());
        // Set accountInfo using repository
        return entity;
    }

    private PreferredWorkDayDTO mapToDTO(PreferredWorkDay entity) {
        PreferredWorkDayDTO dto = new PreferredWorkDayDTO();

            dto.setId (entity.getId());
            dto.setDayOfWeek (entity.getDayOfWeek());
//            dto.setAccountInfo (entity.getAccountInfo().getId());
        return dto;
    }

    private void updateEntityFromDTO(PreferredWorkDay entity, PreferredWorkDayDTO dto) {
//        entity.setDayOfWeek(dto.dayOfWeek());
//        entity.setPreferred(dto.preferred());
    }
} 