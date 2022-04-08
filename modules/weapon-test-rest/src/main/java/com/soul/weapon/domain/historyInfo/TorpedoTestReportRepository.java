package com.soul.weapon.domain.historyInfo;


import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.historyInfo.AntiMissileShipGunTestReport;
import com.soul.weapon.entity.historyInfo.TorpedoTestReport;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@CacheConfig(cacheNames = TorpedoTestReport.NAME)
public interface TorpedoTestReportRepository extends AbstractRepositoryBase<TorpedoTestReport,String> {

    @Select("SELECT a.* FROM his_torpedo_report a,(select max(createTime) createTime from his_torpedo_report) b where a.createTime = b.createTime and taskId = #{taskId}")
    TorpedoTestReport getNewTorpeByTaskId(String taskId);
}
