package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.entity.DictionaryItem;
import com.egova.generic.service.DictionaryService;
import com.soul.weapon.condition.PipeTestCondition;
import com.soul.weapon.domain.PipeTestRepository;
import com.soul.weapon.entity.PipeTest;
import com.soul.weapon.service.PipeTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Priority;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Priority(5)
@CacheConfig(cacheNames = PipeTest.NAME)
public class PipeTestServiceImpl extends TemplateService<PipeTest, String> implements PipeTestService {

    private final PipeTestRepository pipeTestRepository;
    private final DictionaryService dictionaryService;

    @Override
    protected AbstractRepositoryBase<PipeTest, String> getRepository() {
        return pipeTestRepository;
    }

    @Override
    public String insert(PipeTest pipeTest) {
        pipeTest.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(pipeTest);
    }

    @Override
    public void update(PipeTest pipeTest) {
        pipeTest.setModifyTime(new Timestamp(System.currentTimeMillis()));
        super.update(pipeTest);
    }

    @Override
    public List<PipeTest> getByTaskId(String taskId) {
        PipeTestCondition pipeTestCondition = new PipeTestCondition();
        pipeTestCondition.setTaskId(taskId);
        return super.query(pipeTestCondition);
    }

    @Override
    public List<PipeTest> list(PipeTestCondition pipeTestCondition) {
        return super.query(pipeTestCondition);
    }


    @Override
    public int deleteByTaskId(String taskId) {
        List<PipeTest> pipeTests = getByTaskId(taskId);
        List<String> ids = pipeTests.stream().map(PipeTest::getId).collect(Collectors.toList());
        return super.deleteByIds(ids);
    }

    //TODO:1209 两个事物需要加回滚，需要先判断第一步删除是否成功，否则无法进行第二步！！！
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveByTaskId(String taskId, List<PipeTest> list) {
        int i = deleteByTaskId(taskId);
        if (i != 0) {
            super.insertList(list);
        }


    }

    @Override
    public List<DictionaryItem> getPipeTestTree() {
        return dictionaryService.getItemTreeByType("weapon-test:pipe-test-type");
    }

}
