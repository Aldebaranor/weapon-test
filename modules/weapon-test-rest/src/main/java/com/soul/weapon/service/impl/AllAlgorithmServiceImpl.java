package com.soul.weapon.service.impl;

import com.egova.json.utils.JsonUtils;
import com.egova.model.PropertyItem;
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
public class AllAlgorithmServiceImpl implements AllAlgorithmService{
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


    public static final String AIRTYPE = "对空目标";


    /**
     * 获取阈值，如果 pipeTest.getThreshold()为空则使用默认的阈值
     * @param pipeTest
     * @param key
     * @return
     */
    private Double getThreshold(PipeTest pipeTest , String key){
        if(pipeTest == null ){
            return null;
        }
        PropertyItem<Double> item = new PropertyItem<>();
        String threshold = pipeTest.getThreshold();
        if (StringUtils.isEmpty(threshold)) {
            item = WeaponTestConstant.WEAPON_THRESHOLD.get(pipeTest.getType())
                    .stream().filter((q -> StringUtils.equals(q.getName(), key))).findFirst().orElse(null);
        }else{
            List<PropertyItem<Double>> list = null;
            try {
                list = JsonUtils.deserializeList(threshold, PropertyItem.class);
            } catch (Exception e) {
                return null;
            }
            if(CollectionUtils.isEmpty(list)){
                return null;
            }
            item = list.stream().filter((q -> StringUtils.equals(q.getName(), key))).findFirst().orElse(null);
        }
        if(item == null){
            return null;
        }else {
            return item.getValue();
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
        Map<String, EquipmentStatus> allEquipmentStatus = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(key,EquipmentStatus.class);
        if(allEquipmentStatus == null ){
            return;
        }
        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.AirMissileRadar.getValue())){
            return;
        }
        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.AirMissileFireControl.getValue())){
            return;
        }
        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.AirMissileLauncher.getValue())){
            return;
        }
        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.AirMissileShortRange.getValue())){
            return;
        }
        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.AirMissileMediumRange.getValue())){
            return;
        }
        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.AirMissileLongRange.getValue())){
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
        Double threshold = getThreshold(pipeTest,"pipeTest_cycle_threshold");


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

        Map<String, EquipmentStatus> allEquipmentStatus = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(key,EquipmentStatus.class);
        if(allEquipmentStatus == null ){
            return;
        }

        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.AntiMissileShipGun.getValue())){
            return;
        }
        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.AntiMissileShipGunRadar.getValue())){
            return;
        }
        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.AntiMissileShipGunControl.getValue())){
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
        Double threshold = getThreshold(pipeTest,"pipeTest_cycle_threshold");
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

        Map<String, EquipmentStatus> allEquipmentStatus = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(key,EquipmentStatus.class);
        if(allEquipmentStatus == null ){
            return;
        }

        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.Sonar.getValue())){
            return;
        }
        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.TorpedoFireControl.getValue())){
            return;
        }
        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.TorpedoLauncher.getValue())){
            return;
        }
        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.Torpedo.getValue())){
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
        Double threshold = getThreshold(pipeTest,"pipeTest_cycle_threshold");
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

        Map<String, EquipmentStatus> allEquipmentStatus = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(key,EquipmentStatus.class);
        if(allEquipmentStatus == null ){
            return;
        }
        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.ElectronicDetection.getValue())){
            return;
        }
        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.ElectronicCountermeasure.getValue())){
            return;
        }
        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.MultiUsageLaunch.getValue())){
            return;
        }
        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.OutBoardElectronicCountermeasure.getValue())){
            return;
        }
        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.InBoardElectronicCountermeasure.getValue())){
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
        Double threshold = getThreshold(pipeTest,"pipeTest_cycle_threshold");

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

        Map<String, EquipmentStatus> allEquipmentStatus = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(key,EquipmentStatus.class);
        if(allEquipmentStatus == null ){
            return;
        }
        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.UnderwaterAcousticCountermeasureControl.getValue())){
            return;
        }
        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.UnderwaterAcousticCountermeasure.getValue())){
            return;
        }
        if(! allEquipmentStatus.containsKey(PipeWeaponIndices.MultiUsageLaunch.getValue())){
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
        Double threshold = getThreshold(pipeTest,"pipeTest_cycle_threshold");
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

        String instructionsKey = String.format("%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, DateParserUtils.getTime());
        String targetKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());
        String equipmentKey = String.format("%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, DateParserUtils.getTime());


        StringRedisTemplate template = RedisUtils.getService(config.getPumpDataBase()).getTemplate();
        if (!template.hasKey(instructionsKey) ||
                !template.hasKey(targetKey)
                || !template.hasKey(equipmentKey)) {
            log.error("从Redis中获取信息失败！");
            return;
        }
        Map<String, TargetFireControlInfo> allFireControlInfos = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(
                targetKey,TargetFireControlInfo.class);
        if(allFireControlInfos == null){
            return;
        }
        Double threshold = getThreshold(pipeTest,"progress_time_threshold");
        for (String key2 : allFireControlInfos.keySet()) {
            TargetFireControlInfo targetFireControlInfo = allFireControlInfos.get(key2);

            String keyTarget = String.format("%s_%s:%s", Constant.TARGET_INFO_HTTP_KEY, targetFireControlInfo.getTargetId(), DateParserUtils.getTime());
            String keyEquipment = String.format("%s_%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, targetFireControlInfo.getTargetId(), DateParserUtils.getTime());

            Long n = template.boundListOps(keyTarget).size();

            for (int i = 0; i < n; i++) {

                TargetInstructionsInfo targetInstructionsInfo = JsonUtils.deserialize(template.boundListOps(
                        keyTarget).index(i),
                        TargetInstructionsInfo.class);

                Long m = template.boundListOps(keyEquipment).size();

                for (int j = 0; j < m; j++) {

                    EquipmentLaunchStatus equipmentLaunchStatus = JsonUtils.deserialize(template.boundListOps(
                            keyEquipment).index(j),
                            EquipmentLaunchStatus.class);

                    if (StringUtils.equals(targetFireControlInfo.getTargetId(), targetInstructionsInfo.getTargetId()) &&
                            StringUtils.equals(targetFireControlInfo.getTargetId(), equipmentLaunchStatus.getTargetId())) {

                        InfoProcessTestReport infoProcessTestReport = new InfoProcessTestReport();

                        boolean b = equipmentLaunchStatus.getTime() > targetFireControlInfo.getTime() &&
                                targetFireControlInfo.getTime() > targetInstructionsInfo.getTime();
                        boolean d = equipmentLaunchStatus.getTime() - targetInstructionsInfo.getTime() < threshold;

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
    @Override
    public void threatJudgment(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }

        String instructionsKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());
        String targetKey = String.format("%s:%s", Constant.TARGET_INFO_HTTP_KEY, DateParserUtils.getTime());


        StringRedisTemplate template = RedisUtils.getService(config.getPumpDataBase()).getTemplate();
        if (!template.hasKey(instructionsKey) || !template.hasKey(targetKey)) {
            log.error("从Redis中获取目标指示信息或目标真值信息失败！");
            return;
        }

        Map<String, TargetInstructionsInfo> allInstructionInfos = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(
                instructionsKey,TargetInstructionsInfo.class);
        if(allInstructionInfos == null){
            return;
        }
        Double threshold = getThreshold(pipeTest,"detector_time_threshold");

        for (String key2 : allInstructionInfos.keySet()) {
            TargetInstructionsInfo targetInstructionsInfo = allInstructionInfos.get(key2);
            String key = String.format("%s_%s:%s", Constant.TARGET_INFO_HTTP_KEY, targetInstructionsInfo.getTargetId(), DateParserUtils.getTime());
            Long n = template.boundListOps(key).size();

            for (int i = 0; i < n; i++) {
                TargetInfo targetInfo = JsonUtils.deserialize(template.boundListOps(
                        key).index(i), TargetInfo.class);

                assert targetInfo != null;

                if (StringUtils.equals(targetInfo.getTargetId(), targetInstructionsInfo.getTargetId())) {

                    if (Math.abs(targetInfo.getTime() - targetInstructionsInfo.getTime()) < threshold) {

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
    @Override
    public void indicationAccuracyTest(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }

        String instructionsKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());
        String targetKey = String.format("%s:%s", Constant.TARGET_INFO_HTTP_KEY, DateParserUtils.getTime());

        StringRedisTemplate template = RedisUtils.getService(config.getPumpDataBase()).getTemplate();
        if (!template.hasKey(instructionsKey) || !template.hasKey(targetKey)) {
            log.error("从Redis中获取目标指示信息或目标真值信息失败！");
            return;
        }

        Map<String, TargetInstructionsInfo> allInstructionInfos = RedisUtils.getService(config.getPumpDataBase()).extrasForHash()
                .hgetall(instructionsKey,TargetInstructionsInfo.class);
        if(allInstructionInfos == null){
            return;
        }
        Double threshold = getThreshold(pipeTest,"instruction_time_threshold");
        for (String key2 : allInstructionInfos.keySet()) {
            TargetInstructionsInfo targetInstructionsInfo = allInstructionInfos.get(key2);
            String key = String.format("%s_%s:%s", Constant.TARGET_INFO_HTTP_KEY, targetInstructionsInfo.getTargetId(), DateParserUtils.getTime());
            Long n = template.boundListOps(key).size();

            for (int i = 0; i < n; i++) {
                TargetInfo targetInfo = JsonUtils.deserialize(template.boundListOps(
                        key).index(i), TargetInfo.class);

                assert targetInfo != null;

                if (StringUtils.equals(targetInfo.getTargetId(), targetInstructionsInfo.getTargetId())) {
                    // 指示处理精度测试
                    if (Math.abs(targetInfo.getTime() - targetInstructionsInfo.getTime()) < threshold) {

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
    @Override
    public void executionStatusTest(String taskId, PipeTest pipeTest) {
        if (beStart(taskId, pipeTest)) {
            return;
        }

        String instructionsKey = String.format("%s:%s", Constant.EQUIPMENT_STATUS_HTTP_KEY, DateParserUtils.getTime());
        String targetKey = String.format("%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, DateParserUtils.getTime());
        String equipmentKey = String.format("%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, DateParserUtils.getTime());


        StringRedisTemplate template = RedisUtils.getService(config.getPumpDataBase()).getTemplate();
        if (!template.hasKey(instructionsKey) ||
                !template.hasKey(targetKey)
                || !template.hasKey(equipmentKey)) {
            log.error("从Redis中获取信息失败！");
            return;
        }
        Map<String, TargetFireControlInfo> allFireControlInfos = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(
                targetKey,TargetFireControlInfo.class);

        if(allFireControlInfos == null){
            return;
        }
        Double threshold = getThreshold(pipeTest,"execution_time_threshold");
        for (String key2 : allFireControlInfos.keySet()) {
            TargetFireControlInfo targetFireControlInfo = allFireControlInfos.get(key2);

            String keyTarget = String.format("%s_%s:%s", Constant.TARGET_INFO_HTTP_KEY, targetFireControlInfo.getTargetId(), DateParserUtils.getTime());
            String keyEquipment = String.format("%s_%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, targetFireControlInfo.getTargetId(), DateParserUtils.getTime());

            Long n = template.boundListOps(keyTarget).size();

            for (int i = 0; i < n; i++) {

                TargetInstructionsInfo targetInstructionsInfo = JsonUtils.deserialize(template.boundListOps(
                        keyTarget).index(i),
                        TargetInstructionsInfo.class);

                Long m = template.boundListOps(keyEquipment).size();

                for (int j = 0; j < m; j++) {

                    EquipmentLaunchStatus equipmentLaunchStatus = JsonUtils.deserialize(template.boundListOps(
                            keyEquipment).index(j),
                            EquipmentLaunchStatus.class);

                    if (StringUtils.equals(targetFireControlInfo.getTargetId(), targetInstructionsInfo.getTargetId()) &&
                            StringUtils.equals(targetFireControlInfo.getTargetId(), equipmentLaunchStatus.getTargetId())) {

                        ExecutionStatusReport executionStatusReport = new ExecutionStatusReport();

                        boolean b = equipmentLaunchStatus.getTime() > targetFireControlInfo.getTime() &&
                                targetFireControlInfo.getTime() > targetInstructionsInfo.getTime();
                        boolean c = equipmentLaunchStatus.getTime() - targetInstructionsInfo.getTime() < threshold;

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
    @Override
    public void radarTrackTest(String taskId, PipeTest pipeTest) {


        if (beStart(taskId, pipeTest)) {
            return;
        }
        String instructionsKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());
        String targetKey = String.format("%s:%s", Constant.TARGET_INFO_HTTP_KEY, DateParserUtils.getTime());

        StringRedisTemplate template = RedisUtils.getService(config.getPumpDataBase()).getTemplate();
        if (!template.hasKey(instructionsKey) || !template.hasKey(targetKey)) {
            log.error("从Redis中获取目标指示信息或目标真值信息失败！");
            return;
        }
        Map<String, TargetInstructionsInfo> allInstructionInfos = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(
                instructionsKey,TargetInstructionsInfo.class);
        if(allInstructionInfos == null){
            return;
        }
        Double threshold = getThreshold(pipeTest,"radar_path_time_threshold");
        for (String key2 : allInstructionInfos.keySet()) {
            TargetInstructionsInfo targetInstructionsInfo = allInstructionInfos.get(key2);
            String key = String.format("%s_%s:%s", Constant.TARGET_INFO_HTTP_KEY, targetInstructionsInfo.getTargetId(), DateParserUtils.getTime());
            Long n = template.boundListOps(key).size();

            for (int i = 0; i < n; i++) {
                TargetInfo targetInfo = JsonUtils.deserialize(template.boundListOps(
                        key).index(i), TargetInfo.class);

                assert targetInfo != null;

                if (StringUtils.equals(targetInfo.getTargetId(), targetInstructionsInfo.getTargetId())) {

                    // 雷达航迹测试
                    if (targetInfo.getTime() - targetInstructionsInfo.getTime() > 0
                            && targetInfo.getTime() - targetInstructionsInfo.getTime() < threshold
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
    @Override
    public void interceptDistanceTest(String taskId, PipeTest pipeTest) {
        if (beStart(taskId, pipeTest)) {
            return;
        }

        String key = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());

        Map<String, TargetInstructionsInfo> allInstructions = RedisUtils.getService(config.getPumpDataBase()).extrasForHash()
                .hgetall(key,TargetInstructionsInfo.class);

        if(allInstructions == null){
            return;
        }
        Double threshold = getThreshold(pipeTest,"interception_distance");

        for (TargetInstructionsInfo targetInstructionsInfo : allInstructions.values()) {
            String keyTarget = String.format("%s_%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, targetInstructionsInfo.getTargetId(), DateParserUtils.getTime());


            Long n = RedisUtils.getService(config.getPumpDataBase()).getTemplate().boundListOps(keyTarget).size();

            Long maxTime = 0L;
            Float minDistance = Float.MAX_VALUE;

            for (int i = 0; i < n; i++) {

                String tmpInstruction = RedisUtils.getService(config.getPumpDataBase()).getTemplate().boundListOps(keyTarget).index(i);

                TargetInstructionsInfo targetInstructionsInfo1 = JsonUtils.deserialize(
                        tmpInstruction, TargetInstructionsInfo.class);

                if (targetInstructionsInfo1.getDistance() < threshold) {
                    continue;
                }

                maxTime = targetInstructionsInfo1.getTime() > maxTime ? targetInstructionsInfo1.getTime() : maxTime;

                minDistance = targetInstructionsInfo1.getDistance() < minDistance ?
                        targetInstructionsInfo1.getDistance() : minDistance;


            }

            if (minDistance == Float.MAX_VALUE) {
                continue;
            }

            InterceptDistanceReport interceptDistanceReport = new InterceptDistanceReport();
            interceptDistanceReport.setInterceptDistance(minDistance);
            interceptDistanceReport.setTime(maxTime);
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
    @Override
    public void fireControlTest(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }

        String targetKey = String.format("%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, DateParserUtils.getTime());

        Map<String, TargetFireControlInfo> allFireControlInfos = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(
                targetKey,TargetFireControlInfo.class);
        if(allFireControlInfos == null){
            return;
        }

        Double threshold = getThreshold(pipeTest,"fire_control_time_threshold");
        for (TargetFireControlInfo targetFireControlInfo : allFireControlInfos.values()) {

            String keyTarget = String.format("%s_%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, targetFireControlInfo.getTargetId(), DateParserUtils.getTime());

            Long n = RedisUtils.getService(config.getPumpDataBase()).getTemplate().boundListOps(
                    keyTarget).size();


            for (int i = 0; i < n; i++) {
                TargetInstructionsInfo targetInstructionsInfo = JsonUtils.deserialize(RedisUtils.getService(config.getPumpDataBase()).getTemplate().boundListOps(
                        keyTarget).index(i),
                        TargetInstructionsInfo.class);

                assert targetInstructionsInfo != null;

                if (targetInstructionsInfo.getTargetId().equals(targetFireControlInfo.getTargetId())) {

                    if (Math.abs(targetFireControlInfo.getTime() - targetInstructionsInfo.getTime()) < threshold) {

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
    @Override
    public void reactionTimeTest(String taskId, PipeTest pipeTest) {
        if (beStart(taskId, pipeTest)) {
            return;
        }

        String equipmentKey = String.format("%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, DateParserUtils.getTime());
        String targetKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());


        Map<String, EquipmentLaunchStatus> allEquipmentStatus = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(
                equipmentKey,EquipmentLaunchStatus.class);
        if(allEquipmentStatus == null){
            return;
        }

        for (EquipmentLaunchStatus equipmentLaunchStatus : allEquipmentStatus.values()) {

            String keyTarget = String.format("%s_%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, equipmentLaunchStatus.getTargetId(), DateParserUtils.getTime());

            Long n = RedisUtils.getService(config.getPumpDataBase()).getTemplate().boundListOps(
                    keyTarget).size();

            Long reactionTime = Long.MAX_VALUE;

            TargetInstructionsInfo targetInstructionsInfo = new TargetInstructionsInfo();

            for (int i = 0; i < n; i++) {
                String tmpLaunch = RedisUtils.getService(config.getPumpDataBase()).getTemplate().boundListOps(
                        keyTarget).index(i);


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
    @Override
    public void launcherRotationTest(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }

        String equipmentKey = String.format("%s:%s", Constant.EQUIPMENT_STATUS_HTTP_KEY, DateParserUtils.getTime());
        String targetKey = String.format("%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, DateParserUtils.getTime());


        Map<String, TargetFireControlInfo> allFireControlInfos = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(
                targetKey,TargetFireControlInfo.class);
        if(allFireControlInfos == null){
            return;
        }
        Double threshold = getThreshold(pipeTest,"launcher_rotation_time_threshold");
        for (TargetFireControlInfo targetFireControlInfo : allFireControlInfos.values()) {

            String keyEquipment = String.format("%s_%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, targetFireControlInfo.getTargetId(), DateParserUtils.getTime());


            Long n = RedisUtils.getService(config.getPumpDataBase()).getTemplate().boundListOps(
                    keyEquipment).size();


            for (int i = 0; i < n; i++) {

                EquipmentLaunchStatus equipmentLaunchStatus = JsonUtils.deserialize(RedisUtils.getService(config.getPumpDataBase()).getTemplate().boundListOps(
                        keyEquipment).index(i),
                        EquipmentLaunchStatus.class);

                assert equipmentLaunchStatus != null;

                if (equipmentLaunchStatus.getTargetId().equals(targetFireControlInfo.getTargetId())) {

                    if (equipmentLaunchStatus.getTime() - targetFireControlInfo.getTime() < threshold
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
    @Override
    public void multiTargetInterceptionTest(String taskId, PipeTest pipeTest) {

        if (beStart(taskId, pipeTest)) {
            return;
        }

        String targetKey = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime());


        List<MultiTargetInterceptionReport> finalReport = new ArrayList<>();
        Set<String> targetInstructionInfoIndices = Objects.requireNonNull(
                RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                        targetKey).entries()).keySet();
        Double threshold = getThreshold(pipeTest,"min_interception_distance");
        for (String targetInstructionInfoId : targetInstructionInfoIndices) {

            String targetInstructionInfoHistoryId = String.format("%s_%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, targetInstructionInfoId, DateParserUtils.getTime());
            String targetType = "";

            // 目标最后消失时间
            Long maxDetectTargetTime = Long.MIN_VALUE;
            // 目标最近探测距离
            Float minInterceptionDistance = Float.MAX_VALUE;
            TargetInstructionsInfo targetInstructionsInfo = new TargetInstructionsInfo();

            Long targetInstructionInfoHistoryCnt = Objects.requireNonNull(RedisUtils.getService(
                    config.getPumpDataBase()).boundListOps(targetInstructionInfoHistoryId).size());
            for (int i = 0; i < targetInstructionInfoHistoryCnt; ++i) {
                String tmpInstruction = RedisUtils.getService(config.getPumpDataBase()).getTemplate().boundListOps(targetInstructionInfoHistoryId).index(i);
                TargetInstructionsInfo instruction = JsonUtils.deserialize(tmpInstruction, TargetInstructionsInfo.class);
                maxDetectTargetTime = Math.max(maxDetectTargetTime, instruction.getTime());
                minInterceptionDistance = Math.min(minInterceptionDistance, instruction.getDistance());
                targetType = instruction.getTargetTypeId();
            }

            if (targetInstructionInfoHistoryCnt <= 0 ||
                    maxDetectTargetTime == Long.MAX_VALUE ||
                    minInterceptionDistance == Float.MAX_VALUE ||
                    minInterceptionDistance <= threshold
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
    public boolean meetTestCycleHelper(long[] timeVec, Double pipeTestCycleThreshold) {
        return MathUtils.findMaxByCollections(timeVec) - MathUtils.findMinByCollections(timeVec) <
                pipeTestCycleThreshold * 1000;
    }


    /**
     * 是否开始执行
     * @param taskId
     * @param pipeTest
     * @return
     */
    public Boolean beStart(String taskId, PipeTest pipeTest) {
        return StringUtils.isEmpty(taskId) || pipeTest == null;
    }


    /**
     * 获得水下防御时间节点信息
     * @return 水下防御事件的时间节点列表
     */
    public List<StateAnalysisTimeReport> getUnderWaterDefendInfo(){

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
        String fireKey =String.format("%s:%s",Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, DateParserUtils.getTime());
        if(Boolean.FALSE.equals(template.hasKey(fireKey))){
            log.error("从Redis中获取发射架调转信息失败！");
            return null;
        }
        String launcherKey = String.format("%s:%s", Constant.LAUNCHER_ROTATION_INFO_HTTP_KEY, DateParserUtils.getTime());
        if (Boolean.FALSE.equals(template.hasKey(launcherKey))) {
            log.error("从Redis中获取发射架调转信息失败！");
            return null;
        }

        Map<String,String> allInstructionsInfo = RedisUtils.getService(config.getPumpDataBase())
                .boundHashOps(targetKey).entries();

        if(allInstructionsInfo==null) {
            return null;
        }

        Map<String,TargetInstructionsInfo> targetInstructionsInfoMap = allInstructionsInfo.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        pair->JsonUtils.deserialize(pair.getValue(),TargetInstructionsInfo.class)
                ));

        for(TargetInstructionsInfo targetInstructionsInfo:targetInstructionsInfoMap.values()){

            if(!StringUtils.equals(targetInstructionsInfo.getTargetTypeId(),"水下目标")) {
                continue;
            }
            StateAnalysisTimeReport stateAnalysisTimeReport = new StateAnalysisTimeReport();
            String targetId = targetInstructionsInfo.getTargetId();

            stateAnalysisTimeReport.setId(targetId);
            stateAnalysisTimeReport.setInstructionTime(targetInstructionsInfo.getTime());

            Map<String,String> allFireControlInfo = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                    fireControlKey).entries();
            if(allFireControlInfo==null){
                continue;
            }

            String temp = allFireControlInfo.get(targetId);
            if(temp==null) {
                continue;
            }
            TargetFireControlInfo targetFireControlInfo = JsonUtils.deserialize(
                    temp,
                    TargetFireControlInfo.class);
            if(targetFireControlInfo==null) {
                continue;
            }
            stateAnalysisTimeReport.setFireControlTime(targetFireControlInfo.getTime());

            launcherKey = String.format("%s:%s", Constant.LAUNCHER_ROTATION_INFO_HTTP_KEY + Constant.TARGET_ID, DateParserUtils.getTime());
            Map<String,String> allLauncherRotationInfo = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                    launcherKey).entries();
            if(allLauncherRotationInfo==null) {
                continue;
            }

            temp = allLauncherRotationInfo.get(targetId);
            if(temp==null) {
                continue;
            }

            LauncherRotationInfo launcherRotationInfo = JsonUtils.deserialize(
                    temp,
                    LauncherRotationInfo.class);
            if(launcherRotationInfo==null) {
                continue;
            }

            stateAnalysisTimeReport.setLauncherRotationTime(launcherRotationInfo.getTime());

            fireKey =String.format("%s:%s",Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY + Constant.TARGET_ID, DateParserUtils.getTime());
            Map<String,String> allEquipmentLaunchStatus = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                    fireKey).entries();
            if(allEquipmentLaunchStatus==null) {
                continue;
            }

            temp = allEquipmentLaunchStatus.get(targetId);
            if(temp==null) {
                continue;
            }

            EquipmentLaunchStatus equipmentLaunchStatus = JsonUtils.deserialize(
                    temp,
                    EquipmentLaunchStatus.class);
            if(equipmentLaunchStatus==null) {
                continue;
            }

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
        String fireKey =String.format("%s:%s",Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, DateParserUtils.getTime());
        if(Boolean.FALSE.equals(template.hasKey(fireKey))){
            log.error("从Redis中获取武器发射信息失败！");
            return null;
        }
        String launcherKey = String.format("%s:%s", Constant.LAUNCHER_ROTATION_INFO_HTTP_KEY, DateParserUtils.getTime());
        if (Boolean.FALSE.equals(template.hasKey(launcherKey))) {
            log.error("从Redis中获取发射架调转信息失败！");
            return null;
        }

        Map<String,String> allInstructionsInfo = RedisUtils.getService(config.getPumpDataBase())
                .boundHashOps(targetKey).entries();

        if(allInstructionsInfo==null) {
            return null;
        }

        Map<String,TargetInstructionsInfo> targetInstructionsInfoMap = allInstructionsInfo.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        pair->JsonUtils.deserialize(pair.getValue(),TargetInstructionsInfo.class)
                ));

        for(TargetInstructionsInfo targetInstructionsInfo:targetInstructionsInfoMap.values()){

            if(!StringUtils.equals(targetInstructionsInfo.getTargetTypeId(),"对空目标")){
                continue;
            }
            StateAnalysisTimeReport stateAnalysisTimeReport = new StateAnalysisTimeReport();
            String targetId = targetInstructionsInfo.getTargetId();

            stateAnalysisTimeReport.setId(targetId);
            stateAnalysisTimeReport.setInstructionTime(targetInstructionsInfo.getTime());

            Map<String,String> allFireControlInfo = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                    fireControlKey).entries();
            if(allFireControlInfo==null) {
                continue;
            }

            String temp = allFireControlInfo.get(targetId);
            if(temp==null) {
                continue;
            }
            TargetFireControlInfo targetFireControlInfo = JsonUtils.deserialize(
                    temp,
                    TargetFireControlInfo.class);
            if(targetFireControlInfo==null) {
                continue;
            }
            stateAnalysisTimeReport.setFireControlTime(targetFireControlInfo.getTime());

            launcherKey = String.format("%s:%s", Constant.LAUNCHER_ROTATION_INFO_HTTP_KEY + Constant.TARGET_ID, DateParserUtils.getTime());
            Map<String,String> allLauncherRotationInfo = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                    launcherKey).entries();
            if(allLauncherRotationInfo==null) {
                continue;
            }

            temp = allLauncherRotationInfo.get(targetId);
            if(temp==null) {
                continue;
            }

            LauncherRotationInfo launcherRotationInfo = JsonUtils.deserialize(
                    temp,
                    LauncherRotationInfo.class);
            if(launcherRotationInfo==null) {
                continue;
            }

            stateAnalysisTimeReport.setLauncherRotationTime(launcherRotationInfo.getTime());

            fireKey =String.format("%s:%s",Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY + Constant.TARGET_ID, DateParserUtils.getTime());
            Map<String,String> allEquipmentLaunchStatus = RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                    fireKey).entries();
            if(allEquipmentLaunchStatus==null) {
                continue;
            }

            temp = allEquipmentLaunchStatus.get(targetId);
            if(temp==null) {
                continue;
            }

            EquipmentLaunchStatus equipmentLaunchStatus = JsonUtils.deserialize(
                    temp,
                    EquipmentLaunchStatus.class);
            if(equipmentLaunchStatus==null) {
                continue;
            }

            stateAnalysisTimeReport.setFireTime(equipmentLaunchStatus.getTime());

            stateAnalysisTimeReportList.add(stateAnalysisTimeReport);
        }
        return stateAnalysisTimeReportList;
    }
}
