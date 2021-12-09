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
import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
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
public class PipeTestServiceImpl extends TemplateService<PipeTest,String> implements PipeTestService {

    private final PipeTestRepository pipeTestRepository;

    @Override
    protected AbstractRepositoryBase<PipeTest,String> getRepository(){
        return pipeTestRepository;
    }

    @Override
    public String insert(PipeTest pipeTest){
        pipeTest.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(pipeTest);
    }

    //TODO:1209
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@RequestBody PipeTest pipeTest){
        pipeTest.setModifyTime(new Timestamp(System.currentTimeMillis()));
        super.update(pipeTest);
    }

    //TODO:1209
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PipeTest> getByTaskId(String taskId) {
        PipeTestCondition pipeTestCondition=new PipeTestCondition();
        pipeTestCondition.setTaskId(taskId);
        return super.query(pipeTestCondition);
    }


    @Override
    public int deleteByTaskId(String taskId) {
        List<PipeTest> pipeTests = getByTaskId(taskId);

        List<String> ids = pipeTests.stream().map(PipeTest::getId).collect(Collectors.toList());
        //TODO:1209 可以用Lamda表达式，这样更优雅
//        List<String> ids=new ArrayList<>();
//        for (PipeTest pipeTest : pipeTests) {
//            ids.add(pipeTest.getId());
//        }
        return super.deleteByIds(ids);
    }

    //TODO:1209 两个事物需要加回滚，需要先判断第一步删除是否成功，否则无法进行第二步！！！
    @Override
    public void saveByTaskId(String taskId, List<PipeTest> list) {
        deleteByTaskId(taskId);
        super.insertList(list);
    }




}
