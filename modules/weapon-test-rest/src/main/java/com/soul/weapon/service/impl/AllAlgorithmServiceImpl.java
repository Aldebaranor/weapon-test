package com.soul.weapon.service.impl;

import com.egova.json.utils.JsonUtils;
import com.egova.model.PropertyItem;
import com.egova.redis.RedisService;
import com.egova.redis.RedisUtils;
import com.flagwind.commons.StringUtils;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.config.WeaponTestConstant;
import com.soul.weapon.entity.*;
import com.soul.weapon.entity.historyInfo.*;
import com.soul.weapon.entity.enums.PipeWeaponIndices;
import com.soul.weapon.model.StateAnalysisTimeReport;
import com.soul.weapon.model.dds.*;
import com.soul.weapon.service.*;
import com.soul.weapon.utils.MathUtils;
import com.soul.weapon.utils.DateParserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
public class AllAlgorithmServiceImpl implements AllAlgorithmService {
    private final CommonConfig config;

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
     * 获取阈值，如果 pipeTest.getThreshold()为空则使用默认的阈值
     *
     * @param pipeTest
     * @param key
     * @return
     */
    private Double getThreshold(PipeTest pipeTest, String key) {
        if (pipeTest == null) {
            return null;
        }
        PropertyItem<Double> item = new PropertyItem<Double>();
        String threshold = pipeTest.getThreshold();
        if (StringUtils.isEmpty(threshold)) {
            item = WeaponTestConstant.WEAPON_THRESHOLD.get(pipeTest.getType().getValue())
                    .stream().filter((q -> StringUtils.equals(q.getName(), key))).findFirst().orElse(null);
        } else {
            List<PropertyItem<Double>> list = null;
            try {
                list = JsonUtils.deserializeList(threshold, PropertyItem.class);
            } catch (Exception e) {
                return null;
            }
            if (CollectionUtils.isEmpty(list)) {
                return null;
            }
            item = list.stream().filter((q -> StringUtils.equals(q.getName(), key))).findFirst().orElse(null);
        }
        if (item == null) {
            return null;
        } else {
            String s = String.valueOf(item.getValue());
            return Double.valueOf(s);
        }
    }

