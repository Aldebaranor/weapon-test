package com.soul.weapon.domain.historyInfo;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.historyInfo.InterceptDistanceReport;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@CacheConfig(cacheNames = InterceptDistanceReport.NAME)
public interface InterceptDistanceReportRepository extends AbstractRepositoryBase<InterceptDistanceReport,String> {
    @Select("select a.* from his_intercept_distance_report a,(SELECT targetId,max(createTime) createTime FROM his_intercept_distance_report GROUP BY targetId) b where a.targetId = b.targetId and a.createTime = b.createTime and taskId = #{taskId}")
    List<InterceptDistanceReport> getNewintDisByTaskId(String taskId);
}
