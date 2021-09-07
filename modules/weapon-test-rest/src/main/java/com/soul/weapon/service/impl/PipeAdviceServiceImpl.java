package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.flagwind.persistent.model.SingleClause;
import com.soul.weapon.domain.PipeAdviceRepository;
import com.soul.weapon.entity.PipeAdvice;
import com.soul.weapon.service.PipeAdviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Priority;
import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Priority(5)
@CacheConfig(cacheNames = PipeAdvice.NAME)
public class PipeAdviceServiceImpl extends TemplateService<PipeAdvice,String> implements PipeAdviceService {

    public final PipeAdviceRepository pipeAdviceRepository;

    @Override
    protected AbstractRepositoryBase<PipeAdvice,String> getRepository(){
        return pipeAdviceRepository;
    }

    @Override
    public PipeAdvice getById(String id)
    {
        return pipeAdviceRepository.query(SingleClause.equal("id",id)).stream().findFirst().orElse(null);
    }

    @Override
    public List<PipeAdvice> getAll(){
        return pipeAdviceRepository.query(SingleClause.equal("disabled",0));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insert(@RequestBody PipeAdvice pipeAdvice){
        pipeAdvice.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(pipeAdvice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@RequestBody PipeAdvice pipeAdvice){
        pipeAdvice.setModifyTime(new Timestamp(System.currentTimeMillis()));
        pipeAdvice.setCreateTime(pipeAdviceRepository.query(SingleClause.equal("id", pipeAdvice.getId())).stream().findFirst().orElse(null).getCreateTime());
        super.update(pipeAdvice);
    }

}
