package com.soul.fire.controller.unity;

import com.egova.web.annotation.Api;
import com.soul.fire.condition.FirePriorityCondition;
import com.soul.fire.entity.FirePriority;
import com.soul.fire.service.FirePriorityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/unity/priority")
@RequiredArgsConstructor
public class FirePriorityController {

    private final FirePriorityService firePriorityService;

    @Api
    @PutMapping()
    public void updateTask(@RequestBody FirePriority modPriority) {
        firePriorityService.update(modPriority);
    }

    @Api
    @PostMapping("/list")
    public List<FirePriority> getPriorityByType(@RequestBody FirePriorityCondition con) {
        String type = con.getConflictType();
        switch (type){
            case "0":
                con.setConflictType("1");
                break;
            case "1":
                con.setConflictType("2");
                break;
            case "2":
                con.setConflictType("3");
                break;
            default:
                break;
        }
        return firePriorityService.list(con);
    }
}
