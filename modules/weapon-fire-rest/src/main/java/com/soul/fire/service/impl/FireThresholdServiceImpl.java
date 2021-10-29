package com.soul.fire.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.flagwind.persistent.model.Clause;
import com.flagwind.persistent.model.CombineClause;
import com.flagwind.persistent.model.SingleClause;
import com.flagwind.persistent.model.Sorting;
import com.soul.fire.condition.FireThresholdCondition;
import com.soul.fire.condition.FireWeaponCondition;
import com.soul.fire.domain.FireThresholdRepository;
import com.soul.fire.entity.FireThreshold;
import com.soul.fire.entity.FireWeapon;
import com.soul.fire.service.FireThresholdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Priority;
import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
@CacheConfig(cacheNames = FireThreshold.NAME)
public class FireThresholdServiceImpl extends TemplateService<FireThreshold, String> implements FireThresholdService {

    private final FireThresholdRepository fireThresholdRepository;

    @Override
    protected AbstractRepositoryBase<FireThreshold, String> getRepository() {
        return fireThresholdRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insert(FireThreshold fireThreshold) {
        fireThreshold.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(fireThreshold);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FireThreshold fireThreshold) {
        fireThreshold.setModifyTime(new Timestamp(System.currentTimeMillis()));
        fireThreshold.setCreateTime(fireThresholdRepository.query(SingleClause.equal("id", fireThreshold.getId())).stream().findFirst().orElse(null).getCreateTime());
        super.update(fireThreshold);
    }

    @Override
    public FireThreshold getById(String id){
        FireThresholdCondition fireThresholdCondition = new FireThresholdCondition();
        fireThresholdCondition.setId(id);
        return super.query(fireThresholdCondition).stream().findFirst().orElse(null);
    }

    @Override
    public PageResult<FireThreshold> page(QueryModel<FireThresholdCondition> model) {
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }

    /**
     * 列表查询全部
     *
     * @return 阈值表
     */
    @Override
    public List<FireThreshold> getAll() {
        FireThresholdCondition fireThresholdCondition = new FireThresholdCondition();
        fireThresholdCondition.setDisabled(false);
        return super.query(fireThresholdCondition);//.stream().collect(Collectors.toList());
    }

    @Override
    public List<FireThreshold> list(FireThresholdCondition condition) {
        return super.query(condition, Sorting.descending("modifyTime", "createTime"));
    }

}
