package com.soul.fire.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.soul.fire.domain.FireConflictPriorityRepository;
import com.soul.fire.entity.FireConflictPriority;
import com.soul.fire.service.FireConflictPriorityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Priority;
import java.util.List;

@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
@CacheConfig(cacheNames = FireConflictPriority.NAME)
public class FireConflictPriorityServiceImpl extends TemplateService<FireConflictPriority, String> implements FireConflictPriorityService {

    private  final FireConflictPriorityRepository fireConflictPriorityRepository;

    @Override
    protected  AbstractRepositoryBase<FireConflictPriority, String> getRepository() {
        return fireConflictPriorityRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FireConflictPriority> listPriorities() {
        return super.getAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePriorities(List<FireConflictPriority> priorities) {
        super.updateList(priorities);
        // for (FireConflictPriority priority: priorities) {
        //     super.update(priority);
        // }
    }

}
