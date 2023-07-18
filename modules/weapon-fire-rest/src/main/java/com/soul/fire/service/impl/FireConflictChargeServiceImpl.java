package com.soul.fire.service.impl;

import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.soul.fire.entity.FirePriority;
import com.soul.fire.entity.FireWeapon;
import com.soul.fire.service.FireConflictChargeService;

import com.soul.fire.entity.FireThreshold;
import com.soul.fire.service.FirePriorityService;
import com.soul.fire.service.FireThresholdService;
import com.soul.fire.service.FireWeaponService;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.model.ChargeReport;
import com.soul.weapon.model.dds.EquipmentStatus;
import com.soul.weapon.model.ReportDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @Author: XinLai
 * @Date: 2021/11/1 11:11
 */
@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
public class FireConflictChargeServiceImpl implements FireConflictChargeService {

    private final FirePriorityService firePriorityService;
    private final FireWeaponService fireWeaponService;
    private final FireThresholdService fireThresholdService;
    private final CommonConfig config;

    private final String TIME_ID = "1";
    private final String PITCH_ID = "2";
    private final String AZIMUTH_ID = "3";
    private final String ELECTFREQUENCY_ID = "12";
    private final String WATERFREQUENCY_ID = "13";
    private final int AN_HOUR=3600;

    ReportDetail chargeReportDetailA = new ReportDetail();
    ReportDetail chargeReportDetailB = new ReportDetail();
    ChargeReport chargeReport = new ChargeReport();

