package com.soul.weapon.service.impl;


import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.soul.weapon.condition.ComDictionaryItemCondition;
import com.soul.weapon.domain.ComDictionaryItemRepository;
import com.soul.weapon.entity.ComDictionaryItem;
import com.soul.weapon.service.ComDictionaryItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;
import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/2 15:35
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Priority(5)
@CacheConfig(cacheNames = ComDictionaryItem.NAME)
public class ComDictionaryItemServiceImpl extends TemplateService<ComDictionaryItem,String> implements ComDictionaryItemService  {

    public  final ComDictionaryItemRepository comDictionaryItemRepository;
    @Override
    public List<ComDictionaryItem> getByCode(String code) {
        ComDictionaryItemCondition dictionaryItemCondition=new ComDictionaryItemCondition();
        dictionaryItemCondition.setValue(code);
        return super.query(dictionaryItemCondition);
    }

    @Override
    protected AbstractRepositoryBase<ComDictionaryItem, String> getRepository() {
        return comDictionaryItemRepository;
    }
}
