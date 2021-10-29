package com.soul.fire.controller.free;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.web.annotation.Api;
import com.soul.fire.condition.FireThresholdCondition;
import com.soul.fire.condition.FireWeaponCondition;
import com.soul.fire.entity.FireThreshold;
import com.soul.fire.entity.FireWeapon;
import com.soul.fire.service.FireThresholdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/free/threshold")
@RequiredArgsConstructor
public class FireThresholdController {

    private final FireThresholdService fireThresholdService;

    @Api
    @GetMapping(value = "/{id}")
    public FireThreshold getById(@PathVariable String id) {return fireThresholdService.getById(id);}

    @Api
    @PostMapping
    public String insert(@RequestBody FireThreshold fireThreshold) {
        return fireThresholdService.insert(fireThreshold);
    }

    @Api
    @PutMapping
    public void update(@RequestBody FireThreshold fireThreshold) {
        fireThresholdService.update(fireThreshold);
    }

    /**
     * 主键删除
     * @param id 主键
     * @return 影响记录行数
     */
    @Api
    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return fireThresholdService.deleteById(id);
    }

    @Api
    @PostMapping("/list")
    public List<FireThreshold> list(@RequestBody FireThresholdCondition condition) {
        return fireThresholdService.list(condition);
    }

    /**
     * 分页查询
     * @param model 模型
     * @return PageResult
     */
    @Api
    @PostMapping("/page")
    public PageResult<FireThreshold> page(@RequestBody QueryModel<FireThresholdCondition> model) {
        return fireThresholdService.page(model);
    }


}
