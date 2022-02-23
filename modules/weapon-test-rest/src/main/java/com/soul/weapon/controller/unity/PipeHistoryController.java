package com.soul.weapon.controller.unity;

import com.egova.web.annotation.Api;
import com.soul.weapon.condition.AntiMissileShipGunTestReportCondition;
import com.soul.weapon.condition.ElectronicWeaponTestReportCondition;
import com.soul.weapon.condition.ExecutionStatusReportCondition;
import com.soul.weapon.condition.InfoProcessTestReportCondition;
import com.soul.weapon.entity.PipeHistory;
import com.soul.weapon.entity.historyInfo.AntiMissileShipGunTestReport;
import com.soul.weapon.entity.historyInfo.ElectronicWeaponTestReport;
import com.soul.weapon.entity.historyInfo.ExecutionStatusReport;
import com.soul.weapon.entity.historyInfo.InfoProcessTestReport;
import com.soul.weapon.service.AntiMissileShipGunTestReportService;
import com.soul.weapon.service.ElectronicWeaponTestReportService;
import com.soul.weapon.service.ExecutionStatusReportService;
import com.soul.weapon.service.InfoProcessTestReportService;
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
    private final AntiMissileShipGunTestReportService antiMissileShipGunTestReportService;

    @Autowired
    private final ElectronicWeaponTestReportService electronicWeaponTestReportService;

    @Autowired
    private final ExecutionStatusReportService executionStatusReportService;

    @Autowired
    private final InfoProcessTestReportService infoProcessTestReportService;


    @Api
    @GetMapping(value = "/anti/{taskId}")
    public List<AntiMissileShipGunTestReport> getAnTiByTaskId(@PathVariable("taskId") String taskId)
    {
        AntiMissileShipGunTestReportCondition condition = new AntiMissileShipGunTestReportCondition();
        condition.setTaskId(taskId);
        return  antiMissileShipGunTestReportService.list(condition);
    }

    @Api
    @GetMapping(value = "/electWeapon/{taskId}")
    public List<ElectronicWeaponTestReport> getElectWeaponByTaskId(@PathVariable("taskId") String taskId)
    {
        ElectronicWeaponTestReportCondition condition = new ElectronicWeaponTestReportCondition();
        condition.setTaskId(taskId);
        return  electronicWeaponTestReportService.list(condition);
    }

    @Api
    @GetMapping(value = "/execStatus/{taskId}")
    public List<ExecutionStatusReport> getExecStatusByTaskId(@PathVariable("taskId") String taskId)
    {
        ExecutionStatusReportCondition condition = new ExecutionStatusReportCondition();
        condition.setTaskId(taskId);
        return  executionStatusReportService.list(condition);
    }


    //获取信息流程测试历史报告
    @Api
    @GetMapping(value = "/infoProcessTest/{taskId}")
    public List<InfoProcessTestReport> getInfoProcessTest(@PathVariable("taskId") String taskId){
        InfoProcessTestReportCondition condition = new InfoProcessTestReportCondition();
        condition.setTaskId(taskId);
        return infoProcessTestReportService.list(condition);
    }



}
