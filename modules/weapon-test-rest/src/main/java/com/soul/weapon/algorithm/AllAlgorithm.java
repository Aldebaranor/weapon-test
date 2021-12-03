package com.soul.weapon.algorithm;

import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.flagwind.commons.StringUtils;
import com.soul.weapon.config.CommonRedisConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.config.TaskTypeConstant;
import com.soul.weapon.entity.*;
import com.soul.weapon.entity.codes.PipeState;
import com.soul.weapon.entity.enums.PipeWeaponIndices;
import com.soul.weapon.model.dds.*;
import com.soul.weapon.service.ComDictionaryItemService;
import com.soul.weapon.service.PipeHistoryService;
import com.soul.weapon.service.PipeTaskService;
import com.soul.weapon.service.PipeTestService;
import com.soul.weapon.utils.MathUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;
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

    private final PipeTaskService pipeTaskService;

    private final PipeTestService pipeTestService;

    private final ComDictionaryItemService comDictionaryItemService;

    /**
     * 不执行算法返回值
     */
    private final String NONE = "-1";
    /**
     * 通道测试的测试周期阈值
     **/
    private Long PIPETEST_CYCLE_THRESHOLD = 3L;

    /**
     * 威胁判断-传感器探测时间阈值
     **/
    private Long DETECTOR_TIME_THRESHOLD = 2L;

    /**
     * 指示处理精度测试-指示处理时间阈值
     */
    private Long INSTRUCTION_TIME_THRESHOLD = 2L;

    /**
     * 雷达航迹测试-雷达航迹测试阈值
     */
    private Long RADARPATH_TIME_THRESHOLD = 2L;
    private String AIRTYPE = "对空目标";

    /**
     * 信息流程时间阈值
     **/
    private Long PROGRESS_TIME_THRESHOLD = 60L;

    /**
     * 执行时间阈值
     **/
    private Long EXECUTION_TIME_THRESHOLD = 60L;

    /**
     * 拦截距离测试阈值
     */
    private Long INTERCEPTION_DISTANCE = 50L;

    /**
     * 火控解算时间阈值
     **/
    private Long FIRECONTROL_TIME_THRESHOLD = 5L;

    /**
     * 发射架调转时间阈值
     **/
    private Long LAUNCHER_ROTATION_TIME_THRESHOLD = 5L;

    /**
     * 多目标拦截中最小有效拦截距离
     **/
    private Long MIN_INTERCEPTION_DISTANCE = 50L;

    private final PipeHistoryService pipeHistoryService;


    /**
     * 航空导弹-1
     */
    public void shipToAirMissile() {
        String threshold = pipeTestSetThreshold(TaskTypeConstant.Ship_Air_Missile);
        if (StringUtils.equals(threshold, NONE)) {
            return;
        }
        //更新阈值
        PIPETEST_CYCLE_THRESHOLD=Long.valueOf(threshold);

        if (!Boolean.TRUE.equals(RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate()
                .hasKey(Constant.EQUIPMENT_STATUS_HTTP_KEY))) {
            log.error("从Redis中获取装备信息失败！-1");
            return;
        }
        Map<String, String> tmpEquipments = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).
                boundHashOps(Constant.EQUIPMENT_STATUS_HTTP_KEY).entries();
        assert tmpEquipments != null;
        Map<String, EquipmentStatus> allEquipmentStatus = tmpEquipments.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), EquipmentStatus.class))
        );

        HistoryInfo.ShipToAirMissileTestReport tmpReport = new HistoryInfo.ShipToAirMissileTestReport();

        assert allEquipmentStatus.containsKey(PipeWeaponIndices.AirMissileRadar.getValue());
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.AirMissileFireControl.getValue());
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.AirMissileLauncher.getValue());
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.AirMissileShortRange.getValue());
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.AirMissileMediumRange.getValue());
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.AirMissileLongRange.getValue());

        tmpReport.setTime(System.currentTimeMillis());
        tmpReport.setRadarSelfCheck(allEquipmentStatus.get(
                PipeWeaponIndices.AirMissileRadar.getValue()).getCheckStatus());
        tmpReport.setRadarId(PipeWeaponIndices.AirMissileRadar.getValue());
        tmpReport.setFireControlId(PipeWeaponIndices.AirMissileFireControl.getValue());
        tmpReport.setFireControlSelfCheck(allEquipmentStatus.get(
                PipeWeaponIndices.AirMissileFireControl.getValue()).getCheckStatus());
        tmpReport.setLauncherId(PipeWeaponIndices.AirMissileLauncher.getValue());
        tmpReport.setLauncherSelfCheck(allEquipmentStatus.get(
                PipeWeaponIndices.AirMissileLauncher.getValue()).getCheckStatus());
        tmpReport.setMissileShortId(PipeWeaponIndices.AirMissileShortRange.getValue());
        tmpReport.setMissileSelfShortCheck(allEquipmentStatus.get(
                PipeWeaponIndices.AirMissileShortRange.getValue()).getCheckStatus());
        tmpReport.setMissileMediumId(PipeWeaponIndices.AirMissileMediumRange.getValue());
        tmpReport.setMissileSelfMediumCheck(allEquipmentStatus.get(
                PipeWeaponIndices.AirMissileMediumRange.getValue()).getCheckStatus());
        tmpReport.setMissileLongId(PipeWeaponIndices.AirMissileLongRange.getValue());
        tmpReport.setMissileSelfLongCheck(allEquipmentStatus.get(
                PipeWeaponIndices.AirMissileLongRange.getValue()).getCheckStatus());

        long[] timeVec = {
                allEquipmentStatus.get(PipeWeaponIndices.AirMissileRadar.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.AirMissileFireControl.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.AirMissileLauncher.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.AirMissileShortRange.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.AirMissileMediumRange.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.AirMissileLongRange.getValue()).getTime()
        };
        boolean pipeStatus = tmpReport.getRadarSelfCheck() &&
                tmpReport.getFireControlSelfCheck() &&
                tmpReport.getLauncherSelfCheck() &&
                tmpReport.getMissileSelfShortCheck() &&
                tmpReport.getMissileSelfMediumCheck() &&
                tmpReport.getMissileSelfLongCheck() &&
                meetTestCycleHelper(timeVec, PIPETEST_CYCLE_THRESHOLD);
        tmpReport.setStatus(pipeStatus);

        PipeHistory pipeHistory = new PipeHistory();
        pipeHistory.setId(UUID.randomUUID().toString());
        pipeHistory.setCreateTime(new Timestamp(System.currentTimeMillis()));
        pipeHistory.setType("1");
        pipeHistory.setDisabled(false);
        pipeHistory.setRes(JsonUtils.serialize(tmpReport));
        pipeHistoryService.insert(pipeHistory);
    }


    /**
     * 反导舰炮算法-2
     */
    public void antiMissileShipGun() {
        String threshold = pipeTestSetThreshold(TaskTypeConstant.Anti_missile_naval_gun);
        if (StringUtils.equals(threshold, NONE)) {
            return;
        }
        //更新阈值
        PIPETEST_CYCLE_THRESHOLD=Long.valueOf(threshold);

        if (!Boolean.TRUE.equals(RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate()
                .hasKey(Constant.EQUIPMENT_STATUS_HTTP_KEY))) {
            log.error("从Redis中获取装备信息失败！-2");
            return;
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
        tmpReport.setRadarSelfCheck(allEquipmentStatus.get(
                PipeWeaponIndices.AntiMissileShipGunRadar.getValue()).getCheckStatus());
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
     * 鱼类防御武器测试-3
     */
    public void torpedoDefense() {
        String threshold = pipeTestSetThreshold(TaskTypeConstant.Torpedo_defense);
        if (StringUtils.equals(threshold, NONE)) {
            return;
        }
        //更新阈值
        PIPETEST_CYCLE_THRESHOLD=Long.valueOf(threshold);

        if (!Boolean.TRUE.equals(RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate()
                .hasKey(Constant.EQUIPMENT_STATUS_HTTP_KEY))) {
            log.error("从Redis中获取装备信息失败！-3");
            return;
        }

        Map<String, String> tmpEquipments = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).
                boundHashOps(Constant.EQUIPMENT_STATUS_HTTP_KEY).entries();
        assert tmpEquipments != null;
        Map<String, EquipmentStatus> allEquipmentStatus = tmpEquipments.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), EquipmentStatus.class))
        );

        HistoryInfo.TorpedoTestReport tmpReport = new HistoryInfo.TorpedoTestReport();
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.Sonar.getValue());
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.TorpedoFireControl.getValue());
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.TorpedoLauncher.getValue());
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.Torpedo.getValue());

        tmpReport.setTime(System.currentTimeMillis());
        tmpReport.setSonarId(PipeWeaponIndices.Sonar.getValue());
        tmpReport.setSonarSelfCheck(
                allEquipmentStatus.get(PipeWeaponIndices.Sonar.getValue()).getCheckStatus());
        tmpReport.setFireControlId(PipeWeaponIndices.TorpedoFireControl.getValue());
        tmpReport.setFireControlSelfCheck(
                allEquipmentStatus.get(PipeWeaponIndices.TorpedoFireControl.getValue()).getCheckStatus());
        tmpReport.setLauncherId(PipeWeaponIndices.TorpedoLauncher.getValue());
        tmpReport.setLauncherSelfCheck(
                allEquipmentStatus.get(PipeWeaponIndices.TorpedoLauncher.getValue()).getCheckStatus());
        tmpReport.setTorpedoId(PipeWeaponIndices.Torpedo.getValue());
        tmpReport.setTorpedoSelfCheck(
                allEquipmentStatus.get(PipeWeaponIndices.Torpedo.getValue()).getCheckStatus());
        long[] timeVec = {
                allEquipmentStatus.get(PipeWeaponIndices.Sonar.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.TorpedoFireControl.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.TorpedoLauncher.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.Torpedo.getValue()).getTime()
        };
        boolean pipeStatus = tmpReport.getSonarSelfCheck() &&
                tmpReport.getFireControlSelfCheck() &&
                tmpReport.getLauncherSelfCheck() &&
                tmpReport.getTorpedoSelfCheck() &&
                meetTestCycleHelper(timeVec, PIPETEST_CYCLE_THRESHOLD);
        tmpReport.setStatus(pipeStatus);

        PipeHistory pipeHistory = new PipeHistory();
        pipeHistory.setId(UUID.randomUUID().toString());
        pipeHistory.setCreateTime(new Timestamp(System.currentTimeMillis()));
        pipeHistory.setType("3");
        pipeHistory.setDisabled(false);
        pipeHistory.setRes(JsonUtils.serialize(tmpReport));
        pipeHistoryService.insert(pipeHistory);
    }

    /**
     * 电子对抗武器测试-4
     */
    public void electronicCountermeasure() {
        String threshold = pipeTestSetThreshold(TaskTypeConstant.electronic_countermeasure);
        if (StringUtils.equals(threshold, NONE)) {
            return;
        }

        //更新阈值
        PIPETEST_CYCLE_THRESHOLD=Long.valueOf(threshold);

        if (!Boolean.TRUE.equals(RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate()
                .hasKey(Constant.EQUIPMENT_STATUS_HTTP_KEY))) {
            log.error("从Redis中获取装备信息失败！-4");
            return;
        }

        Map<String, String> tmpEquipments = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).
                boundHashOps(Constant.EQUIPMENT_STATUS_HTTP_KEY).entries();
        assert tmpEquipments != null;
        Map<String, EquipmentStatus> allEquipmentStatus = tmpEquipments.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), EquipmentStatus.class))
        );

        HistoryInfo.ElectronicWeaponTestReport tmpReport = new HistoryInfo.ElectronicWeaponTestReport();
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.ElectronicDetection.getValue());
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.ElectronicCountermeasure.getValue());
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.MultiUsageLaunch.getValue());
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.OutBoardElectronicCountermeasure.getValue());
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.InBoardElectronicCountermeasure.getValue());

        tmpReport.setTime(System.currentTimeMillis());
        tmpReport.setElectronicDetectorId(PipeWeaponIndices.ElectronicDetection.getValue());
        tmpReport.setElectronicDetectorSelfCheck(
                allEquipmentStatus.get(PipeWeaponIndices.ElectronicDetection.getValue()).getCheckStatus());
        tmpReport.setFireControlId(PipeWeaponIndices.ElectronicCountermeasure.getValue());
        tmpReport.setFireControlSelfCheck(
                allEquipmentStatus.get(PipeWeaponIndices.ElectronicCountermeasure.getValue()).getCheckStatus());
        tmpReport.setLauncherId(PipeWeaponIndices.MultiUsageLaunch.getValue());
        tmpReport.setLauncherSelfCheck(
                allEquipmentStatus.get(PipeWeaponIndices.MultiUsageLaunch.getValue()).getCheckStatus());
        tmpReport.setOuterElectronicWeaponId(PipeWeaponIndices.OutBoardElectronicCountermeasure.getValue());
        tmpReport.setOuterElectronicWeaponSelfCheck(
                allEquipmentStatus.get(PipeWeaponIndices.OutBoardElectronicCountermeasure.getValue()).getCheckStatus());
        tmpReport.setInnerElectronicWeaponId(PipeWeaponIndices.InBoardElectronicCountermeasure.getValue());
        tmpReport.setInnerElectronicWeaponSelfCheck(
                allEquipmentStatus.get(PipeWeaponIndices.InBoardElectronicCountermeasure.getValue()).getCheckStatus());
        long[] timeVec = {
                allEquipmentStatus.get(PipeWeaponIndices.ElectronicDetection.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.ElectronicCountermeasure.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.MultiUsageLaunch.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.OutBoardElectronicCountermeasure.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.InBoardElectronicCountermeasure.getValue()).getTime()
        };
        boolean pipeStatus = tmpReport.getElectronicDetectorSelfCheck() &&
                tmpReport.getFireControlSelfCheck() &&
                tmpReport.getLauncherSelfCheck() &&
                tmpReport.getOuterElectronicWeaponSelfCheck() &&
                tmpReport.getInnerElectronicWeaponSelfCheck() &&
                meetTestCycleHelper(timeVec, PIPETEST_CYCLE_THRESHOLD);
        tmpReport.setStatus(pipeStatus);

        PipeHistory pipeHistory = new PipeHistory();
        pipeHistory.setId(UUID.randomUUID().toString());
        pipeHistory.setCreateTime(new Timestamp(System.currentTimeMillis()));
        pipeHistory.setType("4");
        pipeHistory.setDisabled(false);
        pipeHistory.setRes(JsonUtils.serialize(tmpReport));
        pipeHistoryService.insert(pipeHistory);
    }


    /**
     * 水声对抗武器测试 -5
     */
    public void underwaterAcousticCountermeasure() {
        String threshold = pipeTestSetThreshold(TaskTypeConstant.Underwater_acoustic_countermeasure);
        if (StringUtils.equals(threshold, NONE)) {
            return;
        }
        //更新阈值
        PIPETEST_CYCLE_THRESHOLD=Long.valueOf(threshold);

        if (!Boolean.TRUE.equals(RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate()
                .hasKey(Constant.EQUIPMENT_STATUS_HTTP_KEY))) {
            log.error("从Redis中获取装备信息失败！-5");
            return;
        }

        Map<String, String> tmpEquipments = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).
                boundHashOps(Constant.EQUIPMENT_STATUS_HTTP_KEY).entries();
        assert tmpEquipments != null;
        Map<String, EquipmentStatus> allEquipmentStatus = tmpEquipments.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), EquipmentStatus.class))
        );

        HistoryInfo.WaterWeaponTestReport tmpReport = new HistoryInfo.WaterWeaponTestReport();
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.UnderwaterAcousticCountermeasureControl.getValue());
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.UnderwaterAcousticCountermeasure.getValue());
        assert allEquipmentStatus.containsKey(PipeWeaponIndices.MultiUsageLaunch.getValue());

        tmpReport.setTime(System.currentTimeMillis());
        tmpReport.setSonarId(PipeWeaponIndices.Sonar.getValue());
        tmpReport.setSonarSelfCheck(
                allEquipmentStatus.get(PipeWeaponIndices.Sonar.getValue()).getCheckStatus());
        tmpReport.setFireControlId(PipeWeaponIndices.UnderwaterAcousticCountermeasureControl.getValue());
        tmpReport.setFireControlSelfCheck(
                allEquipmentStatus.get(PipeWeaponIndices.UnderwaterAcousticCountermeasureControl.getValue()).getCheckStatus());
        tmpReport.setLauncherId(PipeWeaponIndices.MultiUsageLaunch.getValue());
        tmpReport.setLauncherSelfCheck(
                allEquipmentStatus.get(PipeWeaponIndices.MultiUsageLaunch.getValue()).getCheckStatus());
        tmpReport.setWaterWeaponId(PipeWeaponIndices.UnderwaterAcousticCountermeasure.getValue());
        tmpReport.setWaterWeaponSelfCheck(
                allEquipmentStatus.get(PipeWeaponIndices.UnderwaterAcousticCountermeasure.getValue()).getCheckStatus());

        long[] timeVec = {
                allEquipmentStatus.get(PipeWeaponIndices.Sonar.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.UnderwaterAcousticCountermeasureControl.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.MultiUsageLaunch.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.UnderwaterAcousticCountermeasure.getValue()).getTime()
        };
        boolean pipeStatus = tmpReport.getSonarSelfCheck() &&
                tmpReport.getFireControlSelfCheck() &&
                tmpReport.getLauncherSelfCheck() &&
                tmpReport.getWaterWeaponSelfCheck() &&
                meetTestCycleHelper(timeVec, PIPETEST_CYCLE_THRESHOLD);
        tmpReport.setStatus(pipeStatus);

        PipeHistory pipeHistory = new PipeHistory();
        pipeHistory.setId(UUID.randomUUID().toString());
        pipeHistory.setCreateTime(new Timestamp(System.currentTimeMillis()));
        pipeHistory.setType("5");
        pipeHistory.setDisabled(false);
        pipeHistory.setRes(JsonUtils.serialize(tmpReport));
        pipeHistoryService.insert(pipeHistory);
    }

    /**
     * 信息流程测试-6
     */
    public void infoProcessTest() {

    }


    /**
     * 威胁判断算法-7 指示处理精度测试-8 雷达航迹测试-10
     */
    public void threatenEstimation() {
        String threshold1 = pipeTestSetThreshold(TaskTypeConstant.Threat_judgment);
        String threshold2 = pipeTestSetThreshold(TaskTypeConstant.Indicates_the_processing_accuracy);
        String threshold3 = pipeTestSetThreshold(TaskTypeConstant.Radar_track);
        if (StringUtils.equals(threshold1, NONE)&&StringUtils.equals(threshold2, NONE) &&StringUtils.equals(threshold3, NONE)) {
            return;
        }
        //更新阈值
        if (!StringUtils.equals(threshold1, NONE)){
            DETECTOR_TIME_THRESHOLD=Long.valueOf(threshold1);
        }
        if (!StringUtils.equals(threshold2, NONE)){
            INSTRUCTION_TIME_THRESHOLD=Long.valueOf(threshold2);
        }
        if (!StringUtils.equals(threshold3, NONE)){
            RADARPATH_TIME_THRESHOLD=Long.valueOf(threshold3);
        }

        StringRedisTemplate template = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate();
        if (!template.hasKey(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY) || !template.hasKey(Constant.TARGET_INFO_HTTP_KEY)) {
            log.error("从Redis中获取目标指示信息或目标真值信息失败！");
            return;
        }

        Map<String, String> tmpInstructionInfos = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).boundHashOps(
                Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY).entries();
        assert tmpInstructionInfos != null;
        Map<String, TargetInstructionsInfo> allInstructionInfos = tmpInstructionInfos.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), TargetInstructionsInfo.class))
        );

        for (String key2 : allInstructionInfos.keySet()) {

            TargetInstructionsInfo targetInstructionsInfo = allInstructionInfos.get(key2);

            Long n = template.boundListOps(Constant.TARGET_INFO_HTTP_KEY + "_" + targetInstructionsInfo.getTargetId()).size();

            boolean threatenState = false, instructionState = false, radarState = false;

            for (int i = 0; i < n; i++) {
                TargetInfo targetInfo = JsonUtils.deserialize(template.boundListOps(
                        Constant.TARGET_INFO_HTTP_KEY + "_" + targetInstructionsInfo.getTargetId()).index(i), TargetInfo.class);

                assert targetInfo != null;

                if (StringUtils.equals(targetInfo.getTargetId(), targetInstructionsInfo.getTargetId())) {

                    if (!threatenState && Math.abs(targetInfo.getTime() - targetInstructionsInfo.getTime()) < DETECTOR_TIME_THRESHOLD) {

                        HistoryInfo.ThreatenReport threatenReport = new HistoryInfo.ThreatenReport();
                        threatenReport.setId(targetInfo.getTargetId());
                        threatenReport.setType(targetInfo.getTargetTypeId());
                        threatenReport.setDistanceOffset(
                                Math.abs(targetInfo.getDistance() - targetInstructionsInfo.getDistance()));
                        threatenReport.setTime(targetInstructionsInfo.getTime());
                        threatenReport.setPitchOffset(
                                Math.abs(targetInfo.getPitchAngle() - targetInstructionsInfo.getPitchAngle()));
                        threatenReport.setSpeedOffset(
                                Math.abs(targetInfo.getSpeed() - targetInstructionsInfo.getSpeed()));

                        PipeHistory threatenPipeHistory = new PipeHistory();
                        threatenPipeHistory.setId(UUID.randomUUID().toString());
                        threatenPipeHistory.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        threatenPipeHistory.setType("7");
                        threatenPipeHistory.setDisabled(false);
                        threatenPipeHistory.setRes(JsonUtils.serialize(threatenReport));
                        pipeHistoryService.insert(threatenPipeHistory);

                        threatenState = true;

                    }

                    // 指示处理精度测试
                    if (!instructionState &&
                            Math.abs(targetInfo.getTime() - targetInstructionsInfo.getTime()) < INSTRUCTION_TIME_THRESHOLD) {

                        HistoryInfo.InstructionAccuracyReport instructionAccuracyReport = new HistoryInfo.InstructionAccuracyReport();
                        instructionAccuracyReport.setTime(targetInstructionsInfo.getTime());
                        instructionAccuracyReport.setTargetId(targetInstructionsInfo.getTargetId());
                        instructionAccuracyReport.setTargetType(targetInstructionsInfo.getTargetTypeId());
                        instructionAccuracyReport.setSensorId(targetInstructionsInfo.getEquipmentId());
                        instructionAccuracyReport.setSensorType(targetInstructionsInfo.getEquipmentTypeId());

                        instructionAccuracyReport.setDistanceAccuracy(
                                Math.abs(targetInstructionsInfo.getDistance() - targetInfo.getDistance()));
                        instructionAccuracyReport.setAzimuthAccuracy(
                                Math.abs(targetInstructionsInfo.getAzimuth() - targetInfo.getAzimuth()));
                        instructionAccuracyReport.setDepthAccuracy(
                                Math.abs(targetInstructionsInfo.getDepth() - targetInfo.getDepth()));
                        instructionAccuracyReport.setPitchAccuracy(
                                Math.abs(targetInstructionsInfo.getPitchAngle() - targetInfo.getPitchAngle()));

                        PipeHistory accuracyPipeHistory = new PipeHistory();
                        accuracyPipeHistory.setId(UUID.randomUUID().toString());
                        accuracyPipeHistory.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        accuracyPipeHistory.setType("8");
                        accuracyPipeHistory.setDisabled(false);
                        accuracyPipeHistory.setRes(JsonUtils.serialize(instructionAccuracyReport));
                        pipeHistoryService.insert(accuracyPipeHistory);

                        instructionState = true;

                    }

                    // 雷达航迹测试
                    if (!radarState && targetInfo.getTime() - targetInstructionsInfo.getTime() > 0
                            && targetInfo.getTime() - targetInstructionsInfo.getTime() < RADARPATH_TIME_THRESHOLD
                            && targetInfo.getTargetTypeId().equals(AIRTYPE)
                            && targetInstructionsInfo.getTargetTypeId().equals(AIRTYPE)) {

                        HistoryInfo.RadarPathReport radarPathReport = new HistoryInfo.RadarPathReport();
                        radarPathReport.setTime(targetInstructionsInfo.getTime());
                        radarPathReport.setTargetId(targetInstructionsInfo.getTargetId());
                        radarPathReport.setTargetType(targetInstructionsInfo.getTargetTypeId());
                        radarPathReport.setSensorId(targetInstructionsInfo.getEquipmentId());
                        radarPathReport.setSensorType(targetInstructionsInfo.getEquipmentTypeId());
                        radarPathReport.setActualTargetPitch(targetInfo.getPitchAngle());
                        radarPathReport.setActualTargetDistance(targetInfo.getDistance());
                        radarPathReport.setShowedTargetDistance(targetInstructionsInfo.getDistance());
                        radarPathReport.setShowedTargetPitch(targetInstructionsInfo.getPitchAngle());

                        PipeHistory radarPathPipeHistory = new PipeHistory();
                        radarPathPipeHistory.setId(UUID.randomUUID().toString());
                        radarPathPipeHistory.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        radarPathPipeHistory.setType("10");
                        radarPathPipeHistory.setDisabled(false);
                        radarPathPipeHistory.setRes(JsonUtils.serialize(radarPathReport));
                        pipeHistoryService.insert(radarPathPipeHistory);
                        radarState = true;
                    }

                }

                if (threatenState && radarState && instructionState) {
                    break;
                }

            }
        }
    }


    /**
     * 执行情况测试-9
     */
    public void executionStatusTest() {
        String threshold1 = pipeTestSetThreshold(TaskTypeConstant.Information_flow);
        String threshold2 = pipeTestSetThreshold(TaskTypeConstant.Implementation);
        if (StringUtils.equals(threshold1, NONE)&&StringUtils.equals(threshold2, NONE)) {
           return;
        }
        //更新阈值
        if (!StringUtils.equals(threshold1, NONE)){
            PROGRESS_TIME_THRESHOLD=Long.valueOf(threshold1);
        }
        if (!StringUtils.equals(threshold2, NONE)){
            EXECUTION_TIME_THRESHOLD=Long.valueOf(threshold2);
        }

        StringRedisTemplate template = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate();
        if (!template.hasKey(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY) ||
                !template.hasKey(Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY)
                || !template.hasKey(Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY)) {
            log.error("从Redis中获取信息失败！");
            return;
        }
        Map<String, String> tmpFireControlInfos = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).boundHashOps(
                Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY).entries();

        assert tmpFireControlInfos != null;
        Map<String, TargetFireControlInfo> allFireControlInfos = tmpFireControlInfos.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), TargetFireControlInfo.class))
        );

        for (String key : allFireControlInfos.keySet()) {

            boolean state = false;

            TargetFireControlInfo targetFireControlInfo = allFireControlInfos.get(key);

            Long n = template.boundListOps(Constant.TARGET_INFO_HTTP_KEY + "_" + targetFireControlInfo.getTargetId()).size();

            for (int i = 0; i < n; i++) {

                TargetInstructionsInfo targetInstructionsInfo = JsonUtils.deserialize(template.boundListOps(
                        Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY + "_" + targetFireControlInfo.getTargetId()).index(i),
                        TargetInstructionsInfo.class);

                Long m = template.boundListOps(Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY + "_" +
                        targetFireControlInfo.getTargetId()).size();

                for (int j = 0; j < m; j++) {

                    EquipmentLaunchStatus equipmentLaunchStatus = JsonUtils.deserialize(template.boundListOps(
                            Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY + "_" + targetFireControlInfo.getTargetId()).index(j),
                            EquipmentLaunchStatus.class);

                    if (StringUtils.equals(targetFireControlInfo.getTargetId(), targetInstructionsInfo.getTargetId()) &&
                            StringUtils.equals(targetFireControlInfo.getTargetId(), equipmentLaunchStatus.getTargetId())) {

                        HistoryInfo.ExecutionStatusReport executionStatusReport = new HistoryInfo.ExecutionStatusReport();
                        HistoryInfo.InfoProcessTestReport infoProcessTestReport = new HistoryInfo.InfoProcessTestReport();

                        boolean b = equipmentLaunchStatus.getTime() > targetFireControlInfo.getTime() &&
                                targetFireControlInfo.getTime() > targetInstructionsInfo.getTime();
                        boolean c = equipmentLaunchStatus.getTime() - targetInstructionsInfo.getTime() < EXECUTION_TIME_THRESHOLD;
                        boolean d = equipmentLaunchStatus.getTime() - targetInstructionsInfo.getTime() < PROGRESS_TIME_THRESHOLD;

                        // 设置执行情况返回报文信息
                        executionStatusReport.setTargetId(equipmentLaunchStatus.getTargetId());
                        executionStatusReport.setTime(equipmentLaunchStatus.getTime());
                        executionStatusReport.setTargetType(equipmentLaunchStatus.getTargetTypeId());
                        if (b && c) {
                            executionStatusReport.setStatus(true);
                        } else {
                            executionStatusReport.setStatus(false);
                        }

                        // 设置信息流程返回报文信息
                        infoProcessTestReport.setId(equipmentLaunchStatus.getTargetId());
                        infoProcessTestReport.setTime(equipmentLaunchStatus.getTime());
                        infoProcessTestReport.setType(equipmentLaunchStatus.getTargetTypeId());
                        if (b && d) {
                            infoProcessTestReport.setStatus(true);
                        } else {
                            infoProcessTestReport.setStatus(false);
                        }

                        PipeHistory pipeHistory = new PipeHistory();
                        pipeHistory.setId(UUID.randomUUID().toString());
                        pipeHistory.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        pipeHistory.setType("9");
                        pipeHistory.setDisabled(false);
                        pipeHistory.setRes(JsonUtils.serialize(executionStatusReport));
                        pipeHistoryService.insert(pipeHistory);

                        pipeHistory.setId(UUID.randomUUID().toString());
                        pipeHistory.setType("6");
                        pipeHistory.setRes(JsonUtils.serialize(infoProcessTestReport));
                        pipeHistoryService.insert(pipeHistory);

                        state = true;
                        break;
                    }
                }
                if (state) {
                    break;
                }
            }
        }
    }

    /**
     * 拦截距离测试-11
     */
    public void interceptDistanceTest() {

        String threshold = pipeTestSetThreshold(TaskTypeConstant.Intercept_distance);
        if (StringUtils.equals(threshold, NONE)) {
            return;
        }
        //更新阈值
        INTERCEPTION_DISTANCE=Long.valueOf(threshold);

        StringRedisTemplate template = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate();
        if (!template.hasKey(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY)) {
            log.error("从Redis中获取目标指示信息失败！");
            return;
        }

        Map<String, String> tempInstructions = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).boundHashOps(
                Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY).entries();

        assert tempInstructions != null;
        Map<String, TargetInstructionsInfo> allInstructions = tempInstructions.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), TargetInstructionsInfo.class)
        ));

        for (TargetInstructionsInfo targetInstructionsInfo : allInstructions.values()) {

            Long n = template.boundListOps(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY + "_" +
                    targetInstructionsInfo.getTargetId()).size();

            Long MaxTime = 0L;
            Float MinDistance = Float.MAX_VALUE;

            for (int i = 0; i < n; i++) {

                String tmpInstruction = template.boundListOps(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY + "_" +
                        targetInstructionsInfo.getTargetId()).index(i);

                TargetInstructionsInfo targetInstructionsInfo1 = JsonUtils.deserialize(
                        tmpInstruction, TargetInstructionsInfo.class);

                if (targetInstructionsInfo1.getDistance() < INTERCEPTION_DISTANCE) {
                    continue;
                }

                MaxTime = targetInstructionsInfo1.getTime() > MaxTime ? targetInstructionsInfo1.getTime() : MaxTime;

                MinDistance = targetInstructionsInfo1.getDistance() < MinDistance ?
                        targetInstructionsInfo1.getDistance() : MinDistance;


            }

            if (MinDistance == Float.MAX_VALUE) {
                continue;
            }

            HistoryInfo.InterceptDistanceReport interceptDistanceReport = new HistoryInfo.InterceptDistanceReport();
            interceptDistanceReport.setInterceptDistance(MinDistance);
            interceptDistanceReport.setTime(MaxTime);
            interceptDistanceReport.setTargetType(targetInstructionsInfo.getTargetTypeId());
            interceptDistanceReport.setTargetId(targetInstructionsInfo.getTargetId());

            PipeHistory pipeHistory = new PipeHistory();
            pipeHistory.setRes(JsonUtils.serialize(interceptDistanceReport));
            pipeHistory.setType("11");
            pipeHistory.setId(UUID.randomUUID().toString());
            pipeHistory.setDisabled(false);
            pipeHistory.setCreateTime(new Timestamp(System.currentTimeMillis()));
            pipeHistoryService.insert(pipeHistory);

        }
    }

    /**
     * 火控解算精度测试-12
     */
    public void fireControlTest() {
        String threshold = pipeTestSetThreshold(TaskTypeConstant.Fire_control_calculation_accuracy);
        if (StringUtils.equals(threshold, NONE)) {
            return;
        }
        //更新阈值
        FIRECONTROL_TIME_THRESHOLD=Long.valueOf(threshold);

        StringRedisTemplate template = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate();
        if (!template.hasKey(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY) ||
                !template.hasKey(Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY)) {
            log.error("从Redis中获取目标指示信息或目标火控信息失败！");
            return;
        }

        Map<String, String> tmpFireControls = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).boundHashOps(
                Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY).entries();
        assert tmpFireControls != null;
        Map<String, TargetFireControlInfo> allFireControlInfos = tmpFireControls.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), TargetFireControlInfo.class))
        );

        for (TargetFireControlInfo targetFireControlInfo : allFireControlInfos.values()) {

            Long n = template.boundListOps(
                    Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY + "_" + targetFireControlInfo.getTargetId()).size();


            for (int i = 0; i < n; i++) {
                TargetInstructionsInfo targetInstructionsInfo = JsonUtils.deserialize(template.boundListOps(
                        Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY + "_" + targetFireControlInfo.getTargetId()).index(i),
                        TargetInstructionsInfo.class);

                assert targetInstructionsInfo != null;

                if (targetInstructionsInfo.getTargetId().equals(targetFireControlInfo.getTargetId())) {

                    if (Math.abs(targetFireControlInfo.getTime() - targetInstructionsInfo.getTime()) < FIRECONTROL_TIME_THRESHOLD) {

                        HistoryInfo.FireControlReport fireControlReport = new HistoryInfo.FireControlReport();
                        fireControlReport.setTargetId(targetInstructionsInfo.getTargetId());
                        fireControlReport.setTargetType(targetInstructionsInfo.getTargetTypeId());
                        fireControlReport.setFireControlId(targetFireControlInfo.getFireControlSystemId());
                        fireControlReport.setFireControlType(targetFireControlInfo.getFireControlSystemTypeId());
                        fireControlReport.setTime(targetFireControlInfo.getTime());

                        fireControlReport.setTargetDistance(
                                Math.abs(targetFireControlInfo.getDistance() - targetInstructionsInfo.getDistance()));
                        fireControlReport.setTargetPitch(
                                Math.abs(targetFireControlInfo.getPitchAngle() - targetInstructionsInfo.getPitchAngle()));
                        fireControlReport.setTargetDepth(
                                Math.abs(targetFireControlInfo.getDepth() - targetInstructionsInfo.getDepth()));
                        fireControlReport.setTargetAzimuth(
                                Math.abs(targetFireControlInfo.getAzimuth() - targetInstructionsInfo.getAzimuth()));

                        PipeHistory fireControlPipeHistory = new PipeHistory();
                        fireControlPipeHistory.setId(UUID.randomUUID().toString());
                        fireControlPipeHistory.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        fireControlPipeHistory.setType("12");
                        fireControlPipeHistory.setDisabled(false);
                        fireControlPipeHistory.setRes(JsonUtils.serialize(fireControlReport));
                        pipeHistoryService.insert(fireControlPipeHistory);

                        break;
                    }
                }
            }
        }
    }


    /**
     * 反应时间测试-13
     */
    public void reactionTimeTest() {

        String threshold = pipeTestSetThreshold(TaskTypeConstant.reaction_time);
        if (StringUtils.equals(threshold, NONE)) {
            return;
        }
        StringRedisTemplate template = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate();
        if (!template.hasKey(Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY) ||
                !template.hasKey(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY)) {
            log.error("从Redis中获取武器发射或目标指示信息失败！");
            return;
        }

        Map<String, String> tmpEqupimentLaunch = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).boundHashOps(
                Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY).entries();
        assert tmpEqupimentLaunch != null;

        Map<String, EquipmentLaunchStatus> allEquipmentStatus = tmpEqupimentLaunch.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), EquipmentLaunchStatus.class)
        ));

        for (EquipmentLaunchStatus equipmentLaunchStatus : allEquipmentStatus.values()) {

            Long n = template.boundListOps(
                    Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY + "_" + equipmentLaunchStatus.getTargetId()).size();

            Long reactionTime = Long.MAX_VALUE;

            TargetInstructionsInfo targetInstructionsInfo = new TargetInstructionsInfo();

            for (int i = 0; i < n; i++) {
                String tmpLaunch = template.boundListOps(
                        Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY + "_" + equipmentLaunchStatus.getTargetId()).index(i);


                TargetInstructionsInfo tmpInstruction = JsonUtils.deserialize(tmpLaunch, TargetInstructionsInfo.class);
                if (reactionTime > equipmentLaunchStatus.getTime() - tmpInstruction.getTime()) {
                    reactionTime = equipmentLaunchStatus.getTime() - tmpInstruction.getTime();
                    targetInstructionsInfo = tmpInstruction;
                }

            }

            if (reactionTime == Long.MAX_VALUE) {
                continue;
            }

            HistoryInfo.ReactionReport reactionReport = new HistoryInfo.ReactionReport();
            reactionReport.setTime(equipmentLaunchStatus.getTime());
            reactionReport.setReactionTime(reactionTime);
            reactionReport.setTargetId(targetInstructionsInfo.getTargetId());
            reactionReport.setTargetType(targetInstructionsInfo.getTargetTypeId());
            reactionReport.setSensorId(targetInstructionsInfo.getEquipmentId());
            reactionReport.setSensorType(targetInstructionsInfo.getEquipmentTypeId());
            reactionReport.setWeaponId(equipmentLaunchStatus.getEquipmentId());
            reactionReport.setWeaponType(equipmentLaunchStatus.getEquipmentTypeId());

            PipeHistory reactionPipeHistory = new PipeHistory();
            reactionPipeHistory.setId(UUID.randomUUID().toString());
            reactionPipeHistory.setCreateTime(new Timestamp(System.currentTimeMillis()));
            reactionPipeHistory.setType("13");
            reactionPipeHistory.setDisabled(false);
            reactionPipeHistory.setRes(JsonUtils.serialize(reactionReport));
            pipeHistoryService.insert(reactionPipeHistory);
        }
    }


    /**
     * 发射架调转精度测试-14
     */
    public void launcherRotationTest() {
        String threshold = pipeTestSetThreshold(TaskTypeConstant.Launcher_rotation_accuracy);
        if (StringUtils.equals(threshold, NONE)) {
            return;
        }
        //更新阈值
        LAUNCHER_ROTATION_TIME_THRESHOLD=Long.valueOf(threshold);

        StringRedisTemplate template = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate();
        if (!template.hasKey(Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY) ||
                !template.hasKey(Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY)) {
            log.error("从Redis中获取武器发射或目标火控信息失败！");
            return;
        }

        Map<String, String> tmpFireControls = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).boundHashOps(
                Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY).entries();
        assert tmpFireControls != null;
        Map<String, TargetFireControlInfo> allFireControlInfos = tmpFireControls.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), TargetFireControlInfo.class))
        );

        for (TargetFireControlInfo targetFireControlInfo : allFireControlInfos.values()) {

            Long n = template.boundListOps(
                    Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY + "_" + targetFireControlInfo.getTargetId()).size();


            for (int i = 0; i < n; i++) {

                EquipmentLaunchStatus equipmentLaunchStatus = JsonUtils.deserialize(template.boundListOps(
                        Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY + "_" + targetFireControlInfo.getTargetId()).index(i),
                        EquipmentLaunchStatus.class);

                assert equipmentLaunchStatus != null;

                if (equipmentLaunchStatus.getTargetId().equals(targetFireControlInfo.getTargetId())) {

                    if (equipmentLaunchStatus.getTime() - targetFireControlInfo.getTime() < LAUNCHER_ROTATION_TIME_THRESHOLD
                            && 0 < equipmentLaunchStatus.getTime() - targetFireControlInfo.getTime()) {

                        HistoryInfo.LauncherRotationReport launcherRotationReport = new HistoryInfo.LauncherRotationReport();
                        launcherRotationReport.setTargetId(equipmentLaunchStatus.getTargetId());
                        launcherRotationReport.setTargetType(equipmentLaunchStatus.getTargetTypeId());
                        launcherRotationReport.setWeaponId(equipmentLaunchStatus.getEquipmentId());
                        launcherRotationReport.setWeaponType(equipmentLaunchStatus.getEquipmentTypeId());
                        launcherRotationReport.setTime(equipmentLaunchStatus.getTime());

                        launcherRotationReport.setLauncherPitchAccuracy(Math.abs(targetFireControlInfo.getPitchAngle() -
                                equipmentLaunchStatus.getLaunchPitchAngle()));
                        ;
                        launcherRotationReport.setLauncherAzimuthAccuracy(Math.abs(targetFireControlInfo.getAzimuth() -
                                equipmentLaunchStatus.getLaunchAzimuth()));

                        PipeHistory launcherRotationPipeHistory = new PipeHistory();
                        launcherRotationPipeHistory.setId(UUID.randomUUID().toString());
                        launcherRotationPipeHistory.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        launcherRotationPipeHistory.setType("14");
                        launcherRotationPipeHistory.setDisabled(false);
                        launcherRotationPipeHistory.setRes(JsonUtils.serialize(launcherRotationReport));
                        pipeHistoryService.insert(launcherRotationPipeHistory);

                        break;
                    }
                }
            }
        }

    }


    /**
     * 多目标拦截能力测试-15
     */
    public void multiTargetInterceptionTest() {

        String threshold = pipeTestSetThreshold(TaskTypeConstant.Multi_target_interception);
        if (StringUtils.equals(threshold, NONE)) {
            return;
        }
        //更新阈值
        MIN_INTERCEPTION_DISTANCE=Long.valueOf(threshold);

        StringRedisTemplate template = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate();
        if (Boolean.FALSE.equals(template.hasKey(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY))) {
            log.error("从Redis中获取目标指示信息失败！");
            return;
        }

        Map<String, String> tmpEquipmentLaunch = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).boundHashOps(
                Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY).entries();
        assert tmpEquipmentLaunch != null;

        Map<String, EquipmentLaunchStatus> allEquipmentStatus = tmpEquipmentLaunch.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), EquipmentLaunchStatus.class)
        ));


        List<HistoryInfo.MultiTargetInterceptionReport> finalReport = new ArrayList<>();
        Set<String> targetInstructionInfoIndices = Objects.requireNonNull(
                RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).boundHashOps(
                        Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY).entries()).keySet();

        for (String targetInstructionInfoId : targetInstructionInfoIndices) {
            String targetInstructionInfoHistoryId = Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY + "_" + targetInstructionInfoId;
            String targetType = "";

            // 目标最后消失时间
            Long maxDetectTargetTime = Long.MIN_VALUE;
            // 目标最近探测距离
            Float minInterceptionDistance = Float.MAX_VALUE;
            TargetInstructionsInfo targetInstructionsInfo = new TargetInstructionsInfo();

            Long targetInstructionInfoHistoryCnt = Objects.requireNonNull(RedisUtils.getService(
                    commonRedisConfig.getHttpDataBaseIdx()).boundListOps(targetInstructionInfoHistoryId).size());
            for (int i = 0; i < targetInstructionInfoHistoryCnt; ++i) {
                String tmpInstruction = template.boundListOps(targetInstructionInfoHistoryId).index(i);
                TargetInstructionsInfo instruction = JsonUtils.deserialize(tmpInstruction, TargetInstructionsInfo.class);
                maxDetectTargetTime = Math.max(maxDetectTargetTime, instruction.getTime());
                minInterceptionDistance = Math.min(minInterceptionDistance, instruction.getDistance());
                targetType = instruction.getTargetTypeId();
            }

            if (targetInstructionInfoHistoryCnt <= 0 ||
                    maxDetectTargetTime == Long.MAX_VALUE ||
                    minInterceptionDistance == Float.MAX_VALUE ||
                    minInterceptionDistance <= MIN_INTERCEPTION_DISTANCE
            ) {
                continue;
            }

            HistoryInfo.MultiTargetInterceptionReport curReport = new HistoryInfo.MultiTargetInterceptionReport();
            curReport.setTargetId(targetInstructionInfoId);
            curReport.setTargetType(targetType);
            curReport.setTime(maxDetectTargetTime);
            curReport.setInterceptionTime(maxDetectTargetTime);
            // 得所有目标的测试做完才知道拦截总目标个数
            curReport.setInterceptionAccount(0);
            finalReport.add(curReport);
        }
        for (HistoryInfo.MultiTargetInterceptionReport oneTargetReport : finalReport) {
            oneTargetReport.setInterceptionAccount(finalReport.size());
        }

        PipeHistory reactionPipeHistory = new PipeHistory();
        reactionPipeHistory.setId(UUID.randomUUID().toString());
        reactionPipeHistory.setCreateTime(new Timestamp(System.currentTimeMillis()));
        reactionPipeHistory.setType("15");
        reactionPipeHistory.setDisabled(false);
        reactionPipeHistory.setRes(JsonUtils.serialize(finalReport));
        pipeHistoryService.insert(reactionPipeHistory);
    }

    /**
     * @param allEquipmentStatus 所有的装备状态
     * @param indices            通道测试涉及的装备下标
     * @return bool 通道测试是否成功
     */
    public boolean pipeTestHelper(Map<String, EquipmentStatus> allEquipmentStatus, Integer[] indices) {
        boolean res = true;
        for (Integer idx : indices) {
            if (!allEquipmentStatus.containsKey(idx.toString())) {
                return false;
            }
            res = res && allEquipmentStatus.get(idx.toString()).getCheckStatus();
        }
        return res;
    }


    /**
     * 是否满足时间阈值
     *
     * @param timeVec                时间串
     * @param pipeTestCycleThreshold 通道测试周期阈值，单位s
     * @return 测试结果
     */
    public boolean meetTestCycleHelper(long[] timeVec, long pipeTestCycleThreshold) {
        return MathUtils.findMaxByCollections(timeVec) - MathUtils.findMinByCollections(timeVec) <
                pipeTestCycleThreshold * 1000;
    }

    /**
     * 是否执行算法并传递阈值
     *
     * @param Code
     * @return
     */
    public String pipeTestSetThreshold(String Code) {
        //1.找到taskId
        int status=1;
        PipeTest pipeTest = pipeTaskService.getByState(status);
        if (pipeTest == null) {
            return NONE;
        }
        //2.根据taskId找到通道测试集合
        List<PipeTest> pipeTestList = pipeTestService.getByTaskId(pipeTest.getId());
        //3.TestCode--》value判断
        for (PipeTest test : pipeTestList) {
            List<ComDictionaryItem> comDictionaryItems = comDictionaryItemService.getByCode(test.getCode());
            if (StringUtils.equals(comDictionaryItems.get(0).getText(), Code)) {
                //返回阈值
                return test.getThreshold();
            }
        }
        return NONE;
    }
}
