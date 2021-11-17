package com.soul.fire.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.flagwind.persistent.model.CombineClause;
import com.flagwind.persistent.model.SingleClause;
import com.flagwind.persistent.model.Sorting;
import com.soul.fire.condition.FirePriorityCondition;
import com.soul.fire.condition.FireTaskCondition;
import com.soul.fire.domain.FirePriorityRepository;
import com.soul.fire.entity.FirePriority;
import com.soul.fire.entity.FireTask;
import com.soul.fire.service.FirePriorityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.core.util.CollectionUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Priority;
import java.util.List;

@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
@CacheConfig(cacheNames = FirePriority.NAME)
public class FirePriorityServiceImpl extends TemplateService<FirePriority, String> implements FirePriorityService {

    private  final FirePriorityRepository firePriorityRepository;

    @Override
    protected  AbstractRepositoryBase<FirePriority, String> getRepository() {
        return firePriorityRepository;
    }

    //TODO :逻辑有问题。是不是应该根据主键

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePriorityByPair(FirePriority modPriority) {

        FirePriority tmp = firePriorityRepository.query(CombineClause.and(
                SingleClause.equal("weaponAId", modPriority.getWeaponAId()),
                SingleClause.equal("weaponBId", modPriority.getWeaponBId())
        )).stream().findFirst().orElse(null);
        if (tmp != null) {
            return;
        }
        tmp.setABetterThanB(modPriority.isABetterThanB());
        super.update(tmp);
        return;

    }

    @Override
    public FirePriority getPriorityByIds(String idA,String idB){
        FirePriorityCondition condition = new FirePriorityCondition();
        condition.setWeaponAId(idA);
        condition.setWeaponBId(idB);
        return super.query(condition).stream().findFirst().orElse(null);
    }

    @Override
    public List<FirePriority> getPriorityByType(String conflictType) {
        FirePriorityCondition condition = new FirePriorityCondition();
        condition.setConflictType(conflictType);
        return super.query(condition);

    }

    @Override
    public List<FirePriority> list(FirePriorityCondition condition) {
        return super.query(condition, Sorting.descending("modifyTime", "createTime"));

    }

}
