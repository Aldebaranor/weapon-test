package com.soul.weapon.controller.unity;

import com.egova.web.annotation.Api;
import com.soul.weapon.condition.*;
import com.soul.weapon.entity.PipeHistory;
import com.soul.weapon.entity.historyInfo.*;
import com.soul.weapon.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
@Slf4j
@RestController
@RequestMapping("/unity/history")
@RequiredArgsConstructor
public class PipeHistoryController {

    @Autowired
    private final ShipToAirMissileTestReportService shipToAirMissileTestReportService;
    @Autowired
    private final AntiMissileShipGunTestReportService antiMissileShipGunTestReportService;
    @Autowired
    private final TorpedoTestReportService torpedoTestReportService;
    @Autowired
    private final ElectronicWeaponTestReportService electronicWeaponTestReportService;
    @Autowired
    private final WaterWeaponTestReportService waterWeaponTestReportService;
    @Autowired
    private final InfoProcessTestReportService infoProcessTestReportService;
    @Autowired
    private final ThreatenReportService threatenReportService;
    @Autowired
    private final InstructionAccuracyReportService instructionAccuracyReportService;
    @Autowired
    private final ExecutionStatusReportService executionStatusReportService;
    @Autowired
    private final RadarPathReportService radarPathReportService;
    @Autowired
    private final InterceptDistanceReportService interceptDistanceReportService;
    @Autowired
    private final FireControlReportService fireControlReportService;
    @Autowired
    private final ReactionReportService reactionReportService;
    @Autowired
    private final LauncherRotationReportService launcherRotationReportService;
    @Autowired
    private final MultiTargetInterceptionReportService multiTargetInterceptionReportService;


    //舰空导弹武器通道测试-1
    @Api
    @GetMapping(value = "/ship/{taskId}")
    public List<ShipToAirMissileTestReport> getShipByTaskId(@PathVariable("taskId") String taskId)
    {
        ShipToAirMissileTestReportCondition condition = new ShipToAirMissileTestReportCondition();
        condition.setTaskId(taskId);
        return  shipToAirMissileTestReportService.list(condition);
    }
    //反导舰炮武器通道测试-2
    @Api
    @GetMapping(value = "/anti/{taskId}")
    public List<AntiMissileShipGunTestReport> getAnTiByTaskId(@PathVariable("taskId") String taskId)
    {
        AntiMissileShipGunTestReportCondition condition = new AntiMissileShipGunTestReportCondition();
        condition.setTaskId(taskId);
        return  antiMissileShipGunTestReportService.list(condition);
    }
    //鱼雷防御武器通道测试-3
    @Api
    @GetMapping(value = "/torpe/{taskId}")
    public List<TorpedoTestReport> getTorpeByTaskId(@PathVariable("taskId") String taskId)
    {
        TorpedoTestReportCondition condition = new TorpedoTestReportCondition();
        condition.setTaskId(taskId);
        return  torpedoTestReportService.list(condition);
    }
    //电子对抗武器通道测试-4
    @Api
    @GetMapping(value = "/electWeapon/{taskId}")
    public List<ElectronicWeaponTestReport> getElectWeaponByTaskId(@PathVariable("taskId") String taskId)
    {
        ElectronicWeaponTestReportCondition condition = new ElectronicWeaponTestReportCondition();
        condition.setTaskId(taskId);
        return  electronicWeaponTestReportService.list(condition);
    }

    //水声对抗武器通道测试-5
    @Api
    @GetMapping(value = "/waterWeapon/{taskId}")
    public List<WaterWeaponTestReport> getWaterWeaponByTaskId(@PathVariable("taskId") String taskId)
    {
        WaterWeaponTestReportCondition condition = new WaterWeaponTestReportCondition();
        condition.setTaskId(taskId);
        return  waterWeaponTestReportService.list(condition);
    }

    //信息流程测试-6
    @Api
    @GetMapping(value = "/infoProcessTest/{taskId}")
    public List<InfoProcessTestReport> getInfoProcessTest(@PathVariable("taskId") String taskId){
        InfoProcessTestReportCondition condition = new InfoProcessTestReportCondition();
        condition.setTaskId(taskId);
        return infoProcessTestReportService.list(condition);
    }

    //威胁判断测试-7
    @Api
    @GetMapping(value = "/threaten/{taskId}")
    public List<ThreatenReport> getThreatenTest(@PathVariable("taskId") String taskId){
        ThreatenReportCondition condition = new ThreatenReportCondition();
        condition.setTaskId(taskId);
        return threatenReportService.list(condition);
    }

    //指示处理精度测试-8
    @Api
    @GetMapping(value = "/insAcc/{taskId}")
    public List<InstructionAccuracyReport> getInsAccTest(@PathVariable("taskId") String taskId){
        InstructionAccuracyReportCondition condition = new InstructionAccuracyReportCondition();
        condition.setTaskId(taskId);
        return instructionAccuracyReportService.list(condition);
    }

    //执行情况测试-9
    @Api
    @GetMapping(value = "/execStatus/{taskId}")
    public List<ExecutionStatusReport> getExecStatusByTaskId(@PathVariable("taskId") String taskId)
    {
        ExecutionStatusReportCondition condition = new ExecutionStatusReportCondition();
        condition.setTaskId(taskId);
        return  executionStatusReportService.list(condition);
    }

    //雷达航迹测试-10
    @Api
    @GetMapping(value = "/radarPath/{taskId}")
    public List<RadarPathReport> getRadarPathByTaskId(@PathVariable("taskId") String taskId)
    {
        RadarPathReportCondition condition = new RadarPathReportCondition();
        condition.setTaskId(taskId);
        return  radarPathReportService.list(condition);
    }

    //拦截距离测试-11
    @Api
    @GetMapping(value = "/intDis/{taskId}")
    public List<InterceptDistanceReport> getInterceptDistanceByTaskId(@PathVariable("taskId") String taskId)
    {
        InterceptDistanceReportCondition condition = new InterceptDistanceReportCondition();
        condition.setTaskId(taskId);
        return  interceptDistanceReportService.list(condition);
    }

    //火控解算精度测试-12
    @Api
    @GetMapping(value = "/fireControl/{taskId}")
    public List<FireControlReport> getFireControlByTaskId(@PathVariable("taskId") String taskId)
    {
        FireControlReportCondition condition = new FireControlReportCondition();
        condition.setTaskId(taskId);
        return  fireControlReportService.list(condition);
    }

    //反应时间测试-13
    @Api
    @GetMapping(value = "/reaction/{taskId}")
    public List<ReactionReport> getReactionByTaskId(@PathVariable("taskId") String taskId)
    {
        ReactionReportCondition condition = new ReactionReportCondition();
        condition.setTaskId(taskId);
        return  reactionReportService.list(condition);
    }

    //发射架调转精度测试-14
    @Api
    @GetMapping(value = "/lauRot/{taskId}")
    public List<LauncherRotationReport> getLauncherRotationByTaskId(@PathVariable("taskId") String taskId)
    {
        LauncherRotationReportCondition condition = new LauncherRotationReportCondition();
        condition.setTaskId(taskId);
        return  launcherRotationReportService.list(condition);
    }

    //多目标拦截能力测试-15
    @Api
    @GetMapping(value = "/mit/{taskId}")
    public List<MultiTargetInterceptionReport> getMultiTargetInterceptionByTaskId(@PathVariable("taskId") String taskId)
    {
        MultiTargetInterceptionReportCondition condition = new MultiTargetInterceptionReportCondition();
        condition.setTaskId(taskId);
        return  multiTargetInterceptionReportService.list(condition);
    }


}
