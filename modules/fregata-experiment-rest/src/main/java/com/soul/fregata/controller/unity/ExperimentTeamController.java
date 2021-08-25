package com.soul.fregata.controller.unity;

import com.egova.entity.Person;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.web.annotation.Api;
import com.egova.web.annotation.RequestDecorating;
import com.soul.fregata.condition.ExperimentTeamCondition;
import com.soul.fregata.entity.ExperimentTeam;
import com.soul.fregata.facade.ExperimentTeamFacade;
import com.soul.fregata.service.ExperimentTeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * created by 迷途小码农
 */
@Slf4j
@RestController
@RequestMapping("/unity/experiment-team")
@RequiredArgsConstructor
public class ExperimentTeamController implements ExperimentTeamFacade {

    private final ExperimentTeamService experimentTeamService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return ExperimentShare
     */
    @Api
    @Override
    public ExperimentTeam getById(@PathVariable String id) {
        return experimentTeamService.getById(id);
    }

    /**
     * 保存
     *
     * @param experimentTeam 试验分享
     * @return 主键
     */
    @Api
    @Override
    public String insert(@RequestBody ExperimentTeam experimentTeam) {
        return experimentTeamService.insert(experimentTeam);
    }

    /**
     * 更新
     *
     * @param experimentTeam 试验分享
     */
    @Api
    @Override
    public void update(@RequestBody ExperimentTeam experimentTeam) {
        experimentTeamService.update(experimentTeam);
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
        return experimentTeamService.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param model 模型
     * @return PageResult
     */
    @Api
    @PostMapping("/page")
    public PageResult<ExperimentTeam> page(@RequestBody QueryModel<ExperimentTeamCondition> model) {
        return experimentTeamService.page(model);
    }

    /**
     * 批量删除
     *
     * @param ids 主键列表
     * @return 影响记录行数
     */
    @Api
    @PostMapping("/batch")
    @RequestDecorating(value = "delete")
    public int batchDelete(@RequestBody List<String> ids) {
        return experimentTeamService.deleteByIds(ids);
    }

    /**
     * 查询团队成员
     *
     * @param condition
     * @return List<ExperimentTeam>
     */
    @Api
    @PostMapping("/list")
    public List<ExperimentTeam> list(@RequestBody ExperimentTeamCondition condition) {
        return experimentTeamService.list(condition);
    }

    /**
     * 人员搜索
     *
     * @param experimentId
     * @return List<Person>
     */
    @Api
    @GetMapping("/experimentId/{experimentId}")
    public List<Person> getByExperimentId(@PathVariable String experimentId) {
        return experimentTeamService.getByExperimentId(experimentId);
    }

    /**
     * 获取操作人权限
     *
     * @param experimentId 试验ID
     * @return boolean
     */
    @Api
    @PostMapping("/check-authorization")
    public boolean checkAuthorization(@RequestParam String experimentId) {
        return experimentTeamService.checkAuthorization(experimentId);
    }

}
