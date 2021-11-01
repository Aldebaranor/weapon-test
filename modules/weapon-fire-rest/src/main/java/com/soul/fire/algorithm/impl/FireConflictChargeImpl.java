package com.soul.fire.algorithm.impl;

import com.soul.fire.algorithm.FireConflictCharge;
import com.soul.fire.config.ChargeConfig;
import com.soul.weapon.model.ChargeReport;
import com.soul.weapon.model.EquipmentStatus;
import com.soul.weapon.model.ReportDetail;

/**
 * @Author: XinLai
 * @Date: 2021/11/1 11:11
 */
public class FireConflictChargeImpl implements FireConflictCharge {

    ReportDetail chargeReportDetailA = new ReportDetail();
    ReportDetail chargeReportDetailB = new ReportDetail();
    ChargeReport chargeReport = new ChargeReport();
    ChargeConfig chargeConfig = new ChargeConfig();
    private Long fireChargeTimeThreshold = 3L;
    private Float fireChargePitchAngleThreshold = 0.05F;
    private Float fireChargeAzimuthThreshold = 0.05F;
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

        chargeReport.setId(equipmentStatusA.getEquipmentId()+" " +equipmentStatusB.getEquipmentId());

        Long timeA = equipmentStatusA.getTime();
        Long timeB = equipmentStatusB.getTime();

        chargeReport.setTime(Math.min(timeA,timeB));
        chargeReport.setChargeEquipId(equipmentStatusA.getEquipmentId());
        chargeReport.setFreeEquipId(equipmentStatusB.getEquipmentId());
        // 是否都处于工作状态
        boolean beWork = equipmentStatusA.getBeWork()&&equipmentStatusB.getBeWork();

        if(equipmentStatusA.getLaunchAzimuth()!=null && equipmentStatusB.getLaunchAzimuth()!=null){

            // 火力兼容管控计算
            boolean timeState = Math.abs(equipmentStatusA.getTime()-equipmentStatusB.getTime())<fireChargeTimeThreshold;
            boolean pitchState = Math.abs(equipmentStatusA.getLaunchPitchAngle()-equipmentStatusB.getLaunchPitchAngle())<fireChargePitchAngleThreshold;

            boolean azimuthState = Math.sqrt((Math.exp(posAx-posBx)+Math.exp(posAy-posBy)/100))<fireChargeAzimuthThreshold;

            if(timeState && pitchState && azimuthState && beWork){
                chargeReport.setChargeType(0);
                GenerateDetail(equipmentStatusA,equipmentStatusB);
                /*
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
    
}
