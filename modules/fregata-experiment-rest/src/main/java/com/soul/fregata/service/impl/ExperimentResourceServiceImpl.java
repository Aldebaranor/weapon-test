package com.soul.fregata.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.security.UserContext;
import com.flagwind.persistent.model.SingleClause;
import com.soul.fregata.condition.ExperimentResourceCondition;
import com.soul.fregata.domain.ExperimentResourceRepository;
import com.soul.fregata.entity.ExperimentResource;
import com.soul.fregata.service.ExperimentResourceService;
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
public class ExperimentResourceServiceImpl extends TemplateService<ExperimentResource, String> implements ExperimentResourceService {

    private final ExperimentResourceRepository experimentResourceRepository;

    @Override
    protected AbstractRepositoryBase<ExperimentResource, String> getRepository() {
        return experimentResourceRepository;
    }

    @Override
    public PageResult<ExperimentResource> page(QueryModel<ExperimentResourceCondition> model) {
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }

    @Override
    public List<ExperimentResource> list(ExperimentResourceCondition condition) {
        return super.query(condition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insert(ExperimentResource resource) {
        resource.setCreator(UserContext.username());
        resource.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(resource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ExperimentResource resource) {
        resource.setModifier(UserContext.username());
        resource.setModifyTime(new Timestamp(System.currentTimeMillis()));
        super.update(resource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByExperimentId(String experimentId) {
        experimentResourceRepository.delete(SingleClause.equal("experimentId", "experimentId"));
        clearCache();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByExperimentIds(List<String> experimentIds) {
        experimentResourceRepository.delete(SingleClause.in("experimentId", experimentIds.toArray()));
        clearCache();
    }

}
