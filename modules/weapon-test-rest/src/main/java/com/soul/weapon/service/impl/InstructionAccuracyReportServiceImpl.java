package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.InstructionAccuracyReportCondition;
import com.soul.weapon.domain.historyInfo.InstructionAccuracyReportRepository;
import com.soul.weapon.entity.historyInfo.InstructionAccuracyReport;
import com.soul.weapon.service.InstructionAccuracyReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;
import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Priority(5)
@CacheConfig(cacheNames = InstructionAccuracyReport.NAME)
public class InstructionAccuracyReportServiceImpl extends TemplateService<InstructionAccuracyReport,String> implements InstructionAccuracyReportService {

    private final InstructionAccuracyReportRepository instructionAccuracyReportRepository;

    @Override
    protected AbstractRepositoryBase<InstructionAccuracyReport, String> getRepository() {
        return instructionAccuracyReportRepository;
    }

    @Override
    public PageResult<InstructionAccuracyReport> page(QueryModel<InstructionAccuracyReportCondition> model) {
       return super.page(model.getCondition(),model.getPaging(),model.getSorts());
    }

    @Override
    public List<InstructionAccuracyReport> list(InstructionAccuracyReportCondition condition) {
        //TODO:
        return super.query(condition,null);
    }
}
