package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.flagwind.persistent.model.Sorting;
import com.soul.weapon.condition.ReactionReportCondition;
import com.soul.weapon.domain.historyInfo.ReactionReportRepository;
import com.soul.weapon.entity.historyInfo.ReactionReport;
import com.soul.weapon.service.ReactionReportService;
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
@CacheConfig(cacheNames = ReactionReport.NAME)
public class ReactionReportServiceImpl extends TemplateService<ReactionReport,String> implements ReactionReportService {

    private final ReactionReportRepository reactionReportRepository;

    @Override
    protected AbstractRepositoryBase<ReactionReport, String> getRepository() {
        return reactionReportRepository;
    }

    @Override
    public PageResult<ReactionReport> page(QueryModel<ReactionReportCondition> model) {
       return super.page(model.getCondition(),model.getPaging(),model.getSorts());
    }

    @Override
    public List<ReactionReport> list(ReactionReportCondition condition) {

        return super.query(condition,new Sorting[]{Sorting.descending("createTime")});
    }
}
