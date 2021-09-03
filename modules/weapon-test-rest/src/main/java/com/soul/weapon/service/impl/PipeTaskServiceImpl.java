package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.flagwind.persistent.model.SingleClause;
import com.soul.weapon.domain.PipeTaskRepository;
import com.soul.weapon.entity.PipeTask;
import com.soul.weapon.service.PipeTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Priority;
import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Priority(5)
@CacheConfig(cacheNames = PipeTask.NAME)
public class PipeTaskServiceImpl extends TemplateService<PipeTask,String> implements PipeTaskService {

    public PipeTaskRepository pipeTaskRepository;

    @Override
    protected AbstractRepositoryBase<PipeTask,String> getRepository(){
        return pipeTaskRepository;
    }

    @Override
    public PipeTask getById(String id)
    {
        return pipeTaskRepository.query(SingleClause.equal("id",id)).stream().findFirst().orElse(null);
    }

    @Override
    public List<PipeTask> getAll(){
        return pipeTaskRepository.query(SingleClause.equal("disabled",0));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insert(@RequestBody PipeTask pipeTask){
            pipeTask.setCreateTime(new Timestamp(System.currentTimeMillis()));
            return super.insert(pipeTask);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@RequestBody PipeTask pipeTask){
        pipeTask.setModifyTime(new Timestamp(System.currentTimeMillis()));
        pipeTask.setCreateTime(pipeTaskRepository.query(SingleClause.equal("id", pipeTask.getId())).stream().findFirst().orElse(null).getCreateTime());
        super.update(pipeTask);
    }

}

