package com.voyagerss.api.service;

import com.voyagerss.persist.dto.ScheduleDto;
import com.voyagerss.persist.entity.Schedule;
import com.voyagerss.persist.repository.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public ScheduleDto createSchedule(ScheduleDto scheduleDto) {
        Schedule schedule = new Schedule(scheduleDto);
        scheduleRepository.save(schedule);
        return new ScheduleDto(schedule);
    }

    public ScheduleDto updateSchedule(Long id, ScheduleDto scheduleDto) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
        schedule.updateFromDto(scheduleDto);
        scheduleRepository.save(schedule);
        return new ScheduleDto(schedule);
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}