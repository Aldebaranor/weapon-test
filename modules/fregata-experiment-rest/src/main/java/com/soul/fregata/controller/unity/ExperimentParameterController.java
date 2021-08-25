package com.soul.fregata.controller.unity;

import com.egova.web.annotation.Api;
import com.soul.fregata.entity.ExperimentParameter;
import com.soul.fregata.facade.ExperimentParameterFacade;
import com.soul.fregata.service.ExperimentParameterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * created by yangL
 */
@Slf4j
@RestController
@RequestMapping("/unity/experiment-parameter")
@RequiredArgsConstructor
public class ExperimentParameterController implements ExperimentParameterFacade {

    private final ExperimentParameterService experimentParameterService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return ExperimentParameter
     */
    @Api
    @Override
    public ExperimentParameter getById(@PathVariable String id) {
        return experimentParameterService.getById(id);
    }

    /**
     * 保存
     *
     * @param experimentParameter 试验参数
     * @return 主键
     */
    @Api
    @Override
    public String insert(@RequestBody ExperimentParameter experimentParameter) {
        return experimentParameterService.insert(experimentParameter);
    }

    /**
     * 更新
     *
     * @param experimentParameter 试验参数
     */
    @Api
    @Override
    public void update(@RequestBody ExperimentParameter experimentParameter) {
        experimentParameterService.update(experimentParameter);

    }

    @Api
    @GetMapping("/experiment-id/{experimentId}")
    public ExperimentParameter list(@PathVariable String experimentId) {
        return experimentParameterService.getByExperimentId(experimentId);
    }

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    @Api
    @Override
    public int deleteById(@PathVariable String id) {
        return experimentParameterService.deleteById(id);
    }

}
