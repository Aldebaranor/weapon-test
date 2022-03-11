package com.soul.weapon.controller.unity;


import com.egova.entity.DictionaryItem;
import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.egova.web.annotation.Api;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.entity.PipeTest;
import com.soul.weapon.service.PipeTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final CommonConfig config;

    /**
     * @Author: Shizuan
     * @Date: 2022/3/10 11:06
     * @Description: 根据ID获取测试项
     * @params:[id]
     * @return:com.soul.weapon.entity.PipeTest
     **/
    @Api
    @GetMapping(value = "/{id}")
    public PipeTest getById(@PathVariable("id") String id)
    {
        return pipeTestService.getById(id);
    }

    /**
     * @Author: Shizuan
     * @Date: 2022/3/10 11:06
     * @Description: 获取所有测试项
     * @params:[]
     * @return:java.util.List<com.soul.weapon.entity.PipeTest>
     **/
    @Api
    @GetMapping(value = "/list-all")
    public List<PipeTest> getAll()
    {
        return pipeTestService.getAll();
    }

    /**
     * @Author: Shizuan
     * @Date: 2022/3/10 11:07
     * @Description: 添加测试项
     * @params:[pipeTest]
     * @return:java.lang.String
     **/
    @Api
    @PostMapping
    public String insert(@RequestBody PipeTest pipeTest)
    {
        return pipeTestService.insert(pipeTest);
    }

    /**
     * @Author: Shizuan
     * @Date: 2022/3/10 11:08
     * @Description: 修改测试项
     * @params:[pipeTest]
     * @return:void
     **/
    @Api
    @PutMapping
    public void update(@RequestBody PipeTest pipeTest) {
        RedisUtils.getService(config.getPumpDataBase()).opsForHash().put(Constant.WEAPON_CURRENT_PIPETEST, pipeTest.getCode(), JsonUtils.serialize(pipeTest));
        pipeTestService.update(pipeTest);

    }

    /**
     * @Author: Shizuan
     * @Date: 2022/3/10 11:09
     * @Description: 通过Id删除测试项
     * @params:[id]
     * @return:int
     **/
    @Api
    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return pipeTestService.deleteById(id);
    }


    /**
     * @Author: Shizuan
     * @Date: 2022/3/10 11:12
     * @Description: 获取所有测试项的tree结构
     * @params:[]
     * @return:java.util.List<com.egova.entity.DictionaryItem>
     **/
    @Api
    @GetMapping(value = "/pipe-test-tree")
    public List<DictionaryItem> getPipeTestTree() {
        return pipeTestService.getPipeTestTree();
    }

    /**
     * @Author: Shizuan
     * @Date: 2022/3/10 11:13
     * @Description: 根据任务ID获取测试项
     * @params:[taskId] 任务Id
     * @return:java.util.List<com.soul.weapon.entity.PipeTest>
     **/
    @Api
    @GetMapping(value = "/getByTaskId/{taskId}")
    public List<PipeTest> getByTaskId(@PathVariable("taskId") String taskId) {
        return pipeTestService.getByTaskId(taskId);
    }


    /**
     * @Author: Shizuan
     * @Date: 2022/3/10 10:59
     * @Description: 通过任务ID获取测试项的tree结构
     * @params:[taskId] 任务Id
     * @return:java.util.List<com.egova.entity.DictionaryItem>
     **/
    @Api
    @GetMapping(value = "/getCurrentTaskTestItems/{taskId}")
    public List<DictionaryItem> getCurrentTaskTestItems(@PathVariable("taskId") String taskId){
        return pipeTestService.getCurrentTaskTestItems(taskId);
    }

}
