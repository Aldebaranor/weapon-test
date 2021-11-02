package com.soul.fire.controller.free;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.web.annotation.Api;
import com.soul.fire.condition.FireConflictCondition;
import com.soul.fire.condition.FireThresholdCondition;
import com.soul.fire.entity.FireConflict;
import com.soul.fire.entity.FireThreshold;
import com.soul.fire.service.FireConflictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xinl
 */
@Slf4j
@RestController
@RequestMapping("/free/conflict")
@RequiredArgsConstructor
public class FireConflictController {
    
    private final FireConflictService fireConflictService;

    @Api
    @GetMapping(value = "/{id}")
    public FireConflict getById(@PathVariable String id) {
        return fireConflictService.getById(id);
    }

    @Api
    @PostMapping
    public String insert(@RequestBody FireConflict fireConflict) {
        return fireConflictService.insert(fireConflict);
    }

    @Api
    @PutMapping
    public void update(@RequestBody FireConflict fireConflict) {
        fireConflictService.update(fireConflict);

    }

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    @Api
    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return fireConflictService.deleteById(id);
    }


    /**
     * 根据冲突类型及任务进行查询
     *
     * @return 冲突表
     */
    @Api
    @GetMapping("/type-id")
    public List<FireConflict> getByTypeAndId(@RequestParam("type") String type, @RequestParam("id") String id) {
        return fireConflictService.getByTypeAndTask(type,id);
    }

    /**
     * 分页查询
     * @param model 模型
     * @return PageResult
     */
    @Api
    @PostMapping("/page")
    public PageResult<FireConflict> page(@RequestBody QueryModel<FireConflictCondition> model) {
        return fireConflictService.page(model);
    }

}
