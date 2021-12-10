package com.soul.fire.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.flagwind.persistent.model.Clause;
import com.flagwind.persistent.model.CombineClause;
import com.flagwind.persistent.model.SingleClause;
import com.soul.fire.condition.FireConflictCondition;
import com.soul.fire.domain.FireConflictRepository;
import com.soul.fire.entity.FireConflict;
import com.soul.fire.service.FireConflictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Priority;
import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
@Priority(1)
@RequiredArgsConstructor
@CacheConfig(cacheNames = FireConflict.NAME)
public class FireConflictServiceImpl extends TemplateService<FireConflict, String> implements FireConflictService {

    private final FireConflictRepository fireConflictRepository;

    @Override
    protected AbstractRepositoryBase<FireConflict, String> getRepository() {
        return fireConflictRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insert(FireConflict fireConflict) {
        fireConflict.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(fireConflict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FireConflict fireConflict) {
        fireConflict.setModifyTime(new Timestamp(System.currentTimeMillis()));
         super.update(fireConflict);
    }


    @Override
    public FireConflict getById(String id){
          FireConflictCondition conflictCondition = new FireConflictCondition();
          conflictCondition.setId(id);
          conflictCondition.setDisabled(false);
          return super.query(conflictCondition).stream().findFirst().orElse(null);
    }

    @Override
    public List<FireConflict> list(FireConflictCondition condition) {
        return super.query(condition);
    }

    @Override
    public PageResult<FireConflict> page(QueryModel<FireConflictCondition> model) {
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }

    /**
     * 根据冲突类型type和任务id查询
     * @param type
     * @param taskId
     * @return 冲突表
     */
    @Override
    public List<FireConflict> getByTypeAndTask(String type,String taskId){

        FireConflictCondition conflictCondition = new FireConflictCondition();
        conflictCondition.setConflictType(type);
        conflictCondition.setTaskId(taskId);
        return super.query(conflictCondition);

    }

}
