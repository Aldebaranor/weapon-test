package com.soul.fire.service.impl;

import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.soul.fire.entity.FireWeapon;
import com.soul.fire.service.FireConflictPredictService;
import com.soul.fire.entity.FireThreshold;
import com.soul.fire.service.FireThresholdService;
import com.soul.fire.service.FireWeaponService;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.model.ConflictReport;
import com.soul.weapon.model.ReportDetail;
import com.soul.weapon.model.ScenariosInfo;
import com.soul.weapon.model.dds.CombatScenariosInfo;
import com.soul.weapon.model.dds.EquipmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

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
    private final int AN_HOUR=3600;
    private final CommonConfig config;

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

        readThreshold(scenariosA,scenariosB);
        //这里用UUID会产生重复存储的问题
//        conflictReport.setId(UUID.randomUUID().toString());
        conflictReport.setId(scenariosA.getEquipmentId()+scenariosB.getEquipmentId());

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
                conflictReport.setConflictType(1);
                generateDetail(scenariosA,scenariosB);

                if(!RedisUtils.getService(config.getFireDataBase()).exists(Constant.PREDICT_KEY)){
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.PREDICT_KEY,
                            conflictReport.getId(), JsonUtils.serialize(conflictReport));
                    RedisUtils.getService(config.getFireDataBase()).expire(Constant.PREDICT_KEY,AN_HOUR);
                }else{
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.PREDICT_KEY,
                            conflictReport.getId(), JsonUtils.serialize(conflictReport));
                }

                if(!RedisUtils.getService(config.getFireDataBase()).exists(Constant.PREDICTDETAIL_KEY)){
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.PREDICTDETAIL_KEY,
                            conflictReportDetailA.getId(), JsonUtils.serialize(conflictReportDetailA));
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.PREDICTDETAIL_KEY,
                            conflictReportDetailB.getId(), JsonUtils.serialize(conflictReportDetailB));
                    RedisUtils.getService(config.getFireDataBase()).expire(Constant.PREDICTDETAIL_KEY,AN_HOUR);
                }else{
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.PREDICTDETAIL_KEY,
                            conflictReportDetailA.getId(), JsonUtils.serialize(conflictReportDetailA));
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.PREDICTDETAIL_KEY,
                            conflictReportDetailB.getId(), JsonUtils.serialize(conflictReportDetailB));
                }

                return conflictReport;
            }else {
                return null;
            }
        }else if(scenariosA.getElectromagneticFrequency()!=null && scenariosB.getElectromagneticFrequency()!=null){
            // 电磁冲突预判
            boolean timeState = (0<(timeA-timeB) &&(timeA-timeB)<scenariosB.getDuration())||(0<(timeB-timeA) &&(timeB-timeA)<scenariosA.getDuration());
            boolean frequencyState = Math.abs(scenariosA.getElectromagneticFrequency()-scenariosB.getElectromagneticFrequency())<electFrequency;

            if(timeState && frequencyState){
                conflictReport.setConflictType(2);
                generateDetail(scenariosA,scenariosB);

                if(!RedisUtils.getService(config.getFireDataBase()).exists(Constant.PREDICT_KEY)){
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.PREDICT_KEY,
                            conflictReport.getId(), JsonUtils.serialize(conflictReport));
                    RedisUtils.getService(config.getFireDataBase()).expire(Constant.PREDICT_KEY,AN_HOUR);
                }else{
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.PREDICT_KEY,
                            conflictReport.getId(), JsonUtils.serialize(conflictReport));
                }

                if(!RedisUtils.getService(config.getFireDataBase()).exists(Constant.PREDICTDETAIL_KEY)){
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.PREDICTDETAIL_KEY,
                            conflictReportDetailA.getId(), JsonUtils.serialize(conflictReportDetailA));
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.PREDICTDETAIL_KEY,
                            conflictReportDetailB.getId(), JsonUtils.serialize(conflictReportDetailB));
                    RedisUtils.getService(config.getFireDataBase()).expire(Constant.PREDICTDETAIL_KEY,AN_HOUR);
                }else{
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.PREDICTDETAIL_KEY,
                            conflictReportDetailA.getId(), JsonUtils.serialize(conflictReportDetailA));
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.PREDICTDETAIL_KEY,
                            conflictReportDetailB.getId(), JsonUtils.serialize(conflictReportDetailB));
                }

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
                conflictReport.setConflictType(3);
                generateDetail(scenariosA,scenariosB);

                if(!RedisUtils.getService(config.getFireDataBase()).exists(Constant.PREDICT_KEY)){
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.PREDICT_KEY,
                            conflictReport.getId(), JsonUtils.serialize(conflictReport));
                    RedisUtils.getService(config.getFireDataBase()).expire(Constant.PREDICT_KEY,AN_HOUR);
                }else{
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.PREDICT_KEY,
                            conflictReport.getId(), JsonUtils.serialize(conflictReport));
                }

                if(!RedisUtils.getService(config.getFireDataBase()).exists(Constant.PREDICTDETAIL_KEY)){
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.PREDICTDETAIL_KEY,
                            conflictReportDetailA.getId(), JsonUtils.serialize(conflictReportDetailA));
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.PREDICTDETAIL_KEY,
                            conflictReportDetailB.getId(), JsonUtils.serialize(conflictReportDetailB));
                    RedisUtils.getService(config.getFireDataBase()).expire(Constant.PREDICTDETAIL_KEY,AN_HOUR);
                }else{
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.PREDICTDETAIL_KEY,
                            conflictReportDetailA.getId(), JsonUtils.serialize(conflictReportDetailA));
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.PREDICTDETAIL_KEY,
                            conflictReportDetailB.getId(), JsonUtils.serialize(conflictReportDetailB));
                }

                return conflictReport;
            }else{
                return null;
            }
        }
        return null;
    }

    @Override
    public void predictTest() {
//        if (!Boolean.TRUE.equals(RedisUtils.getService(config.getPumpDataBase()).getTemplate()
//                .hasKey(Constant.COMBAT_SCENARIOS_INFO_HTTP_KEY))) {
        if (!Boolean.TRUE.equals(RedisUtils.getService(config.getPumpDataBase()).getTemplate()
                .keys(Constant.COMBAT_SCENARIOS_INFO_HTTP_KEY+":*").size()!=0)) {
            log.debug("从"+Constant.COMBAT_SCENARIOS_INFO_HTTP_KEY+"中获取作战方案信息失败！");
            return;
        }

        String key = String.format("%s:%s", Constant.COMBAT_SCENARIOS_INFO_HTTP_KEY, getTime());

        CombatScenariosInfo combatScenariosInfo = JsonUtils.deserialize(RedisUtils.getService(
                config.getPumpDataBase()).getTemplate().boundValueOps(key).get(),CombatScenariosInfo.class);

        List<ScenariosInfo> scenariosInfos = combatScenariosInfo.getScenariosList();
        int n = scenariosInfos.size();

        for(int i=0;i<n;i++){
            for(int j =i+1;j<n;j++){
                conflictPredict(scenariosInfos.get(i),scenariosInfos.get(j));
            }
        }

    }

    /**
     * 生成冲突实体详细内容
     * @param scenariosInfoA
     * @param scenariosInfoB
     */
    private void generateDetail(ScenariosInfo scenariosInfoA, ScenariosInfo scenariosInfoB){

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
    private void readThreshold(ScenariosInfo scenariosA, ScenariosInfo scenariosB){
        FireThreshold fireThreshold;

        fireThreshold=fireThresholdService.getById(TIME_ID);
        fireConflictTimeThreshold = (fireThreshold!=null)?Long.valueOf(fireThreshold.getThresholdValue().substring(
            0,fireThreshold.getThresholdValue().length()-1)):3L;

        fireThreshold=fireThresholdService.getById(PITCH_ID);
        fireConflictPitchAngleThreshold = (fireThreshold!=null)?Float.valueOf(fireThreshold.getThresholdValue().substring(0,fireThreshold.getThresholdValue().length()-3)):0.05F;

        fireThreshold=fireThresholdService.getById(AZIMUTH_ID);
        fireConflictAzimuthThreshold = (fireThreshold!=null)?Float.valueOf(fireThreshold.getThresholdValue().substring(0,fireThreshold.getThresholdValue().length()-3)):0.05F;


        posAx = fireWeaponService.getById(scenariosA.getEquipmentId()).getX();
        posAy = fireWeaponService.getById(scenariosA.getEquipmentId()).getY();
        posBx = fireWeaponService.getById(scenariosB.getEquipmentId()).getX();
        posBy = fireWeaponService.getById(scenariosB.getEquipmentId()).getY();

        fireThreshold=fireThresholdService.getById(ELECTFREQUENCY_ID);
        electFrequency = (fireThreshold!=null)?Float.valueOf(fireThreshold.getThresholdValue().substring(0,fireThreshold.getThresholdValue().length()-3)):10.0F;

        fireThreshold=fireThresholdService.getById(WATERFREQUENCY_ID);
        waterFrequency = (fireThreshold!=null)?Float.valueOf(fireThreshold.getThresholdValue().substring(0,fireThreshold.getThresholdValue().length()-3)):5.0F;


    }

    private String getTime() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(System.currentTimeMillis());
    }
}


