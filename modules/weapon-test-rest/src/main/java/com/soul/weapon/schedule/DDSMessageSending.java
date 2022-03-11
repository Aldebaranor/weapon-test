package com.soul.weapon.schedule;

import com.google.common.collect.Lists;
import com.soul.weapon.controller.free.PumpController;
import com.soul.weapon.model.dds.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

/**
 * @ClassName DDSMessageSending
 * @Description 定时发送dds报文
 * @Author ShiZuan
 * @Date 2022/3/11 10:56
 * @Version
 **/

@Slf4j
@Component
public class DDSMessageSending {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * @Author: Shizuan
     * @Date: 2022/3/11 11:00
     * @Description: 每隔1s发送一次报文
     * @params:[]
     * @return:void
     **/
    @Scheduled(fixedDelay = 1000)
    public void execute(){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Random random = new Random();

        if (PumpController.start) {

            //武器状态
            EquipmentStatus equipmentStatus = new EquipmentStatus();
            //武器发射
            EquipmentLaunchStatus equipmentLaunchStatus = new EquipmentLaunchStatus();
            //发射架调转
            LauncherRotationInfo launcherRotationInfo = new LauncherRotationInfo();
            //火控装置
            TargetFireControlInfo targetFireControlInfo = new TargetFireControlInfo();
            //目标指示
            TargetInstructionsInfo targetInstructionsInfo = new TargetInstructionsInfo();
            //目标信息
            TargetInfo targetInfo = new TargetInfo();
            //战场环境
            EnvironmentInfo environmentInfo = new EnvironmentInfo();
            //作战方案
            CombatScenariosInfo combatScenariosInfo = new CombatScenariosInfo();

            //作战方案报文暂不发送
/*            combatScenariosInfo.setSender("");
            combatScenariosInfo.setMsgTime(0L);
            combatScenariosInfo.setTime(0L);
            combatScenariosInfo.setScenarios("[{\"equipmentId\":\"1\",\"equipmentTypeId\":\"1\",\"equipmentMode\":\"1\",\"beginTime\":1636581,\"duration\":5,\"launchAzimuth\":0.1,\"launchPitchAngle\":0.1,\"electromagneticFrequency\":0.1,\"minHydroacousticFrequency\":0.1,\"maxHydroacousticFrequency\":0.1}]");
            combatScenariosInfo.setScenariosList(Lists.newArrayList());
            HttpEntity<Object> combatScenariosInfoRequest = new HttpEntity<>(combatScenariosInfo, headers);

            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", combatScenariosInfoRequest, String.class);*/
            //----------------------------舰空导弹武器通道测试---------------------------
            //武器状态报文发送1
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("1");
            equipmentStatus.setEquipmentTypeId("3");
            equipmentStatus.setEquipmentMode("舰载雷达传感器");
            //设置武器自检状态
            equipmentStatus.setCheckStatus(true);
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //武器状态报文发送2
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("2");
            equipmentStatus.setEquipmentTypeId("1");
            equipmentStatus.setEquipmentMode("舰空导弹火控装置");
            //设置武器自检状态
            equipmentStatus.setCheckStatus(true);
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //武器状态报文发送3
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("3");
            equipmentStatus.setEquipmentTypeId("1");
            equipmentStatus.setEquipmentMode("舰空导弹发射装置");
            //设置武器自检状态
            equipmentStatus.setCheckStatus(true);
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //武器状态报文发送4
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("4");
            equipmentStatus.setEquipmentTypeId("1");
            equipmentStatus.setEquipmentMode("近程防空导弹");
            //设置武器自检状态
            equipmentStatus.setCheckStatus(true);
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //武器状态报文发送5
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("5");
            equipmentStatus.setEquipmentTypeId("1");
            equipmentStatus.setEquipmentMode("中程防空导弹");
            //设置武器自检状态
            equipmentStatus.setCheckStatus(true);
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //武器状态报文发送6
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("6");
            equipmentStatus.setEquipmentTypeId("1");
            equipmentStatus.setEquipmentMode("远程防空导弹");
            //设置武器自检状态(随机生成远程防空导弹自检状态)
            equipmentStatus.setCheckStatus(random.nextBoolean());
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //----------------------------反导舰炮武器通道测试---------------------------
            //武器状态报文发送7
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("11");
            equipmentStatus.setEquipmentTypeId("3");
            equipmentStatus.setEquipmentMode("反导舰炮跟踪雷达");
            //设置武器自检状态
            equipmentStatus.setCheckStatus(true);
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //武器状态报文发送8
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("12");
            equipmentStatus.setEquipmentTypeId("1");
            equipmentStatus.setEquipmentMode("反导舰炮火控装置");
            //设置武器自检状态
            equipmentStatus.setCheckStatus(true);
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //武器状态报文发送9
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("13");
            equipmentStatus.setEquipmentTypeId("1");
            equipmentStatus.setEquipmentMode("反导舰炮");
            //设置武器自检状态(获取随机的自检)
            equipmentStatus.setCheckStatus(random.nextBoolean());
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //----------------------------鱼雷防御武器通道测试---------------------------
            //武器状态报文发送10
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("15");
            equipmentStatus.setEquipmentTypeId("3");
            equipmentStatus.setEquipmentMode("舰壳声纳");
            //设置武器自检状态
            equipmentStatus.setCheckStatus(true);
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //武器状态报文发送11
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("17");
            equipmentStatus.setEquipmentTypeId("1");
            equipmentStatus.setEquipmentMode("鱼雷防御武器火控装置");
            //设置武器自检状态
            equipmentStatus.setCheckStatus(true);
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //武器状态报文发送12
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("18");
            equipmentStatus.setEquipmentTypeId("1");
            equipmentStatus.setEquipmentMode("鱼雷防御武器发射装置");
            //设置武器自检状态
            equipmentStatus.setCheckStatus(true);
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //武器状态报文发送13
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("19");
            equipmentStatus.setEquipmentTypeId("1");
            equipmentStatus.setEquipmentMode("鱼雷防御武器");
            //设置武器自检状态(随机自检)
            equipmentStatus.setCheckStatus(random.nextBoolean());
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //----------------------------电子对抗武器通道测试---------------------------
            //武器状态报文发送14
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("20");
            equipmentStatus.setEquipmentTypeId("2");
            equipmentStatus.setEquipmentMode("电子侦察设备");
            //设置武器自检状态
            equipmentStatus.setCheckStatus(true);
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //----------------------------电子对抗武器通道测试---------------------------
            //武器状态报文发送15
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("20");
            equipmentStatus.setEquipmentTypeId("2");
            equipmentStatus.setEquipmentMode("电子侦察设备");
            //设置武器自检状态
            equipmentStatus.setCheckStatus(true);
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //武器状态报文发送16
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("21");
            equipmentStatus.setEquipmentTypeId("2");
            equipmentStatus.setEquipmentMode("电子对抗武器火控装置");
            //设置武器自检状态
            equipmentStatus.setCheckStatus(true);
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //武器状态报文发送17
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("22");
            equipmentStatus.setEquipmentTypeId("2");
            equipmentStatus.setEquipmentMode("舷外干扰发射装置");
            //设置武器自检状态
            equipmentStatus.setCheckStatus(random.nextBoolean());
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //武器状态报文发送18
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("23");
            equipmentStatus.setEquipmentTypeId("2");
            equipmentStatus.setEquipmentMode("舷外电子对抗武器");
            //设置武器自检状态
            equipmentStatus.setCheckStatus(true);
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //武器状态报文发送19
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("25");
            equipmentStatus.setEquipmentTypeId("2");
            equipmentStatus.setEquipmentMode("舷内电子对抗武器");
            //设置武器自检状态(随机自检)
            equipmentStatus.setCheckStatus(true);
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //----------------------------水声对抗武器通道测试---------------------------
            //武器状态报文发送20
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("16");
            equipmentStatus.setEquipmentTypeId("3");
            equipmentStatus.setEquipmentMode("拖曳声纳");
            //设置武器自检状态
            equipmentStatus.setCheckStatus(true);
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //武器状态报文发送21
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("27");
            equipmentStatus.setEquipmentTypeId("2");
            equipmentStatus.setEquipmentMode("水声对抗武器火控装置");
            //设置武器自检状态
            equipmentStatus.setCheckStatus(true);
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
            //武器状态报文发送22
            equipmentStatus.setSender("自动生成");
            equipmentStatus.setMsgTime(System.currentTimeMillis());
            equipmentStatus.setEquipmentId("28");
            equipmentStatus.setEquipmentTypeId("2");
            equipmentStatus.setEquipmentMode("水声对抗武器");
            //设置武器自检状态
            equipmentStatus.setCheckStatus(true);
            //设置武器发射时间
            equipmentStatus.setTime(System.currentTimeMillis());
            //设置武器开机状态
            equipmentStatus.setBeWork(true);
            equipmentStatus.setLaunchAzimuth(random.nextFloat()*100);
            equipmentStatus.setLaunchPitchAngle(random.nextFloat()*100);
            equipmentStatus.setElectromagneticFrequency(random.nextFloat()*100);
            equipmentStatus.setMinFrequency(random.nextFloat()*100);
            equipmentStatus.setMaxFrequency(random.nextFloat()*100);
            restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/EquipmentStatus", new HttpEntity<>(equipmentStatus, headers),String.class);
        }
    }
}
