package com.soul.weapon.domain.historyInfo;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.historyInfo.LauncherRotationReport;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@CacheConfig(cacheNames = LauncherRotationReport.NAME)
public interface LauncherRotationReportRepository extends AbstractRepositoryBase<LauncherRotationReport,String> {

    @Select("select a.* from his_launcher_rotation_report a,(SELECT weaponId,targetId,max(createTime) createTime FROM his_launcher_rotation_report group by weaponId,targetId) b where a.weaponId = b.weaponId and a.targetId = b.targetId and a.createTime = b.createTime and taskId = #{taskId}")
    List<LauncherRotationReport> getNewlauRotByTaskId(String taskId);
}
