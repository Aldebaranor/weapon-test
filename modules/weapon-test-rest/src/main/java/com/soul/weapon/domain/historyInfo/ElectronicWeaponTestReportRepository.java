package com.soul.weapon.domain.historyInfo;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.historyInfo.ElectronicWeaponTestReport;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@CacheConfig(cacheNames = ElectronicWeaponTestReport.NAME)
public interface ElectronicWeaponTestReportRepository extends AbstractRepositoryBase<ElectronicWeaponTestReport,String> {


    @Select("SELECT a.* FROM his_electronic_weapon_report a,(select max(createTime) createTime from his_electronic_weapon_report) b where a.createTime = b.createTime and taskId = #{taskId}")
    ElectronicWeaponTestReport getNewelectWeaponByTaskId(String taskId);
}