    // 从阈值表项中获取阈值
    private Long  fireChargeTimeThreshold = 3L;
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
    public ChargeReport  chargeReport(EquipmentStatus equipmentStatusA,EquipmentStatus equipmentStatusB){

        readThreshold(equipmentStatusA,equipmentStatusB);

        chargeReport.setId(equipmentStatusA.getEquipmentId() +equipmentStatusB.getEquipmentId());

        Long timeA = equipmentStatusA.getTime();
        Long timeB = equipmentStatusB.getTime();

        Random random = new Random(System.currentTimeMillis());

        // 设置管控时间
        chargeReport.setTime(Math.min(timeA,timeB));

        //火力兼容中有舰载机就停止其他所有
        if(equipmentStatusA.getEquipmentId().equals("14")||equipmentStatusB.getEquipmentId().equals("14")){
            chargeReport.setChargeType(1);
            generateDetail(equipmentStatusA,equipmentStatusB);
            if(equipmentStatusA.getEquipmentId().equals("14")&&equipmentStatusB.getEquipmentMode().equals("1")&&(equipmentStatusB.getMsgTime()-equipmentStatusA.getMsgTime()<300000)){
                chargeReport.setFreeEquipId(equipmentStatusA.getEquipmentId());
                chargeReport.setChargeEquipId(fireWeaponService.getById(equipmentStatusB.getEquipmentId()).getCode());
                String method = fireWeaponService.getById(equipmentStatusA.getEquipmentId()).getName()+"起飞，武器系统禁用5分钟";
                if(equipmentStatusA.getMsgTime()<System.currentTimeMillis()-300000){
                    method = method+"(已过期)";
                }
                chargeReport.setChargeMethod(method);
            }else if(equipmentStatusB.getEquipmentId().equals("14")&&equipmentStatusA.getEquipmentMode().equals("1")&&(equipmentStatusA.getMsgTime()-equipmentStatusB.getMsgTime()<300000)) {
                chargeReport.setFreeEquipId(fireWeaponService.getById(equipmentStatusB.getEquipmentId()).getCode());
                chargeReport.setChargeEquipId(fireWeaponService.getById(equipmentStatusA.getEquipmentId()).getCode());
                String method = fireWeaponService.getById(equipmentStatusB.getEquipmentId()).getName() + "起飞，武器系统禁用5分钟";
                if (equipmentStatusB.getMsgTime() < System.currentTimeMillis() - 300000) {
                    method = method + "(已过期)";
                }
                chargeReport.setChargeMethod(method);
            }
            if(!RedisUtils.getService(config.getFireDataBase()).exists(Constant.CHARGEDETAIL_KEY)){
                RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                        chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                        chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                RedisUtils.getService(config.getFireDataBase()).expire(Constant.CHARGEDETAIL_KEY,AN_HOUR);
            }else{
                RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                        chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                        chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
            }
            return chargeReport;
        }

        //水声兼容中有声干扰显示干扰反潜声纳
        if(Integer.valueOf(equipmentStatusA.getEquipmentId())>=30&&Integer.valueOf(equipmentStatusA.getEquipmentId())<=35&&(equipmentStatusB.getEquipmentId().equals("15")||equipmentStatusB.getEquipmentId().equals("16"))&&(Math.abs(equipmentStatusB.getMsgTime()-equipmentStatusA.getMsgTime())<300000)){
            chargeReport.setChargeType(3);
            generateDetail(equipmentStatusA,equipmentStatusB);
            chargeReport.setFreeEquipId(equipmentStatusA.getEquipmentId());
            chargeReport.setChargeEquipId(fireWeaponService.getById(equipmentStatusB.getEquipmentId()).getCode());
            String method = fireWeaponService.getById(equipmentStatusA.getEquipmentId()).getName() + "发射，"+fireWeaponService.getById(equipmentStatusB.getEquipmentId()).getName()+"受到影响";
            if (equipmentStatusA.getMsgTime() < System.currentTimeMillis() - 300000) {
                method = method + "(已过期)";
            }
            chargeReport.setChargeMethod(method);
            if(!RedisUtils.getService(config.getFireDataBase()).exists(Constant.CHARGEDETAIL_KEY)){
                RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                        chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                        chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                RedisUtils.getService(config.getFireDataBase()).expire(Constant.CHARGEDETAIL_KEY,AN_HOUR);
            }else{
                RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                        chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                        chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
            }
            return chargeReport;
        }else if(Integer.valueOf(equipmentStatusB.getEquipmentId())>=30&&Integer.valueOf(equipmentStatusB.getEquipmentId())<=35&&(equipmentStatusA.getEquipmentId().equals("15")||equipmentStatusA.getEquipmentId().equals("16"))&&(Math.abs(equipmentStatusA.getMsgTime()-equipmentStatusB.getMsgTime())<300000)){
            chargeReport.setChargeType(3);
            chargeReport.setFreeEquipId(fireWeaponService.getById(equipmentStatusB.getEquipmentId()).getCode());
            chargeReport.setChargeEquipId(fireWeaponService.getById(equipmentStatusA.getEquipmentId()).getCode());
            String method = fireWeaponService.getById(equipmentStatusA.getEquipmentId()).getName() + "发射，"+fireWeaponService.getById(equipmentStatusB.getEquipmentId()).getName()+"受到影响";
            if (equipmentStatusB.getMsgTime() < System.currentTimeMillis() - 300000) {
                method = method + "(已过期)";
            }
            chargeReport.setChargeMethod(method);
            if(!RedisUtils.getService(config.getFireDataBase()).exists(Constant.CHARGEDETAIL_KEY)){
                RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                        chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                        chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                RedisUtils.getService(config.getFireDataBase()).expire(Constant.CHARGEDETAIL_KEY,AN_HOUR);
            }else{
                RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                        chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                        chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
            }
            return chargeReport;
        }

        // 此处设置管控装备和自由装备id（应该根据管控措施具体实现）
        // 是否都处于工作状态
        boolean beWork = equipmentStatusA.getBeWork()&&equipmentStatusB.getBeWork();

//        if(equipmentStatusA.getLaunchAzimuth()!=-1 && equipmentStatusB.getLaunchAzimuth()!=-1){
        if(equipmentStatusA.getEquipmentMode().equals("1")&&equipmentStatusB.getEquipmentMode().equals("1")){
            // 火力兼容管控计算
            boolean timeState = Math.abs(equipmentStatusA.getTime()-equipmentStatusB.getTime())<fireChargeTimeThreshold*1000;
            //计算方位角
            boolean pitchState = Math.abs(Math.toRadians(equipmentStatusA.getLaunchPitchAngle())-Math.toRadians(equipmentStatusB.getLaunchPitchAngle()))<fireChargePitchAngleThreshold;

            boolean azimuthState = Math.abs(Math.toRadians(equipmentStatusA.getLaunchAzimuth())-Math.toRadians(equipmentStatusB.getLaunchAzimuth()))<fireChargeAzimuthThreshold*(Math.sqrt(Math.pow((posAx-posBx),2)+Math.pow((posAy-posBy),2))/100+1);
            if(timeState && pitchState && azimuthState && beWork){
                //1火力，2电磁，3水声
                chargeReport.setChargeType(1);
                generateDetail(equipmentStatusA,equipmentStatusB);
                //这里有什么规则？直接查数据库表？
                FirePriority firePriority = firePriorityService.getPriorityByIds(equipmentStatusA.getEquipmentTypeId(),equipmentStatusB.getEquipmentTypeId());
                if(firePriority==null)
                {
                    return null;
                }
                if(firePriority.isABetterThanB()){
                    chargeReport.setFreeEquipId(equipmentStatusA.getEquipmentId());
                    chargeReport.setChargeEquipId(equipmentStatusB.getEquipmentId());
                    String method = fireWeaponService.getById(equipmentStatusB.getEquipmentId()).getName()+"禁用5分钟";
                    if(equipmentStatusA.getMsgTime()<System.currentTimeMillis()-300000){
                        method = method+"(已过期)";
                    }
                    chargeReport.setChargeMethod(method);                }else{
                    chargeReport.setFreeEquipId(fireWeaponService.getById(equipmentStatusB.getEquipmentId()).getCode());
                    chargeReport.setChargeEquipId(fireWeaponService.getById(equipmentStatusA.getEquipmentId()).getCode());
                    String method = fireWeaponService.getById(equipmentStatusB.getEquipmentId()).getName()+"禁用5分钟";
                    if(equipmentStatusB.getMsgTime()<System.currentTimeMillis()-300000){
                        method = method+"(已过期)";
                    }
                    chargeReport.setChargeMethod(method);                }

                if(!RedisUtils.getService(config.getFireDataBase()).exists(Constant.CHARGEDETAIL_KEY)){
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                            chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                            chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                    RedisUtils.getService(config.getFireDataBase()).expire(Constant.CHARGEDETAIL_KEY,AN_HOUR);
                }else{
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                            chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                            chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                }


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
        }else if(equipmentStatusA.getEquipmentMode().equals("2") && equipmentStatusB.getEquipmentMode().equals("2")){
            // 电磁冲突预判
            boolean timeState = Math.abs(equipmentStatusA.getTime()-equipmentStatusB.getTime())<electTimeThreshold;
            boolean frequencyState = Math.abs(equipmentStatusA.getElectromagneticFrequency()-equipmentStatusB.getElectromagneticFrequency())< electFrequencyThreshold;

            if(timeState && frequencyState && beWork){
                chargeReport.setChargeType(2);
                generateDetail(equipmentStatusA,equipmentStatusB);
                FirePriority firePriority = firePriorityService.getPriorityByIds(equipmentStatusA.getEquipmentTypeId(),equipmentStatusB.getEquipmentTypeId());

                if(firePriority.isABetterThanB()){
                    chargeReport.setFreeEquipId(equipmentStatusA.getEquipmentId());
                    chargeReport.setChargeEquipId(equipmentStatusB.getEquipmentId());
                    String method = fireWeaponService.getById(equipmentStatusB.getEquipmentId()).getName()+"禁用5分钟";
                    if(equipmentStatusA.getMsgTime()<System.currentTimeMillis()-300000){
                        method = method+"(已过期)";
                    }
                    chargeReport.setChargeMethod(method);                }else{
                    chargeReport.setFreeEquipId(fireWeaponService.getById(equipmentStatusB.getEquipmentId()).getCode());
                    chargeReport.setChargeEquipId(fireWeaponService.getById(equipmentStatusA.getEquipmentId()).getCode());
                    String method = fireWeaponService.getById(equipmentStatusB.getEquipmentId()).getName()+"禁用5分钟";
                    if(equipmentStatusB.getMsgTime()<System.currentTimeMillis()-300000){
                        method = method+"(已过期)";
                    }
                    chargeReport.setChargeMethod(method);                }

                if(!RedisUtils.getService(config.getFireDataBase()).exists(Constant.CHARGEDETAIL_KEY)){
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                            chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                            chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                    RedisUtils.getService(config.getFireDataBase()).expire(Constant.CHARGEDETAIL_KEY,AN_HOUR);
                }else{
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                            chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                            chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                }

                return chargeReport;
            }else{
                return null;
            }

        }else if(equipmentStatusA.getEquipmentMode().equals("3") && equipmentStatusB.getEquipmentMode().equals("3")){
            // 水声冲突预判
            Float minFreA = equipmentStatusA.getMinFrequency();
            Float maxFreA = equipmentStatusA.getMaxFrequency();
            Float minFreB = equipmentStatusB.getMinFrequency();
            Float maxFreB = equipmentStatusB.getMaxFrequency();

            boolean timeState = Math.abs(equipmentStatusA.getTime()-equipmentStatusB.getTime())<waterTimeThreshold;
            boolean frequencyState =(0<(minFreB-maxFreA) &&(minFreB-maxFreA)<waterFrequencyThreshold)||(0<(minFreA-maxFreB) &&(minFreA-maxFreB)< waterFrequencyThreshold);

            if(timeState && frequencyState && beWork){
                chargeReport.setChargeType(3);
                generateDetail(equipmentStatusA,equipmentStatusB);
                FirePriority firePriority = firePriorityService.getPriorityByIds(equipmentStatusA.getEquipmentTypeId(),equipmentStatusB.getEquipmentTypeId());
                if(firePriority.isABetterThanB()){
                    chargeReport.setFreeEquipId(equipmentStatusA.getEquipmentId());
                    chargeReport.setChargeEquipId(equipmentStatusB.getEquipmentId());
                    String method = fireWeaponService.getById(equipmentStatusB.getEquipmentId()).getName()+"禁用5分钟";
                    if(equipmentStatusA.getMsgTime()<System.currentTimeMillis()-300000){
                        method = method+"(已过期)";
                    }
                    chargeReport.setChargeMethod(method);                }else{
                    chargeReport.setFreeEquipId(fireWeaponService.getById(equipmentStatusB.getEquipmentId()).getCode());
                    chargeReport.setChargeEquipId(fireWeaponService.getById(equipmentStatusA.getEquipmentId()).getCode());
                    String method = fireWeaponService.getById(equipmentStatusB.getEquipmentId()).getName()+"禁用5分钟";
                    if(equipmentStatusB.getMsgTime()<System.currentTimeMillis()-300000){
                        method = method+"(已过期)";
                    }
                    chargeReport.setChargeMethod(method);                }
                if(!RedisUtils.getService(config.getFireDataBase()).exists(Constant.CHARGEDETAIL_KEY)){
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                            chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                            chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                    RedisUtils.getService(config.getFireDataBase()).expire(Constant.CHARGEDETAIL_KEY,AN_HOUR);
                }else{
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                            chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                    RedisUtils.getService(config.getFireDataBase()).opsForHash().put(Constant.CHARGEDETAIL_KEY,
                            chargeReportDetailA.getId(), JsonUtils.serialize(chargeReportDetailA));
                }

                return chargeReport;
            }else{
                return null;
            }
        }
        return null;
    }

    @Override
    public void chargeTest() {

//        if(!RedisUtils.getService(config.getPumpDataBase()).getTemplate().hasKey(
//                Constant.EQUIPMENT_STATUS_HTTP_KEY)){
        if(RedisUtils.getService(config.getPumpDataBase()).getTemplate().keys(
                Constant.EQUIPMENT_STATUS_HTTP_KEY+":*").size()==0){
            log.debug("从"+Constant.EQUIPMENT_STATUS_HTTP_KEY+"中获取装备状态信息失败！");
        }

        String key = String.format("%s:%s", Constant.EQUIPMENT_STATUS_HTTP_KEY, getTime());

        Map<String,String> equipmentStatus = RedisUtils.getService(config.
                getPumpDataBase()).boundHashOps(key).entries();

        if(equipmentStatus==null) {
            return;
        }

        Map<String,EquipmentStatus> equipmentStatusMap = equipmentStatus.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        pair -> JsonUtils.deserialize(pair.getValue(), EquipmentStatus.class)
                )
        );

