package com.soul.weapon.controller.free;

import com.soul.weapon.entity.Task;
import com.soul.weapon.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * create by nash5
 */

@Slf4j
@RestController
@RequestMapping("/free/task")
@RequiredArgsConstructor
public class FreeTaskController {

    @Autowired
    public TaskService taskService;

    @GetMapping(value = "/test")
    Task test() {
        return taskService.getById("1");
        // Task byId = taskService.getById(id);
    };

    @GetMapping(value = "/{id}")
    Task getByIdx(@PathVariable("id") String id) {
        return taskService.getById(id);
        // Task byId = taskService.getById(id);
    };

}
