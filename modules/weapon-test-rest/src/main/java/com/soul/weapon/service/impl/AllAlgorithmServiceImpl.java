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
            log.error("redis报文信息不满足航空导弹测试");
            return;
        }
        if (!pipeTestHelper(allEquipmentStatus, new String[]{
                PipeWeaponIndices.AirMissileRadar.getValue(),
                PipeWeaponIndices.AirMissileFireControl.getValue(),
                PipeWeaponIndices.AirMissileLauncher.getValue(),
                PipeWeaponIndices.AirMissileShortRange.getValue(),
                PipeWeaponIndices.AirMissileMediumRange.getValue(),
                PipeWeaponIndices.AirMissileLongRange.getValue()
        })) {
            log.error("redis报文信息不满足航空导弹测试");
            return;
        }
        //创建测试报告
        ShipToAirMissileTestReport tmpReport = new ShipToAirMissileTestReport();
        //添加装备标识和自检状态
        tmpReport.setTime(System.currentTimeMillis());
        tmpReport.setRadarSelfCheck(allEquipmentStatus.get(PipeWeaponIndices.AirMissileRadar.getValue()).getCheckStatus());
        tmpReport.setRadarId(PipeWeaponIndices.AirMissileRadar.getValue());
        tmpReport.setFireControlId(PipeWeaponIndices.AirMissileFireControl.getValue());
        tmpReport.setFireControlSelfCheck(allEquipmentStatus.get(PipeWeaponIndices.AirMissileFireControl.getValue()).getCheckStatus());
        tmpReport.setLauncherId(PipeWeaponIndices.AirMissileLauncher.getValue());
        tmpReport.setLauncherSelfCheck(allEquipmentStatus.get(PipeWeaponIndices.AirMissileLauncher.getValue()).getCheckStatus());
        tmpReport.setMissileShortId(PipeWeaponIndices.AirMissileShortRange.getValue());
        tmpReport.setMissileSelfShortCheck(allEquipmentStatus.get(PipeWeaponIndices.AirMissileShortRange.getValue()).getCheckStatus());
        tmpReport.setMissileMediumId(PipeWeaponIndices.AirMissileMediumRange.getValue());
        tmpReport.setMissileSelfMediumCheck(allEquipmentStatus.get(PipeWeaponIndices.AirMissileMediumRange.getValue()).getCheckStatus());
        tmpReport.setMissileLongId(PipeWeaponIndices.AirMissileLongRange.getValue());
        tmpReport.setMissileSelfLongCheck(allEquipmentStatus.get(PipeWeaponIndices.AirMissileLongRange.getValue()).getCheckStatus());
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
            log.error("redis报文信息不满足反导舰炮测试");
            return;
        }

        if (!pipeTestHelper(allEquipmentStatus, new String[]{
                PipeWeaponIndices.AntiMissileShipGun.getValue(),
                PipeWeaponIndices.AntiMissileShipGunRadar.getValue(),
                PipeWeaponIndices.AntiMissileShipGunControl.getValue()
        })) {
            log.error("redis报文信息不满足反导舰炮测试");
            return;
        }

        AntiMissileShipGunTestReport tmpReport = new AntiMissileShipGunTestReport();

        tmpReport.setTime(System.currentTimeMillis());
        tmpReport.setRadarId(PipeWeaponIndices.AntiMissileShipGunRadar.getValue());
        tmpReport.setRadarSelfCheck(allEquipmentStatus.get(PipeWeaponIndices.AntiMissileShipGunRadar.getValue()).getCheckStatus());
        tmpReport.setFireControlId(PipeWeaponIndices.AntiMissileShipGunControl.getValue());
        tmpReport.setFireControlSelfCheck(allEquipmentStatus.get(PipeWeaponIndices.AntiMissileShipGunControl.getValue()).getCheckStatus());
        tmpReport.setShipGunId(PipeWeaponIndices.AntiMissileShipGun.getValue());
        tmpReport.setShipGunSelfCheck(allEquipmentStatus.get(PipeWeaponIndices.AntiMissileShipGun.getValue()).getCheckStatus());

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
            log.error("redis报文信息不满足鱼类防御武器测试");
            return;
        }

        if (!pipeTestHelper(allEquipmentStatus, new String[]{
                PipeWeaponIndices.Sonar.getValue(),
                PipeWeaponIndices.TorpedoFireControl.getValue(),
                PipeWeaponIndices.TorpedoLauncher.getValue(),
                PipeWeaponIndices.Torpedo.getValue()
        })) {
            log.error("redis报文信息不满足鱼类防御武器测试");
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
            log.error("redis报文信息不满足电子对抗武器测试");
            return;
        }

        if (!pipeTestHelper(allEquipmentStatus, new String[]{
                PipeWeaponIndices.ElectronicDetection.getValue(),
                PipeWeaponIndices.ElectronicCountermeasure.getValue(),
                PipeWeaponIndices.MultiUsageLaunch.getValue(),
                PipeWeaponIndices.OutBoardElectronicCountermeasure.getValue(),
                PipeWeaponIndices.InBoardElectronicCountermeasure.getValue()
        })) {
            log.error("redis报文信息不满足电子对抗武器测试");
            return;
        }

        ElectronicWeaponTestReport tmpReport = new ElectronicWeaponTestReport();
        tmpReport.setTime(System.currentTimeMillis());
        tmpReport.setElectronicDetectorId(PipeWeaponIndices.ElectronicDetection.getValue());
        tmpReport.setElectronicDetectorSelfCheck(allEquipmentStatus.get(PipeWeaponIndices.ElectronicDetection.getValue()).getCheckStatus());
        tmpReport.setFireControlId(PipeWeaponIndices.ElectronicCountermeasure.getValue());
        tmpReport.setFireControlSelfCheck(allEquipmentStatus.get(PipeWeaponIndices.ElectronicCountermeasure.getValue()).getCheckStatus());
        tmpReport.setLauncherId(PipeWeaponIndices.MultiUsageLaunch.getValue());
        tmpReport.setLauncherSelfCheck(allEquipmentStatus.get(PipeWeaponIndices.MultiUsageLaunch.getValue()).getCheckStatus());
        tmpReport.setOuterElectronicWeaponId(PipeWeaponIndices.OutBoardElectronicCountermeasure.getValue());
        tmpReport.setOuterElectronicWeaponSelfCheck(allEquipmentStatus.get(PipeWeaponIndices.OutBoardElectronicCountermeasure.getValue()).getCheckStatus());
        tmpReport.setInnerElectronicWeaponId(PipeWeaponIndices.InBoardElectronicCountermeasure.getValue());
        tmpReport.setInnerElectronicWeaponSelfCheck(allEquipmentStatus.get(PipeWeaponIndices.InBoardElectronicCountermeasure.getValue()).getCheckStatus());
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
            log.error("redis报文信息不满足水声对抗武器测试");
            return;
        }

        if (!pipeTestHelper(allEquipmentStatus, new String[]{
                PipeWeaponIndices.UnderwaterAcousticCountermeasureControl.getValue(),
                PipeWeaponIndices.UnderwaterAcousticCountermeasure.getValue(),
                PipeWeaponIndices.MultiUsageLaunch.getValue(),
                PipeWeaponIndices.TowedSonar.getValue()
        })) {
            log.error("redis报文信息不满足水声对抗武器测试");
            return;
        }

        WaterWeaponTestReport tmpReport = new WaterWeaponTestReport();
        tmpReport.setTime(System.currentTimeMillis());
        tmpReport.setSonarId(PipeWeaponIndices.TowedSonar.getValue());
        tmpReport.setSonarSelfCheck(allEquipmentStatus.get(PipeWeaponIndices.TowedSonar.getValue()).getCheckStatus());
        tmpReport.setFireControlId(PipeWeaponIndices.UnderwaterAcousticCountermeasureControl.getValue());
        tmpReport.setFireControlSelfCheck(allEquipmentStatus.get(PipeWeaponIndices.UnderwaterAcousticCountermeasureControl.getValue()).getCheckStatus());
        tmpReport.setLauncherId(PipeWeaponIndices.MultiUsageLaunch.getValue());
        tmpReport.setLauncherSelfCheck(allEquipmentStatus.get(PipeWeaponIndices.MultiUsageLaunch.getValue()).getCheckStatus());
        tmpReport.setWaterWeaponId(PipeWeaponIndices.UnderwaterAcousticCountermeasure.getValue());
        tmpReport.setWaterWeaponSelfCheck(allEquipmentStatus.get(PipeWeaponIndices.UnderwaterAcousticCountermeasure.getValue()).getCheckStatus());

        long[] timeVec = {
                allEquipmentStatus.get(PipeWeaponIndices.TowedSonar.getValue()).getTime(),
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

        RedisService redisService = RedisUtils.getService(config.getPumpDataBase());

        String targetInstructionKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY_2, DateParserUtils.getTime());
        if (!redisService.exists(targetInstructionKey)) {
            log.error("dds报文信息不满足信息流程测试！");
            return;
        }

        //获取目标指示的报文所有的targetId
        Set<String> targetIds = redisService.boundHashOps(targetInstructionKey).keys();
        InfoProcessTestReport infoProcessTestReport = new InfoProcessTestReport();
        targetIds.forEach((targetId) -> {
            //循环判断是否存在报文所需的key
            String equipmentLaunchzKey = String.format("%s:%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, DateParserUtils.getTime(), targetId);
            String targetFireControlzKey = String.format("%s:%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, DateParserUtils.getTime(), targetId);
            String targetTypeId = redisService.extrasForHash().hgetall(targetInstructionKey, TargetInstructionsInfo.class).get(targetId).getTargetTypeId();
            if (redisService.exists(equipmentLaunchzKey) && redisService.exists(targetFireControlzKey)) {
                //信息流程正常
                infoProcessTestReport.setStatus(true);
            } else {
                //信息流程异常
                infoProcessTestReport.setStatus(false);
            }
            infoProcessTestReport.setTaskId(taskId);
            infoProcessTestReport.setId(UUID.randomUUID().toString());
            infoProcessTestReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
            infoProcessTestReport.setDisabled(false);
            infoProcessTestReport.setTargetId(targetId);
            infoProcessTestReport.setTargetType(targetTypeId);
            infoProcessTestReport.setTime(System.currentTimeMillis());
            infoProcessTestReportService.insert(infoProcessTestReport);
        });

    }

    /**
     * 威胁判断算法-7
     */
    @Override
    public void threatJudgment(String taskId, PipeTest pipeTest) {

        //判断是否开始
        if (beStart(taskId, pipeTest)) {
            return;
        }

        RedisService redisService = RedisUtils.getService(config.getPumpDataBase());

        String instructionsKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY_2, DateParserUtils.getTime());

        if (!redisService.exists(instructionsKey)) {
            log.error("条件无法满足威胁判断算法的要求！");
            return;
        }

        //获取目标指示中所有的目标id
        Set<String> targetIds = redisService.boundHashOps(instructionsKey).keys();
        //获取阈值
        Double threshold1 = getThreshold(pipeTest, "detector_time_threshold");
        Double threshold2 = getThreshold(pipeTest, "distanceOffset");
        Double threshold3 = getThreshold(pipeTest, "speedOffset");
        Double threshold4 = getThreshold(pipeTest, "pitchOffset");

        for (String targetId : targetIds) {
            //目标信息key
            String targetKey = String.format("%s:%s:%s", Constant.TARGET_INFO_HTTP_KEY, DateParserUtils.getTime(), targetId);

            if (!redisService.exists(targetKey)) {
                continue;
            }
            //获取当前目标传感器探测目标时间最新的一个目标指示
            String instructionszKey = String.format("%s:%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime(), targetId);


            Set<String> instructionsSet = redisService.getTemplate().opsForZSet().reverseRange(instructionszKey, 0, 0);

            if (instructionsSet == null || instructionsSet.size() <= 0) {
                continue;
            }

            //反序列化获取目标指示
            TargetInstructionsInfo targetInstructionInfo = JsonUtils.deserialize(instructionsSet.iterator().next(), TargetInstructionsInfo.class);

            //将阈值转为以毫秒为单位
            Double threshold1sm = threshold1 * 1000;

            //获取满足阈值要求的，并且时间最靠近的一个目标信息
            Set<String> targetInfoSet = redisService.getTemplate().opsForZSet().reverseRangeByScore(targetKey, targetInstructionInfo.getTime() - threshold1sm.longValue(), targetInstructionInfo.getTime(), 0, 1
            );

            if (targetInfoSet == null || targetInfoSet.size() <= 0) {
                continue;
            }

            TargetInfo targetInfo = JsonUtils.deserialize(targetInfoSet.iterator().next(), TargetInfo.class);

            float distanceOffset = Math.abs(targetInstructionInfo.getDistance() - targetInfo.getDistance());
            float speedOffset = Math.abs(targetInstructionInfo.getSpeed() - targetInfo.getSpeed());
            float pitchOffset = Math.abs(targetInstructionInfo.getPitchAngle() - targetInfo.getPitchAngle());

            if (distanceOffset > threshold2 || speedOffset > threshold3 || pitchOffset > threshold4) {
                //满足条件
                ThreatenReport threatenReport = new ThreatenReport();
                threatenReport.setTime(targetInstructionInfo.getTime());
                threatenReport.setTargetId(targetId);
                threatenReport.setTargetType(targetInstructionInfo.getTargetTypeId());
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


    /**
     * 指示处理精度测试-8
     */
    @Override
    public void indicationAccuracyTest(String taskId, PipeTest pipeTest) {

        //判断是否开始
        if (beStart(taskId, pipeTest)) {
            return;
        }

        RedisService redisService = RedisUtils.getService(config.getPumpDataBase());

        String instructionsMapKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY_2, DateParserUtils.getTime());

        if (!redisService.exists(instructionsMapKey)) {
            log.error("条件无法满足指示处理精度测试的要求！");
            return;
        }

        //获取目标指示中所有的目标id
        Set<String> targetIds = redisService.boundHashOps(instructionsMapKey).keys();

        for (String targetId : targetIds) {
            //目标信息key
            String targetKey = String.format("%s:%s:%s", Constant.TARGET_INFO_HTTP_KEY, DateParserUtils.getTime(), targetId);

            if (!redisService.exists(targetKey)) {
                continue;
            }
            //获取当前目标传感器探测目标时间最新的一个目标指示
            String instructionszKey = String.format("%s:%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime(), targetId);


            Set<String> instructionsSet = redisService.getTemplate().opsForZSet().reverseRange(instructionszKey, 0, 0);

            if (instructionsSet == null || instructionsSet.size() <= 0) {
                continue;
            }

            Double threshold1 = getThreshold(pipeTest, "instruction_time_threshold");
            Double threshold2 = getThreshold(pipeTest, "distanceAccuracy");
            Double threshold3 = getThreshold(pipeTest, "pitchAccuracy");
            Double threshold4 = getThreshold(pipeTest, "azimuthAccuracy");
            Double threshold5 = getThreshold(pipeTest, "depthAccuracy");

            //反序列化获取目标指示
            TargetInstructionsInfo targetInstructionsInfo = JsonUtils.deserialize(instructionsSet.iterator().next(), TargetInstructionsInfo.class);

            //将阈值转为以毫秒为单位
            Double threshold1sm = threshold1 * 1000;

            //获取满足阈值要求的，并且时间最靠近的一个目标信息
            Set<String> targetInfoSet = redisService.getTemplate().opsForZSet().reverseRangeByScore(targetKey, targetInstructionsInfo.getTime() - threshold1sm.longValue(), targetInstructionsInfo.getTime(), 0, 1);

            if (targetInfoSet == null || targetInfoSet.size() <= 0) {
                continue;
            }

            TargetInfo targetInfo = JsonUtils.deserialize(targetInfoSet.iterator().next(), TargetInfo.class);


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

    /**
     * 执行情况测试-9
     */
    @Override
    public void executionStatusTest(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }

        RedisService redisService = RedisUtils.getService(config.getPumpDataBase());

        String targetInstructionKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY_2, DateParserUtils.getTime());
        if (!redisService.exists(targetInstructionKey)) {
            log.error("dds报文信息不满足信息流程测试！");
            return;
        }
        //获取目标指示的报文所有的targetId
        Set<String> targetIds = redisService.boundHashOps(targetInstructionKey).keys();
        //获取测试的阈值
        Double threshold = getThreshold(pipeTest, "execution_time_threshold") * 1000;

        targetIds.forEach((targetId) -> {
            //循环判断是否存在报文所需的key
            String equipmentLaunchzKey = String.format("%s:%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, DateParserUtils.getTime(), targetId);
            String targetFireControlzKey = String.format("%s:%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, DateParserUtils.getTime(), targetId);
            String targetTypeId = redisService.extrasForHash().hgetall(targetInstructionKey, TargetInstructionsInfo.class).get(targetId).getTargetTypeId();
            ExecutionStatusReport executionStatusReport = new ExecutionStatusReport();
            if (redisService.exists(equipmentLaunchzKey) && redisService.exists(targetFireControlzKey)) {
                //信息流程正常
                TargetInstructionsInfo targetInstructionsInfo = redisService.extrasForHash().hgetall(targetInstructionKey, TargetInstructionsInfo.class).get(targetId);
                Long time = targetInstructionsInfo.getTime() + threshold.longValue();
                Set<String> equipmentLaunchs = redisService.getTemplate().opsForZSet().rangeByScore(equipmentLaunchzKey, 0, time - 1);
                if (equipmentLaunchs != null && equipmentLaunchs.size() > 0) {
                    equipmentLaunchs.forEach(equipmentLaunch -> {
                        EquipmentLaunchStatus equipmentLaunchStatus = JsonUtils.deserialize(equipmentLaunch, EquipmentLaunchStatus.class);
                        Set<String> targetFirControls = redisService.getTemplate().opsForZSet().rangeByScore(targetFireControlzKey, targetInstructionsInfo.getTime() + 1, equipmentLaunchStatus.getTime() - 1);
                        if (targetFirControls != null && targetFirControls.size() > 0) {
                            //信息流程正常
                            executionStatusReport.setStatus(true);
                        } else {
                            //信息流程异常
                            executionStatusReport.setStatus(false);
                        }
                    });
                } else {
                    //信息流程异常
                    executionStatusReport.setStatus(false);
                }
            } else {
                //信息流程异常
                executionStatusReport.setStatus(false);
            }
            executionStatusReport.setTargetId(targetId);
            executionStatusReport.setTime(System.currentTimeMillis());
            executionStatusReport.setTargetType(targetTypeId);
            executionStatusReport.setTaskId(taskId);
            executionStatusReport.setId(UUID.randomUUID().toString());
            executionStatusReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
            executionStatusReport.setDisabled(false);
            executionStatusReportService.insert(executionStatusReport);
        });
    }

    /**
     * 雷达航迹测试-10
     */
    @Override
    public void radarTrackTest(String taskId, PipeTest pipeTest) {


        //判断是否开始
        if (beStart(taskId, pipeTest)) {
            return;
        }

        RedisService redisService = RedisUtils.getService(config.getPumpDataBase());

        String instructionsMapKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY_2, DateParserUtils.getTime());

        if (!redisService.exists(instructionsMapKey)) {
            log.error("条件无法满足指示处理精度测试的要求！");
            return;
        }

        //获取目标指示中所有的目标id
        Set<String> targetIds = redisService.boundHashOps(instructionsMapKey).keys();

        for (String targetId : targetIds) {
            //目标信息key
            String targetKey = String.format("%s:%s:%s", Constant.TARGET_INFO_HTTP_KEY, DateParserUtils.getTime(), targetId);

            if (!redisService.exists(targetKey)) {
                continue;
            }
            //获取当前目标传感器探测目标时间最新的一个目标指示
            String instructionszKey = String.format("%s:%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime(), targetId);


            Set<String> instructionsSet = redisService.getTemplate().opsForZSet().reverseRange(instructionszKey, 0, 0);

            if (instructionsSet == null || instructionsSet.size() <= 0) {
                continue;
            }

            Double threshold1 = getThreshold(pipeTest, "radar_path_time_threshold");
            Double threshold2 = getThreshold(pipeTest, "targetDistance");
            Double threshold3 = getThreshold(pipeTest, "targetPitch");

            //反序列化获取目标指示
            TargetInstructionsInfo targetInstructionsInfo = JsonUtils.deserialize(instructionsSet.iterator().next(), TargetInstructionsInfo.class);

            //将阈值转为以毫秒为单位
            Double threshold1sm = threshold1 * 1000;

            //获取满足阈值要求的，并且时间最靠近的一个目标信息
            Set<String> targetInfoSet = redisService.getTemplate().opsForZSet().rangeByScore(targetKey, targetInstructionsInfo.getTime() - threshold1sm.longValue() + 1, targetInstructionsInfo.getTime());

            if (targetInfoSet == null || targetInfoSet.size() <= 0) {
                continue;
            }

            for (String targetInfos : targetInfoSet) {
                TargetInfo targetInfo = JsonUtils.deserialize(targetInfos, TargetInfo.class);
                if (!targetInfo.getTargetId().equals(targetInstructionsInfo.getTargetId()) && targetInfo.getTargetId().equals(Constant.AIRTYPE)) {
                    continue;
                }
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

    /**
     * 拦截距离测试-11
     */
    @Override
    public void interceptDistanceTest(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }
        String key = String.format("%s:%s:*", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());

        RedisService redisService = RedisUtils.getService(config.getPumpDataBase());
        Set<String> keys = redisService.keys(key);

        if (keys == null || keys.size() == 0) {
            log.error("从Redis中获取拦截距离测试所需报文不满足！");
            return;
        }

        Double threshold1 = getThreshold(pipeTest, "air_interception_distance");
        Double threshold2 = getThreshold(pipeTest, "water_interception_distance");

        for (String targetInstructionsKey : keys) {

            Set<String> targetInstructionsSet = redisService.getTemplate().opsForZSet().reverseRange(targetInstructionsKey, 0, 0);
            if (targetInstructionsSet == null || targetInstructionsSet.size() != 1) {
                continue;
            }
            //获取探测时间最大的一个
            TargetInstructionsInfo targetInstructions = JsonUtils.deserialize(targetInstructionsSet.iterator().next(), TargetInstructionsInfo.class);
            Set<String> set = redisService.getTemplate().opsForZSet().range(targetInstructionsKey, 0, -1);
            Float distance = Float.MAX_VALUE;
            //获取最小距离
            for (String targetInstructionsString : set) {
                TargetInstructionsInfo targetInstructionsInfo = JsonUtils.deserialize(targetInstructionsString, TargetInstructionsInfo.class);
                distance = targetInstructionsInfo.getDistance() < distance ? targetInstructionsInfo.getDistance() : distance;
            }
            if (distance == Float.MAX_VALUE) {
                continue;
            }
            if (targetInstructions.getDistance() != distance.floatValue() && distance < 50) {
                continue;
            }

            if (targetInstructions.getTargetTypeId().equals(Constant.AIRTYPE) ? distance < threshold1 : distance < threshold2) {
                InterceptDistanceReport interceptDistanceReport = new InterceptDistanceReport();
                interceptDistanceReport.setInterceptDistance(distance);
                interceptDistanceReport.setTime(System.currentTimeMillis());
                interceptDistanceReport.setTargetType(targetInstructions.getTargetTypeId());
                interceptDistanceReport.setTargetId(targetInstructions.getTargetId());
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

        RedisService redisService = RedisUtils.getService(config.getPumpDataBase());

        String targetFireKeys = String.format("%s:%s:*", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY_2, DateParserUtils.getTime());


        Set<String> keys = redisService.keys(targetFireKeys);

        if (keys == null || keys.size() == 0) {
            log.error("从Redis中获取火控解算精度测试所需报文不满足！");
            return;
        }

        //获取火控解算时间阈值
        Double threshold1 = getThreshold(pipeTest, "fire_control_time_threshold") * 1000;
        Double threshold2 = getThreshold(pipeTest, "targetDistance");
        Double threshold3 = getThreshold(pipeTest, "targetPitch");
        Double threshold4 = getThreshold(pipeTest, "targetAzimuth");
        Double threshold5 = getThreshold(pipeTest, "targetDepth");

        for (String targetFireKey : keys) {
            Map<String, TargetFireControlInfo> map = redisService.extrasForHash().hgetall(targetFireKey, TargetFireControlInfo.class);
            for (String targetId : map.keySet()) {
                String targetInstructionsKey = String.format("%s:%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime(), targetId);
                if (!redisService.exists(targetInstructionsKey)) {
                    continue;
                }
                TargetFireControlInfo targetFireControlInfo = map.get(targetId);
                Long time = targetFireControlInfo.getTime() - threshold1.longValue();
                //获取最接近阈值的一条目标指示
                Set<String> targetInstructionsSet = redisService.getTemplate().opsForZSet().reverseRangeByScore(targetInstructionsKey, time + 1, map.get(targetId).getTime() - 1, 0, 1);

                if (targetInstructionsSet == null || targetInstructionsSet.size() == 0) {
                    continue;
                }

                TargetInstructionsInfo targetInstructionsInfo = JsonUtils.deserialize(targetInstructionsSet.iterator().next(), TargetInstructionsInfo.class);

                float TargetDistance = Math.abs(targetFireControlInfo.getDistance() - targetInstructionsInfo.getDistance());
                float TargetPitch = Math.abs(targetFireControlInfo.getPitchAngle() - targetInstructionsInfo.getPitchAngle());
                float TargetAzimuth = Math.abs(targetFireControlInfo.getAzimuth() - targetInstructionsInfo.getAzimuth());
                float TargetDepth = Math.abs(targetFireControlInfo.getDepth() - targetInstructionsInfo.getDepth());

                String fireControlSystemId = targetFireControlInfo.getFireControlSystemId();

                Boolean status = false;

                if (fireControlSystemId.equals(Constant.SHIP_TO_AIR_MISSILE_FIRE_CONTROL_DEVICE) ||
                        fireControlSystemId.equals(Constant.ANTI_MISSILE_NAVAL_GUN_FIRE_CONTROL_DEVICE) ||
                        fireControlSystemId.equals(Constant.FIRE_CONTROL_DEVICE_OF_ELECTRONIC_COUNTERMEASURE_WEAPON)) {
                    status = TargetDistance > threshold2 || TargetPitch > threshold3 || TargetAzimuth > threshold4;
                } else if (fireControlSystemId.equals(Constant.TORPEDO_DEFENSE_WEAPON_FIRE_CONTROL_DEVICE)) {
                    status = TargetDistance > threshold2 || TargetPitch > threshold3 || TargetDepth > threshold5;
                }
                if (status) {
                    //封装报文信息
                    FireControlReport fireControlReport = new FireControlReport();
                    fireControlReport.setTargetId(targetInstructionsInfo.getTargetId());
                    fireControlReport.setTargetType(targetInstructionsInfo.getTargetTypeId());
                    fireControlReport.setFireControlId(targetFireControlInfo.getFireControlSystemId());
                    fireControlReport.setFireControlType(targetFireControlInfo.getFireControlSystemTypeId());
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


    /**
     * 反应时间测试-13
     */
    @Override
    public void reactionTimeTest(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }
        RedisService redisService = RedisUtils.getService(config.getPumpDataBase());


        String targetKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY_2, DateParserUtils.getTime());

        if (!redisService.exists(targetKey)) {
            log.error("从Redis中获取反应时间测试测试所需报文不满足！");
            return;
        }
        Double threshold1 = getThreshold(pipeTest, "airReactionTime") * 1000;
        Double threshold2 = getThreshold(pipeTest, "waterReactionTime") * 1000;

        Set<String> targetIds = redisService.boundHashOps(targetKey).keys();

        for (String targetId : targetIds) {
            String equipmentKey = String.format("%s:%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, DateParserUtils.getTime(), targetId);
            if (!redisService.exists(equipmentKey)) {
                continue;
            }
            //获取武器发射时间最小的一条
            Set<String> equipmentLaunchSet = redisService.getTemplate().opsForZSet().range(equipmentKey, 0, 0);
            EquipmentLaunchStatus equipmentLaunchStatus = JsonUtils.deserialize(equipmentLaunchSet.iterator().next(), EquipmentLaunchStatus.class);


            String targetInstructionsKey = String.format("%s:%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime(), targetId);
            if (!redisService.exists(targetInstructionsKey)) {
                continue;
            }
            //获取传感器探测时间最小的一条
            Set<String> targetInstructionSet = redisService.getTemplate().opsForZSet().range(targetInstructionsKey, 0, 0);
            TargetInstructionsInfo targetInstructionsInfo = JsonUtils.deserialize(targetInstructionSet.iterator().next(), TargetInstructionsInfo.class);

            Long reactionTime = equipmentLaunchStatus.getTime() - targetInstructionsInfo.getTime();

            if (targetInstructionsInfo.getTargetTypeId().equals(Constant.AIRTYPE) ? reactionTime > threshold1.longValue() : reactionTime > threshold2.longValue()) {
                ReactionReport reactionReport = new ReactionReport();
                reactionReport.setTime(System.currentTimeMillis());
                reactionReport.setReactionTime(reactionTime);
                reactionReport.setTargetId(targetId);
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
    }


    /**
     * 发射架调转精度测试-14
     */
    @Override
    public void launcherRotationTest(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }

        RedisService redisService = RedisUtils.getService(config.getPumpDataBase());

        String launcherKey = String.format("%s:%s:*", Constant.LAUNCHER_ROTATION_INFO_HTTP_KEY, DateParserUtils.getTime());


        Set<String> keys = redisService.keys(launcherKey);

        if (keys == null || keys.size() == 0) {
            log.error("redis中存在的dds报文信息不满足发射架调转精度测试！");
            return;
        }

        Double threshold1 = getThreshold(pipeTest, "launcher_rotation_time_threshold") * 1000;
        Double threshold2 = getThreshold(pipeTest, "targetDistance");
        Double threshold3 = getThreshold(pipeTest, "targetPitch");

        for (String launcherRotationKey : keys) {
            Map<String, LauncherRotationInfo> map = redisService.extrasForHash().hgetall(launcherRotationKey, LauncherRotationInfo.class);
            for (String targetId : map.keySet()) {
                String targetKey = String.format("%s:%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, DateParserUtils.getTime(), targetId);
                if (!redisService.exists(targetKey)) {
                    continue;
                }
                LauncherRotationInfo launcherRotationInfo = map.get(targetId);
                Long time = launcherRotationInfo.getTime() - threshold1.longValue();
                //获取最近接阈值的一条目标火控
                Set<String> targetFireControlInfoSet = redisService.getTemplate().opsForZSet().reverseRangeByScore(targetKey, time + 1, launcherRotationInfo.getTime() - 1, 0, 1);
                if (targetFireControlInfoSet == null || targetFireControlInfoSet.size() == 0) {
                    continue;
                }
                TargetFireControlInfo targetFireControlInfo = JsonUtils.deserialize(targetFireControlInfoSet.iterator().next(),TargetFireControlInfo.class);

                float launcherPitchAccuracy = Math.abs(launcherRotationInfo.getAzimuth() - targetFireControlInfo.getAzimuth());
                float launcherAzimuthAccuracy = Math.abs(launcherRotationInfo.getPitchAngle() - targetFireControlInfo.getPitchAngle());

                if (launcherPitchAccuracy > threshold2 || launcherAzimuthAccuracy > threshold3) {
                    //封装报文
                    LauncherRotationReport launcherRotationReport = new LauncherRotationReport();
                    launcherRotationReport.setTargetId(launcherRotationInfo.getTargetId());
                    launcherRotationReport.setTargetType(launcherRotationInfo.getTargetTypeId());
                    launcherRotationReport.setWeaponId(launcherRotationInfo.getLauncherId());
                    launcherRotationReport.setWeaponType(launcherRotationInfo.getLauncherTypeId());
                    launcherRotationReport.setTime(launcherRotationInfo.getTime());
                    launcherRotationReport.setLauncherPitchAccuracy(launcherPitchAccuracy);
                    launcherRotationReport.setLauncherAzimuthAccuracy(launcherAzimuthAccuracy);
                    launcherRotationReport.setTaskId(taskId);
                    launcherRotationReport.setId(UUID.randomUUID().toString());
                    launcherRotationReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    launcherRotationReport.setDisabled(false);
                    launcherRotationReportService.insert(launcherRotationReport);
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
        String key = String.format("%s:%s:*", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());

        RedisService redisService = RedisUtils.getService(config.getPumpDataBase());
        Set<String> keys = redisService.keys(key);

        if (keys == null || keys.size() == 0) {
            log.error("从Redis中获取拦截距离测试所需报文不满足！");
            return;
        }

        for (String targetInstructionsKey : keys) {

            Set<String> targetInstructionsSet = redisService.getTemplate().opsForZSet().reverseRange(targetInstructionsKey, 0, 0);
            if (targetInstructionsSet == null || targetInstructionsSet.size() != 1) {
                continue;
            }
            //获取探测时间最大的一个
            TargetInstructionsInfo targetInstructions = JsonUtils.deserialize(targetInstructionsSet.iterator().next(), TargetInstructionsInfo.class);
            Set<String> set = redisService.getTemplate().opsForZSet().range(targetInstructionsKey, 0, -1);
            Float distance = Float.MAX_VALUE;
            //获取最小距离
            for (String targetInstructionsString : set) {
                TargetInstructionsInfo targetInstructionsInfo = JsonUtils.deserialize(targetInstructionsString, TargetInstructionsInfo.class);
                distance = targetInstructionsInfo.getDistance() < distance ? targetInstructionsInfo.getDistance() : distance;
            }
            if (distance == Float.MAX_VALUE) {
                continue;
            }
            if (targetInstructions.getDistance() != distance.floatValue() && distance < 50) {
                continue;
            }
            MultiTargetInterceptionReport multiTargetInterceptionReport = new MultiTargetInterceptionReport();
            multiTargetInterceptionReport.setInterceptionAccount(keys.size());
            multiTargetInterceptionReport.setId(UUID.randomUUID().toString());
            multiTargetInterceptionReport.setTaskId(taskId);
            multiTargetInterceptionReport.setTargetId(targetInstructions.getTargetId());
            multiTargetInterceptionReport.setTargetType(targetInstructions.getTargetTypeId());
            multiTargetInterceptionReport.setInterceptionTime(targetInstructions.getTime());
            multiTargetInterceptionReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
            multiTargetInterceptionReport.setDisabled(false);
            multiTargetInterceptionReportService.insert(multiTargetInterceptionReport);
        }
    }

    /**
     * @param allEquipmentStatus 所有的装备状态
     * @param indices            通道测试涉及的装备下标
     * @return bool 通道测试是否成功
     */
    public boolean pipeTestHelper(Map<String, EquipmentStatus> allEquipmentStatus, String[] indices) {
        boolean res = true;
        for (String idx : indices) {
            if (!allEquipmentStatus.containsKey(idx)) {
                return false;
            }
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
                pipeTestCycleThreshold * 1000;
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

}
