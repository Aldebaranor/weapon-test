package com.soul.fire.controller.free;

import com.egova.exception.ExceptionUtils;
import com.egova.web.annotation.Api;
import com.soul.fire.entity.FireConflictPriority;
import com.soul.fire.service.FireConflictPriorityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.core.util.CollectionUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * @author xinl
 */
@Slf4j
@RestController
@RequestMapping("/free/conflict-priority")
@RequiredArgsConstructor
public class FreeFireConflictPriorityController {

    private final FireConflictPriorityService fireConflictPriorityService;

    @Api
    @PutMapping()
    public void update(@RequestBody List<FireConflictPriority> list) {
        if(CollectionUtils.isEmpty(list)){
            throw  ExceptionUtils.api("list must not be empty");
        }
        fireConflictPriorityService.updateList(list);
    }

    @Api
    @GetMapping()
    public List<FireConflictPriority> getAll() {
        return fireConflictPriorityService.getAll();
    }
}
