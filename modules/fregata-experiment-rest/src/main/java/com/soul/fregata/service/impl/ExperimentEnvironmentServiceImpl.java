package com.soul.fregata.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.security.UserContext;
import com.flagwind.persistent.model.SingleClause;
import com.soul.fregata.condition.ExperimentEnvironmentCondition;
import com.soul.fregata.domain.ExperimentEnvironmentRepository;
import com.soul.fregata.entity.ExperimentEnvironment;
import com.soul.fregata.service.ExperimentEnvironmentService;
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
public class ExperimentEnvironmentServiceImpl extends TemplateService<ExperimentEnvironment, String> implements ExperimentEnvironmentService {

    private final ExperimentEnvironmentRepository experimentEnvironmentRepository;

    @Override
    protected AbstractRepositoryBase<ExperimentEnvironment, String> getRepository() {
        return experimentEnvironmentRepository;
    }

    @Override
    public PageResult<ExperimentEnvironment> page(QueryModel<ExperimentEnvironmentCondition> model) {
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }

    @Override
    public List<ExperimentEnvironment> list(ExperimentEnvironmentCondition condition) {
        return super.query(condition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insert(ExperimentEnvironment environment) {
        environment.setCreator(UserContext.username());
        environment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(environment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ExperimentEnvironment environment) {
        environment.setModifier(UserContext.username());
        environment.setModifyTime(new Timestamp(System.currentTimeMillis()));
        super.update(environment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByExperimentId(String experimentId) {
        experimentEnvironmentRepository.delete(SingleClause.equal("experimentId", "experimentId"));
        clearCache();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByExperimentIds(List<String> experimentIds) {
        experimentEnvironmentRepository.delete(SingleClause.in("experimentId", experimentIds.toArray()));
        clearCache();
    }

}
