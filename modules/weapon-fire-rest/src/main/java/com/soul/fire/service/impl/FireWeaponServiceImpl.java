package com.soul.fire.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.flagwind.persistent.model.Clause;
import com.flagwind.persistent.model.CombineClause;
import com.flagwind.persistent.model.SingleClause;
import com.flagwind.persistent.model.Sorting;
import com.soul.fire.condition.FireThresholdCondition;
import com.soul.fire.condition.FireWeaponCondition;
import com.soul.fire.domain.FireWeaponRepository;
import com.soul.fire.entity.FireWeapon;
import com.soul.fire.service.FireWeaponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Priority;
import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
@CacheConfig(cacheNames = FireWeapon.NAME)
public class FireWeaponServiceImpl extends TemplateService<FireWeapon, String> implements FireWeaponService {

    private final FireWeaponRepository fireWeaponRepository;

    @Override
    protected AbstractRepositoryBase<FireWeapon, String> getRepository() {
        return fireWeaponRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insert(FireWeapon fireWeapon) {
        fireWeapon.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(fireWeapon);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FireWeapon fireWeapon) {
        fireWeapon.setModifyTime(new Timestamp(System.currentTimeMillis()));
        fireWeapon.setCreateTime(fireWeaponRepository.query(SingleClause.equal("id", fireWeapon.getId())).stream().findFirst().orElse(null).getCreateTime());
        super.update(fireWeapon);
    }

    @Override
    public FireWeapon getById(String id){
        return fireWeaponRepository.query(SingleClause.equal("id", id)).stream().findFirst().orElse(null);
    }

    @Override
    public List<FireWeapon> list(FireWeaponCondition condition) {
        return super.query(condition, Sorting.descending("modifyTime", "createTime"));
    }

}
