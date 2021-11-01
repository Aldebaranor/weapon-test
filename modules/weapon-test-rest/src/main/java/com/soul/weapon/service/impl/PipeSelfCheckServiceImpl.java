package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.flagwind.persistent.model.SingleClause;
import com.soul.weapon.condition.PipeSelfCheckCondition;
import com.soul.weapon.domain.PipeSelfCheckRepository;
import com.soul.weapon.entity.PipeSelfCheck;
import com.soul.weapon.entity.PipeTask;
import com.soul.weapon.service.PipeSelfCheckService;
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
@CacheConfig(cacheNames = PipeSelfCheck.NAME)
public class PipeSelfCheckServiceImpl extends TemplateService<PipeSelfCheck,String> implements PipeSelfCheckService {

    private final PipeSelfCheckRepository pipeSelfCheckRepository;

    @Override
    protected AbstractRepositoryBase<PipeSelfCheck,String> getRepository(){
        return pipeSelfCheckRepository;
    }


    @Override
    public PipeSelfCheck getByName(String name)
    {
        PipeSelfCheckCondition pipeSelfCheckCondition = new PipeSelfCheckCondition();
        pipeSelfCheckCondition.setName(name);
        return super.query(pipeSelfCheckCondition).stream().findFirst().orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insert(@RequestBody PipeSelfCheck pipeSelfCheck){
        pipeSelfCheck.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(pipeSelfCheck);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@RequestBody PipeSelfCheck pipeSelfCheck){
        pipeSelfCheck.setModifyTime(new Timestamp(System.currentTimeMillis()));
        super.update(pipeSelfCheck);
    }
}
