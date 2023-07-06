package com.soul.fire.controller.free;

import com.egova.web.annotation.Api;
import com.soul.fire.condition.FirePriorityCondition;
import com.soul.fire.entity.FirePriority;
import com.soul.fire.entity.FireTask;
import com.soul.fire.service.FirePriorityService;
import com.soul.fire.service.FireTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/free/priority")
@RequiredArgsConstructor
public class FreeFirePriorityController {

    private final FirePriorityService firePriorityService;

    @Api
    @PutMapping()
    public void updateTask(@RequestBody FirePriority modPriority) {
        firePriorityService.update(modPriority);
        // firePriorityService.updatePriorityByPair(modPriority);
    }

    // @Api
    // @GetMapping("/{type}")
    // public List<FirePriority> getPriorityByType(@PathVariable("type") String type) {
    //     return firePriorityService.getPriorityByType(type);
    // }

    @Api
    @PostMapping("/list")
    public List<FirePriority> getPriorityByType(@RequestBody FirePriorityCondition con) {
        return firePriorityService.list(con);
    }
}
