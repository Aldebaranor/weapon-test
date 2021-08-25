package com.soul.fregata.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.security.UserContext;
import com.flagwind.persistent.model.SingleClause;
import com.soul.fregata.condition.ExperimentParameterCondition;
import com.soul.fregata.domain.ExperimentParameterRepository;
import com.soul.fregata.entity.ExperimentParameter;
import com.soul.fregata.service.ExperimentParameterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Priority;
import java.sql.Timestamp;
import java.util.List;

/**
 * created by yangL
 */
@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
public class ExperimentParameterServiceImpl extends TemplateService<ExperimentParameter, String> implements ExperimentParameterService {

    private final ExperimentParameterRepository experimentParameterRepository;

    @Override
    protected AbstractRepositoryBase<ExperimentParameter, String> getRepository() {
        return experimentParameterRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insert(ExperimentParameter parameter) {
        parameter.setCreator(UserContext.username());
        parameter.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(parameter);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ExperimentParameter parameter) {
        parameter.setModifier(UserContext.username());
        parameter.setModifyTime(new Timestamp(System.currentTimeMillis()));
        super.update(parameter);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByExperimentId(String experimentId) {
        experimentParameterRepository.delete(SingleClause.equal("experimentId", experimentId));
        clearCache();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByExperimentIds(List<String> experimentIds) {
        experimentParameterRepository.delete(SingleClause.in("experimentId", experimentIds.toArray()));
        clearCache();
    }

    @Override
    public ExperimentParameter getByExperimentId(String experimentId) {
        return experimentParameterRepository.query(SingleClause.equal("experimentId", experimentId)).stream()
                .findFirst()
                .orElse(null);
    }

}
