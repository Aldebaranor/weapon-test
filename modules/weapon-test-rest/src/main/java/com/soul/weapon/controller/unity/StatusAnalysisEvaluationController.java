package com.soul.weapon.controller.unity;

import com.egova.web.annotation.Api;
import com.soul.weapon.model.StatusAnalysisEvaluation;
import com.soul.weapon.service.StatusAnalysisEvaluationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName StatusAnalysisEvaluationController
 * @Description 获取状态分析评估controller
 * @Author ShiZuan
 * @Date 2022/3/8 15:59
 * @Version
 **/
@Slf4j
@RestController
@RequestMapping("/unity/sae")
public class StatusAnalysisEvaluationController {


    @Autowired
    StatusAnalysisEvaluationService statusAnalysisEvaluationService;

    /**
     * @Author: Shizuan
     * @Date: 2022/3/8 16:31
     * @Description: 获取全部目标Id的状态分析评估
     * @params:[]
     * @return:Map targetTypeId, List<StatusAnalysisEvaluation>(所需数据)
     **/

    @Api
    @GetMapping("/list")
    public Map<String, List<StatusAnalysisEvaluation>> getStatusAnglysisAll() {
        return statusAnalysisEvaluationService.getStatusAnglysisAll();
    }
}
