package com.soul.fregata.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.ConditionParser;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.persistent.ClauseBuilder;
import com.egova.security.UserContext;
import com.flagwind.persistent.model.ChildClause;
import com.flagwind.persistent.model.Clause;
import com.flagwind.persistent.model.Paging;
import com.flagwind.persistent.model.SingleClause;
import com.soul.fregata.condition.ExperimentCondition;
import com.soul.fregata.domain.ExperimentRepository;
import com.soul.fregata.domain.ExperimentTeamRepository;
import com.soul.fregata.entity.Experiment;
import com.soul.fregata.entity.ExperimentArmy;
import com.soul.fregata.entity.enums.ArmyType;
import com.soul.fregata.service.ExperimentArmyService;
import com.soul.fregata.service.ExperimentParameterService;
import com.soul.fregata.service.ExperimentResourceService;
import com.soul.fregata.service.ExperimentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Priority;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * created by 迷途小码农
 */
@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
public class ExperimentServiceImpl extends TemplateService<Experiment, String> implements ExperimentService {

    private final ExperimentRepository experimentRepository;

    private final ExperimentArmyService experimentArmyService;
    private final ExperimentResourceService experimentResourceService;
    private final ExperimentParameterService experimentParameterService;

    private final ExperimentTeamRepository experimentTeamRepository;

    @Override
    protected AbstractRepositoryBase<Experiment, String> getRepository() {
        return experimentRepository;
    }

    @Override
    public Experiment getById(String id) {
        Experiment experiment = super.getById(id);
        if (experiment == null) {
            return null;
        }
        Map<ArmyType, List<ExperimentArmy>> armyMap = experimentArmyService.getByExperimentId(id).stream()
                .collect(Collectors.groupingBy(ExperimentArmy::getType));
        experiment.setRedArmies(buildArmyTree(armyMap.getOrDefault(ArmyType.RED, Collections.emptyList())));
        experiment.setBlueArmies(buildArmyTree(armyMap.getOrDefault(ArmyType.BLUE, Collections.emptyList())));
        return experiment;
    }

    private List<ExperimentArmy> buildArmyTree(List<ExperimentArmy> armies) {
        return buildArmyTree(
                armies,
                (first, second) -> StringUtils.equals(first.getId(), second.getParentId()),
                item -> StringUtils.isBlank(item.getParentId())
        );
    }

    /**
     * 构建树结构
     *
     * @param data              数据
     * @param isParentPredicate 前者是否为后者的父级
     * @param isTopPredicate    元素是否为顶级
     * @return 树型结构
     */
    private List<ExperimentArmy> buildArmyTree(
            List<ExperimentArmy> data,
            BiPredicate<ExperimentArmy, ExperimentArmy> isParentPredicate,
            Predicate<ExperimentArmy> isTopPredicate
    ) {
        for (ExperimentArmy first : data) {
            if (first.getChildren() == null) {
                first.setChildren(new LinkedList<>());
            }
            for (ExperimentArmy second : data) {
                // first 为 second 父级
                if (isParentPredicate.test(first, second)) {
                    first.getChildren().add(second);
                }
            }
        }
        // 返回顶级
        return data.stream().filter(isTopPredicate).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insert(Experiment experiment) {
        experiment.setCreator(UserContext.username());
        experiment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(experiment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Experiment experiment) {
        experiment.setModifier(UserContext.username());
        experiment.setModifyTime(new Timestamp(System.currentTimeMillis()));
        super.update(experiment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(String id) {
        experimentArmyService.deleteByExperimentIdAndType(id, null);
        experimentResourceService.deleteByExperimentId(id);
        experimentParameterService.deleteByExperimentId(id);
        experimentTeamRepository.delete(SingleClause.equal("experimentId", id));
        experimentTeamRepository.clearCache();
        return super.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(List<String> ids) {
        experimentArmyService.deleteByExperimentIds(ids);
        experimentResourceService.deleteByExperimentIds(ids);
        experimentParameterService.deleteByExperimentIds(ids);
        experimentTeamRepository.delete(SingleClause.in("experimentId", ids.toArray()));
        experimentTeamRepository.clearCache();
        return super.deleteByIds(ids);
    }

    @Override
    public PageResult<Experiment> page(QueryModel<ExperimentCondition> model) {
        // 只能查询自己的，或分享给自己的
        ChildClause childClause = ChildClause.include("id", "experimentId", "fregata_experiment_team");
        childClause.add(SingleClause.equal("personId", UserContext.personId()));
        Clause clause = ClauseBuilder.and()
                .add(ConditionParser.parser(model.getCondition()))
                .add(
                        ClauseBuilder.or()
                                .add(SingleClause.equal("creator", UserContext.username()))
                                .add(childClause)
                                .toClause()
                ).toClause();
        Paging paging = Optional.ofNullable(model.getPaging())
                .orElse(new Paging());
        List<Experiment> experiments = experimentRepository.query(clause, model.getPaging(), model.getSorts());
        return PageResult.of(experiments, paging.getTotalCount());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upsertArmyTree(Experiment experiment) {
        if (experiment.getId() == null) {
            insert(experiment);
        } else {
            update(experiment);
        }
        // 兵力树维护
        upsertArmies(experiment.getId(), ArmyType.RED, experiment.getRedArmies());
        upsertArmies(experiment.getId(), ArmyType.BLUE, experiment.getBlueArmies());
    }

    private void upsertArmies(String experimentId, ArmyType type, List<ExperimentArmy> armies) {
        experimentArmyService.deleteByExperimentIdAndType(experimentId, type);
        if (CollectionUtils.isEmpty(armies)) {
            return;
        }
        initArmyTree(null, experimentId, type, armies);
        List<ExperimentArmy> list = new ArrayList<>(armies);
        for (ExperimentArmy army : armies) {
            armies = army.getChildren();
            if (CollectionUtils.isEmpty(armies)) {
                continue;
            }
            list.addAll(armies);
        }
        experimentArmyService.insertList(list);
    }

    private void initArmyTree(String parentId, String experimentId, ArmyType type, List<ExperimentArmy> armies) {
        if (CollectionUtils.isEmpty(armies)) {
            return;
        }
        armies.forEach(army -> {
            army.setId(getKey());
            army.setExperimentId(experimentId);
            army.setParentId(parentId);
            army.setType(type);
            army.setCreator(UserContext.username());
            army.setCreateTime(new Timestamp(System.currentTimeMillis()));
            initArmyTree(army.getId(), experimentId, type, army.getChildren());
        });
    }

}
