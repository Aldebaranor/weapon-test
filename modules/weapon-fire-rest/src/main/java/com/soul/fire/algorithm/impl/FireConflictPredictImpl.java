package com.soul.fire.algorithm.impl;

import com.soul.fire.algorithm.FireConflictPredict;
import com.soul.fire.config.PredictConfig;
import com.soul.weapon.model.ConflictReport;
import com.soul.weapon.model.ReportDetail;
import com.soul.weapon.model.ScenariosInfo;

/**
 * @Author: XinLai
 * @Date: 2021/10/29 16:35
 */
public class FireConflictPredictImpl implements FireConflictPredict {

    ReportDetail conflictReportDetailA = new ReportDetail();
    ReportDetail conflictReportDetailB = new ReportDetail();
    ConflictReport conflictReport = new ConflictReport();
    PredictConfig predictConfig = new PredictConfig();
    private Long fireConflictTimeThreshold = 3L;
    private Float fireConflictPitchAngleThreshold = 0.05F;
    private Float fireConflictAzimuthThreshold = 0.05F;
    private Float posAx = 0.0F;
    private Float posAy = 0.0F;
    private Float posBx = 0.0F;
    private Float posBy = 0.0F;

    private Float electFrequency = 10.0F;
    private Float waterFrequency = 5.0F;

    @Override
    public ConflictReport conflictPredict(ScenariosInfo scenariosA , ScenariosInfo scenariosB) {

        conflictReport.setId(scenariosA.getEquipmentId()+" " +scenariosB.getEquipmentId());

        Long timeA = scenariosA.getBeginTime();
        Long timeB = scenariosB.getBeginTime();

        conflictReport.setTime(Math.min(scenariosA.getBeginTime(),scenariosB.getBeginTime()));
        conflictReport.setEquipmentIdA(scenariosA.getEquipmentId());
        conflictReport.setEquipmentIdB(scenariosB.getEquipmentId());

        if(scenariosA.getLaunchAzimuth()!=null && scenariosB.getLaunchAzimuth()!=null){

            // 火力冲突预判
            boolean timeState = Math.abs(scenariosA.getBeginTime()-scenariosB.getBeginTime())<fireConflictTimeThreshold;
            boolean pitchState = Math.abs(scenariosA.getLaunchPitchAngle()-scenariosB.getLaunchPitchAngle())<fireConflictPitchAngleThreshold;

            boolean azimuthState = Math.sqrt((Math.exp(posAx-posBx)+Math.exp(posAy-posBy)/100))<fireConflictAzimuthThreshold;

            if(timeState && pitchState && azimuthState ){
                conflictReport.setConflictType(0);
                GenerateDetail(scenariosA,scenariosB);
                return conflictReport;
            }else {
                return null;
            }
        }else if(scenariosA.getElectromagneticFrequency()!=null && scenariosB.getElectromagneticFrequency()!=null){
            // 电磁冲突预判
            boolean timeState = (0<(timeA-timeB) &&(timeA-timeB)<scenariosB.getDuration())||(0<(timeB-timeA) &&(timeB-timeA)<scenariosA.getDuration());
            boolean frequencyState = Math.abs(scenariosA.getElectromagneticFrequency()-scenariosB.getElectromagneticFrequency())<electFrequency;

            if(timeState && frequencyState){
                conflictReport.setConflictType(1);
                GenerateDetail(scenariosA,scenariosB);
                return conflictReport;
            }else{
                return null;
            }

        }else if(scenariosA.getMinHydroacousticFrequency()!=null && scenariosB.getMinHydroacousticFrequency()!=null){
            // 水声冲突预判
            Float minFreA = scenariosA.getMinHydroacousticFrequency();
            Float maxFreA = scenariosA.getMaxHydroacousticFrequency();
            Float minFreB = scenariosB.getMinHydroacousticFrequency();
            Float maxFreB = scenariosB.getMaxHydroacousticFrequency();

            boolean timeState = (0<(timeA-timeB) &&(timeA-timeB)<scenariosB.getDuration())||(0<(timeB-timeA) &&(timeB-timeA)<scenariosA.getDuration());
            boolean frequencyState =(0<(minFreB-maxFreA) &&(minFreB-maxFreA)<5)||(0<(minFreA-maxFreB) &&(minFreA-maxFreB)<waterFrequency);

            if(timeState && frequencyState){
                conflictReport.setConflictType(2);
                GenerateDetail(scenariosA,scenariosB);
                return conflictReport;
            }else{
                return null;
            }
        }
        return null;
    }

    /**
     * 生成冲突实体详细内容
     * @param scenariosInfoA
     * @param scenariosInfoB
     */
    private void GenerateDetail(ScenariosInfo scenariosInfoA,ScenariosInfo scenariosInfoB){

        conflictReportDetailA.setId(conflictReport.getId());
        conflictReportDetailA.setId(scenariosInfoA.getEquipmentId());
        conflictReportDetailA.setEquipmentTypeId(scenariosInfoA.getEquipmentTypeId());
        conflictReportDetailA.setEquipmentMode(scenariosInfoA.getEquipmentMode());
        conflictReportDetailA.setElectromagneticFrequency(scenariosInfoA.getElectromagneticFrequency());
        conflictReportDetailA.setMinFrequency(scenariosInfoA.getMinHydroacousticFrequency());
        conflictReportDetailA.setMaxFrequency(scenariosInfoA.getMaxHydroacousticFrequency());
        conflictReportDetailA.setLaunchAzimuth(scenariosInfoA.getLaunchAzimuth());
        conflictReportDetailA.setLaunchPitchAngle(scenariosInfoA.getLaunchPitchAngle());

        conflictReportDetailB.setId(conflictReport.getId());
        conflictReportDetailB.setId(scenariosInfoB.getEquipmentId());
        conflictReportDetailB.setEquipmentTypeId(scenariosInfoB.getEquipmentTypeId());
        conflictReportDetailB.setEquipmentMode(scenariosInfoB.getEquipmentMode());
        conflictReportDetailB.setElectromagneticFrequency(scenariosInfoB.getElectromagneticFrequency());
        conflictReportDetailB.setMinFrequency(scenariosInfoB.getMinHydroacousticFrequency());
        conflictReportDetailB.setMaxFrequency(scenariosInfoB.getMaxHydroacousticFrequency());
        conflictReportDetailB.setLaunchAzimuth(scenariosInfoB.getLaunchAzimuth());
        conflictReportDetailB.setLaunchPitchAngle(scenariosInfoB.getLaunchPitchAngle());
    }


}
