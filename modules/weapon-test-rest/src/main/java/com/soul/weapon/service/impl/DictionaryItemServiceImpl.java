package com.soul.weapon.service.impl;


import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.entity.DictionaryItem;
import com.egova.generic.condition.DictionaryItemCondition;
import com.soul.weapon.service.DictionaryItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@CacheConfig(cacheNames = DictionaryItem.NAME)
public class DictionaryItemServiceImpl extends TemplateService<DictionaryItem, String> implements DictionaryItemService {

    public  final com.egova.generic.domain.DictionaryItemRepository DictionaryItemRepository;
    @Override
    public List<DictionaryItem> getByCode(String code) {
        DictionaryItemCondition dictionaryItemCondition=new DictionaryItemCondition();
        dictionaryItemCondition.setValue(code);
        return super.query(dictionaryItemCondition);
    }

    @Override
    protected AbstractRepositoryBase<DictionaryItem, String> getRepository() {
        return DictionaryItemRepository;
    }
}
