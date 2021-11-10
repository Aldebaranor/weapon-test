package com.soul.fire.service.impl;

import com.soul.fire.service.FireConflictCharge;
import com.soul.fire.controller.unity.FireThresholdController;
import com.soul.fire.controller.unity.FireWeaponController;
import com.soul.fire.entity.FireThreshold;
import com.soul.weapon.model.ChargeReport;
import com.soul.weapon.model.dds.EquipmentStatus;
import com.soul.weapon.model.ReportDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;

/**
 * @Author: XinLai
 * @Date: 2021/11/1 11:11
 */
@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
public class FireConflictChargeImpl implements FireConflictCharge {

    private final String TIME_ID = "1";
    private final String PITCH_ID = "2";
    private final String AZIMUTH_ID = "3";
    private final String ELECTFREQUENCY_ID = "12";
    private final String WATERFREQUENCY_ID = "13";

    ReportDetail chargeReportDetailA = new ReportDetail();
    ReportDetail chargeReportDetailB = new ReportDetail();
    ChargeReport chargeReport = new ChargeReport();

    public FireThresholdController fireThresholdController;
    public FireWeaponController fireWeaponController;

    // 从阈值表项中获取阈值
    private Long  fireChargeTimeThreshold = 3L;
    private Float fireChargePitchAngleThreshold = Float.valueOf(fireThresholdController.getById("2").getThresholdValue());
    private Float fireChargeAzimuthThreshold = Float.valueOf(fireThresholdController.getById("3").getThresholdValue());
    private Float posAx = 0.0F;
    private Float posAy = 0.0F;
    private Float posBx = 0.0F;
    private Float posBy = 0.0F;

    private Long electTimeThreshold = 1L;
    private Float electFrequencyThreshold = 10.0F;

    private Long waterTimeThreshold = 1L;
    private Float waterFrequencyThreshold = 5.0F;

    @Override
    public ChargeReport chargeReport(EquipmentStatus equipmentStatusA,EquipmentStatus equipmentStatusB){

        ReadThreshold(equipmentStatusA,equipmentStatusB);

        chargeReport.setId(equipmentStatusA.getEquipmentId()+" " +equipmentStatusB.getEquipmentId());

        Long timeA = equipmentStatusA.getTime();
        Long timeB = equipmentStatusB.getTime();

        // 设置管控时间
        chargeReport.setTime(Math.min(timeA,timeB));
        // 此处设置管控装备和自由装备id（应该根据管控措施具体实现）
        chargeReport.setChargeEquipId(equipmentStatusA.getEquipmentId());
        chargeReport.setFreeEquipId(equipmentStatusB.getEquipmentId());
        // 是否都处于工作状态
        boolean beWork = equipmentStatusA.getBeWork()&&equipmentStatusB.getBeWork();

        if(equipmentStatusA.getLaunchAzimuth()!=null && equipmentStatusB.getLaunchAzimuth()!=null){

            // 火力兼容管控计算
            boolean timeState = Math.abs(equipmentStatusA.getTime()-equipmentStatusB.getTime())<fireChargeTimeThreshold;
            boolean pitchState = Math.abs(equipmentStatusA.getLaunchPitchAngle()-equipmentStatusB.getLaunchPitchAngle())<fireChargePitchAngleThreshold;

            boolean azimuthState = Math.sqrt((Math.pow((posAx-posBx),2)+Math.pow((posAy-posBy),2)/100))<fireChargeAzimuthThreshold;

            if(timeState && pitchState && azimuthState && beWork){
                chargeReport.setChargeType(0);
                GenerateDetail(equipmentStatusA,equipmentStatusB);
                /*
                     根据装备A和装备B去管控措施表中查询相关信息
                     （需要多加一个措施表项？）
                     ......
                     输出具体的管控措施
                     ......
                 */
                return chargeReport;
            }else {
                return null;
            }
        }else if(equipmentStatusA.getElectromagneticFrequency()!=null && equipmentStatusB.getElectromagneticFrequency()!=null){
            // 电磁冲突预判
            boolean timeState = Math.abs(equipmentStatusA.getTime()-equipmentStatusB.getTime())<electTimeThreshold;
            boolean frequencyState = Math.abs(equipmentStatusA.getElectromagneticFrequency()-equipmentStatusB.getElectromagneticFrequency())< electFrequencyThreshold;

            if(timeState && frequencyState && beWork){
                chargeReport.setChargeType(1);
                GenerateDetail(equipmentStatusA,equipmentStatusB);
                return chargeReport;
            }else{
                return null;
            }

        }else if(equipmentStatusA.getMaxFrequency()!=null && equipmentStatusB.getMinFrequency()!=null){
            // 水声冲突预判
            Float minFreA = equipmentStatusA.getMinFrequency();
            Float maxFreA = equipmentStatusA.getMaxFrequency();
            Float minFreB = equipmentStatusB.getMinFrequency();
            Float maxFreB = equipmentStatusB.getMaxFrequency();

            boolean timeState = Math.abs(equipmentStatusA.getTime()-equipmentStatusB.getTime())<waterTimeThreshold;
            boolean frequencyState =(0<(minFreB-maxFreA) &&(minFreB-maxFreA)<waterFrequencyThreshold)||(0<(minFreA-maxFreB) &&(minFreA-maxFreB)< waterFrequencyThreshold);

            if(timeState && frequencyState && beWork){
                chargeReport.setChargeType(2);
                GenerateDetail(equipmentStatusA,equipmentStatusB);
                return chargeReport;
            }else{
                return null;
            }
        }
        return null;
    }


