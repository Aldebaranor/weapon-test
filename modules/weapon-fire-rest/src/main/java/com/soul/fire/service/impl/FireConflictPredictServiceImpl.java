package com.soul.fire.service.impl;

import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.soul.fire.entity.FireConflictPriority;
import com.soul.fire.service.FireConflictPredictService;
import com.soul.fire.config.PredictConfig;
import com.soul.fire.controller.unity.FireThresholdController;
import com.soul.fire.controller.unity.FireWeaponController;
import com.soul.fire.entity.FireThreshold;
import com.soul.fire.service.FireThresholdService;
import com.soul.fire.service.FireWeaponService;
import com.soul.weapon.model.ConflictReport;
import com.soul.weapon.model.ReportDetail;
import com.soul.weapon.model.ScenariosInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;

/**
 * @Author: XinLai
 * @Date: 2021/10/29 16:35
 */
@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
public class FireConflictPredictServiceImpl implements FireConflictPredictService {

    private final String TIME_ID = "1";
    private final String PITCH_ID = "2";
    private final String AZIMUTH_ID = "3";
    private final String ELECTFREQUENCY_ID = "12";
    private final String WATERFREQUENCY_ID = "13";

    public final FireThresholdService fireThresholdService;
    public final FireWeaponService fireWeaponService;

    ReportDetail conflictReportDetailA = new ReportDetail();
    ReportDetail conflictReportDetailB = new ReportDetail();
    ConflictReport conflictReport = new ConflictReport();

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

        ReadThreshold(scenariosA,scenariosB);
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
            boolean azimuthState = Math.sqrt((Math.pow((posAx-posBx),2)+Math.pow((posAy-posBy),2)/100))<fireConflictAzimuthThreshold;

            if(timeState && pitchState && azimuthState ){
                conflictReport.setConflictType(0);
                GenerateDetail(scenariosA,scenariosB);
                RedisUtils.getService().opsForHash().put(PredictConfig.PREDICT_KEY,conflictReport.getId(), JsonUtils.serialize(conflictReport));
                RedisUtils.getService().opsForHash().put(PredictConfig.PREDICTDETAIL_KEY,conflictReportDetailA.getId(), JsonUtils.serialize(conflictReportDetailA));
                RedisUtils.getService().opsForHash().put(PredictConfig.PREDICTDETAIL_KEY,conflictReportDetailB.getId(), JsonUtils.serialize(conflictReportDetailB));
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

    /**
     * 从数据库中读取阈值
     */
    private void ReadThreshold(ScenariosInfo scenariosA, ScenariosInfo scenariosB){
        FireThreshold fireThreshold;
        fireConflictTimeThreshold = ((fireThreshold=fireThresholdService.getById(TIME_ID))!=null)?Long.valueOf(fireThreshold.getThresholdValue()):3L;

        fireConflictPitchAngleThreshold = ((fireThreshold=fireThresholdService.getById(PITCH_ID))!=null)?Float.valueOf(fireThreshold.getThresholdValue()):0.05F;
        fireConflictAzimuthThreshold = ((fireThreshold=fireThresholdService.getById(AZIMUTH_ID))!=null)?Float.valueOf(fireThreshold.getThresholdValue()):0.05F;

        posAx = fireWeaponService.getById(scenariosA.getEquipmentId()).getX();
        posAy = fireWeaponService.getById(scenariosA.getEquipmentId()).getY();
        posBx = fireWeaponService.getById(scenariosB.getEquipmentId()).getX();
        posBy = fireWeaponService.getById(scenariosB.getEquipmentId()).getY();

        electFrequency = ((fireThreshold=fireThresholdService.getById(ELECTFREQUENCY_ID))!=null)?Float.valueOf(fireThreshold.getThresholdValue()):10.0F;
        waterFrequency = ((fireThreshold=fireThresholdService.getById(WATERFREQUENCY_ID))!=null)?Float.valueOf(fireThreshold.getThresholdValue()):5.0F;
    }

}


