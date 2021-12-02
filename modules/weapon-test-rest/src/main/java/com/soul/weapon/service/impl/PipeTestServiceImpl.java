package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.flagwind.persistent.model.SingleClause;
import com.soul.weapon.condition.PipeTestCondition;
import com.soul.weapon.domain.PipeTestRepository;
import com.soul.weapon.entity.PipeTest;
import com.soul.weapon.service.PipeTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Priority;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Priority(5)
@CacheConfig(cacheNames = PipeTest.NAME)
public class PipeTestServiceImpl extends TemplateService<PipeTest,String> implements PipeTestService {

    private final PipeTestRepository pipeTestRepository;
    private final PipeTestCondition pipeTestCondition;

    @Override
    protected AbstractRepositoryBase<PipeTest,String> getRepository(){
        return pipeTestRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insert(@RequestBody PipeTest pipeTest){
        pipeTest.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(pipeTest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@RequestBody PipeTest pipeTest){
        pipeTest.setModifyTime(new Timestamp(System.currentTimeMillis()));
        super.update(pipeTest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PipeTest> getByTaskId(String taskId) {
        pipeTestCondition.setTaskId(taskId);
        return super.query(pipeTestCondition);
    }


}
