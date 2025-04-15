package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.TaskEmployeeDTO;
import com.voyagerss.persist.entity.Task;
import com.voyagerss.persist.entity.TaskEmployee;
import com.voyagerss.persist.repository.TaskEmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TaskEmployeeService {

    @Autowired
    private TaskEmployeeRepository taskEmployeeRepository;

    public Task save(TaskEmployeeDTO vO) {
        TaskEmployee bean = new TaskEmployee();
        BeanUtils.copyProperties(vO, bean);
        bean = taskEmployeeRepository.save(bean);
        return bean.getTask();
    }

    public void delete(Long id) {
        taskEmployeeRepository.deleteById(id);
    }

    public void update(Long id, TaskEmployeeDTO vO) {
        TaskEmployee bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        taskEmployeeRepository.save(bean);
    }

    public TaskEmployeeDTO getById(Long id) {
        TaskEmployee original = requireOne(id);
        return toDTO(original);
    }

    public Page<TaskEmployeeDTO> query(TaskEmployeeDTO vO) {
        throw new UnsupportedOperationException();
    }

    private TaskEmployeeDTO toDTO(TaskEmployee original) {
        TaskEmployeeDTO bean = new TaskEmployeeDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private TaskEmployee requireOne(Long id) {
        return taskEmployeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
