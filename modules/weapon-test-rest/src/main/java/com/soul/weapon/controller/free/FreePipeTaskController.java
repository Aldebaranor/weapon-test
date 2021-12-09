package com.soul.weapon.controller.free;

import com.egova.exception.ExceptionUtils;
import com.egova.json.utils.JsonUtils;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.redis.RedisUtils;
import com.egova.web.annotation.Api;
import com.flagwind.commons.StringUtils;
import com.soul.weapon.condition.PipeTaskCondition;
import com.soul.weapon.config.CommonRedisConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.entity.PipeTask;
import com.soul.weapon.entity.PipeTest;
import com.soul.weapon.service.PipeTaskService;
import com.soul.weapon.service.PipeTestService;
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

    private final PipeTestService pipeTestService;

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

        return  pipeTaskService.page(con);
    }

    @Api
    @GetMapping(value = "/start/{takeId}")
    public void startTest(@PathVariable String takeId){
        //TODO:
        List<PipeTest> list = pipeTestService.getByTaskId(takeId);
        pipeTaskService.startTest(takeId,list);
    }

    @Api
    @PostMapping(value = "/save/{taskId}")
    public void safeTest(@PathVariable String taskId,@RequestBody List<PipeTest> pipeTests){
      if (pipeTaskService.getById(taskId)==null){
          throw ExceptionUtils.api("没有找到该任务", new Object[0]);
      }
      pipeTestService.insertList(pipeTests);

    }

    @Api
    @DeleteMapping(value = "/stop/{takeId}")
    public void safeTest(@PathVariable String takeId){
        pipeTaskService.stopTest(takeId);

    }

    @Api
    @GetMapping(value = "/name/{name}")
    public List<PipeTask> getByName(@PathVariable String name)
    {
        return pipeTaskService.getByName(name);
    }
}
