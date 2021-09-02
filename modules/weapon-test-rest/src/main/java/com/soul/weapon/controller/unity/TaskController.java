package com.soul.weapon.controller.unity;

import com.egova.web.annotation.Api;
import com.soul.weapon.condition.TaskCondition;
import com.soul.weapon.entity.Task;
import com.soul.weapon.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * create by nash5
 */

@Slf4j
@RestController
@RequestMapping("/unity/task")
@RequiredArgsConstructor
public class TaskController {

    @Autowired
    public TaskService taskService;


    @GetMapping(value = "/{id}")
    Task getByIdx(@PathVariable("id") String id) {
        return taskService.getById(id);
        // Task byId = taskService.getById(id);
    };

}
