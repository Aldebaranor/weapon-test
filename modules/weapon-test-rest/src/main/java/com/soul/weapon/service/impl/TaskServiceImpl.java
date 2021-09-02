package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.soul.weapon.domain.TaskRepository;
import com.soul.weapon.entity.Task;
import com.soul.weapon.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;

@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
public class TaskServiceImpl extends TemplateService<Task, String> implements TaskService {
    public TaskRepository taskRepository;

    @Override
    protected AbstractRepositoryBase<Task, String> getRepository() {

        return taskRepository;
    }
}
