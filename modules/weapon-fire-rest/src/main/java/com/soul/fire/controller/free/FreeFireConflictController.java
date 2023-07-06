package com.soul.fire.controller.free;

import com.egova.json.utils.JsonUtils;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.redis.RedisUtils;
import com.egova.web.annotation.Api;
import com.soul.fire.condition.FireConflictCondition;
import com.soul.fire.condition.FireThresholdCondition;
import com.soul.fire.entity.FireConflict;
import com.soul.fire.entity.FireThreshold;
import com.soul.fire.service.FireConflictService;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.model.ConflictReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xinl
 */
@Slf4j
@RestController
@RequestMapping("/free/conflict")
@RequiredArgsConstructor
public class FreeFireConflictController {

    private final FireConflictService fireConflictService;

    private final CommonConfig config;

    /**
     * 冲突结果查询
     *
     * @return List<ConflictReport>
     */
    @Api
    @GetMapping("/result")
    public List<ConflictReport> chargeResult() {
        List<ConflictReport> results = new ArrayList<>();
        Map<String, String> conflictResults = RedisUtils.getService(config.

                getFireDataBase()).boundHashOps(Constant.PREDICT_KEY).entries();

        if (conflictResults == null) {
            return results;
        }

        Map<String, ConflictReport> chargeResultsMap = conflictResults.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        pair -> JsonUtils.deserialize(pair.getValue(), ConflictReport.class)
                )
        );
        return new ArrayList<>(chargeResultsMap.values());
    }


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