    /**
     * 航空导弹-1
     */
    @Override
    public void shipToAirMissile(String taskId, PipeTest pipeTest) {
        if (beStart(taskId, pipeTest)) {
            return;
        }
        String key = String.format("%s:%s", Constant.EQUIPMENT_STATUS_HTTP_KEY, DateParserUtils.getTime());
        Map<String, EquipmentStatus> allEquipmentStatus = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(key, EquipmentStatus.class);
        if (allEquipmentStatus == null) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.AirMissileRadar.getValue())) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.AirMissileFireControl.getValue())) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.AirMissileLauncher.getValue())) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.AirMissileShortRange.getValue())) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.AirMissileMediumRange.getValue())) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.AirMissileLongRange.getValue())) {
            return;
        }
        //创建测试报告
        ShipToAirMissileTestReport tmpReport = new ShipToAirMissileTestReport();

        //添加装备标识和自检状态
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


        //获取信息采集时间
        long[] timeVec = {
                allEquipmentStatus.get(PipeWeaponIndices.AirMissileRadar.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.AirMissileFireControl.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.AirMissileLauncher.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.AirMissileShortRange.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.AirMissileMediumRange.getValue()).getTime(),
                allEquipmentStatus.get(PipeWeaponIndices.AirMissileLongRange.getValue()).getTime()
        };

        //获取测试项的阈值
        Double threshold = getThreshold(pipeTest, "pipeTest_cycle_threshold");


        //对测试项进行计算并判断是否合格
        boolean pipeStatus = tmpReport.getRadarSelfCheck() &&
                tmpReport.getFireControlSelfCheck() &&
                tmpReport.getLauncherSelfCheck() &&
                tmpReport.getMissileSelfShortCheck() &&
                tmpReport.getMissileSelfMediumCheck() &&
                tmpReport.getMissileSelfLongCheck() &&
                meetTestCycleHelper(timeVec, threshold);

        //设置测试项的状态
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
    @Override
    public void antiMissileShipGun(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }

        String key = String.format("%s:%s", Constant.EQUIPMENT_STATUS_HTTP_KEY, DateParserUtils.getTime());

        Map<String, EquipmentStatus> allEquipmentStatus = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(key, EquipmentStatus.class);
        if (allEquipmentStatus == null) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.AntiMissileShipGun.getValue())) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.AntiMissileShipGunRadar.getValue())) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.AntiMissileShipGunControl.getValue())) {
            return;
        }
        AntiMissileShipGunTestReport tmpReport = new AntiMissileShipGunTestReport();

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
        Double threshold = getThreshold(pipeTest, "pipeTest_cycle_threshold");
        boolean pipeStatus = tmpReport.getRadarSelfCheck() &&
                tmpReport.getFireControlSelfCheck() &&
                tmpReport.getShipGunSelfCheck() &&
                meetTestCycleHelper(timeVec, threshold);
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
    @Override
    public void torpedoDefense(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }

        String key = String.format("%s:%s", Constant.EQUIPMENT_STATUS_HTTP_KEY, DateParserUtils.getTime());

        Map<String, EquipmentStatus> allEquipmentStatus = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(key, EquipmentStatus.class);
        if (allEquipmentStatus == null) {
            return;
        }

        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.Sonar.getValue())) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.TorpedoFireControl.getValue())) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.TorpedoLauncher.getValue())) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.Torpedo.getValue())) {
            return;
        }
        TorpedoTestReport tmpReport = new TorpedoTestReport();
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
        Double threshold = getThreshold(pipeTest, "pipeTest_cycle_threshold");
        boolean pipeStatus = tmpReport.getSonarSelfCheck() &&
                tmpReport.getFireControlSelfCheck() &&
                tmpReport.getLauncherSelfCheck() &&
                tmpReport.getTorpedoSelfCheck() &&
                meetTestCycleHelper(timeVec, threshold);
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
    @Override
    public void electronicCountermeasure(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }
        String key = String.format("%s:%s", Constant.EQUIPMENT_STATUS_HTTP_KEY, DateParserUtils.getTime());

        Map<String, EquipmentStatus> allEquipmentStatus = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(key, EquipmentStatus.class);
        if (allEquipmentStatus == null) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.ElectronicDetection.getValue())) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.ElectronicCountermeasure.getValue())) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.MultiUsageLaunch.getValue())) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.OutBoardElectronicCountermeasure.getValue())) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.InBoardElectronicCountermeasure.getValue())) {
            return;
        }
        ElectronicWeaponTestReport tmpReport = new ElectronicWeaponTestReport();
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
        Double threshold = getThreshold(pipeTest, "pipeTest_cycle_threshold");

        boolean pipeStatus = tmpReport.getElectronicDetectorSelfCheck() &&
                tmpReport.getFireControlSelfCheck() &&
                tmpReport.getLauncherSelfCheck() &&
                tmpReport.getOuterElectronicWeaponSelfCheck() &&
                tmpReport.getInnerElectronicWeaponSelfCheck() &&
                meetTestCycleHelper(timeVec, threshold);
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
    @Override
    public void underwaterAcousticCountermeasure(String taskId, PipeTest pipeTest) {


        if (beStart(taskId, pipeTest)) {
            return;
        }

        String key = String.format("%s:%s", Constant.EQUIPMENT_STATUS_HTTP_KEY, DateParserUtils.getTime());

        Map<String, EquipmentStatus> allEquipmentStatus = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(key, EquipmentStatus.class);
        if (allEquipmentStatus == null) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.UnderwaterAcousticCountermeasureControl.getValue())) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.UnderwaterAcousticCountermeasure.getValue())) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.MultiUsageLaunch.getValue())) {
            return;
        }
        if (!allEquipmentStatus.containsKey(PipeWeaponIndices.Sonar.getValue())) {
            return;
        }
        WaterWeaponTestReport tmpReport = new WaterWeaponTestReport();
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
        Double threshold = getThreshold(pipeTest, "pipeTest_cycle_threshold");
        boolean pipeStatus = tmpReport.getSonarSelfCheck() &&
                tmpReport.getFireControlSelfCheck() &&
                tmpReport.getLauncherSelfCheck() &&
                tmpReport.getWaterWeaponSelfCheck() &&
                meetTestCycleHelper(timeVec, threshold);
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
    @Override
    public void infoProcessTest(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }

        //拼接redis中当天信息流程测试相关的key
        String equipmentLaunchKey = String.format("%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, DateParserUtils.getTime());
        String targetInstructionsKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());
        String targetFireControlKey = String.format("%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, DateParserUtils.getTime());

        RedisService redisService = RedisUtils.getService(config.getPumpDataBase());

        if (!redisService.exists(equipmentLaunchKey) || !redisService.exists(targetInstructionsKey) || !redisService.exists(targetFireControlKey)) {
            log.error("dds报文信息不满足信息流程测试！");
            return;
        }

        //获取redis中当天信息流程测试所需dds报文
        Map<String, Map> allEquipmentLaunchs = redisService.extrasForHash().hgetall(equipmentLaunchKey, Map.class);
        Map<String, Map> allTargetInstructions = redisService.extrasForHash().hgetall(targetInstructionsKey, Map.class);
        Map<String, Map> allTargetFireControlInfos = redisService.extrasForHash().hgetall(targetFireControlKey, Map.class);


        //获取信息流程测试的阈值
        Double threshold = getThreshold(pipeTest, "progress_time_threshold");

        //判断信息流程信息第一个条件
        for (String targetId : allEquipmentLaunchs.keySet()) {
            if (allTargetInstructions.containsKey(targetId) && allTargetFireControlInfos.containsKey(targetId)) {
                //通过第一条件
                for (Object equipmentLaunchsList : allEquipmentLaunchs.get(targetId).values()) {
                    //同一目标，同一装备，形成的不同的信息流程报文
                    List<EquipmentLaunchStatus> list = JsonUtils.deserializeList(JsonUtils.serialize(equipmentLaunchsList), EquipmentLaunchStatus.class);
                    for (EquipmentLaunchStatus equipmentLaunchStatus : list) {
                        for (Object targetInstructionsList : allTargetInstructions.get(targetId).values()) {
                            List<TargetInstructionsInfo> list1 = JsonUtils.deserializeList(JsonUtils.serialize(targetInstructionsList), TargetInstructionsInfo.class);
                            for (TargetInstructionsInfo targetInstructionsInfo : list1) {
                                for (Object targetFireControlInfosList : allTargetFireControlInfos.get(targetId).values()) {
                                    List<TargetFireControlInfo> list2 = JsonUtils.deserializeList(JsonUtils.serialize(targetFireControlInfosList), TargetFireControlInfo.class);
                                    for (TargetFireControlInfo targetFireControlInfo : list2) {
                                        //创建测试结果报文
                                        InfoProcessTestReport infoProcessTestReport = new InfoProcessTestReport();
                                        //设置信息流程返回报文信息
                                        infoProcessTestReport.setTaskId(taskId);
                                        infoProcessTestReport.setId(UUID.randomUUID().toString());
                                        infoProcessTestReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
                                        infoProcessTestReport.setDisabled(false);
                                        infoProcessTestReport.setTargetId(targetId);
                                        infoProcessTestReport.setTargetType(equipmentLaunchStatus.getTargetTypeId());
                                        long[] times = new long[]{equipmentLaunchStatus.getTime(), targetInstructionsInfo.getTime(), targetFireControlInfo.getTime()};
                                        infoProcessTestReport.setTime(MathUtils.findMinByCollections(times));
                                        //2.判断信息流程信息第二个条件
                                        if (meetTestCycleHelper(times, threshold)) {
                                            infoProcessTestReport.setStatus(true);
                                        } else {
                                            infoProcessTestReport.setStatus(false);
                                        }
                                        infoProcessTestReportService.insert(infoProcessTestReport);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 威胁判断算法-7
     */
    @Override
    public void threatJudgment(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }
        //获取redis中算法相关dds报文的key
        String instructionsKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());
        String targetKey = String.format("%s:%s", Constant.TARGET_INFO_HTTP_KEY, DateParserUtils.getTime());
        RedisService redisService = RedisUtils.getService(config.getPumpDataBase());
        if (!redisService.exists(instructionsKey) || !redisService.exists(targetKey)) {
            log.error("条件无法满足威胁判断算法的要求！");
            return;
        }

        Map<String, Map> allTargetInstructions = redisService.extrasForHash().hgetall(instructionsKey, Map.class);
        Map<String, TargetInfo> allTargetInfos = redisService.extrasForHash().hgetall(targetKey, TargetInfo.class);

        Double threshold1 = getThreshold(pipeTest, "detector_time_threshold");
        Double threshold2 = getThreshold(pipeTest, "distanceOffset");
        Double threshold3 = getThreshold(pipeTest, "speedOffset");
        Double threshold4 = getThreshold(pipeTest, "pitchOffset");


        //进行算法判断第一个条件  a.A1==B1
        for (String targetId : allTargetInstructions.keySet()) {
            if (allTargetInfos.containsKey(targetId)) {
                for (Object targetInstructionsList : allTargetInstructions.get(targetId).values()) {
                    List<TargetInstructionsInfo> list = JsonUtils.deserializeList(JsonUtils.serialize(targetInstructionsList), TargetInstructionsInfo.class);
                    for (TargetInstructionsInfo targetInstructionsInfo : list) {
                        //进行算法判断第二个条件 b.|A3-B3| < 传感器探测时间阀值
                        if (Math.abs(targetInstructionsInfo.getTime() - allTargetInfos.get(targetId).getTime()) < threshold1) {
                            TargetInfo targetInfo = allTargetInfos.get(targetId);
                            float distanceOffset = Math.abs(targetInstructionsInfo.getDistance() - targetInfo.getDistance());
                            float speedOffset = Math.abs(targetInstructionsInfo.getSpeed() - targetInfo.getSpeed());
                            float pitchOffset = Math.abs(targetInstructionsInfo.getPitchAngle() - targetInfo.getPitchAngle());

                            if (distanceOffset > threshold2 || speedOffset > threshold3 || pitchOffset > threshold4) {
                                //装填测试报文
                                ThreatenReport threatenReport = new ThreatenReport();
                                threatenReport.setTime(targetInstructionsInfo.getTime());
                                threatenReport.setTargetId(targetId);
                                threatenReport.setTargetType(targetInstructionsInfo.getTargetTypeId());
                                threatenReport.setDistanceOffset(distanceOffset);
                                threatenReport.setSpeedOffset(speedOffset);
                                threatenReport.setPitchOffset(pitchOffset);
                                threatenReport.setDisabled(false);
                                threatenReport.setId(UUID.randomUUID().toString());
                                threatenReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
                                threatenReportService.insert(threatenReport);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 指示处理精度测试-8
     */
    @Override
    public void indicationAccuracyTest(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }

        String instructionsKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());
        String targetKey = String.format("%s:%s", Constant.TARGET_INFO_HTTP_KEY, DateParserUtils.getTime());

        RedisService redisService = RedisUtils.getService(config.getPumpDataBase());

        if (!redisService.exists(instructionsKey) || !redisService.exists(targetKey)) {
            log.error("从Redis中获取指示处理精度测试dds报文条件不满足！");
            return;
        }

        Map<String, Map> allTargetInstructions = redisService.extrasForHash().hgetall(instructionsKey, Map.class);
        Map<String, TargetInfo> allTargetInfos = redisService.extrasForHash().hgetall(targetKey, TargetInfo.class);


        Double threshold1 = getThreshold(pipeTest, "instruction_time_threshold");
        Double threshold2 = getThreshold(pipeTest, "distanceAccuracy");
        Double threshold3 = getThreshold(pipeTest, "pitchAccuracy");
        Double threshold4 = getThreshold(pipeTest, "azimuthAccuracy");
        Double threshold5 = getThreshold(pipeTest, "depthAccuracy");

        //进行算法判断第一个条件  a.A1==B1
        for (String targetId : allTargetInstructions.keySet()) {
            if (allTargetInfos.containsKey(targetId)) {
                for (Object targetInstructionsList : allTargetInstructions.get(targetId).values()) {
                    List<TargetInstructionsInfo> list = JsonUtils.deserializeList(JsonUtils.serialize(targetInstructionsList), TargetInstructionsInfo.class);
                    for (TargetInstructionsInfo targetInstructionsInfo : list) {
                        TargetInfo targetInfo = allTargetInfos.get(targetId);
                        //进行算法判断第二个条件 b.|A5-B3| < 指示处理时间阀值
                        if (Math.abs(targetInstructionsInfo.getTime() - targetInfo.getTime()) < threshold1) {

                            float distanceAccuracy = Math.abs(targetInstructionsInfo.getDistance() - targetInfo.getDistance());
                            float pitchAccuracy = Math.abs(targetInstructionsInfo.getPitchAngle() - targetInfo.getPitchAngle());
                            float azimuthAccuracy = Math.abs(targetInstructionsInfo.getAzimuth() - targetInfo.getAzimuth());
                            float depthAccuracy = Math.abs(targetInstructionsInfo.getDepth() - targetInfo.getDepth());
                            if (targetInstructionsInfo.getTargetTypeId().equals(Constant.AIRTYPE) ?
                                    distanceAccuracy > threshold2 || pitchAccuracy > threshold3 || azimuthAccuracy > threshold4 :
                                    distanceAccuracy > threshold2 || pitchAccuracy > threshold3 || depthAccuracy > threshold5) {
                                //装填报文
                                InstructionAccuracyReport instructionAccuracyReport = new InstructionAccuracyReport();
                                instructionAccuracyReport.setTime(targetInstructionsInfo.getTime());
                                instructionAccuracyReport.setTargetId(targetInstructionsInfo.getTargetId());
                                instructionAccuracyReport.setTargetType(targetInstructionsInfo.getTargetTypeId());
                                instructionAccuracyReport.setSensorId(targetInstructionsInfo.getEquipmentId());
                                instructionAccuracyReport.setSensorType(targetInstructionsInfo.getEquipmentTypeId());
                                instructionAccuracyReport.setDistanceAccuracy(distanceAccuracy);
                                instructionAccuracyReport.setPitchAccuracy(pitchAccuracy);
                                instructionAccuracyReport.setAzimuthAccuracy(azimuthAccuracy);
                                instructionAccuracyReport.setDepthAccuracy(depthAccuracy);
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
        }
    }

    /**
     * 执行情况测试-9
     */
    @Override
    public void executionStatusTest(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }
        //拼接redis中当天信息流程测试相关的key
        String equipmentLaunchKey = String.format("%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, DateParserUtils.getTime());
        String targetInstructionsKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());
        String targetFireControlKey = String.format("%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, DateParserUtils.getTime());

        RedisService redisService = RedisUtils.getService(config.getPumpDataBase());

        if (!redisService.exists(equipmentLaunchKey) || !redisService.exists(targetInstructionsKey) || !redisService.exists(targetFireControlKey)) {
            log.error("dds报文信息不满足执行情况测试！");
            return;
        }
        //获取redis中当天信息流程测试所需dds报文EquipmentLaunchStatus,TargetInstructionsInfo,TargetFireControlInfo
        Map<String, Map> allEquipmentLaunchs = redisService.extrasForHash().hgetall(equipmentLaunchKey, Map.class);
        Map<String, Map> allTargetInstructions = redisService.extrasForHash().hgetall(targetInstructionsKey, Map.class);
        Map<String, Map> allTargetFireControlInfos = redisService.extrasForHash().hgetall(targetFireControlKey, Map.class);


        //获取测试的阈值
        Double threshold = getThreshold(pipeTest, "execution_time_threshold");

        //循环外设置变量用于存储时间用于运算
        Long equipmentLaunchTime;
        Long targetInstructionsTime;
        Long targetFireControlInfoTime;

        //判断第一个条件 A6==B1==C1
        for (String targetId : allEquipmentLaunchs.keySet()) {
            if (allTargetInstructions.containsKey(targetId) && allTargetFireControlInfos.containsKey(targetId)) {
                //通过第一条件
                for (Object equipemntLaunchsList : allEquipmentLaunchs.get(targetId).values()) {
                    List<EquipmentLaunchStatus> list = JsonUtils.deserializeList(JsonUtils.serialize(equipemntLaunchsList), EquipmentLaunchStatus.class);
                    for (EquipmentLaunchStatus equipmentLaunchStatus : list) {
                        //1.获取武器发射时间
                        equipmentLaunchTime = equipmentLaunchStatus.getTime();
                        for (Object targetInstructionsInfoList : allTargetInstructions.get(targetId).values()) {
                            List<TargetInstructionsInfo> list1 = JsonUtils.deserializeList(JsonUtils.serialize(targetInstructionsInfoList), TargetInstructionsInfo.class);
                            for (TargetInstructionsInfo targetInstructionsInfo : list1) {
                                //2.获取探测目标时间
                                targetInstructionsTime = targetInstructionsInfo.getTime();
                                for (Object targetFireControlInfosList : allTargetFireControlInfos.get(targetId).values()) {
                                    List<TargetFireControlInfo> list2 = JsonUtils.deserializeList(JsonUtils.serialize(targetFireControlInfosList), TargetFireControlInfo.class);
                                    for (TargetFireControlInfo targetFireControlInfo : list2) {
                                        //2.获取诸元时间
                                        targetFireControlInfoTime = targetFireControlInfo.getTime();
                                        // 设置执行情况返回报文信息
                                        ExecutionStatusReport executionStatusReport = new ExecutionStatusReport();
                                        executionStatusReport.setTargetId(equipmentLaunchStatus.getTargetId());
                                        executionStatusReport.setTime(equipmentLaunchStatus.getTime());
                                        executionStatusReport.setTargetType(equipmentLaunchStatus.getTargetTypeId());
                                        executionStatusReport.setTaskId(taskId);
                                        executionStatusReport.setId(UUID.randomUUID().toString());
                                        executionStatusReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
                                        executionStatusReport.setDisabled(false);
                                        //判断第二个条件 B5<C5<A4
                                        if (targetInstructionsTime < targetFireControlInfoTime && targetFireControlInfoTime < equipmentLaunchTime) {
                                            //判断第三个条件 （A4－B5）< 执行时间阈值
                                            if ((equipmentLaunchTime - targetInstructionsTime) < threshold) {
                                                //true
                                                executionStatusReport.setStatus(true);
                                            } else {
                                                //false
                                                executionStatusReport.setStatus(false);
                                            }
                                        } else {
                                            //false
                                            executionStatusReport.setStatus(false);
                                        }
                                        executionStatusReportService.insert(executionStatusReport);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 雷达航迹测试-10
     */
    @Override
    public void radarTrackTest(String taskId, PipeTest pipeTest) {


        if (beStart(taskId, pipeTest)) {
            return;
        }

        String instructionsKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());
        String targetKey = String.format("%s:%s", Constant.TARGET_INFO_HTTP_KEY, DateParserUtils.getTime());

        RedisService redisService = RedisUtils.getService(config.getPumpDataBase());

        if (!redisService.exists(instructionsKey) || !redisService.exists(targetKey)) {
            log.error("从Redis中获取雷达航迹测试所需报文不满足！");
            return;
        }

        Map<String, Map> allTargetInstructins = redisService.extrasForHash().hgetall(instructionsKey, Map.class);
        Map<String, TargetInfo> allTargetInfo = redisService.extrasForHash().hgetall(targetKey, TargetInfo.class);

        Double threshold1 = getThreshold(pipeTest, "radar_path_time_threshold");
        Double threshold2 = getThreshold(pipeTest, "targetDistance");
        Double threshold3 = getThreshold(pipeTest, "targetPitch");

        //判断第一个条件 A1==B1
        for (String targetId : allTargetInstructins.keySet()) {
            if (allTargetInfo.containsKey(targetId)) {
                TargetInfo targetInfo = allTargetInfo.get(targetId);
                for (Object targetInstructionsList : allTargetInstructins.get(targetId).values()) {
                    List<TargetInstructionsInfo> list = JsonUtils.deserializeList(JsonUtils.serialize(targetInstructionsList), TargetInstructionsInfo.class);
                    for (TargetInstructionsInfo targetInstructionsInfo : list) {
                        //判断第二个条件 A2==B2==对空目标
                        if (targetInstructionsInfo.getTargetTypeId().equals(targetInfo.getTargetTypeId()) && targetInfo.getTargetTypeId().equals(Constant.AIRTYPE)) {
                            //判断第三个条件 0<（A5－B3）< 雷达航迹测试时间阈值
                            if ((targetInstructionsInfo.getTime() - targetInfo.getTime()) > 0 && (targetInstructionsInfo.getTime() - targetInfo.getTime()) < threshold1) {
                                if (Math.abs(targetInstructionsInfo.getDistance() - targetInfo.getDistance()) > threshold2 || Math.abs(targetInstructionsInfo.getPitchAngle() - targetInfo.getPitchAngle()) > threshold3) {
                                    //封装报文数据
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
            }

        }
    }

    /**
     * 拦截距离测试-11
     */
    @Override
    public void interceptDistanceTest(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }
        String key = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());

        if (!RedisUtils.getService(config.getPumpDataBase()).exists(key)) {
            log.error("redis中报文信息不满足拦截距离测试！");
            return;
        }
        Map<String, Map> allInstructions = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(key, Map.class);

        if (allInstructions == null) {
            return;
        }
        //同一目标
        for (Map<String, List> map : allInstructions.values()) {

            Long maxTime = Long.MIN_VALUE;
            Float minDistance = Float.MAX_VALUE;
            String targetId = "";
            String targetTypeId = "";
            //同一传感器
            for (List list : map.values()) {
                for (Object targetInstructions : list) {
                    TargetInstructionsInfo targetInstructionsInfo = JsonUtils.deserialize(JsonUtils.serialize(targetInstructions), TargetInstructionsInfo.class);
                    //获取最大探测目标时间
                    maxTime = targetInstructionsInfo.getTime() > maxTime ? targetInstructionsInfo.getTime() : maxTime;
                    //获取最小探测目标距离
                    minDistance = targetInstructionsInfo.getDistance() < minDistance ? targetInstructionsInfo.getDistance() : minDistance;
                    targetId = targetInstructionsInfo.getTargetId();
                    targetTypeId = targetInstructionsInfo.getTargetTypeId();
                }
            }
            //不满足下列条件进入下个目标的拦截距离测试
            if (minDistance == Float.MAX_VALUE || maxTime == Long.MIN_VALUE || minDistance < 50) {
                continue;
            }
            if (targetTypeId.equals(Constant.AIRTYPE) ? minDistance < getThreshold(pipeTest, "air_interception_distance") : minDistance < getThreshold(pipeTest, "water_interception_distance")) {
                InterceptDistanceReport interceptDistanceReport = new InterceptDistanceReport();
                interceptDistanceReport.setInterceptDistance(minDistance);
                interceptDistanceReport.setTime(maxTime);
                interceptDistanceReport.setTargetType(targetId);
                interceptDistanceReport.setTargetId(targetTypeId);
                interceptDistanceReport.setTaskId(taskId);
                interceptDistanceReport.setId(UUID.randomUUID().toString());
                interceptDistanceReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
                interceptDistanceReport.setDisabled(false);
                interceptDistanceReportService.insert(interceptDistanceReport);
            }
        }
    }

    /**
     * 火控解算精度测试-12
     */
    @Override
    public void fireControlTest(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }

        String targetFireKey = String.format("%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, DateParserUtils.getTime());
        String targetInstructionsKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());

        if (!RedisUtils.getService(config.getPumpDataBase()).exists(targetFireKey) || !RedisUtils.getService(config.getPumpDataBase()).exists(targetInstructionsKey)) {
            log.error("从Redis中获取火控解算精度测试所需报文不满足！");
            return;
        }

        Map<String, Map> targetFireAll = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(targetFireKey, Map.class);
        Map<String, Map> targetInstructionsAll = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(targetInstructionsKey, Map.class);

        //获取火控解算时间阈值
        Double threshold1 = getThreshold(pipeTest, "fire_control_time_threshold");
        Double threshold2 = getThreshold(pipeTest, "targetDistance");
        Double threshold3 = getThreshold(pipeTest, "targetPitch");
        Double threshold4 = getThreshold(pipeTest, "targetAzimuth");
        Double threshold5 = getThreshold(pipeTest, "targetDepth");

        //判断第一个条件 A1==B1
        for (String targetId : targetFireAll.keySet()) {
            if (targetInstructionsAll.containsKey(targetId)) {
                //通过第一个条件，判断第二条件 |B5 - A5| < 火控解算时间阈值
                for (Object targetFireList : targetFireAll.get(targetId).values()) {
                    for (Object targetInstructionsList : targetInstructionsAll.get(targetId).values()) {
                        List<TargetFireControlInfo> list = JsonUtils.deserializeList(JsonUtils.serialize(targetFireList), TargetFireControlInfo.class);
                        List<TargetInstructionsInfo> list1 = JsonUtils.deserializeList(JsonUtils.serialize(targetInstructionsList), TargetInstructionsInfo.class);
                        for (TargetInstructionsInfo targetInstructionsInfo : list1) {
                            for (TargetFireControlInfo targetFireControlInfo : list) {
                                //判断第二条件 |B5 - A5| < 火控解算时间阈值
                                if (Math.abs(targetFireControlInfo.getTime() - targetInstructionsInfo.getTime()) < threshold1) {

                                    float TargetDistance = Math.abs(targetFireControlInfo.getDistance() - targetInstructionsInfo.getDistance());
                                    float TargetPitch = Math.abs(targetFireControlInfo.getPitchAngle() - targetInstructionsInfo.getPitchAngle());
                                    float TargetAzimuth = Math.abs(targetFireControlInfo.getAzimuth() - targetInstructionsInfo.getAzimuth());
                                    float TargetDepth = Math.abs(targetFireControlInfo.getDepth() - targetInstructionsInfo.getDepth());

                                    String fireControlSystemTypeId = targetFireControlInfo.getFireControlSystemTypeId();

                                    Boolean status = false;

                                    if (fireControlSystemTypeId.equals(Constant.SHIP_TO_AIR_MISSILE_FIRE_CONTROL_DEVICE) ||
                                            fireControlSystemTypeId.equals(Constant.ANTI_MISSILE_NAVAL_GUN_FIRE_CONTROL_DEVICE) ||
                                            fireControlSystemTypeId.equals(Constant.FIRE_CONTROL_DEVICE_OF_ELECTRONIC_COUNTERMEASURE_WEAPON)) {
                                        status = TargetDistance > threshold2 || TargetPitch > threshold3 || TargetAzimuth > threshold4;
                                    } else if (fireControlSystemTypeId.equals(Constant.TORPEDO_DEFENSE_WEAPON_FIRE_CONTROL_DEVICE)) {
                                        status = TargetDistance > threshold2 || TargetPitch > threshold3 || TargetDepth > threshold5;
                                    }

                                    if (status) {
                                        //封装报文信息
                                        FireControlReport fireControlReport = new FireControlReport();
                                        fireControlReport.setTargetId(targetInstructionsInfo.getTargetId());
                                        fireControlReport.setTargetType(targetInstructionsInfo.getTargetTypeId());
                                        fireControlReport.setFireControlId(targetFireControlInfo.getFireControlSystemId());
                                        fireControlReport.setFireControlType(fireControlSystemTypeId);
                                        fireControlReport.setTime(targetFireControlInfo.getTime());
                                        fireControlReport.setTargetDistance(TargetDistance);
                                        fireControlReport.setTargetPitch(TargetPitch);
                                        fireControlReport.setTargetAzimuth(TargetAzimuth);
                                        fireControlReport.setTargetDepth(TargetDepth);
                                        fireControlReport.setTaskId(taskId);
                                        fireControlReport.setId(UUID.randomUUID().toString());
                                        fireControlReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
                                        fireControlReport.setDisabled(false);
                                        fireControlReportService.insert(fireControlReport);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 反应时间测试-13
     */
    @Override
    public void reactionTimeTest(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }

        String equipmentKey = String.format("%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, DateParserUtils.getTime());
        String targetKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());

        if (!RedisUtils.getService(config.getPumpDataBase()).exists(equipmentKey) || !RedisUtils.getService(config.getPumpDataBase()).exists(targetKey)) {
            log.error("从Redis中获取反应时间测试测试所需报文不满足！");
            return;
        }
        Map<String, Map> allEquipmentLaunchStatus = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(equipmentKey, Map.class);
        Map<String, Map> allTargetInstructions = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(equipmentKey, Map.class);

        for (String targetId : allEquipmentLaunchStatus.keySet()) {
            Long minEquipmentTime = Long.MAX_VALUE;
            Long minTargetTime = Long.MAX_VALUE;
            if (allTargetInstructions.containsKey(targetId)) {
                //目标指示和武器发射目标一致,循环报文，获取最小探测时间和最小武器发射时间
                for (Object equipmentList : allEquipmentLaunchStatus.get(targetId).values()) {
                    List<EquipmentLaunchStatus> list = JsonUtils.deserializeList(JsonUtils.serialize(equipmentList), EquipmentLaunchStatus.class);
                    String equipmentId = "";
                    String equipmentTypeId = "";
                    for (EquipmentLaunchStatus equipmentLaunchStatus : list) {
                        equipmentId = equipmentLaunchStatus.getEquipmentId();
                        equipmentTypeId = equipmentLaunchStatus.getEquipmentTypeId();
                        minEquipmentTime = equipmentLaunchStatus.getTime() < minEquipmentTime ? equipmentLaunchStatus.getTime() : minEquipmentTime;
                    }
                    if (minEquipmentTime == Long.MAX_VALUE) {
                        continue;
                    }
                    for (Object targetList : allTargetInstructions.get(targetId).values()) {
                        List<TargetInstructionsInfo> list1 = JsonUtils.deserializeList(JsonUtils.serialize(targetList), TargetInstructionsInfo.class);
                        String targetTypeId = "";
                        String targetEquipmentId = "";
                        String targetEquipmentTypeId = "";
                        for (TargetInstructionsInfo targetInstructionsInfo : list1) {
                            targetTypeId = targetInstructionsInfo.getTargetTypeId();
                            targetEquipmentId = targetInstructionsInfo.getEquipmentId();
                            targetEquipmentTypeId = targetInstructionsInfo.getEquipmentTypeId();
                            minTargetTime = targetInstructionsInfo.getTime() < minTargetTime ? targetInstructionsInfo.getTime() : minTargetTime;
                        }
                        if (minTargetTime == Long.MAX_VALUE) {
                            continue;
                        }
                        Long reactionTime = minEquipmentTime - minTargetTime;
                        if (targetTypeId.equals(Constant.AIRTYPE) ? reactionTime > getThreshold(pipeTest, "airReactionTime") : reactionTime > getThreshold(pipeTest, "waterReactionTime")) {
                            ReactionReport reactionReport = new ReactionReport();
                            reactionReport.setTime(minEquipmentTime);
                            reactionReport.setReactionTime(reactionTime);
                            reactionReport.setTargetId(targetId);
                            reactionReport.setTargetType(targetTypeId);
                            reactionReport.setSensorId(targetEquipmentId);
                            reactionReport.setSensorType(targetEquipmentTypeId);
                            reactionReport.setWeaponId(equipmentId);
                            reactionReport.setWeaponType(equipmentTypeId);
                            reactionReport.setTaskId(taskId);
                            reactionReport.setId(UUID.randomUUID().toString());
                            reactionReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
                            reactionReport.setDisabled(false);
                            reactionReportService.insert(reactionReport);
                        }
                    }

                }
            }
        }
    }


    /**
     * 发射架调转精度测试-14
     */
    @Override
    public void launcherRotationTest(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }

        String equipmentKey = String.format("%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, DateParserUtils.getTime());
        String targetKey = String.format("%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, DateParserUtils.getTime());

        if (!RedisUtils.getService(config.getPumpDataBase()).exists(equipmentKey) || !RedisUtils.getService(config.getPumpDataBase()).exists(targetKey)) {
            log.error("redis中存在的dds报文信息不满足发射架调转精度测试！");
            return;

        }

        Map<String, Map> allFireControlInfos = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(targetKey, Map.class);
        Map<String, Map> allEquipmentLaunchStatus = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(equipmentKey, Map.class);


        Double threshold1 = getThreshold(pipeTest, "launcher_rotation_time_threshold");
        Double threshold2 = getThreshold(pipeTest, "targetDistance");
        Double threshold3 = getThreshold(pipeTest, "targetPitch");

        //判断第一条件，目标id一致
        for (String targetId : allEquipmentLaunchStatus.keySet()) {
            if (allFireControlInfos.containsKey(targetId)) {
                for (Object equipmentLaunchStatusList : allEquipmentLaunchStatus.get(targetId).values()) {
                    List<EquipmentLaunchStatus> list = JsonUtils.deserializeList(JsonUtils.serialize(equipmentLaunchStatusList), EquipmentLaunchStatus.class);
                    for (EquipmentLaunchStatus equipmentLaunchStatus : list) {
                        for (Object targetFireControInfosList : allFireControlInfos.get(targetId).values()) {
                            List<TargetFireControlInfo> list1 = JsonUtils.deserializeList(JsonUtils.serialize(targetFireControInfosList), TargetFireControlInfo.class);
                            for (TargetFireControlInfo targetFireControlInfo : list1) {
                                Long time = equipmentLaunchStatus.getTime() - targetFireControlInfo.getTime();
                                //判断第二条件
                                if (time > 0 && time < threshold1) {
                                    float launcherPitchAccuracy = Math.abs(equipmentLaunchStatus.getLaunchAzimuth() - targetFireControlInfo.getAzimuth());
                                    float launcherAzimuthAccuracy = Math.abs(equipmentLaunchStatus.getLaunchPitchAngle() - targetFireControlInfo.getPitchAngle());
                                    if (launcherPitchAccuracy > threshold2 || launcherAzimuthAccuracy > threshold3) {
                                        //封装报文
                                        LauncherRotationReport launcherRotationReport = new LauncherRotationReport();
                                        launcherRotationReport.setTargetId(equipmentLaunchStatus.getTargetId());
                                        launcherRotationReport.setTargetType(equipmentLaunchStatus.getTargetTypeId());
                                        launcherRotationReport.setWeaponId(equipmentLaunchStatus.getEquipmentId());
                                        launcherRotationReport.setWeaponType(equipmentLaunchStatus.getEquipmentTypeId());
                                        launcherRotationReport.setTime(equipmentLaunchStatus.getTime());
                                        launcherRotationReport.setLauncherPitchAccuracy(launcherPitchAccuracy);
                                        launcherRotationReport.setLauncherAzimuthAccuracy(launcherAzimuthAccuracy);
                                        launcherRotationReport.setTargetId(taskId);
                                        launcherRotationReport.setId(UUID.randomUUID().toString());
                                        launcherRotationReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
                                        launcherRotationReport.setDisabled(false);
                                        launcherRotationReportService.insert(launcherRotationReport);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 多目标拦截能力测试-15
     */
    @Override
    public void multiTargetInterceptionTest(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }

        String targetKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());

        if (!RedisUtils.getService(config.getPumpDataBase()).exists(targetKey)) {
            log.error("redis中的dds报文无法满足多目标拦截能力测试！");
            return;
        }

        Map<String, Map> allInstructions = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(targetKey, Map.class);

        MultiTargetInterceptionReport multiTargetInterceptionReport = new MultiTargetInterceptionReport();
        ArrayList<Long> times = new ArrayList<>();
        ArrayList<MultiTargetInterceptionReport> multiTargetInterceptionReportArrayList = new ArrayList<>();
        //同一目标
        for (Map<String, List> map : allInstructions.values()) {

            String targetId = "";
            String targetTypeId = "";
            Long maxTime = Long.MIN_VALUE;
            Float minDistance = Float.MAX_VALUE;
            //同一传感器
            for (List list : map.values()) {
                for (Object targetInstructions : list) {
                    TargetInstructionsInfo targetInstructionsInfo = JsonUtils.deserialize(JsonUtils.serialize(targetInstructions), TargetInstructionsInfo.class);
                    //获取最大探测目标时间
                    maxTime = targetInstructionsInfo.getTime() > maxTime ? targetInstructionsInfo.getTime() : maxTime;
                    //获取最小探测目标距离
                    minDistance = targetInstructionsInfo.getDistance() < minDistance ? targetInstructionsInfo.getDistance() : minDistance;
                    targetId = targetInstructionsInfo.getTargetId();
                    targetTypeId = targetInstructionsInfo.getTargetTypeId();
                }
            }
            //不满足下列条件进入下个目标的拦截距离测试
            if (minDistance == Float.MAX_VALUE || maxTime == Long.MIN_VALUE || minDistance < 50) {
                continue;
            }
            times.add(maxTime);
            multiTargetInterceptionReport.setId(UUID.randomUUID().toString());
            multiTargetInterceptionReport.setTaskId(taskId);
            multiTargetInterceptionReport.setTargetId(targetId);
            multiTargetInterceptionReport.setTargetType(targetTypeId);
            multiTargetInterceptionReport.setInterceptionTime(maxTime);
            multiTargetInterceptionReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
            multiTargetInterceptionReport.setDisabled(false);
            multiTargetInterceptionReportArrayList.add(multiTargetInterceptionReport);
        }
        if (multiTargetInterceptionReportArrayList == null || multiTargetInterceptionReportArrayList.size() == 0) {
            return;
        }
        Collections.sort(times);
        Long time = times.get(times.size() - 1);
        int size = multiTargetInterceptionReportArrayList.size();
        multiTargetInterceptionReportArrayList.forEach(Report -> {
            Report.setTime(time);
            Report.setInterceptionAccount(size);
        });
        multiTargetInterceptionReportService.insertList(multiTargetInterceptionReportArrayList);
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
    public boolean meetTestCycleHelper(long[] timeVec, Double pipeTestCycleThreshold) {
        return MathUtils.findMaxByCollections(timeVec) - MathUtils.findMinByCollections(timeVec) <
                pipeTestCycleThreshold;
    }


    /**
     * 是否开始执行
     *
     * @param taskId
     * @param pipeTest
     * @return
     */
    public Boolean beStart(String taskId, PipeTest pipeTest) {
        return StringUtils.isEmpty(taskId) || pipeTest == null;
    }


    /**
     * 获得水下防御时间节点信息
     *
     * @return 水下防御事件的时间节点列表
     */
    public List<StateAnalysisTimeReport> getUnderWaterDefendInfo() {

        List<StateAnalysisTimeReport> stateAnalysisTimeReportList = new ArrayList<>();

        String targetKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());

        StringRedisTemplate template = RedisUtils.getService(config.getPumpDataBase()).getTemplate();
        if (Boolean.FALSE.equals(template.hasKey(targetKey))) {
            log.error("从Redis中获取目标指示信息失败！");
            return null;
        }
        String fireControlKey = String.format("%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, DateParserUtils.getTime());
        if (Boolean.FALSE.equals(template.hasKey(fireControlKey))) {
            log.error("从Redis中获取目标火控信息失败！");
            return null;
        }
        String fireKey = String.format("%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, DateParserUtils.getTime());
        if (Boolean.FALSE.equals(template.hasKey(fireKey))) {
            log.error("从Redis中获取发射架调转信息失败！");
            return null;
        }
        String launcherKey = String.format("%s:%s", Constant.LAUNCHER_ROTATION_INFO_HTTP_KEY, DateParserUtils.getTime());
        if (Boolean.FALSE.equals(template.hasKey(launcherKey))) {
            log.error("从Redis中获取发射架调转信息失败！");
            return null;
        }

        Map<String, String> allInstructionsInfo = RedisUtils.getService(config.getPumpDataBase())
                .boundHashOps(targetKey).entries();

        if (allInstructionsInfo == null) {
            return null;
        }

        Map<String, TargetInstructionsInfo> targetInstructionsInfoMap = allInstructionsInfo.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        pair -> JsonUtils.deserialize(pair.getValue(), TargetInstructionsInfo.class)
                ));

        for (TargetInstructionsInfo targetInstructionsInfo : targetInstructionsInfoMap.values()) {

            if (!StringUtils.equals(targetInstructionsInfo.getTargetTypeId(), "水下目标")) {
                continue;
            }
            StateAnalysisTimeReport stateAnalysisTimeReport = new StateAnalysisTimeReport();
            String targetId = targetInstructionsInfo.getTargetId();

            stateAnalysisTimeReport.setId(targetId);
            stateAnalysisTimeReport.setInstructionTime(targetInstructionsInfo.getTime());

            Map<String, String> allFireControlInfo = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                    fireControlKey).entries();
            if (allFireControlInfo == null) {
                continue;
            }

            String temp = allFireControlInfo.get(targetId);
            if (temp == null) {
                continue;
            }
            TargetFireControlInfo targetFireControlInfo = JsonUtils.deserialize(
                    temp,
                    TargetFireControlInfo.class);
            if (targetFireControlInfo == null) {
                continue;
            }
            stateAnalysisTimeReport.setFireControlTime(targetFireControlInfo.getTime());

            launcherKey = String.format("%s:%s", Constant.LAUNCHER_ROTATION_INFO_HTTP_KEY + Constant.TARGET_ID, DateParserUtils.getTime());
            Map<String, String> allLauncherRotationInfo = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                    launcherKey).entries();
            if (allLauncherRotationInfo == null) {
                continue;
            }

            temp = allLauncherRotationInfo.get(targetId);
            if (temp == null) {
                continue;
            }

            LauncherRotationInfo launcherRotationInfo = JsonUtils.deserialize(
                    temp,
                    LauncherRotationInfo.class);
            if (launcherRotationInfo == null) {
                continue;
            }

            stateAnalysisTimeReport.setLauncherRotationTime(launcherRotationInfo.getTime());

            fireKey = String.format("%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY + Constant.TARGET_ID, DateParserUtils.getTime());
            Map<String, String> allEquipmentLaunchStatus = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                    fireKey).entries();
            if (allEquipmentLaunchStatus == null) {
                continue;
            }

            temp = allEquipmentLaunchStatus.get(targetId);
            if (temp == null) {
                continue;
            }

            EquipmentLaunchStatus equipmentLaunchStatus = JsonUtils.deserialize(
                    temp,
                    EquipmentLaunchStatus.class);
            if (equipmentLaunchStatus == null) {
                continue;
            }

            stateAnalysisTimeReport.setFireTime(equipmentLaunchStatus.getTime());

            stateAnalysisTimeReportList.add(stateAnalysisTimeReport);
        }
        return stateAnalysisTimeReportList;
    }

    /**
     * 获得对空防御时间节点信息
     *
     * @return 对空防御事件的时间节点列表
     */
    public List<StateAnalysisTimeReport> getAirDefendInfo() {

        List<StateAnalysisTimeReport> stateAnalysisTimeReportList = new ArrayList<>();

        String targetKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());

        StringRedisTemplate template = RedisUtils.getService(config.getPumpDataBase()).getTemplate();
        if (Boolean.FALSE.equals(template.hasKey(targetKey))) {
            log.error("从Redis中获取目标指示信息失败！");
            return null;
        }
        String fireControlKey = String.format("%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, DateParserUtils.getTime());
        if (Boolean.FALSE.equals(template.hasKey(fireControlKey))) {
            log.error("从Redis中获取目标火控信息失败！");
            return null;
        }
        String fireKey = String.format("%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, DateParserUtils.getTime());
        if (Boolean.FALSE.equals(template.hasKey(fireKey))) {
            log.error("从Redis中获取武器发射信息失败！");
            return null;
        }
        String launcherKey = String.format("%s:%s", Constant.LAUNCHER_ROTATION_INFO_HTTP_KEY, DateParserUtils.getTime());
        if (Boolean.FALSE.equals(template.hasKey(launcherKey))) {
            log.error("从Redis中获取发射架调转信息失败！");
            return null;
        }

        Map<String, String> allInstructionsInfo = RedisUtils.getService(config.getPumpDataBase())
                .boundHashOps(targetKey).entries();

        if (allInstructionsInfo == null) {
            return null;
        }

        Map<String, TargetInstructionsInfo> targetInstructionsInfoMap = allInstructionsInfo.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        pair -> JsonUtils.deserialize(pair.getValue(), TargetInstructionsInfo.class)
                ));

        for (TargetInstructionsInfo targetInstructionsInfo : targetInstructionsInfoMap.values()) {

            if (!StringUtils.equals(targetInstructionsInfo.getTargetTypeId(), "对空目标")) {
                continue;
            }
            StateAnalysisTimeReport stateAnalysisTimeReport = new StateAnalysisTimeReport();
            String targetId = targetInstructionsInfo.getTargetId();

            stateAnalysisTimeReport.setId(targetId);
            stateAnalysisTimeReport.setInstructionTime(targetInstructionsInfo.getTime());

            Map<String, String> allFireControlInfo = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                    fireControlKey).entries();
            if (allFireControlInfo == null) {
                continue;
            }

            String temp = allFireControlInfo.get(targetId);
            if (temp == null) {
                continue;
            }
            TargetFireControlInfo targetFireControlInfo = JsonUtils.deserialize(
                    temp,
                    TargetFireControlInfo.class);
            if (targetFireControlInfo == null) {
                continue;
            }
            stateAnalysisTimeReport.setFireControlTime(targetFireControlInfo.getTime());

            launcherKey = String.format("%s:%s", Constant.LAUNCHER_ROTATION_INFO_HTTP_KEY + Constant.TARGET_ID, DateParserUtils.getTime());
            Map<String, String> allLauncherRotationInfo = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                    launcherKey).entries();
            if (allLauncherRotationInfo == null) {
                continue;
            }

            temp = allLauncherRotationInfo.get(targetId);
            if (temp == null) {
                continue;
            }

            LauncherRotationInfo launcherRotationInfo = JsonUtils.deserialize(
                    temp,
                    LauncherRotationInfo.class);
            if (launcherRotationInfo == null) {
                continue;
            }

            stateAnalysisTimeReport.setLauncherRotationTime(launcherRotationInfo.getTime());

            fireKey = String.format("%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY + Constant.TARGET_ID, DateParserUtils.getTime());
            Map<String, String> allEquipmentLaunchStatus = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                    fireKey).entries();
            if (allEquipmentLaunchStatus == null) {
                continue;
            }

            temp = allEquipmentLaunchStatus.get(targetId);
            if (temp == null) {
                continue;
            }

            EquipmentLaunchStatus equipmentLaunchStatus = JsonUtils.deserialize(
                    temp,
                    EquipmentLaunchStatus.class);
            if (equipmentLaunchStatus == null) {
                continue;
            }

            stateAnalysisTimeReport.setFireTime(equipmentLaunchStatus.getTime());

            stateAnalysisTimeReportList.add(stateAnalysisTimeReport);
        }
        return stateAnalysisTimeReportList;
    }

}
