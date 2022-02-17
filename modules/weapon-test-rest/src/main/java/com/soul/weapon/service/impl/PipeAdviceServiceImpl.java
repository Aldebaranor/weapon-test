package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.exception.ExceptionUtils;
import com.flagwind.persistent.model.SingleClause;
import com.soul.weapon.condition.PipeAdviceCondition;
import com.soul.weapon.domain.PipeAdviceRepository;
import com.soul.weapon.entity.PipeAdvice;
import com.soul.weapon.service.PipeAdviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
@CacheConfig(cacheNames = PipeAdvice.NAME)
public class PipeAdviceServiceImpl extends TemplateService<PipeAdvice,String> implements PipeAdviceService {

    public final PipeAdviceRepository pipeAdviceRepository;

    @Override
    protected AbstractRepositoryBase<PipeAdvice,String> getRepository(){
        return pipeAdviceRepository;
    }


    @Override
    public String insert(@RequestBody PipeAdvice entity){
        if(StringUtils.isEmpty(entity.getCode())){
            throw ExceptionUtils.api("code为空，新增或修改失败");
        }
        PipeAdvice advice = this.getByCode(entity.getCode());
        if(advice == null){
            entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            return super.insert(entity);
        }
            entity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            super.update(entity);
            return "修改成功";
    }

/*    @Override
    public void update(@RequestBody PipeAdvice pipeAdvice){

        pipeAdvice.setModifyTime(new Timestamp(System.currentTimeMillis()));
        super.update(pipeAdvice);
    }*/

    @Override
    public PipeAdvice getByCode(String code) {
        PipeAdviceCondition pipeAdviceCondition = new PipeAdviceCondition();
        pipeAdviceCondition.setCode(code);
        return pipeAdviceRepository.single("code", code);
    }

}
