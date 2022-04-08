package com.soul.weapon.domain.historyInfo;


import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.historyInfo.RadarPathReport;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@CacheConfig(cacheNames = RadarPathReport.NAME)
public interface RadarPathReportRepository extends AbstractRepositoryBase<RadarPathReport,String> {

    @Select("select a.* from his_radar_path_report a,(SELECT targetId,max(createTime) createTime FROM his_radar_path_report GROUP BY targetId) b where a.targetId = b.targetId and a.createTime = b.createTime and taskId = #{taskId}")
    List<RadarPathReport> getNewradarPathByTaskId(String taskId);
}
