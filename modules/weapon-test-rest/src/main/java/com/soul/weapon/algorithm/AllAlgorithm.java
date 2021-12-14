package com.soul.weapon.algorithm;

import com.egova.entity.DictionaryItem;
import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.flagwind.commons.StringUtils;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.entity.*;
import com.soul.weapon.entity.historyInfo.*;
import com.soul.weapon.entity.enums.PipeWeaponIndices;
import com.soul.weapon.model.StateAnalysisTimeReport;
import com.soul.weapon.model.dds.*;
import com.soul.weapon.service.*;
import com.soul.weapon.utils.MathUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private final CommonConfig config;

    private final PipeTaskService pipeTaskService;

    private final PipeTestService pipeTestService;

    private final DictionaryItemService DictionaryItemService;

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

    //private final PipeHistoryService pipeHistoryService;

    private final ShipToAirMissileTestReportService shipToAirMissileTestReportService;
    private final AntiMissileShipGunTestReportService antiMissileShipGunTestReportService;
    private final TorpedoTestReportService torpedoTestReportService;
    private final ElectronicWeaponTestReportService electronicWeaponTestReportService;
    private final WaterWeaponTestReportService waterWeaponTestReportService;
    private final InfoProcessTestReportService infoProcessTestReportService;
    private final ThreatenReportService threatenReportService;
    private final InstructionAccuracyReportService instructionAccuracyReportService;
    private final ExecutionStatusReportService executionStatusReportService;
    private final RadarPathReportService radarPathReportService;
    private final InterceptDistanceReportService interceptDistanceReportService;
    private final FireControlReportService fireControlReportService;
    private final ReactionReportService reactionReportService;
    private final LauncherRotationReportService launcherRotationReportService;
    private final MultiTargetInterceptionReportService multiTargetInterceptionReportService;

    /**
     * 航空导弹-1
     */
    public void shipToAirMissile(String taskId, PipeTest pipeTest) {
        if (IsStart(taskId, pipeTest)) {
            return;
        }
        String threshold = pipeTest.getThreshold();
        if (StringUtils.isEmpty(threshold)) {
            PIPETEST_CYCLE_THRESHOLD = Long.parseLong(pipeTest.getThreshold());
        }

        String key = String.format("%s:%s", Constant.EQUIPMENT_STATUS_HTTP_KEY, getTime());
        if (!Boolean.TRUE.equals(RedisUtils.getService(config.getPumpDataBase()).getTemplate()
                .hasKey(key))) {
            log.error("从Redis中获取装备信息失败！-1");
            return;
        }
        Map<String, String> tmpEquipments = RedisUtils.getService(config.getPumpDataBase()).
                boundHashOps(key).entries();

        assert tmpEquipments != null;
        Map<String, EquipmentStatus> allEquipmentStatus = tmpEquipments.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), EquipmentStatus.class))
        );

        ShipToAirMissileTestReport tmpReport = new ShipToAirMissileTestReport();

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

        tmpReport.setTaskId(taskId);
        tmpReport.setId(UUID.randomUUID().toString());
        tmpReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
        tmpReport.setDisabled(false);
        shipToAirMissileTestReportService.insert(tmpReport);

    }


    /**
     * 反导舰炮算法-2
     */
    public void antiMissileShipGun(String taskId, PipeTest pipeTest) {

        if (IsStart(taskId, pipeTest)) {
            return;
        }
        String threshold = pipeTest.getThreshold();
        if (StringUtils.isEmpty(threshold)) {
            PIPETEST_CYCLE_THRESHOLD = Long.parseLong(pipeTest.getThreshold());
        }

        String key = String.format("%s:%s", Constant.EQUIPMENT_STATUS_HTTP_KEY, getTime());
        
        if (!Boolean.TRUE.equals(RedisUtils.getService(config.getPumpDataBase()).getTemplate()
                .hasKey(key))) {
            log.error("从Redis中获取装备信息失败！-2");
            return;
        }

        Map<String, String> tmpEquipments = RedisUtils.getService(config.getPumpDataBase()).
                boundHashOps(key).entries();
        assert tmpEquipments != null;
        Map<String, EquipmentStatus> allEquipmentStatus = tmpEquipments.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), EquipmentStatus.class))
        );

        AntiMissileShipGunTestReport tmpReport = new AntiMissileShipGunTestReport();
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

        tmpReport.setTaskId(taskId);
        tmpReport.setId(UUID.randomUUID().toString());
        tmpReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
        tmpReport.setDisabled(false);
        antiMissileShipGunTestReportService.insert(tmpReport);
    }

    /**
     * 鱼类防御武器测试-3
     */
    public void torpedoDefense(String taskId, PipeTest pipeTest) {

        if (IsStart(taskId, pipeTest)) {
            return;
        }
        String threshold = pipeTest.getThreshold();
        if (StringUtils.isEmpty(threshold)) {
            PIPETEST_CYCLE_THRESHOLD = Long.parseLong(pipeTest.getThreshold());
        }

        String key = String.format("%s:%s", Constant.EQUIPMENT_STATUS_HTTP_KEY, getTime());

        if (!Boolean.TRUE.equals(RedisUtils.getService(config.getPumpDataBase()).getTemplate()
                .hasKey(Constant.EQUIPMENT_STATUS_HTTP_KEY))) {
            log.error("从Redis中获取装备信息失败！-3");
            return;
        }

        Map<String, String> tmpEquipments = RedisUtils.getService(config.getPumpDataBase()).
                boundHashOps(Constant.EQUIPMENT_STATUS_HTTP_KEY).entries();
        assert tmpEquipments != null;
        Map<String, EquipmentStatus> allEquipmentStatus = tmpEquipments.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), EquipmentStatus.class))
        );

        TorpedoTestReport tmpReport = new TorpedoTestReport();
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

        tmpReport.setTaskId(taskId);
        tmpReport.setId(UUID.randomUUID().toString());
        tmpReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
        tmpReport.setDisabled(false);
        torpedoTestReportService.insert(tmpReport);
    }

    /**
     * 电子对抗武器测试-4
     */
    public void electronicCountermeasure(String taskId, PipeTest pipeTest) {

        if (IsStart(taskId, pipeTest)) {
            return;
        }
        String threshold = pipeTest.getThreshold();
        if (StringUtils.isEmpty(threshold)) {
            PIPETEST_CYCLE_THRESHOLD = Long.parseLong(pipeTest.getThreshold());
        }

        String key = String.format("%s:%s", Constant.EQUIPMENT_STATUS_HTTP_KEY, getTime());


        if (!Boolean.TRUE.equals(RedisUtils.getService(config.getPumpDataBase()).exists(Constant.EQUIPMENT_STATUS_HTTP_KEY))) {
            log.error("从Redis中获取装备信息失败！-4");
            return;
        }

        Map<String, String> tmpEquipments = RedisUtils.getService(config.getPumpDataBase()).
                boundHashOps(Constant.EQUIPMENT_STATUS_HTTP_KEY).entries();
        assert tmpEquipments != null;
        Map<String, EquipmentStatus> allEquipmentStatus = tmpEquipments.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), EquipmentStatus.class))
        );

        ElectronicWeaponTestReport tmpReport = new ElectronicWeaponTestReport();
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

        tmpReport.setTaskId(taskId);
        tmpReport.setId(UUID.randomUUID().toString());
        tmpReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
        tmpReport.setDisabled(false);
        electronicWeaponTestReportService.insert(tmpReport);
    }


    /**
     * 水声对抗武器测试 -5
     */
    public void underwaterAcousticCountermeasure(String taskId, PipeTest pipeTest) {


        if (IsStart(taskId, pipeTest)) {
            return;
        }
        String threshold = pipeTest.getThreshold();
        if (StringUtils.isEmpty(threshold)) {
            PIPETEST_CYCLE_THRESHOLD = Long.parseLong(pipeTest.getThreshold());
        }

        String key = String.format("%s:%s", Constant.EQUIPMENT_STATUS_HTTP_KEY, getTime());


        if (!Boolean.TRUE.equals(RedisUtils.getService(config.getPumpDataBase()).getTemplate()
                .hasKey(Constant.EQUIPMENT_STATUS_HTTP_KEY))) {
            log.error("从Redis中获取装备信息失败！-5");
            return;
        }

        Map<String, String> tmpEquipments = RedisUtils.getService(config.getPumpDataBase()).
                boundHashOps(Constant.EQUIPMENT_STATUS_HTTP_KEY).entries();
        assert tmpEquipments != null;
        Map<String, EquipmentStatus> allEquipmentStatus = tmpEquipments.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), EquipmentStatus.class))
        );

        WaterWeaponTestReport tmpReport = new WaterWeaponTestReport();
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

        tmpReport.setTaskId(taskId);
        tmpReport.setId(UUID.randomUUID().toString());
        tmpReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
        tmpReport.setDisabled(false);
        waterWeaponTestReportService.insert(tmpReport);
    }

    /**
     * 信息流程测试-6
     */
    public void infoProcessTest(String taskId, PipeTest pipeTest) {
        if (IsStart(taskId, pipeTest)) {
            return;
        }
        String threshold = pipeTest.getThreshold();
        if (StringUtils.isEmpty(threshold)) {
            PROGRESS_TIME_THRESHOLD = Long.parseLong(pipeTest.getThreshold());
        }
        String instructions_key = String.format("%s:%s", Constant.EQUIPMENT_STATUS_HTTP_KEY, getTime());
        String target_key = String.format("%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, getTime());
        String equipment_key = String.format("%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, getTime());


        StringRedisTemplate template = RedisUtils.getService(config.getPumpDataBase()).getTemplate();
        if (!template.hasKey(instructions_key) ||
                !template.hasKey(target_key)
                || !template.hasKey(equipment_key)) {
            log.error("从Redis中获取信息失败！");
            return;
        }
        Map<String, String> tmpFireControlInfos = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                target_key).entries();

        assert tmpFireControlInfos != null;
        Map<String, TargetFireControlInfo> allFireControlInfos = tmpFireControlInfos.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), TargetFireControlInfo.class))
        );
        for (String key2 : allFireControlInfos.keySet()) {
            TargetFireControlInfo targetFireControlInfo = allFireControlInfos.get(key2);

            String key_target = String.format("%s_%s:%s", Constant.TARGET_INFO_HTTP_KEY, targetFireControlInfo.getTargetId(), getTime());
            String key_equipment = String.format("%s_%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, targetFireControlInfo.getTargetId(), getTime());

            Long n = template.boundListOps(key_target).size();

            for (int i = 0; i < n; i++) {

                TargetInstructionsInfo targetInstructionsInfo = JsonUtils.deserialize(template.boundListOps(
                        key_target).index(i),
                        TargetInstructionsInfo.class);

                Long m = template.boundListOps(key_equipment).size();

                for (int j = 0; j < m; j++) {

                    EquipmentLaunchStatus equipmentLaunchStatus = JsonUtils.deserialize(template.boundListOps(
                            key_equipment).index(j),
                            EquipmentLaunchStatus.class);

                    if (StringUtils.equals(targetFireControlInfo.getTargetId(), targetInstructionsInfo.getTargetId()) &&
                            StringUtils.equals(targetFireControlInfo.getTargetId(), equipmentLaunchStatus.getTargetId())) {

                        InfoProcessTestReport infoProcessTestReport = new InfoProcessTestReport();

                        boolean b = equipmentLaunchStatus.getTime() > targetFireControlInfo.getTime() &&
                                targetFireControlInfo.getTime() > targetInstructionsInfo.getTime();
                        boolean d = equipmentLaunchStatus.getTime() - targetInstructionsInfo.getTime() < PROGRESS_TIME_THRESHOLD;

                        // 设置信息流程返回报文信息
                        infoProcessTestReport.setTargetId(equipmentLaunchStatus.getTargetId());
                        infoProcessTestReport.setTime(equipmentLaunchStatus.getTime());
                        infoProcessTestReport.setTargetType(equipmentLaunchStatus.getTargetTypeId());
                        if (b && d) {
                            infoProcessTestReport.setStatus(true);
                        } else {
                            infoProcessTestReport.setStatus(false);
                        }

                        infoProcessTestReport.setTaskId(taskId);
                        infoProcessTestReport.setId(UUID.randomUUID().toString());
                        infoProcessTestReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        infoProcessTestReport.setDisabled(false);
                        infoProcessTestReportService.insert(infoProcessTestReport);


                    }
                }

            }
        }
    }

    /**
     * 威胁判断算法-7
     */
    public void threatJudgment(String taskId, PipeTest pipeTest) {

        if (IsStart(taskId, pipeTest)) {
            return;
        }

        String instructions_key = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, getTime());
        String target_key = String.format("%s:%s", Constant.TARGET_INFO_HTTP_KEY, getTime());

        String threshold = pipeTest.getThreshold();
        if (StringUtils.isEmpty(threshold)) {
            DETECTOR_TIME_THRESHOLD = Long.parseLong(pipeTest.getThreshold());
        }

        StringRedisTemplate template = RedisUtils.getService(config.getPumpDataBase()).getTemplate();
        if (!template.hasKey(instructions_key) || !template.hasKey(target_key)) {
            log.error("从Redis中获取目标指示信息或目标真值信息失败！");
            return;
        }

        Map<String, String> tmpInstructionInfos = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                instructions_key).entries();
        assert tmpInstructionInfos != null;
        Map<String, TargetInstructionsInfo> allInstructionInfos = tmpInstructionInfos.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), TargetInstructionsInfo.class))
        );


        for (String key2 : allInstructionInfos.keySet()) {
            TargetInstructionsInfo targetInstructionsInfo = allInstructionInfos.get(key2);
            String key = String.format("%s_%s:%s", Constant.TARGET_INFO_HTTP_KEY, targetInstructionsInfo.getTargetId(), getTime());
            Long n = template.boundListOps(key).size();

            for (int i = 0; i < n; i++) {
                TargetInfo targetInfo = JsonUtils.deserialize(template.boundListOps(
                        key).index(i), TargetInfo.class);

                assert targetInfo != null;

                if (StringUtils.equals(targetInfo.getTargetId(), targetInstructionsInfo.getTargetId())) {

                    if (Math.abs(targetInfo.getTime() - targetInstructionsInfo.getTime()) < DETECTOR_TIME_THRESHOLD) {

                        ThreatenReport threatenReport = new ThreatenReport();
                        threatenReport.setTargetId(targetInfo.getTargetId());
                        threatenReport.setTargetType(targetInfo.getTargetTypeId());
                        threatenReport.setDistanceOffset(
                                Math.abs(targetInfo.getDistance() - targetInstructionsInfo.getDistance()));
                        threatenReport.setTime(targetInstructionsInfo.getTime());
                        threatenReport.setPitchOffset(
                                Math.abs(targetInfo.getPitchAngle() - targetInstructionsInfo.getPitchAngle()));
                        threatenReport.setSpeedOffset(
                                Math.abs(targetInfo.getSpeed() - targetInstructionsInfo.getSpeed()));

                        threatenReport.setTaskId(taskId);
                        threatenReport.setId(UUID.randomUUID().toString());
                        threatenReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        threatenReport.setDisabled(false);
                        threatenReportService.insert(threatenReport);
                    }

                }


            }
        }
    }

    /**
     * 指示处理精度测试-8
     */
    public void indicationAccuracyTest(String taskId, PipeTest pipeTest) {

        if (IsStart(taskId, pipeTest)) {
            return;
        }

        String instructions_key = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, getTime());
        String target_key = String.format("%s:%s", Constant.TARGET_INFO_HTTP_KEY, getTime());

        String threshold = pipeTest.getThreshold();
        if (StringUtils.isEmpty(threshold)) {
            DETECTOR_TIME_THRESHOLD = Long.parseLong(pipeTest.getThreshold());
        }

        StringRedisTemplate template = RedisUtils.getService(config.getPumpDataBase()).getTemplate();
        if (!template.hasKey(instructions_key) || !template.hasKey(target_key)) {
            log.error("从Redis中获取目标指示信息或目标真值信息失败！");
            return;
        }

        Map<String, String> tmpInstructionInfos = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                instructions_key).entries();
        assert tmpInstructionInfos != null;
        Map<String, TargetInstructionsInfo> allInstructionInfos = tmpInstructionInfos.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), TargetInstructionsInfo.class))
        );

        for (String key2 : allInstructionInfos.keySet()) {
            TargetInstructionsInfo targetInstructionsInfo = allInstructionInfos.get(key2);
            String key = String.format("%s_%s:%s", Constant.TARGET_INFO_HTTP_KEY, targetInstructionsInfo.getTargetId(), getTime());
            Long n = template.boundListOps(key).size();

            for (int i = 0; i < n; i++) {
                TargetInfo targetInfo = JsonUtils.deserialize(template.boundListOps(
                        key).index(i), TargetInfo.class);

                assert targetInfo != null;

                if (StringUtils.equals(targetInfo.getTargetId(), targetInstructionsInfo.getTargetId())) {


                    // 指示处理精度测试
                    if (Math.abs(targetInfo.getTime() - targetInstructionsInfo.getTime()) < INSTRUCTION_TIME_THRESHOLD) {

                        InstructionAccuracyReport instructionAccuracyReport = new InstructionAccuracyReport();
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

                        instructionAccuracyReport.setTaskId(taskId);
                        instructionAccuracyReport.setId(UUID.randomUUID().toString());
                        instructionAccuracyReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        instructionAccuracyReport.setDisabled(false);
                        instructionAccuracyReportService.insert(instructionAccuracyReport);
                    }

                }

            }
        }
    }

    /**
     * 执行情况测试-9
     */
    public void executionStatusTest(String taskId, PipeTest pipeTest) {
        if (IsStart(taskId, pipeTest)) {
            return;
        }

        String instructions_key = String.format("%s:%s", Constant.EQUIPMENT_STATUS_HTTP_KEY, getTime());
        String target_key = String.format("%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, getTime());
        String equipment_key = String.format("%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, getTime());


        StringRedisTemplate template = RedisUtils.getService(config.getPumpDataBase()).getTemplate();
        if (!template.hasKey(instructions_key) ||
                !template.hasKey(target_key)
                || !template.hasKey(equipment_key)) {
            log.error("从Redis中获取信息失败！");
            return;
        }
        Map<String, String> tmpFireControlInfos = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                target_key).entries();

        assert tmpFireControlInfos != null;
        Map<String, TargetFireControlInfo> allFireControlInfos = tmpFireControlInfos.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), TargetFireControlInfo.class))
        );

        for (String key2 : allFireControlInfos.keySet()) {
            TargetFireControlInfo targetFireControlInfo = allFireControlInfos.get(key2);

            String key_target = String.format("%s_%s:%s", Constant.TARGET_INFO_HTTP_KEY, targetFireControlInfo.getTargetId(), getTime());
            String key_equipment = String.format("%s_%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, targetFireControlInfo.getTargetId(), getTime());

            Long n = template.boundListOps(key_target).size();

            for (int i = 0; i < n; i++) {

                TargetInstructionsInfo targetInstructionsInfo = JsonUtils.deserialize(template.boundListOps(
                        key_target).index(i),
                        TargetInstructionsInfo.class);

                Long m = template.boundListOps(key_equipment).size();

                for (int j = 0; j < m; j++) {

                    EquipmentLaunchStatus equipmentLaunchStatus = JsonUtils.deserialize(template.boundListOps(
                            key_equipment).index(j),
                            EquipmentLaunchStatus.class);

                    if (StringUtils.equals(targetFireControlInfo.getTargetId(), targetInstructionsInfo.getTargetId()) &&
                            StringUtils.equals(targetFireControlInfo.getTargetId(), equipmentLaunchStatus.getTargetId())) {

                        ExecutionStatusReport executionStatusReport = new ExecutionStatusReport();

                        boolean b = equipmentLaunchStatus.getTime() > targetFireControlInfo.getTime() &&
                                targetFireControlInfo.getTime() > targetInstructionsInfo.getTime();
                        boolean c = equipmentLaunchStatus.getTime() - targetInstructionsInfo.getTime() < EXECUTION_TIME_THRESHOLD;

                        // 设置执行情况返回报文信息
                        executionStatusReport.setTargetId(equipmentLaunchStatus.getTargetId());
                        executionStatusReport.setTime(equipmentLaunchStatus.getTime());
                        executionStatusReport.setTargetType(equipmentLaunchStatus.getTargetTypeId());
                        if (b && c) {
                            executionStatusReport.setStatus(true);
                        } else {
                            executionStatusReport.setStatus(false);
                        }


                        executionStatusReport.setTaskId(taskId);
                        executionStatusReport.setId(UUID.randomUUID().toString());
                        executionStatusReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        executionStatusReport.setDisabled(false);
                        executionStatusReportService.insert(executionStatusReport);

                    }
                }

            }
        }
    }

    /**
     * 雷达航迹测试-10
     */
    public void radarTrackTest(String taskId, PipeTest pipeTest) {


        if (IsStart(taskId, pipeTest)) {
            return;
        }
        String instructions_key = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, getTime());
        String target_key = String.format("%s:%s", Constant.TARGET_INFO_HTTP_KEY, getTime());

        String threshold = pipeTest.getThreshold();
        if (StringUtils.isEmpty(threshold)) {
            RADARPATH_TIME_THRESHOLD = Long.parseLong(pipeTest.getThreshold());
        }

        StringRedisTemplate template = RedisUtils.getService(config.getPumpDataBase()).getTemplate();
        if (!template.hasKey(instructions_key) || !template.hasKey(target_key)) {
            log.error("从Redis中获取目标指示信息或目标真值信息失败！");
            return;
        }

        Map<String, String> tmpInstructionInfos = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                instructions_key).entries();
        assert tmpInstructionInfos != null;
        Map<String, TargetInstructionsInfo> allInstructionInfos = tmpInstructionInfos.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), TargetInstructionsInfo.class))
        );

        for (String key2 : allInstructionInfos.keySet()) {
            TargetInstructionsInfo targetInstructionsInfo = allInstructionInfos.get(key2);
            String key = String.format("%s_%s:%s", Constant.TARGET_INFO_HTTP_KEY, targetInstructionsInfo.getTargetId(), getTime());
            Long n = template.boundListOps(key).size();

            for (int i = 0; i < n; i++) {
                TargetInfo targetInfo = JsonUtils.deserialize(template.boundListOps(
                        key).index(i), TargetInfo.class);

                assert targetInfo != null;

                if (StringUtils.equals(targetInfo.getTargetId(), targetInstructionsInfo.getTargetId())) {

                    // 雷达航迹测试
                    if (targetInfo.getTime() - targetInstructionsInfo.getTime() > 0
                            && targetInfo.getTime() - targetInstructionsInfo.getTime() < RADARPATH_TIME_THRESHOLD
                            && targetInfo.getTargetTypeId().equals(AIRTYPE)
                            && targetInstructionsInfo.getTargetTypeId().equals(AIRTYPE)) {

                        RadarPathReport radarPathReport = new RadarPathReport();
                        radarPathReport.setTime(targetInstructionsInfo.getTime());
                        radarPathReport.setTargetId(targetInstructionsInfo.getTargetId());
                        radarPathReport.setTargetType(targetInstructionsInfo.getTargetTypeId());
                        radarPathReport.setSensorId(targetInstructionsInfo.getEquipmentId());
                        radarPathReport.setSensorType(targetInstructionsInfo.getEquipmentTypeId());
                        radarPathReport.setActualTargetPitch(targetInfo.getPitchAngle());
                        radarPathReport.setActualTargetDistance(targetInfo.getDistance());
                        radarPathReport.setShowedTargetDistance(targetInstructionsInfo.getDistance());
                        radarPathReport.setShowedTargetPitch(targetInstructionsInfo.getPitchAngle());

                        radarPathReport.setTaskId(taskId);
                        radarPathReport.setId(UUID.randomUUID().toString());
                        radarPathReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        radarPathReport.setDisabled(false);
                        radarPathReportService.insert(radarPathReport);

                    }

                }


            }
        }
    }

    /**
     * 拦截距离测试-11
     */
    public void interceptDistanceTest(String taskId, PipeTest pipeTest) {
        if (IsStart(taskId, pipeTest)) {
            return;
        }
        String threshold = pipeTest.getThreshold();
        if (StringUtils.isEmpty(threshold)) {
            INTERCEPTION_DISTANCE = Long.parseLong(pipeTest.getThreshold());
        }

        StringRedisTemplate template = RedisUtils.getService(config.getPumpDataBase()).getTemplate();
        if (!template.hasKey(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY)) {
            log.error("从Redis中获取目标指示信息失败！");
            return;
        }

        String key = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, getTime());

        Map<String, String> tempInstructions = RedisUtils.getService(config.getPumpDataBase()).
                boundHashOps(key).entries();

        assert tempInstructions != null;
        Map<String, TargetInstructionsInfo> allInstructions = tempInstructions.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), TargetInstructionsInfo.class)
        ));

        for (TargetInstructionsInfo targetInstructionsInfo : allInstructions.values()) {
            String key_target = String.format("%s_%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, targetInstructionsInfo.getTargetId(), getTime());


            Long n = template.boundListOps(key_target).size();

            Long MaxTime = 0L;
            Float MinDistance = Float.MAX_VALUE;

            for (int i = 0; i < n; i++) {

                String tmpInstruction = template.boundListOps(key_target).index(i);

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

            InterceptDistanceReport interceptDistanceReport = new InterceptDistanceReport();
            interceptDistanceReport.setInterceptDistance(MinDistance);
            interceptDistanceReport.setTime(MaxTime);
            interceptDistanceReport.setTargetType(targetInstructionsInfo.getTargetTypeId());
            interceptDistanceReport.setTargetId(targetInstructionsInfo.getTargetId());

            interceptDistanceReport.setTaskId(taskId);
            interceptDistanceReport.setId(UUID.randomUUID().toString());
            interceptDistanceReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
            interceptDistanceReport.setDisabled(false);
            interceptDistanceReportService.insert(interceptDistanceReport);

        }
    }

    /**
     * 火控解算精度测试-12
     */
    public void fireControlTest(String taskId, PipeTest pipeTest) {

        if (IsStart(taskId, pipeTest)) {
            return;
        }
        String threshold = pipeTest.getThreshold();
        if (StringUtils.isEmpty(threshold)) {
            FIRECONTROL_TIME_THRESHOLD = Long.parseLong(pipeTest.getThreshold());
        }

        StringRedisTemplate template = RedisUtils.getService(config.getPumpDataBase()).getTemplate();
        if (!template.hasKey(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY) ||
                !template.hasKey(Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY)) {
            log.error("从Redis中获取目标指示信息或目标火控信息失败！");
            return;
        }
        String target_key = String.format("%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, getTime());

        Map<String, String> tmpFireControls = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                target_key).entries();
        assert tmpFireControls != null;
        Map<String, TargetFireControlInfo> allFireControlInfos = tmpFireControls.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), TargetFireControlInfo.class))
        );

        for (TargetFireControlInfo targetFireControlInfo : allFireControlInfos.values()) {

            String key_target = String.format("%s_%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, targetFireControlInfo.getTargetId(), getTime());

            Long n = template.boundListOps(
                    key_target).size();


            for (int i = 0; i < n; i++) {
                TargetInstructionsInfo targetInstructionsInfo = JsonUtils.deserialize(template.boundListOps(
                        key_target).index(i),
                        TargetInstructionsInfo.class);

                assert targetInstructionsInfo != null;

                if (targetInstructionsInfo.getTargetId().equals(targetFireControlInfo.getTargetId())) {

                    if (Math.abs(targetFireControlInfo.getTime() - targetInstructionsInfo.getTime()) < FIRECONTROL_TIME_THRESHOLD) {

                        FireControlReport fireControlReport = new FireControlReport();
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

                        fireControlReport.setTaskId(taskId);
                        fireControlReport.setId(UUID.randomUUID().toString());
                        fireControlReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        fireControlReport.setDisabled(false);
                        fireControlReportService.insert(fireControlReport);

                        break;
                    }
                }
            }
        }
    }


    /**
     * 反应时间测试-13
     */
    public void reactionTimeTest(String taskId, PipeTest pipeTest) {
        if (IsStart(taskId, pipeTest)) {
            return;
        }

        String equipment_key = String.format("%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, getTime());
        String target_key = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, getTime());


        StringRedisTemplate template = RedisUtils.getService(config.getPumpDataBase()).getTemplate();
        if (!template.hasKey(equipment_key) ||
                !template.hasKey(target_key)) {
            log.error("从Redis中获取武器发射或目标指示信息失败！");
            return;
        }

        Map<String, String> tmpEqupimentLaunch = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                equipment_key).entries();
        assert tmpEqupimentLaunch != null;

        Map<String, EquipmentLaunchStatus> allEquipmentStatus = tmpEqupimentLaunch.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), EquipmentLaunchStatus.class)
        ));

        for (EquipmentLaunchStatus equipmentLaunchStatus : allEquipmentStatus.values()) {

            String key_target = String.format("%s_%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, equipmentLaunchStatus.getTargetId(), getTime());

            Long n = template.boundListOps(
                    key_target).size();

            Long reactionTime = Long.MAX_VALUE;

            TargetInstructionsInfo targetInstructionsInfo = new TargetInstructionsInfo();

            for (int i = 0; i < n; i++) {
                String tmpLaunch = template.boundListOps(
                        key_target).index(i);


                TargetInstructionsInfo tmpInstruction = JsonUtils.deserialize(tmpLaunch, TargetInstructionsInfo.class);
                if (reactionTime > equipmentLaunchStatus.getTime() - tmpInstruction.getTime()) {
                    reactionTime = equipmentLaunchStatus.getTime() - tmpInstruction.getTime();
                    targetInstructionsInfo = tmpInstruction;
                }

            }

            if (reactionTime == Long.MAX_VALUE) {
                continue;
            }

            ReactionReport reactionReport = new ReactionReport();
            reactionReport.setTime(equipmentLaunchStatus.getTime());
            reactionReport.setReactionTime(reactionTime);
            reactionReport.setTargetId(targetInstructionsInfo.getTargetId());
            reactionReport.setTargetType(targetInstructionsInfo.getTargetTypeId());
            reactionReport.setSensorId(targetInstructionsInfo.getEquipmentId());
            reactionReport.setSensorType(targetInstructionsInfo.getEquipmentTypeId());
            reactionReport.setWeaponId(equipmentLaunchStatus.getEquipmentId());
            reactionReport.setWeaponType(equipmentLaunchStatus.getEquipmentTypeId());

            reactionReport.setTaskId(taskId);
            reactionReport.setId(UUID.randomUUID().toString());
            reactionReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
            reactionReport.setDisabled(false);
            reactionReportService.insert(reactionReport);
        }
    }


    /**
     * 发射架调转精度测试-14
     */
    public void launcherRotationTest(String taskId, PipeTest pipeTest) {

        if (IsStart(taskId, pipeTest)) {
            return;
        }
        String threshold = pipeTest.getThreshold();
        if (StringUtils.isEmpty(threshold)) {
            PROGRESS_TIME_THRESHOLD = Long.parseLong(pipeTest.getThreshold());
        }
        String equipment_key = String.format("%s:%s", Constant.EQUIPMENT_STATUS_HTTP_KEY, getTime());
        String target_key = String.format("%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, getTime());


        StringRedisTemplate template = RedisUtils.getService(config.getPumpDataBase()).getTemplate();
        if (!template.hasKey(equipment_key) ||
                !template.hasKey(target_key)) {
            log.error("从Redis中获取武器发射或目标火控信息失败！");
            return;
        }

        Map<String, String> tmpFireControls = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                target_key).entries();
        assert tmpFireControls != null;
        Map<String, TargetFireControlInfo> allFireControlInfos = tmpFireControls.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), TargetFireControlInfo.class))
        );

        for (TargetFireControlInfo targetFireControlInfo : allFireControlInfos.values()) {

            String key_equipment = String.format("%s_%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, targetFireControlInfo.getTargetId(), getTime());


            Long n = template.boundListOps(
                    key_equipment).size();


            for (int i = 0; i < n; i++) {

                EquipmentLaunchStatus equipmentLaunchStatus = JsonUtils.deserialize(template.boundListOps(
                        key_equipment).index(i),
                        EquipmentLaunchStatus.class);

                assert equipmentLaunchStatus != null;

                if (equipmentLaunchStatus.getTargetId().equals(targetFireControlInfo.getTargetId())) {

                    if (equipmentLaunchStatus.getTime() - targetFireControlInfo.getTime() < LAUNCHER_ROTATION_TIME_THRESHOLD
                            && 0 < equipmentLaunchStatus.getTime() - targetFireControlInfo.getTime()) {

                        LauncherRotationReport launcherRotationReport = new LauncherRotationReport();
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

                        launcherRotationReport.setTargetId(taskId);
                        launcherRotationReport.setId(UUID.randomUUID().toString());
                        launcherRotationReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        launcherRotationReport.setDisabled(false);
                        launcherRotationReportService.insert(launcherRotationReport);

                        break;
                    }
                }
            }
        }

    }


    /**
     * 多目标拦截能力测试-15
     */
    public void multiTargetInterceptionTest(String taskId, PipeTest pipeTest) {

        if (IsStart(taskId, pipeTest)) {
            return;
        }
        String threshold = pipeTest.getThreshold();
        if (StringUtils.isEmpty(threshold)) {
            MIN_INTERCEPTION_DISTANCE = Long.parseLong(pipeTest.getThreshold());
        }
        String target_key = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, getTime());


        StringRedisTemplate template = RedisUtils.getService(config.getPumpDataBase()).getTemplate();
        if (Boolean.FALSE.equals(template.hasKey(target_key))) {
            log.error("从Redis中获取目标指示信息失败！");
            return;
        }

        Map<String, String> tmpEquipmentLaunch = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                target_key).entries();
        assert tmpEquipmentLaunch != null;

        Map<String, EquipmentLaunchStatus> allEquipmentStatus = tmpEquipmentLaunch.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), EquipmentLaunchStatus.class)
        ));


        List<MultiTargetInterceptionReport> finalReport = new ArrayList<>();
        Set<String> targetInstructionInfoIndices = Objects.requireNonNull(
                RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                        target_key).entries()).keySet();

        for (String targetInstructionInfoId : targetInstructionInfoIndices) {

            String targetInstructionInfoHistoryId = String.format("%s_%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, targetInstructionInfoId, getTime());
            String targetType = "";

            // 目标最后消失时间
            Long maxDetectTargetTime = Long.MIN_VALUE;
            // 目标最近探测距离
            Float minInterceptionDistance = Float.MAX_VALUE;
            TargetInstructionsInfo targetInstructionsInfo = new TargetInstructionsInfo();

            Long targetInstructionInfoHistoryCnt = Objects.requireNonNull(RedisUtils.getService(
                    config.getPumpDataBase()).boundListOps(targetInstructionInfoHistoryId).size());
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

            MultiTargetInterceptionReport curReport = new MultiTargetInterceptionReport();
            curReport.setTargetId(targetInstructionInfoId);
            curReport.setTargetType(targetType);
            curReport.setTime(maxDetectTargetTime);
            curReport.setInterceptionTime(maxDetectTargetTime);
            // 得所有目标的测试做完才知道拦截总目标个数
            curReport.setInterceptionAccount(0);
            finalReport.add(curReport);
        }
        for (MultiTargetInterceptionReport oneTargetReport : finalReport) {
            oneTargetReport.setTaskId(taskId);
            oneTargetReport.setId(UUID.randomUUID().toString());
            oneTargetReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
            oneTargetReport.setInterceptionAccount(finalReport.size());
            oneTargetReport.setDisabled(false);
        }
        multiTargetInterceptionReportService.insertList(finalReport);
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
     * 获取时间
     *
     * @return
     */
    private String getTime() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(System.currentTimeMillis());
    }

    /**
     * 是否开始执行
     * @param taskId
     * @param pipeTest
     * @return
     */
    public Boolean IsStart(String taskId, PipeTest pipeTest) {
        return StringUtils.isEmpty(taskId) || pipeTest == null;
    }


    /**
     * 获得水下防御时间节点信息
     * @return 水下防御事件的时间节点列表
     */
    public List<StateAnalysisTimeReport> getUnderWaterDefendInfo(){

        List<StateAnalysisTimeReport> stateAnalysisTimeReportList = new ArrayList<>();

        String target_key = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, getTime());

        StringRedisTemplate template = RedisUtils.getService(config.getPumpDataBase()).getTemplate();
        if (Boolean.FALSE.equals(template.hasKey(target_key))) {
            log.error("从Redis中获取目标指示信息失败！");
            return null;
        }
        String fireControlKey = String.format("%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, getTime());
        if (Boolean.FALSE.equals(template.hasKey(fireControlKey))) {
            log.error("从Redis中获取目标火控信息失败！");
            return null;
        }
        String fireKey =String.format("%s:%s",Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY,getTime());
        if(Boolean.FALSE.equals(template.hasKey(fireKey))){
            log.error("从Redis中获取发射架调转信息失败！");
            return null;
        }
        String launcherKey = String.format("%s:%s", Constant.LAUNCHER_ROTATION_INFO_HTTP_KEY, getTime());
        if (Boolean.FALSE.equals(template.hasKey(launcherKey))) {
            log.error("从Redis中获取发射架调转信息失败！");
            return null;
        }

        Map<String,String> allInstructionsInfo = RedisUtils.getService(config.getPumpDataBase())
                .boundHashOps(target_key).entries();

        if(allInstructionsInfo==null) return null;

        Map<String,TargetInstructionsInfo> targetInstructionsInfoMap = allInstructionsInfo.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        pair->JsonUtils.deserialize(pair.getValue(),TargetInstructionsInfo.class)
                ));

        for(TargetInstructionsInfo targetInstructionsInfo:targetInstructionsInfoMap.values()){

            if(!targetInstructionsInfo.getTargetTypeId().equals("水下目标")) continue;
            StateAnalysisTimeReport stateAnalysisTimeReport = new StateAnalysisTimeReport();
            String targetId = targetInstructionsInfo.getTargetId();

            stateAnalysisTimeReport.setId(targetId);
            stateAnalysisTimeReport.setInstructionTime(targetInstructionsInfo.getTime());

            Map<String,String> allFireControlInfo = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                    fireControlKey).entries();
            if(allFireControlInfo==null) continue;

            String temp = allFireControlInfo.get(targetId);
            if(temp==null) continue;
            TargetFireControlInfo targetFireControlInfo = JsonUtils.deserialize(
                    temp,
                    TargetFireControlInfo.class);
            if(targetFireControlInfo==null) continue;
            stateAnalysisTimeReport.setFireControlTime(targetFireControlInfo.getTime());

            launcherKey = String.format("%s:%s", Constant.LAUNCHER_ROTATION_INFO_HTTP_KEY + Constant.TARGET_ID, getTime());
            Map<String,String> allLauncherRotationInfo = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                    launcherKey).entries();
            if(allLauncherRotationInfo==null) continue;

            temp = allLauncherRotationInfo.get(targetId);
            if(temp==null) continue;

            LauncherRotationInfo launcherRotationInfo = JsonUtils.deserialize(
                    temp,
                    LauncherRotationInfo.class);
            if(launcherRotationInfo==null) continue;

            stateAnalysisTimeReport.setLauncherRotationTime(launcherRotationInfo.getTime());

            fireKey =String.format("%s:%s",Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY + Constant.TARGET_ID,getTime());
            Map<String,String> allEquipmentLaunchStatus = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                    fireKey).entries();
            if(allEquipmentLaunchStatus==null) continue;

            temp = allEquipmentLaunchStatus.get(targetId);
            if(temp==null) continue;

            EquipmentLaunchStatus equipmentLaunchStatus = JsonUtils.deserialize(
                    temp,
                    EquipmentLaunchStatus.class);
            if(equipmentLaunchStatus==null) continue;

            stateAnalysisTimeReport.setFireTime(equipmentLaunchStatus.getTime());

            stateAnalysisTimeReportList.add(stateAnalysisTimeReport);
        }
        return stateAnalysisTimeReportList;
    }

    /**
     * 获得对空防御时间节点信息
     * @return 对空防御事件的时间节点列表
     */
    public List<StateAnalysisTimeReport> getAirDefendInfo(){

        List<StateAnalysisTimeReport> stateAnalysisTimeReportList = new ArrayList<>();

        String target_key = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, getTime());

        StringRedisTemplate template = RedisUtils.getService(config.getPumpDataBase()).getTemplate();
        if (Boolean.FALSE.equals(template.hasKey(target_key))) {
            log.error("从Redis中获取目标指示信息失败！");
            return null;
        }
        String fireControlKey = String.format("%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, getTime());
        if (Boolean.FALSE.equals(template.hasKey(fireControlKey))) {
            log.error("从Redis中获取目标火控信息失败！");
            return null;
        }
        String fireKey =String.format("%s:%s",Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY,getTime());
        if(Boolean.FALSE.equals(template.hasKey(fireKey))){
            log.error("从Redis中获取武器发射信息失败！");
            return null;
        }
        String launcherKey = String.format("%s:%s", Constant.LAUNCHER_ROTATION_INFO_HTTP_KEY, getTime());
        if (Boolean.FALSE.equals(template.hasKey(launcherKey))) {
            log.error("从Redis中获取发射架调转信息失败！");
            return null;
        }

        Map<String,String> allInstructionsInfo = RedisUtils.getService(config.getPumpDataBase())
                .boundHashOps(target_key).entries();

        if(allInstructionsInfo==null) return null;

        Map<String,TargetInstructionsInfo> targetInstructionsInfoMap = allInstructionsInfo.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        pair->JsonUtils.deserialize(pair.getValue(),TargetInstructionsInfo.class)
                ));

        for(TargetInstructionsInfo targetInstructionsInfo:targetInstructionsInfoMap.values()){

            if(!targetInstructionsInfo.getTargetTypeId().equals("对空目标")) continue;
            StateAnalysisTimeReport stateAnalysisTimeReport = new StateAnalysisTimeReport();
            String targetId = targetInstructionsInfo.getTargetId();

            stateAnalysisTimeReport.setId(targetId);
            stateAnalysisTimeReport.setInstructionTime(targetInstructionsInfo.getTime());

            Map<String,String> allFireControlInfo = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                    fireControlKey).entries();
            if(allFireControlInfo==null) continue;

            String temp = allFireControlInfo.get(targetId);
            if(temp==null) continue;
            TargetFireControlInfo targetFireControlInfo = JsonUtils.deserialize(
                    temp,
                    TargetFireControlInfo.class);
            if(targetFireControlInfo==null) continue;
            stateAnalysisTimeReport.setFireControlTime(targetFireControlInfo.getTime());

            launcherKey = String.format("%s:%s", Constant.LAUNCHER_ROTATION_INFO_HTTP_KEY + Constant.TARGET_ID, getTime());
            Map<String,String> allLauncherRotationInfo = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                    launcherKey).entries();
            if(allLauncherRotationInfo==null) continue;

            temp = allLauncherRotationInfo.get(targetId);
            if(temp==null) continue;

            LauncherRotationInfo launcherRotationInfo = JsonUtils.deserialize(
                    temp,
                    LauncherRotationInfo.class);
            if(launcherRotationInfo==null) continue;

            stateAnalysisTimeReport.setLauncherRotationTime(launcherRotationInfo.getTime());

            fireKey =String.format("%s:%s",Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY + Constant.TARGET_ID,getTime());
            Map<String,String> allEquipmentLaunchStatus = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                    fireKey).entries();
            if(allEquipmentLaunchStatus==null) continue;

            temp = allEquipmentLaunchStatus.get(targetId);
            if(temp==null) continue;

            EquipmentLaunchStatus equipmentLaunchStatus = JsonUtils.deserialize(
                    temp,
                    EquipmentLaunchStatus.class);
            if(equipmentLaunchStatus==null) continue;

            stateAnalysisTimeReport.setFireTime(equipmentLaunchStatus.getTime());

            stateAnalysisTimeReportList.add(stateAnalysisTimeReport);
        }
        return stateAnalysisTimeReportList;
    }

}
