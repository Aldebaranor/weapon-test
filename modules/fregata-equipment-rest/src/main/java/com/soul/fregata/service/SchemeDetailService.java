package com.soul.fregata.service;

import com.soul.fregata.condition.SchemeDetailCondition;
import com.soul.fregata.entity.SchemeDetail;
import com.soul.fregata.facade.SchemeDetailFacade;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;

import java.util.List;

/**
 * created by 迷途小码农
 */
public interface SchemeDetailService extends SchemeDetailFacade {

    /**
     * 分页查询
     *
     * @param model QueryModel
     * @return 分页数据
     */
    PageResult<SchemeDetail> page(QueryModel<SchemeDetailCondition> model);

    /**
     * 主键批量删除
     *
     * @param ids 主键
     * @return 影响记录行数
     */
    int deleteByIds(List<String> ids);

    /**
     * 列表查询
     * @param condition
     * @return
     */
    List<SchemeDetail> list(SchemeDetailCondition condition);

    /**
     * 保存
     * @param schemeId
     * @param details
     */
    void save(String schemeId, List<SchemeDetail> details);

}