    /**
     * 生成冲突实体详细内容
     * @param equipmentStatusA
     * @param equipmentStatusB
     */
    private void GenerateDetail(EquipmentStatus equipmentStatusA, EquipmentStatus equipmentStatusB){

        chargeReportDetailA.setId(chargeReport.getId());
        chargeReportDetailA.setId(equipmentStatusA.getEquipmentId());
        chargeReportDetailA.setEquipmentTypeId(equipmentStatusA.getEquipmentTypeId());
        chargeReportDetailA.setEquipmentMode(equipmentStatusA.getEquipmentMode());
        chargeReportDetailA.setElectromagneticFrequency(equipmentStatusA.getElectromagneticFrequency());
        chargeReportDetailA.setMinFrequency(equipmentStatusA.getMinFrequency());
        chargeReportDetailA.setMaxFrequency(equipmentStatusA.getMaxFrequency());
        chargeReportDetailA.setLaunchAzimuth(equipmentStatusA.getLaunchAzimuth());
        chargeReportDetailA.setLaunchPitchAngle(equipmentStatusA.getLaunchPitchAngle());

        chargeReportDetailB.setId(chargeReport.getId());
        chargeReportDetailB.setId(equipmentStatusB.getEquipmentId());
        chargeReportDetailB.setEquipmentTypeId(equipmentStatusB.getEquipmentTypeId());
        chargeReportDetailB.setEquipmentMode(equipmentStatusB.getEquipmentMode());
        chargeReportDetailB.setElectromagneticFrequency(equipmentStatusB.getElectromagneticFrequency());
        chargeReportDetailB.setMinFrequency(equipmentStatusB.getMinFrequency());
        chargeReportDetailB.setMaxFrequency(equipmentStatusB.getMaxFrequency());
        chargeReportDetailB.setLaunchAzimuth(equipmentStatusB.getLaunchAzimuth());
        chargeReportDetailB.setLaunchPitchAngle(equipmentStatusB.getLaunchPitchAngle());
    }

    /**
     * 从数据库中读取阈值
     */
    private void ReadThreshold(EquipmentStatus equipmentStatusA,EquipmentStatus equipmentStatusB){
        FireThreshold fireThreshold;
        fireChargeTimeThreshold = ((fireThreshold=fireThresholdController.getById(TIME_ID))!=null)?Long.valueOf(fireThreshold.getThresholdValue()):3L;

        fireChargePitchAngleThreshold = ((fireThreshold=fireThresholdController.getById(PITCH_ID))!=null)?Float.valueOf(fireThreshold.getThresholdValue()):0.05F;
        fireChargeAzimuthThreshold = ((fireThreshold=fireThresholdController.getById(AZIMUTH_ID))!=null)?Float.valueOf(fireThreshold.getThresholdValue()):0.05F;

        posAx = fireWeaponController.getById(equipmentStatusA.getEquipmentId()).getX();
        posAy = fireWeaponController.getById(equipmentStatusA.getEquipmentId()).getY();
        posBx = fireWeaponController.getById(equipmentStatusB.getEquipmentId()).getX();
        posBy = fireWeaponController.getById(equipmentStatusB.getEquipmentId()).getY();

        electFrequencyThreshold = ((fireThreshold=fireThresholdController.getById(ELECTFREQUENCY_ID))!=null)?Float.valueOf(fireThreshold.getThresholdValue()):10.0F;
        waterFrequencyThreshold = ((fireThreshold=fireThresholdController.getById(WATERFREQUENCY_ID))!=null)?Float.valueOf(fireThreshold.getThresholdValue()):5.0F;

    }

}
