package com.soul.weapon.controller.free;


import com.egova.web.annotation.Api;
import com.soul.weapon.service.impl.AllAlgorithmServiceImpl;
import com.soul.weapon.entity.PipeTest;
import com.soul.weapon.model.StateAnalysisTimeReport;
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
@RequestMapping("/free/test")
@RequiredArgsConstructor
public class FreePipeTestController {

    private final PipeTestService pipeTestService;
    private final AllAlgorithmServiceImpl allAlgorithm;

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
    public String inset(@RequestBody PipeTest pipeTest)
    {
        return pipeTestService.insert(pipeTest);
    }

    @Api
    @PutMapping
    public void update(@RequestBody PipeTest pipeTest) {
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
        if(type.equals("underWater")){
            return allAlgorithm.getUnderWaterDefendInfo();
        }else if(type.equals("toAir")){
            return allAlgorithm.getAirDefendInfo();
        }else return null;
    }

}
