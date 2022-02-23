package com.soul.weapon.controller.unity;

import com.egova.exception.ExceptionUtils;
import com.egova.json.utils.JsonUtils;
import com.egova.model.PageResult;
import com.egova.model.PropertyItem;
import com.egova.model.QueryModel;
import com.egova.web.annotation.Api;
import com.flagwind.commons.Monment;
import com.flagwind.commons.StringUtils;
import com.soul.weapon.condition.PipeTaskCondition;
import com.soul.weapon.condition.PipeTestCondition;
import com.soul.weapon.config.WeaponTestConstant;
import com.soul.weapon.entity.PipeTask;
import com.soul.weapon.entity.PipeTest;
import com.soul.weapon.service.PipeTaskService;
import com.soul.weapon.service.PipeTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
@Slf4j
@RestController
@RequestMapping("/unity/task")
@RequiredArgsConstructor
public class PipeTaskController {

    private final PipeTaskService pipeTaskService;
    private final PipeTestService pipeTestService;

    @Api
    @GetMapping(value = "/{id}")
    public PipeTask getById(@PathVariable("id") String id) {
        return pipeTaskService.getById(id);
    }

    @Api
    @GetMapping(value = "/list-all")
    public List<PipeTask> getAll() {
        return pipeTaskService.getAll();
    }

    @Api
    @PostMapping
    public String insert(@RequestBody PipeTask pipeTask) {
        pipeTask.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return pipeTaskService.insert(pipeTask);
    }

    @Api
    @PutMapping
    public void update(@RequestBody PipeTask pipeTask) {
        pipeTaskService.update(pipeTask);
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
        return pipeTaskService.deleteById(id);
    }

    @Api
    @PostMapping(value = "/page")
    public PageResult<PipeTask> page(@RequestBody QueryModel<PipeTaskCondition> con) {
        return pipeTaskService.page(con);
    }

    /**
     * 开始任务
     * @param takeId
     */
    @Api
    @GetMapping(value = "/start/{takeId}")
    public void startTest(@PathVariable String takeId) {

        List<PipeTest> list = pipeTestService.getByTaskId(takeId);
        pipeTaskService.startTest(takeId, list);
    }

    /**
     * 保存任务
     * @param taskId
     * @param pipeTests
     */
    @Api
    @PostMapping(value = "/save/{taskId}")
    public void safeTest(@PathVariable String taskId, @RequestBody List<PipeTest> pipeTests) {
        if (pipeTaskService.getById(taskId) == null) {
            throw ExceptionUtils.api("没有找到该任务", new Object[0]);
        }
        pipeTests.stream().forEach(pipeTest -> {
            pipeTest.setId(UUID.randomUUID().toString());
            pipeTest.setCreateTime(new Timestamp(System.currentTimeMillis()));
        });
        pipeTestService.insertList(pipeTests);
    }

    /**
     * 停止任务
     * @param taskId
     */
    @Api
    @DeleteMapping(value = "/stop/{taskId}")
    public void safeTest(@PathVariable String taskId) {
        pipeTaskService.stopTest(taskId);

    }

    @Api
    @GetMapping(value = "/name/{name}")
    public List<PipeTask> getByName(@PathVariable String name)
    {
        return pipeTaskService.getByName(name);
    }

    @Api
    @GetMapping(value = "/getThreshold")
    public List<PropertyItem<Double>> getThreshold(@RequestParam(value="taskId") String taskId, @RequestParam(value="pipeTestType") String pipeTestType)
    {
        PipeTestCondition condition = new PipeTestCondition();
        condition.setTaskId(taskId);
        condition.setType(pipeTestType);
        try {
            PipeTest pipeTest = pipeTestService.list(condition).stream().findFirst().orElse(null);
            if(pipeTest == null ){
                return null;
            }
            if(StringUtils.isBlank(pipeTest.getThreshold())){
                return WeaponTestConstant.WEAPON_THRESHOLD.get(pipeTestType);
            }else{
                return JsonUtils.deserializeList(pipeTest.getThreshold(),PropertyItem.class);
            }
        } catch (Exception e) {
            throw ExceptionUtils.api("获取阈值失败", new Object[0]);
        }
    }






}
