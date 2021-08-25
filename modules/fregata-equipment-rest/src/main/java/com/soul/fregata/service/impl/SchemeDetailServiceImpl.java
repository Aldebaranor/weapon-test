package com.soul.fregata.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.exception.ExceptionUtils;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.security.UserContext;
import com.flagwind.persistent.model.Sorting;
import com.soul.fregata.condition.SchemeDetailCondition;
import com.soul.fregata.domain.SchemeDetailRepository;
import com.soul.fregata.domain.SchemeRepository;
import com.soul.fregata.entity.Scheme;
import com.soul.fregata.entity.SchemeDetail;
import com.soul.fregata.service.SchemeDetailService;
import com.soul.fregata.utils.ValidateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Priority;
import java.sql.Timestamp;
import java.util.List;

/**
 * created by 迷途小码农
 */
@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
public class SchemeDetailServiceImpl extends TemplateService<SchemeDetail, String> implements SchemeDetailService {

    private final SchemeDetailRepository schemeDetailRepository;
    private final SchemeRepository schemeRepository;

    @Override
    protected AbstractRepositoryBase<SchemeDetail, String> getRepository() {
        return schemeDetailRepository;
    }

    @Override
    public PageResult<SchemeDetail> page(QueryModel<SchemeDetailCondition> model) {
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }


    @Override
    public List<SchemeDetail> list(SchemeDetailCondition condition) {
        return super.query(condition, Sorting.descending("modifyTime", "createTime"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(String schemeId, List<SchemeDetail> details) {
        Scheme scheme = schemeRepository.getById(schemeId);
        if (scheme == null) {
            throw ExceptionUtils.api("方案不存在");
        }
        schemeDetailRepository.deleteBySchemeId(schemeId);
        if (!CollectionUtils.isEmpty(details)) {
            for (SchemeDetail detail : details) {
                ValidateUtil.validate(detail);
                detail.setId(getKey());
                detail.setSchemeId(schemeId);
                detail.setCreateTime(new Timestamp(System.currentTimeMillis()));
                detail.setCreator(UserContext.username());
            }
            schemeDetailRepository.insertList(details);
        }
    }

}
