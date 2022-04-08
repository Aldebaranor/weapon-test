package com.soul.weapon.domain.historyInfo;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.historyInfo.WaterWeaponTestReport;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@CacheConfig(cacheNames = WaterWeaponTestReport.NAME)
public interface WaterWeaponTestReportRepository extends AbstractRepositoryBase<WaterWeaponTestReport,String> {

    @Select("SELECT a.* FROM his_water_weapon_report a,(select max(createTime) createTime from his_water_weapon_report) b where a.createTime = b.createTime and taskId = #{taskId}")
    WaterWeaponTestReport getNewwaterWeaponByTaskId(String taskId);
}
