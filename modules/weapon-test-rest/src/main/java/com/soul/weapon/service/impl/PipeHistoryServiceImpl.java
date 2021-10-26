package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.flagwind.persistent.model.SingleClause;
import com.soul.weapon.domain.PipeHistoryRepository;
import com.soul.weapon.entity.PipeHistory;
import com.soul.weapon.service.PipeHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Priority;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Priority(5)
@CacheConfig(cacheNames = PipeHistory.NAME)
public class PipeHistoryServiceImpl extends TemplateService<PipeHistory,String> implements PipeHistoryService {

    private final PipeHistoryRepository pipeHistoryRepository;

    @Override
    protected AbstractRepositoryBase<PipeHistory,String> getRepository(){
        return pipeHistoryRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insert(@RequestBody PipeHistory pipeHistory){
        pipeHistory.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(pipeHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@RequestBody PipeHistory pipeHistory){
        pipeHistory.setModifyTime(new Timestamp(System.currentTimeMillis()));
        pipeHistory.setCreateTime(pipeHistoryRepository.query(SingleClause.equal("id", pipeHistory.getId())).stream().findFirst().orElse(null).getCreateTime());
        super.update(pipeHistory);
    }
    
}
