package com.soul.weapon.domain.historyInfo;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.historyInfo.InfoProcessTestReport;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@CacheConfig(cacheNames = InfoProcessTestReport.NAME)
public interface InfoProcessTestReportRepository extends AbstractRepositoryBase<InfoProcessTestReport,String> {

    @Select("select a.* from his_info_process_report a,(SELECT targetId,max(createTime) createTime FROM his_info_process_report GROUP BY targetId) b where a.targetId = b.targetId and a.createTime = b.createTime and taskId = #{taskId}")
    List<InfoProcessTestReport> getNewinfoProcessTestByTaskId(String taskId);
}
