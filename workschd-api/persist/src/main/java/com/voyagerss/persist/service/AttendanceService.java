package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.AttendanceDTO;
import com.voyagerss.persist.entity.Attendance;
import com.voyagerss.persist.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public Long save(AttendanceDTO vO) {
        Attendance bean = new Attendance();
        BeanUtils.copyProperties(vO, bean);
        bean = attendanceRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        attendanceRepository.deleteById(id);
    }

    public void update(Long id, AttendanceDTO vO) {
        Attendance bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        attendanceRepository.save(bean);
    }

    public AttendanceDTO getById(Long id) {
        Attendance original = requireOne(id);
        return toDTO(original);
    }

    public Page<AttendanceDTO> query(AttendanceDTO vO) {
        throw new UnsupportedOperationException();
    }

    private AttendanceDTO toDTO(Attendance original) {
        AttendanceDTO bean = new AttendanceDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private Attendance requireOne(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }

}
