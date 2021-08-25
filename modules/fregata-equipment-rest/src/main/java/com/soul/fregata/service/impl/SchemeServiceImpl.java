package com.soul.fregata.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.exception.ExceptionUtils;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.security.UserContext;
import com.flagwind.commons.StringUtils;
import com.flagwind.persistent.model.Sorting;
import com.soul.fregata.condition.SchemeCondition;
import com.soul.fregata.domain.SchemeRepository;
import com.soul.fregata.entity.Scheme;
import com.soul.fregata.service.SchemeService;
import com.soul.fregata.utils.ValidateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
public class SchemeServiceImpl extends TemplateService<Scheme, String> implements SchemeService {

    private final SchemeRepository schemeRepository;

    @Override
    protected AbstractRepositoryBase<Scheme, String> getRepository() {
        return schemeRepository;
    }

    @Override
    public PageResult<Scheme> page(QueryModel<SchemeCondition> model) {
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }

    @Override
    public List<Scheme> defaultScheme(String equipmentId) {
//        SchemeCondition schemeCondition = new SchemeCondition();
//        schemeCondition.setEquipmentId(equipmentId);
//        schemeCondition.setBeDefault(true);
//        return super.seek(schemeCondition);
        return schemeRepository.equipmentId(equipmentId);
    }

    @Override
    public List<Scheme> list(SchemeCondition condition) {
        return super.query(condition, Sorting.descending("modifyTime", "createTime"));
    }

    @Override
    public String insert(Scheme entity) {
        ValidateUtil.validate(entity);
        String id = getKey();
        entity.setId(id);
        entity.setCode(id);
        entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        entity.setCreator(UserContext.username());
        if (entity.getBeDefault() == null) {
            entity.setBeDefault(true);
        }
        return super.insert(entity);
    }

    @Override
    public void update(Scheme entity) {
        ValidateUtil.validate(entity);
        if (StringUtils.isBlank(entity.getId())) {
            throw ExceptionUtils.api("方案主键不能为空");
        }
        if (entity.getBeDefault() == null) {
            entity.setBeDefault(true);
        }
        entity.setModifyTime(new Timestamp(System.currentTimeMillis()));
        entity.setModifier(UserContext.username());
        super.update(entity);
    }

    @Override
    public Scheme getById(String s) {
        return super.seekById(s);
    }

}
