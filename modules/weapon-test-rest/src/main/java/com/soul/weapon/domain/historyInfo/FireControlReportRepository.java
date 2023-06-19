package com.soul.weapon.domain.historyInfo;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.historyInfo.FireControlReport;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@CacheConfig(cacheNames = FireControlReport.NAME)
public interface FireControlReportRepository extends AbstractRepositoryBase<FireControlReport,String> {
    @Select("select a.* from his_fire_control_report a,(SELECT fireControlId,targetId,max(createTime) createTime FROM his_fire_control_report group by fireControlId,targetId) b where a.fireControlId = b.fireControlId and a.targetId = b.targetId and a.createTime = b.createTime and taskId = #{taskId}")
    List<FireControlReport> getNewfireControlByTaskId(String taskId);
}
