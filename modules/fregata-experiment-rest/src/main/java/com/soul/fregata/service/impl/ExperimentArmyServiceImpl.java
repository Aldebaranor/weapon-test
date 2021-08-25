package com.soul.fregata.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.flagwind.persistent.model.Clause;
import com.flagwind.persistent.model.CombineClause;
import com.flagwind.persistent.model.SingleClause;
import com.soul.fregata.condition.ExperimentArmyCondition;
import com.soul.fregata.domain.ExperimentArmyRepository;
import com.soul.fregata.entity.ExperimentArmy;
import com.soul.fregata.entity.enums.ArmyType;
import com.soul.fregata.service.ExperimentArmyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Priority;
import java.util.List;

/**
 * created by 迷途小码农
 */
@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
public class ExperimentArmyServiceImpl extends TemplateService<ExperimentArmy, String> implements ExperimentArmyService {

    private final ExperimentArmyRepository experimentArmyRepository;

    @Override
    protected AbstractRepositoryBase<ExperimentArmy, String> getRepository() {
        return experimentArmyRepository;
    }

    @Override
    public PageResult<ExperimentArmy> page(QueryModel<ExperimentArmyCondition> model) {
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }

    @Override
    public List<ExperimentArmy> getByExperimentId(String experimentId) {
        return query(SingleClause.equal("experimentId", experimentId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByExperimentIdAndType(String experimentId, ArmyType type) {
        Clause clause;
        if (type == null) {
            clause = SingleClause.equal("experimentId", experimentId);
        } else {
            clause = CombineClause.and(
                    SingleClause.equal("experimentId", experimentId),
                    SingleClause.equal("type", type)
            );
        }
        experimentArmyRepository.delete(clause);
        clearCache();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByExperimentIds(List<String> experimentIds) {
        experimentArmyRepository.delete(SingleClause.in("experimentId", experimentIds.toArray()));
        clearCache();
    }

}
