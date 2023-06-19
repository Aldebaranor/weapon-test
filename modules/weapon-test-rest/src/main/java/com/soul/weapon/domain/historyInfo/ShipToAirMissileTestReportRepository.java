package com.soul.weapon.domain.historyInfo;


import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.config.Constant;
import com.soul.weapon.entity.historyInfo.ShipToAirMissileTestReport;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@CacheConfig(cacheNames = ShipToAirMissileTestReport.NAME)
public interface ShipToAirMissileTestReportRepository extends AbstractRepositoryBase<ShipToAirMissileTestReport,String> {

    @Select("SELECT a.* FROM his_sam_report a,(select max(createTime) createTime from his_sam_report) b where a.createTime = b.createTime and taskId = #{taskId}")
    ShipToAirMissileTestReport getNewShipByTaskId(@Param("taskId") String taskId);
}
