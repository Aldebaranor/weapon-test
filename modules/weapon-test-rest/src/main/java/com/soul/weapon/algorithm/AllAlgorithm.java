package com.soul.weapon.algorithm;

import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.flagwind.commons.StringUtils;
import com.soul.weapon.config.CommonRedisConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.entity.HistoryInfo;
import com.soul.weapon.entity.PipeHistory;
import com.soul.weapon.entity.enums.PipeWeaponIndices;
import com.soul.weapon.model.dds.*;
import com.soul.weapon.service.PipeHistoryService;
import com.soul.weapon.utils.MathUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author: nash5
 * @date: 2021-11-15 16:08
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AllAlgorithm {
    private final CommonRedisConfig commonRedisConfig;

    /** 通道测试的测试周期阈值 **/
    private Long PIPETEST_CYCLE_THRESHOLD = 3L;

    /** 威胁判断-传感器探测时间阈值 **/
    private Long DETECTOR_TIME_THRESHOLD = 2L;

    private Long EXECUTION_TIME_THRESHOLD = 60L;

    private final PipeHistoryService pipeHistoryService;


    /**
     * 航空导弹-1
     *
     */
    public void ShipToAirMissile(){
        if(!Boolean.TRUE.equals(RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate()
                .hasKey(Constant.EQUIPMENT_STATUS_HTTP_KEY))) {
            log.error("从Redis中获取装备信息失败！");
            return ;
        }

    }


    /**
     * 反导舰炮算法-2
     */
    public void antiMissileShipGun() {
        if(!Boolean.TRUE.equals(RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate()
                .hasKey(Constant.EQUIPMENT_STATUS_HTTP_KEY))) {
            log.error("从Redis中获取装备信息失败！");
            return ;
        }

        Map<String, String> tmpEquipments = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).
                boundHashOps(Constant.EQUIPMENT_STATUS_HTTP_KEY).entries();
        assert tmpEquipments != null;
        Map<String, EquipmentStatus> allEquipmentStatus = tmpEquipments.entrySet().stream().collect(Collectors.toMap(
                    Map.Entry::getKey,
                    pair -> JsonUtils.deserialize(pair.getValue(), EquipmentStatus.class))
                );

        HistoryInfo.AntiMissileShipGunTestReport tmpReport = new HistoryInfo.AntiMissileShipGunTestReport();
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.AntiMissileShipGun.getValue());
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.AntiMissileShipGunRadar.getValue());
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.AntiMissileShipGunControl.getValue());

        tmpReport.setTime(System.currentTimeMillis());
        tmpReport.setRadarId(
                PipeWeaponIndices.AntiMissileShipGunRadar.getValue());
        tmpReport.setRadarSelfCheck(allEquipmentStatus.get(PipeWeaponIndices.AntiMissileShipGunRadar.getValue())
                .getCheckStatus());
        tmpReport.setFireControlId(
                PipeWeaponIndices.AntiMissileShipGunControl.getValue());
        tmpReport.setFireControlSelfCheck(allEquipmentStatus.get(
                PipeWeaponIndices.AntiMissileShipGunControl.getValue()).getCheckStatus());
        tmpReport.setShipGunId(
                PipeWeaponIndices.AntiMissileShipGun.getValue());
        tmpReport.setShipGunSelfCheck(allEquipmentStatus.get(
                PipeWeaponIndices.AntiMissileShipGun.getValue()).getCheckStatus());
        long[] timeVec = {
                allEquipmentStatus.get(PipeWeaponIndices.AntiMissileShipGun.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.AntiMissileShipGunControl.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.AntiMissileShipGunRadar.getValue()).getTime()
        };
        boolean pipeStatus = tmpReport.getRadarSelfCheck() &&
                tmpReport.getFireControlSelfCheck() &&
                tmpReport.getShipGunSelfCheck() &&
                meetTestCycleHelper(timeVec, PIPETEST_CYCLE_THRESHOLD);
        tmpReport.setStatus(pipeStatus);

        PipeHistory pipeHistory = new PipeHistory();
        pipeHistory.setId(UUID.randomUUID().toString());
        pipeHistory.setCreateTime(new Timestamp(System.currentTimeMillis()));
        pipeHistory.setType("2");
        pipeHistory.setDisabled(false);
        pipeHistory.setRes(JsonUtils.serialize(tmpReport));
        pipeHistoryService.insert(pipeHistory);
    }

    /**
     * 威胁判断算法-7
     */
    public void threatenEstimation(){
        StringRedisTemplate template = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate();
        if(!template.hasKey(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY)||!template.hasKey(Constant.TARGET_INFO_HTTP_KEY)){
            log.error("从Redis中获取目标指示信息或目标真值信息失败！");
            return ;
        }

        Map<String,String> tmpTargetInfos = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).boundHashOps(Constant.TARGET_INFO_HTTP_KEY).entries();
        assert  tmpTargetInfos != null;
        Map<String, TargetInfo> allTargetInfos = tmpTargetInfos.entrySet().stream().collect(Collectors.toMap(
                    Map.Entry::getKey,
                    pair->JsonUtils.deserialize(pair.getValue(),TargetInfo.class))
        );

        Map<String,String> tmpInstructionInfos = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).boundHashOps(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY).entries();
        assert  tmpInstructionInfos != null;
        Map<String, TargetInstructionsInfo> allInstructionInfos = tmpInstructionInfos.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair->JsonUtils.deserialize(pair.getValue(),TargetInstructionsInfo.class))
        );

        // 用双重循环做的两两匹配测试，估计得优化
        for(String key:allTargetInfos.keySet()){

             TargetInfo targetInfo = allTargetInfos.get(key);

             for(String key2:allInstructionInfos.keySet()){

                 TargetInstructionsInfo targetInstructionsInfo = allInstructionInfos.get(key2);


                 if(StringUtils.equals(targetInfo.getTargetId(),targetInstructionsInfo.getTargetId())){

                     if(Math.abs(targetInfo.getTime()-targetInstructionsInfo.getTime())<DETECTOR_TIME_THRESHOLD){

                         HistoryInfo.ThreatenReport threatenReport = new HistoryInfo.ThreatenReport();
                         threatenReport.setId(targetInfo.getTargetId());
                         threatenReport.setType(targetInfo.getTargetTypeId());
                         threatenReport.setDistanceOffset(Math.abs(targetInfo.getDistance()-targetInstructionsInfo.getDistance()));
                         threatenReport.setTime(targetInstructionsInfo.getTime());
                         threatenReport.setPitchOffset(Math.abs(targetInfo.getPitchAngle()-targetInstructionsInfo.getPitchAngle()));
                         threatenReport.setSpeedOffset(Math.abs(targetInfo.getSpeed()-targetInstructionsInfo.getSpeed()));

                         PipeHistory pipeHistory = new PipeHistory();
                         pipeHistory.setId(UUID.randomUUID().toString());
                         pipeHistory.setCreateTime(new Timestamp(System.currentTimeMillis()));
                         pipeHistory.setType("7");
                         pipeHistory.setDisabled(false);
                         pipeHistory.setRes(JsonUtils.serialize(threatenReport));
                         pipeHistoryService.insert(pipeHistory);

                     }
                     break;
                 }
             }
        }

    }

    /**
     * 执行情况测试-9
     */
    public void executionStatusTest() {

        StringRedisTemplate template = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate();
        if (!template.hasKey(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY) || !template.hasKey(Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY) || !template.hasKey(Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY)) {
            log.error("从Redis中获取信息失败！");
            return;
        }

        Map<String, String> tmpFireControlInfos = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).boundHashOps(Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY).entries();
        assert tmpFireControlInfos != null;
        Map<String, TargetFireControlInfo> allFireControlInfos = tmpFireControlInfos.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), TargetFireControlInfo.class))
        );

        Map<String, String> tmpInstructionInfos = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).boundHashOps(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY).entries();
        assert tmpInstructionInfos != null;
        Map<String, TargetInstructionsInfo> allInstructionInfos = tmpInstructionInfos.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), TargetInstructionsInfo.class))
        );

        Map<String, String> tmpEquipmentLaunchInfos = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).boundHashOps(Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY).entries();
        assert tmpEquipmentLaunchInfos != null;
        Map<String, EquipmentLaunchStatus> allEquipmentLaunchInfos = tmpEquipmentLaunchInfos.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), EquipmentLaunchStatus.class))
        );

        for (String key : allFireControlInfos.keySet()) {

            TargetFireControlInfo targetFireControlInfo = allFireControlInfos.get(key);

            for (String key2 : allInstructionInfos.keySet()) {

                TargetInstructionsInfo targetInstructionsInfo = allInstructionInfos.get(key2);

                for (String key3 : allEquipmentLaunchInfos.keySet()) {

                    EquipmentLaunchStatus equipmentLaunchStatus = allEquipmentLaunchInfos.get(key3);

                    if (targetFireControlInfo.getTargetId() == targetInstructionsInfo.getTargetId() && targetFireControlInfo.getTargetId() == equipmentLaunchStatus.getTargetId()) {

                        HistoryInfo.ExecutionStatusReport executionStatusReport = new HistoryInfo.ExecutionStatusReport();

                        boolean b = equipmentLaunchStatus.getTime() > targetFireControlInfo.getTime() && targetFireControlInfo.getTime() > targetInstructionsInfo.getTime();
                        boolean c = equipmentLaunchStatus.getTime() - targetInstructionsInfo.getTime() < EXECUTION_TIME_THRESHOLD;

                        executionStatusReport.setTargetId(equipmentLaunchStatus.getTargetId());
                        executionStatusReport.setTime(equipmentLaunchStatus.getTime());
                        executionStatusReport.setTargetType(equipmentLaunchStatus.getTargetTypeId());

                        if (b && c) {
                            executionStatusReport.setStatus(true);
                        } else {
                            executionStatusReport.setStatus(false);
                        }

                        PipeHistory pipeHistory = new PipeHistory();
                        pipeHistory.setId(UUID.randomUUID().toString());
                        pipeHistory.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        pipeHistory.setType("8");
                        pipeHistory.setDisabled(false);
                        pipeHistory.setRes(JsonUtils.serialize(executionStatusReport));
                        pipeHistoryService.insert(pipeHistory);

                    }
                }
            }
        }
    }

    /**
    *
    * @param allEquipmentStatus 所有的装备状态
    * @param indices 通道测试涉及的装备下标
    * @return bool 通道测试是否成功
    */
    public boolean pipeTestHelper(Map<String, EquipmentStatus> allEquipmentStatus, Integer[] indices) {
        boolean res = true;
        for(Integer idx : indices){
            if (!allEquipmentStatus.containsKey(idx.toString())) {
                return false;
            }
            res = res && allEquipmentStatus.get(idx.toString()).getCheckStatus();
        }
        return res;
    }


    /**
     * 是否满足时间阈值
     * @param timeVec 时间串
     * @param pipeTestCycleThreshold 通道测试周期阈值，单位s
     * @return 测试结果
     */
    public boolean meetTestCycleHelper(long[] timeVec, long pipeTestCycleThreshold) {
        return MathUtils.findMaxByCollections(timeVec) - MathUtils.findMinByCollections(timeVec) <
                pipeTestCycleThreshold * 1000;
    }
}
