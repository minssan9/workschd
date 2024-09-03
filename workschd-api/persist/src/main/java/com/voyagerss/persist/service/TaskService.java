package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.TaskDTO;
import com.voyagerss.persist.entity.Task;
import com.voyagerss.persist.repository.TaskRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Long save(TaskVO vO) {
        Task bean = new Task();
        BeanUtils.copyProperties(vO, bean);
        bean = taskRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public void update(Long id, TaskUpdateVO vO) {
        Task bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        taskRepository.save(bean);
    }

    public TaskDTO getById(Long id) {
        Task original = requireOne(id);
        return toDTO(original);
    }

    public Page<TaskDTO> query(TaskQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private TaskDTO toDTO(Task original) {
        TaskDTO bean = new TaskDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private Task requireOne(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
