package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.flagwind.persistent.model.Paging;
import com.flagwind.persistent.model.SingleClause;
import com.flagwind.persistent.model.Sorting;
import com.soul.weapon.condition.PipeTaskCondition;
import com.soul.weapon.domain.PipeTaskRepository;
import com.soul.weapon.entity.PipeTask;
import com.soul.weapon.entity.PipeTest;
import com.soul.weapon.service.PipeTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PipeTaskServiceImpl extends TemplateService<PipeTask,String> implements PipeTaskService {

    private final PipeTaskRepository pipeTaskRepository;

    @Override
    protected AbstractRepositoryBase<PipeTask,String> getRepository(){
        return pipeTaskRepository;
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
      super.update(pipeTask);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResult<PipeTask> page(QueryModel<PipeTaskCondition> model) {
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }
}

