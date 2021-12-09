package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.exception.ExceptionUtils;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.flagwind.commons.StringUtils;
import com.soul.weapon.condition.PipeTaskCondition;
import com.soul.weapon.condition.PipeTestCondition;
import com.soul.weapon.domain.PipeTaskRepository;
import com.soul.weapon.entity.PipeTask;
import com.soul.weapon.entity.PipeTest;
import com.soul.weapon.entity.codes.PipeState;
import com.soul.weapon.service.PipeTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Priority;
import java.sql.Timestamp;
import java.util.*;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Priority(5)
@CacheConfig(cacheNames = PipeTask.NAME)
public class PipeTaskServiceImpl extends TemplateService<PipeTask, String> implements PipeTaskService {

    public static List<String> pipeTestRunningCodes;
    private final PipeTaskRepository pipeTaskRepository;
    private final PipeTestServiceImpl pipeTestServiceImpl;

    @Override
    protected AbstractRepositoryBase<PipeTask, String> getRepository() {
        return pipeTaskRepository;
    }

    @Override
    public String insert(PipeTask pipeTask) {
        pipeTask.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(pipeTask);
    }

    @Override
    public void update(PipeTask pipeTask) {
        pipeTask.setModifyTime(new Timestamp(System.currentTimeMillis()));
        super.update(pipeTask);
    }

    @Override
    public String save(PipeTask pipeTask) {
        if (pipeTask == null) {
            throw ExceptionUtils.api("传参不能为空", new Object[0]);
        }

        if (StringUtils.isBlank(pipeTask.getId())) {
            return insert(pipeTask);
        } else {
            update(pipeTask);
            return pipeTask.getId();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResult<PipeTask> page(QueryModel<PipeTaskCondition> model) {
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startTest(String takeId, List<PipeTest> pipeTests) {
        PipeTask pipeTask = super.getById(takeId);
        pipeTask.setStatus(new PipeState("1"));
        super.update(pipeTask);
        for (PipeTest pipeTest : pipeTests) {
            pipeTestRunningCodes.add(pipeTest.getCode());
        }
        pipeTestServiceImpl.insertList(pipeTests);
    }



    @Override
    public List<PipeTask> getByName(String name) {
        PipeTaskCondition pipeTaskCondition = new PipeTaskCondition();
        pipeTaskCondition.setName(name);
        return super.query(pipeTaskCondition);
    }
}

