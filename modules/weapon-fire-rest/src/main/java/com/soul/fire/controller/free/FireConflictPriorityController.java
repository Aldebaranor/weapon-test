package com.soul.fire.controller.free;

import com.egova.web.annotation.Api;
import com.soul.fire.entity.FireConflictPriority;
import com.soul.fire.service.FireConflictPriorityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/free/conflict-priority")
@RequiredArgsConstructor
public class FireConflictPriorityController {

    private final FireConflictPriorityService fireConflictPriorityService;

    @Api
    @PutMapping()
    public void update(@RequestBody List<FireConflictPriority> priorities) {
        fireConflictPriorityService.updatePriorities(priorities);
        // firePriorityService.updatePriorityByPair(modPriority);
    }

    @Api
    @GetMapping()
    public List<FireConflictPriority> getAll() {
        return fireConflictPriorityService.listPriorities();
    }
}
