package com.soul.fire.controller.unity;

import com.egova.web.annotation.Api;
import com.soul.fire.condition.FireTaskCondition;
import com.soul.fire.entity.FireTask;
import com.soul.fire.service.FireTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/unity/task")
@RequiredArgsConstructor
public class FireTaskController {

    private final FireTaskService fireTaskService;

    @Api
    @GetMapping("/current")
    public FireTask getCurTask() {
        return fireTaskService.getCurTask();
    }


    @Api
    @PutMapping("/current")
    public void updateTask(@RequestBody FireTask task) {
        fireTaskService.update(task);
    }

    @Api
    @PostMapping("/current")
    public String insertTask(@RequestBody FireTask task) {
        return fireTaskService.insert(task);
    }

    @Api
    @PostMapping("/list")
    public List<FireTask> list(@RequestBody FireTaskCondition con) {
        return fireTaskService.list(con);
    }

}
