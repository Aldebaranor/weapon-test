package com.soul.weapon.controller.unity;

import com.egova.model.annotation.Display;
import com.egova.utils.ExcelUtils;
import com.egova.web.annotation.Api;
import com.soul.weapon.condition.*;
import com.soul.weapon.entity.historyInfo.*;
import com.soul.weapon.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
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
    public List<ShipToAirMissileTestReport> getShipByTaskId(@PathVariable("taskId") String taskId) {
        ShipToAirMissileTestReportCondition condition = new ShipToAirMissileTestReportCondition();
        condition.setTaskId(taskId);

        return shipToAirMissileTestReportService.list(condition);
    }

    @Api
    @GetMapping("/ship/down/{taskId}")
    public void ShipDown(@PathVariable("taskId") String taskId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        ShipToAirMissileTestReportCondition condition = new ShipToAirMissileTestReportCondition();
        condition.setTaskId(taskId);
        List<ShipToAirMissileTestReport> list = shipToAirMissileTestReportService.list(condition);
        getWorkbook(list,"舰空导弹武器通道测试",httpRequest,httpResponse);
    }

    //反导舰炮武器通道测试-2
    @Api
    @GetMapping(value = "/anti/{taskId}")
    public List<AntiMissileShipGunTestReport> getAnTiByTaskId(@PathVariable("taskId") String taskId) {
        AntiMissileShipGunTestReportCondition condition = new AntiMissileShipGunTestReportCondition();
        condition.setTaskId(taskId);
        return antiMissileShipGunTestReportService.list(condition);
    }

    @Api
    @GetMapping("/anti/down/{taskId}")
    public void AntiDown(@PathVariable("taskId") String taskId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        AntiMissileShipGunTestReportCondition condition = new AntiMissileShipGunTestReportCondition();
        condition.setTaskId(taskId);
        List<AntiMissileShipGunTestReport> list = antiMissileShipGunTestReportService.list(condition);
        getWorkbook(list,"反导舰炮武器通道测试",httpRequest,httpResponse);
    }

    //鱼雷防御武器通道测试-3
    @Api
    @GetMapping(value = "/torpe/{taskId}")
    public List<TorpedoTestReport> getTorpeByTaskId(@PathVariable("taskId") String taskId) {
        TorpedoTestReportCondition condition = new TorpedoTestReportCondition();
        condition.setTaskId(taskId);
        return torpedoTestReportService.list(condition);
    }

    @Api
    @GetMapping("/torpe/down/{taskId}")
    public void TorpeDown(@PathVariable("taskId") String taskId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        TorpedoTestReportCondition condition = new TorpedoTestReportCondition();
        condition.setTaskId(taskId);
        List<TorpedoTestReport> list = torpedoTestReportService.list(condition);
        getWorkbook(list,"鱼雷防御武器通道测试",httpRequest,httpResponse);
    }

    //电子对抗武器通道测试-4
    @Api
    @GetMapping(value = "/electWeapon/{taskId}")
    public List<ElectronicWeaponTestReport> getElectWeaponByTaskId(@PathVariable("taskId") String taskId) {
        ElectronicWeaponTestReportCondition condition = new ElectronicWeaponTestReportCondition();
        condition.setTaskId(taskId);
        return electronicWeaponTestReportService.list(condition);
    }
    @Api
    @GetMapping("/electWeapon/down/{taskId}")
    public void ElectWeaponDown(@PathVariable("taskId") String taskId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        ElectronicWeaponTestReportCondition condition = new ElectronicWeaponTestReportCondition();
        condition.setTaskId(taskId);
        List<ElectronicWeaponTestReport> list = electronicWeaponTestReportService.list(condition);
        getWorkbook(list,"电子对抗武器通道测试",httpRequest,httpResponse);
    }


    //水声对抗武器通道测试-5
    @Api
    @GetMapping(value = "/waterWeapon/{taskId}")
    public List<WaterWeaponTestReport> getWaterWeaponByTaskId(@PathVariable("taskId") String taskId) {
        WaterWeaponTestReportCondition condition = new WaterWeaponTestReportCondition();
        condition.setTaskId(taskId);
        return waterWeaponTestReportService.list(condition);
    }
    @Api
    @GetMapping("/waterWeapon/down/{taskId}")
    public void WaterWeaponDown(@PathVariable("taskId") String taskId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        WaterWeaponTestReportCondition condition = new WaterWeaponTestReportCondition();
        condition.setTaskId(taskId);
        List<WaterWeaponTestReport> list = waterWeaponTestReportService.list(condition);
        getWorkbook(list,"水声对抗武器通道测试",httpRequest,httpResponse);
    }

    //信息流程测试-6
    @Api
    @GetMapping(value = "/infoProcessTest/{taskId}")
    public List<InfoProcessTestReport> getInfoProcessTest(@PathVariable("taskId") String taskId) {
        InfoProcessTestReportCondition condition = new InfoProcessTestReportCondition();
        condition.setTaskId(taskId);
        return infoProcessTestReportService.list(condition);
    }
    @Api
    @GetMapping("/infoProcessTest/down/{taskId}")
    public void InfoProcessTestDown(@PathVariable("taskId") String taskId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        InfoProcessTestReportCondition condition = new InfoProcessTestReportCondition();
        condition.setTaskId(taskId);
        List<InfoProcessTestReport> list = infoProcessTestReportService.list(condition);
        getWorkbook(list,"信息流程测试",httpRequest,httpResponse);
    }

    //威胁判断测试-7
    @Api
    @GetMapping(value = "/threaten/{taskId}")
    public List<ThreatenReport> getThreatenTest(@PathVariable("taskId") String taskId) {
        ThreatenReportCondition condition = new ThreatenReportCondition();
        condition.setTaskId(taskId);
        return threatenReportService.list(condition);
    }
    @Api
    @GetMapping("/threaten/down/{taskId}")
    public void ThreatenDown(@PathVariable("taskId") String taskId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        ThreatenReportCondition condition = new ThreatenReportCondition();
        condition.setTaskId(taskId);
        List<ThreatenReport> list = threatenReportService.list(condition);
        getWorkbook(list,"威胁判断测试",httpRequest,httpResponse);
    }

    //指示处理精度测试-8
    @Api
    @GetMapping(value = "/insAcc/{taskId}")
    public List<InstructionAccuracyReport> getInsAccTest(@PathVariable("taskId") String taskId) {
        InstructionAccuracyReportCondition condition = new InstructionAccuracyReportCondition();
        condition.setTaskId(taskId);
        return instructionAccuracyReportService.list(condition);
    }
    @Api
    @GetMapping("/insAcc/down/{taskId}")
    public void InsAccDown(@PathVariable("taskId") String taskId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        InstructionAccuracyReportCondition condition = new InstructionAccuracyReportCondition();
        condition.setTaskId(taskId);
        List<InstructionAccuracyReport> list = instructionAccuracyReportService.list(condition);
        getWorkbook(list,"指示处理精度测试",httpRequest,httpResponse);
    }

    //执行情况测试-9
    @Api
    @GetMapping(value = "/execStatus/{taskId}")
    public List<ExecutionStatusReport> getExecStatusByTaskId(@PathVariable("taskId") String taskId) {
        ExecutionStatusReportCondition condition = new ExecutionStatusReportCondition();
        condition.setTaskId(taskId);
        return executionStatusReportService.list(condition);
    }
    @Api
    @GetMapping("/execStatus/down/{taskId}")
    public void ExecStatusDown(@PathVariable("taskId") String taskId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        ExecutionStatusReportCondition condition = new ExecutionStatusReportCondition();
        condition.setTaskId(taskId);
        List<ExecutionStatusReport> list = executionStatusReportService.list(condition);
        getWorkbook(list,"执行情况测试",httpRequest,httpResponse);
    }

    //雷达航迹测试-10
    @Api
    @GetMapping(value = "/radarPath/{taskId}")
    public List<RadarPathReport> getRadarPathByTaskId(@PathVariable("taskId") String taskId) {
        RadarPathReportCondition condition = new RadarPathReportCondition();
        condition.setTaskId(taskId);
        return radarPathReportService.list(condition);
    }
    @Api
    @GetMapping("/radarPath/down/{taskId}")
    public void RadarPathDown(@PathVariable("taskId") String taskId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        RadarPathReportCondition condition = new RadarPathReportCondition();
        condition.setTaskId(taskId);
        List<RadarPathReport> list = radarPathReportService.list(condition);
        getWorkbook(list,"雷达航迹测试",httpRequest,httpResponse);
    }

    //拦截距离测试-11
    @Api
    @GetMapping(value = "/intDis/{taskId}")
    public List<InterceptDistanceReport> getInterceptDistanceByTaskId(@PathVariable("taskId") String taskId) {
        InterceptDistanceReportCondition condition = new InterceptDistanceReportCondition();
        condition.setTaskId(taskId);
        return interceptDistanceReportService.list(condition);
    }
    @Api
    @GetMapping("/intDis/down/{taskId}")
    public void IntDisDown(@PathVariable("taskId") String taskId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        InterceptDistanceReportCondition condition = new InterceptDistanceReportCondition();
        condition.setTaskId(taskId);
        List<InterceptDistanceReport> list = interceptDistanceReportService.list(condition);
        getWorkbook(list,"拦截距离测试",httpRequest,httpResponse);
    }

    //火控解算精度测试-12
    @Api
    @GetMapping(value = "/fireControl/{taskId}")
    public List<FireControlReport> getFireControlByTaskId(@PathVariable("taskId") String taskId) {
        FireControlReportCondition condition = new FireControlReportCondition();
        condition.setTaskId(taskId);
        return fireControlReportService.list(condition);
    }
    @Api
    @GetMapping("/fireControl/down/{taskId}")
    public void FireControlDown(@PathVariable("taskId") String taskId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        FireControlReportCondition condition = new FireControlReportCondition();
        condition.setTaskId(taskId);
        List<FireControlReport> list = fireControlReportService.list(condition);
        getWorkbook(list,"拦截距离测试",httpRequest,httpResponse);
    }

    //反应时间测试-13
    @Api
    @GetMapping(value = "/reaction/{taskId}")
    public List<ReactionReport> getReactionByTaskId(@PathVariable("taskId") String taskId) {
        ReactionReportCondition condition = new ReactionReportCondition();
        condition.setTaskId(taskId);
        return reactionReportService.list(condition);
    }
    @Api
    @GetMapping("/reaction/down/{taskId}")
    public void ReactionDown(@PathVariable("taskId") String taskId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        ReactionReportCondition condition = new ReactionReportCondition();
        condition.setTaskId(taskId);
        List<ReactionReport> list = reactionReportService.list(condition);
        getWorkbook(list,"反应时间测试",httpRequest,httpResponse);
    }

    //发射架调转精度测试-14
    @Api
    @GetMapping(value = "/lauRot/{taskId}")
    public List<LauncherRotationReport> getLauncherRotationByTaskId(@PathVariable("taskId") String taskId) {
        LauncherRotationReportCondition condition = new LauncherRotationReportCondition();
        condition.setTaskId(taskId);
        return launcherRotationReportService.list(condition);
    }
    @Api
    @GetMapping("/lauRot/down/{taskId}")
    public void LauRotDown(@PathVariable("taskId") String taskId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        LauncherRotationReportCondition condition = new LauncherRotationReportCondition();
        condition.setTaskId(taskId);
        List<LauncherRotationReport> list = launcherRotationReportService.list(condition);
        getWorkbook(list,"发射架调转精度测试",httpRequest,httpResponse);
    }

    //多目标拦截能力测试-15
    @Api
    @GetMapping(value = "/mit/{taskId}")
    public List<MultiTargetInterceptionReport> getMultiTargetInterceptionByTaskId(@PathVariable("taskId") String taskId) {
        MultiTargetInterceptionReportCondition condition = new MultiTargetInterceptionReportCondition();
        condition.setTaskId(taskId);
        return multiTargetInterceptionReportService.list(condition);
    }
    @Api
    @GetMapping("/mit/down/{taskId}")
    public void MitDown(@PathVariable("taskId") String taskId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        MultiTargetInterceptionReportCondition condition = new MultiTargetInterceptionReportCondition();
        condition.setTaskId(taskId);
        List<MultiTargetInterceptionReport> list = multiTargetInterceptionReportService.list(condition);
        getWorkbook(list,"发射架调转精度测试",httpRequest,httpResponse);
    }


    //下载Excel
    public <T> void getWorkbook(List<T> list, String fileName,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(fileName);
        sheet.setDefaultColumnWidth(15);
        HSSFCellStyle style = ExcelUtils.getCellStyle(workbook);
        HSSFFont font = ExcelUtils.getFont(workbook);
        style.setFont(font);
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 300);
        HSSFCell cell = null;

        for (int i = 0; i < list.size()+1; i++) {

            T t = null;

            if (i == 0) {
                row = sheet.createRow(i);
                t = list.get(i);
            }else {
                row = sheet.createRow(i);
                t = list.get(i-1);
            }

            Field[] fields = t.getClass().getDeclaredFields();

            try {
                for (int j = 0; j < fields.length; j++) {

                    if (i == 0) {
                        Field field = fields[j];
                        if (field.isAnnotationPresent(Display.class)) {
                            cell = row.createCell(j-1);
                            cell.setCellStyle(style);
                            Display annotation = field.getAnnotation(Display.class);
                            HSSFRichTextString text = new HSSFRichTextString(annotation.value());
                            cell.setCellValue(text);
                        }

                    }else {

                        Field field = fields[j];
                        if (field.isAnnotationPresent(Column.class)) {
                            cell = row.createCell(j-1);
                            String fieldName = field.getName();
                            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                            Method getMethod = t.getClass().getMethod(methodName);
                            Object value = getMethod.invoke(t);
                            if (null == value) {
                                value = "";
                            }
                            cell.setCellValue(value.toString());
                        }

                    }
                }
            } catch (Exception var20) {
                log.error(var20.toString());
            }
        }


        String strFileSuffix = ".xls";
        httpServletResponse.resetBuffer();
        httpServletResponse.setContentType("application/octet-stream");

        try {
            if (ExcelUtils.isIE(httpServletRequest)) {
                fileName = URLEncoder.encode(fileName, "UTF8");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            }
        } catch (UnsupportedEncodingException var7) {
            log.error("【ExcelUtils】文件名编码异常", var7);
        }

        try {
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName + strFileSuffix);
            workbook.write(httpServletResponse.getOutputStream());
            httpServletResponse.flushBuffer();
        } catch (IOException var6) {
            log.error("【ExcelUtils】生成Excel输出流异常", var6);
        }
    }


}
