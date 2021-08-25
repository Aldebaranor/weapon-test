package com.soul.fregata.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.security.UserContext;
import com.flagwind.persistent.model.SingleClause;
import com.soul.fregata.condition.ExperimentAttributeCondition;
import com.soul.fregata.domain.ExperimentAttributeRepository;
import com.soul.fregata.entity.ExperimentAttribute;
import com.soul.fregata.service.ExperimentAttributeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Priority;
import java.sql.Timestamp;
import java.util.List;

/**
 * created by 迷途小码农
 */
@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
public class ExperimentAttributeServiceImpl extends TemplateService<ExperimentAttribute, String> implements ExperimentAttributeService {

    private final ExperimentAttributeRepository experimentAttributeRepository;

    @Override
    protected AbstractRepositoryBase<ExperimentAttribute, String> getRepository() {
        return experimentAttributeRepository;
    }

    @Override
    public PageResult<ExperimentAttribute> page(QueryModel<ExperimentAttributeCondition> model) {
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }

    @Override
    public List<ExperimentAttribute> list(ExperimentAttributeCondition condition) {
        return super.query(condition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insert(ExperimentAttribute attribute) {
        attribute.setCreator(UserContext.username());
        attribute.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(attribute);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ExperimentAttribute attribute) {
        attribute.setModifier(UserContext.username());
        attribute.setModifyTime(new Timestamp(System.currentTimeMillis()));
        super.update(attribute);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByExperimentId(String experimentId) {
        super.delete(SingleClause.equal("experimentId", "experimentId"));
        experimentAttributeRepository.delete(SingleClause.equal("experimentId", "experimentId"));
        clearCache();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByExperimentIds(List<String> experimentIds) {
        experimentAttributeRepository.delete(SingleClause.in("experimentId", experimentIds.toArray()));
        clearCache();
    }

}
