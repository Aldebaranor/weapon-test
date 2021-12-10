package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.exception.ExceptionUtils;
import com.egova.json.utils.JsonUtils;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.redis.RedisUtils;
import com.flagwind.commons.StringUtils;
import com.soul.weapon.condition.PipeTaskCondition;
import com.soul.weapon.condition.PipeTestCondition;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
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
import org.springframework.util.CollectionUtils;
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

    private final PipeTaskRepository pipeTaskRepository;
    private final CommonConfig config;

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
    public PageResult<PipeTask> page(QueryModel<PipeTaskCondition> model) {
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }

    @Override
    public Boolean startTest(String takeId, List<PipeTest> pipeTests) {

        String currentTaskId = RedisUtils.getService(config.getPumpDataBase()).extrasForValue().get(Constant.WEAPON_CURRENT_TASK);
        if (!StringUtils.isEmpty(currentTaskId)) {
            throw ExceptionUtils.api("已有任务正在运行，请关闭当前运行任务再开启本任务", new Object[0]);
        }
        if (CollectionUtils.isEmpty(pipeTests)) {
            throw ExceptionUtils.api("当前任务没有测试项", new Object[0]);
        }
        RedisUtils.getService(config.getPumpDataBase()).extrasForValue().set(Constant.WEAPON_CURRENT_TASK, takeId);
        for (PipeTest pipeTest : pipeTests) {
            RedisUtils.getService(config.getPumpDataBase()).opsForHash().put(Constant.WEAPON_CURRENT_PIPETEST, pipeTest.getCode(), JsonUtils.serialize(pipeTest));
        }
        PipeTask pipeTask = super.getById(takeId);
        pipeTask.setStatus(new PipeState("1"));
        super.update(pipeTask);
        return true;
    }

    @Override
    public Boolean stopTest(String takeId) {
        if (!StringUtils.isEmpty(takeId)) {
            throw ExceptionUtils.api("任务Id为空", new Object[0]);
        }
        PipeTask pipeTask = new PipeTask();
        pipeTask.setId(takeId);
        pipeTask.setStatus(new PipeState("2"));
        super.update(pipeTask);
        RedisUtils.getService(config.getPumpDataBase()).delete(Constant.WEAPON_CURRENT_TASK);
        RedisUtils.getService(config.getPumpDataBase()).delete(Constant.WEAPON_CURRENT_PIPETEST);
        return true;
    }

    @Override
    public List<PipeTask> getByName(String name) {
        PipeTaskCondition pipeTaskCondition = new PipeTaskCondition();
        pipeTaskCondition.setName(name);
        return super.query(pipeTaskCondition);
    }
}


