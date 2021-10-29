package com.soul.fire.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.flagwind.persistent.model.Sorting;
import com.soul.fire.condition.FireTaskCondition;
import com.soul.fire.domain.FireTaskRepository;
import com.soul.fire.entity.FireTask;
import com.soul.fire.service.FireTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.validator.ValidatorUtil;
import org.etsi.uri.x01903.v13.GenericTimeStampType;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Priority;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
@CacheConfig(cacheNames = FireTask.NAME)
public class FireTaskServiceImpl extends TemplateService<FireTask, String> implements FireTaskService{

    private  final FireTaskRepository fireTaskRepository;

    @Override
    protected  AbstractRepositoryBase<FireTask, String> getRepository() {
        return fireTaskRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FireTask getByName(String name) {
        FireTaskCondition con = new FireTaskCondition();
        con.setName(name);
        return super.query(con).stream().findFirst().orElse(null);
        // return fireTaskRepository.query(SingleClause.equal("name", name)).stream().findFirst().orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTask(FireTask task) {
//        FireTask tmpTask = getCurTask();
//        if(tmpTask == null) { // then get the latest created one and set to running
//            // List<FireTask> sortedByCreatedTime =
//            //         fireTaskRepository.query(SingleClause.equal("disabled", false));
//
//            FireTaskCondition condition = new FireTaskCondition();
//            condition.setRunning(running);
//            List<FireTask> sortedByCreatedTime = super.query(condition);
//
//            sortedByCreatedTime.sort(Comparator.comparing(FireTask::getStartTime).reversed());
//            tmpTask = sortedByCreatedTime.get(0);
//        }
//        tmpTask.setRunning(running);
        super.update(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertTask(FireTask task) {
        return super.insert(task);
        // String uuid = getKey();
        // task.setId(uuid);
        // Timestamp tmp = new Timestamp((System.currentTimeMillis()));
        // task.setStartTime(tmp);
        // task.setCreateTime(tmp);
        // task.setCreator(UserContext.username());
        // super.insert(task);
        // return task;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public FireTask getCurTask() {
        FireTaskCondition con = new FireTaskCondition();
        con.setDisabled(false);
        return super.query(con).stream().findFirst().orElse(null);
        // return fireTaskRepository.query(CombineClause.and(
        //     SingleClause.equal("running", true),
        //     SingleClause.equal("disabled", false)
        // )).stream().findFirst().orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FireTask> list(FireTaskCondition con) {
        return super.query(con, Sorting.descending("modifyTime", "createTime"));
        // return fireTaskRepository.query(SingleClause.equal("disabled", 0));
    }
}
