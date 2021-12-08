package com.soul.weapon.controller.free;

import com.egova.json.utils.JsonUtils;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.redis.RedisUtils;
import com.egova.web.annotation.Api;
import com.soul.weapon.condition.PipeTaskCondition;
import com.soul.weapon.config.CommonRedisConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.entity.PipeTask;
import com.soul.weapon.entity.PipeTest;
import com.soul.weapon.service.PipeTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
@Slf4j
@RestController
@RequestMapping("/pipe/free/task")
@RequiredArgsConstructor
public class FreePipeTaskController {

    private final PipeTaskService pipeTaskService;
    private final CommonRedisConfig commonRedisConfig;

    @Api
    @GetMapping(value = "/{id}")
    public PipeTask getById(@PathVariable("id") String id)
    {
        return pipeTaskService.getById(id);
    }

    @Api
    @GetMapping(value = "/list-all")
    public List<PipeTask> getAll()
    {
        return pipeTaskService.getAll();
    }

    @Api
    @PostMapping
    public String insert(@RequestBody PipeTask pipeTask)
    {
        return pipeTaskService.insert(pipeTask);
    }

    @Api
    @PutMapping
    public void update(@RequestBody PipeTask pipeTask) {
        pipeTaskService.update(pipeTask);
    }

    /**
     * 主键删除
     * @param id 主键
     * @return 影响记录行数
     */
    @Api
    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return pipeTaskService.deleteById(id);
    }

    @Api
    @PostMapping(value = "/page")
    public PageResult<PipeTask> page(@RequestBody QueryModel<PipeTaskCondition> con) {
    // public QueryModel<PipeTaskCondition> page() {
        // QueryModel<PipeTaskCondition> con = new QueryModel<PipeTaskCondition>();
        return  pipeTaskService.page(con);
    }

    @Api
    @PostMapping(value = "/start/{takeId}")
    public void startTest(@PathVariable String takeId,@RequestBody List<PipeTest> pipeTests){
        //TODO:
        pipeTaskService.startTest(takeId,pipeTests);
        RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).extrasForValue().set(Constant.WEAPON_CURRENT_TASK,takeId);
        for(PipeTest pipeTest : pipeTests){
            RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).opsForHash().put(Constant.WEAPON_CURRENT_PIPETEST,pipeTest.getCode(), JsonUtils.serialize(pipeTest));
        }

    }

    @Api
    @GetMapping(value = "/name/{name}")
    public List<PipeTask> getByName(@PathVariable String name)
    {
        return pipeTaskService.getByName(name);
    }
}
