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
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePriorityByPair(FirePriority modPriority) {

        FirePriority tmp = firePriorityRepository.query(CombineClause.and(
                SingleClause.equal("weaponAId", modPriority.getWeaponAId()),
                SingleClause.equal("weaponBId", modPriority.getWeaponBId())
        )).stream().findFirst().orElse(null);
        if (null == tmp) {
            return;
        }
        tmp.setABetterThanB(modPriority.isABetterThanB());
        super.update(tmp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FirePriority> getPriorityByType(String conflictType) {
        return firePriorityRepository.query(
                SingleClause.equal("conflictType", conflictType)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FirePriority> listPriorities(FirePriorityCondition con) {
        return super.query(con, Sorting.descending("modifyTime", "createTime"));
        // return fireTaskRepository.query(SingleClause.equal("disabled", 0));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePriority(FirePriority p) {
        super.update(p);
    }
}
