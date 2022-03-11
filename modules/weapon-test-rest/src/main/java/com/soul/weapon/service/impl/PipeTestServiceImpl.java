package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.entity.DictionaryItem;
import com.egova.exception.ExceptionUtils;
import com.egova.generic.service.DictionaryService;
import com.egova.generic.service.impl.DictionaryServiceImpl;
import com.egova.json.utils.JsonUtils;
import com.soul.weapon.condition.PipeTestCondition;
import com.soul.weapon.domain.MyDictionaryItemRepository;
import com.soul.weapon.domain.PipeTestRepository;
import com.soul.weapon.entity.PipeTest;
import com.soul.weapon.service.PipeTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Priority;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final MyDictionaryItemRepository myDictionaryItemRepository;


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
    public List<PipeTest> list(PipeTestCondition pipeTestCondition) {
        return super.query(pipeTestCondition);
    }


    @Override
    public List<PipeTest> getByTaskId(String taskId) {
        PipeTestCondition pipeTestCondition = new PipeTestCondition();
        pipeTestCondition.setTaskId(taskId);
        return super.query(pipeTestCondition);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByTaskId(String taskId) {
        PipeTestCondition pipeTestCondition = new PipeTestCondition();
        pipeTestCondition.setTaskId(taskId);
        return super.deleteByIds(super.query(pipeTestCondition).stream().map(PipeTest::getId).collect(Collectors.toList()));
    }


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

    @Override
    public List<DictionaryItem> getCurrentTaskTestItems(String taskId) {


        List<DictionaryItem> mapList = new ArrayList<>();
        List<DictionaryItem> resultList = new ArrayList<>();

        getPipeTestTree().forEach(dictionaryItem -> {
            List<DictionaryItem> children = (List<DictionaryItem>) dictionaryItem.get("children");
            mapList.addAll(children);
        });

        Map<String, DictionaryItem> map = mapList.stream().collect(Collectors.toMap(DictionaryItem::getCode, v -> v, (v1, v2) -> v1));

        getByTaskId(taskId).forEach(pipeTest -> {
            resultList.add(map.get(pipeTest.getCode()));
        });

        return resultList;
    }




}
