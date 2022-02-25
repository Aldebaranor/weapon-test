package com.soul.weapon.controller.unity;


import com.egova.entity.DictionaryItem;
import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.egova.web.annotation.Api;
import com.flagwind.commons.StringUtils;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.service.impl.AllAlgorithmServiceImpl;
import com.soul.weapon.entity.PipeTest;
import com.soul.weapon.model.StateAnalysisTimeReport;
import com.soul.weapon.service.PipeTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
@Slf4j
@RestController
@RequestMapping("/unity/test")
@RequiredArgsConstructor
public class PipeTestController {

    private final PipeTestService pipeTestService;
    private final AllAlgorithmServiceImpl allAlgorithm;
    private final CommonConfig config;

    @Api
    @GetMapping(value = "/{id}")
    public PipeTest getById(@PathVariable("id") String id)
    {
        return pipeTestService.getById(id);
    }

    @Api
    @GetMapping(value = "/list-all")
    public List<PipeTest> getAll()
    {
        return pipeTestService.getAll();
    }

    @Api
    @PostMapping
    public String insert(@RequestBody PipeTest pipeTest)
    {
        return pipeTestService.insert(pipeTest);
    }

    @Api
    @PutMapping
    public void update(@RequestBody PipeTest pipeTest) {
        RedisUtils.getService(config.getPumpDataBase()).opsForHash().put(Constant.WEAPON_CURRENT_PIPETEST, pipeTest.getCode(), JsonUtils.serialize(pipeTest));
        pipeTestService.update(pipeTest);

    }

    /**
     * 主键删除
     * @param id 主键
     * @return 影响记录行数
     */
    @Api
    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return pipeTestService.deleteById(id);
    }

    @Api
    @PostMapping(value = "/defend/{type}")
    public List<StateAnalysisTimeReport> getDefendInfo(@PathVariable String type){

        if(StringUtils.equals("underWater",type)){
            return allAlgorithm.getUnderWaterDefendInfo();
        }else if(StringUtils.equals("toAir",type)){
            return allAlgorithm.getAirDefendInfo();
        }else {
            return null;
        }

    }

    @Api
    @GetMapping(value = "/pipe-test-tree")
    public List<DictionaryItem> getPipeTestTree() {
        return pipeTestService.getPipeTestTree();
    }

    @Api
    @GetMapping(value = "/getByTaskId/{taskId}")
    public List<PipeTest> getByTaskId(@PathVariable("taskId") String taskId) {
        return pipeTestService.getByTaskId(taskId);
    }

    //根据taskId获取测试项tree结构
    @Api
    @GetMapping(value = "/getCurrentTaskTestItems/{taskId}")
    public List<DictionaryItem> getCurrentTaskTestItems(@PathVariable("taskId") String taskId){
        return pipeTestService.getCurrentTaskTestItems(taskId);
    }
}