        int n = equipmentStatusMap.size();

        for(EquipmentStatus statusA:equipmentStatusMap.values()){
            for(EquipmentStatus statusB:equipmentStatusMap.values()){

                if(!statusA.getEquipmentId().equals(statusB.getEquipmentId()) && Integer.valueOf(statusA.getEquipmentId())> Integer.valueOf(statusB.getEquipmentId())){
                    ChargeReport chargeReport = chargeReport(statusA,statusB);

                    if(chargeReport!=null) {
                        if(RedisUtils.getService(config.getFireDataBase()).exists(Constant.CHARGE_KEY)){
                            RedisUtils.getService(config.getFireDataBase()).boundHashOps(Constant.CHARGE_KEY).put(
                                    chargeReport.getId(),JsonUtils.serialize(chargeReport));
                        }else{
                            RedisUtils.getService(config.getFireDataBase()).boundHashOps(Constant.CHARGE_KEY).put(
                                    chargeReport.getId(),JsonUtils.serialize(chargeReport));
                            RedisUtils.getService(config.getFireDataBase()).expire(Constant.CHARGE_KEY,AN_HOUR);
                        }
                    }

                }
            }
        }
    }


    /**
     * 生成冲突实体详细内容
     * @param equipmentStatusA
     * @param equipmentStatusB
     */
    private void generateDetail(EquipmentStatus equipmentStatusA, EquipmentStatus equipmentStatusB){

        chargeReportDetailA.setId(chargeReport.getId());
        chargeReportDetailA.setEquipmentId(equipmentStatusA.getEquipmentId());
        chargeReportDetailA.setEquipmentTypeId(equipmentStatusA.getEquipmentTypeId());
        chargeReportDetailA.setEquipmentMode(equipmentStatusA.getEquipmentMode());
        chargeReportDetailA.setElectromagneticFrequency(equipmentStatusA.getElectromagneticFrequency());
        chargeReportDetailA.setMinFrequency(equipmentStatusA.getMinFrequency());
        chargeReportDetailA.setMaxFrequency(equipmentStatusA.getMaxFrequency());
        chargeReportDetailA.setLaunchAzimuth(equipmentStatusA.getLaunchAzimuth());
        chargeReportDetailA.setLaunchPitchAngle(equipmentStatusA.getLaunchPitchAngle());

        chargeReportDetailB.setId(chargeReport.getId());
        chargeReportDetailB.setEquipmentId(equipmentStatusB.getEquipmentId());
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
    private void readThreshold( EquipmentStatus equipmentStatusA, EquipmentStatus equipmentStatusB){

        FireThreshold fireThreshold;
        fireThreshold=fireThresholdService.getById(TIME_ID);
        fireChargeTimeThreshold = (fireThreshold!=null)?Long.valueOf(fireThreshold.getThresholdValue().substring(
            0,fireThreshold.getThresholdValue().length()-1)):3L;

        fireThreshold=fireThresholdService.getById(PITCH_ID);
        fireChargePitchAngleThreshold = (fireThreshold!=null)?Float.valueOf(fireThreshold.getThresholdValue().substring(0,fireThreshold.getThresholdValue().length()-3)):0.05F;

        fireThreshold=fireThresholdService.getById(AZIMUTH_ID);
        fireChargeAzimuthThreshold = (fireThreshold!=null)?Float.valueOf(fireThreshold.getThresholdValue().substring(0,fireThreshold.getThresholdValue().length()-3)):0.05F;

        FireWeapon fireWeapon = new FireWeapon();
        fireWeapon = fireWeaponService.getById(equipmentStatusA.getEquipmentId());
        posAx = fireWeapon!=null?fireWeapon.getX():0.0f;
        posAy = fireWeapon!=null?fireWeapon.getY():0.0f;
        fireWeapon = fireWeaponService.getById(equipmentStatusB.getEquipmentId());
        posBx =fireWeapon!=null?fireWeapon.getX():0.0f;
        posBy =fireWeapon!=null?fireWeapon.getY():0.0f;

        fireThreshold=fireThresholdService.getById(ELECTFREQUENCY_ID);
        electFrequencyThreshold = (fireThreshold!=null)?Float.valueOf(fireThreshold.getThresholdValue().substring(0,fireThreshold.getThresholdValue().length()-3)):10.0F;

        fireThreshold=fireThresholdService.getById(WATERFREQUENCY_ID);
        waterFrequencyThreshold = (fireThreshold!=null)?Float.valueOf(fireThreshold.getThresholdValue().substring(0,fireThreshold.getThresholdValue().length()-3)):5.0F;

    }

    private String getTime() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(System.currentTimeMillis());
    }

}
