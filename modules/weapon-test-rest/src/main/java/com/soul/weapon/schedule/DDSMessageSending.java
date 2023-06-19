package com.soul.weapon.schedule;

import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisService;
import com.egova.redis.RedisUtils;
import com.google.common.collect.Lists;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.config.WeaponTestConstant;
import com.soul.weapon.model.dds.*;
import com.soul.weapon.utils.DateParserUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


@Slf4j
@Component
public class DDSMessageSending {


    //测试1to5任务
    public static class taskTest1to5 implements Runnable {

        private HttpHeaders headers;

        private List<EquipmentStatus> equipemntStatusList;

        private RestTemplate restTemplate;


        private static Random random = new Random();

        public taskTest1to5(HttpHeaders headers, List<EquipmentStatus> equipemntStatusList,RestTemplate restTemplate) {

            this.headers = headers;
            this.equipemntStatusList = equipemntStatusList;
            this.restTemplate = restTemplate;
        }

        @Override
        public void run() {

            for (EquipmentStatus equipmentStatus : equipemntStatusList) {
                equipmentStatus.setMsgTime(System.currentTimeMillis());
                if (!equipmentStatus.getCheckStatus()) {
                    equipmentStatus.setCheckStatus(random.nextBoolean());
                }
                equipmentStatus.setTime(System.currentTimeMillis());
                equipmentStatus.setLaunchAzimuth(random.nextFloat() * 100);
                equipmentStatus.setLaunchPitchAngle(random.nextFloat() * 100);
                equipmentStatus.setElectromagneticFrequency(random.nextFloat() * 100);
                equipmentStatus.setMinFrequency(random.nextFloat() * 100);
                equipmentStatus.setMaxFrequency(random.nextFloat() * 100);
                restTemplate.postForEntity(Constant.DDS_URL+"EquipmentStatus", new HttpEntity<>(equipmentStatus, headers), String.class);
            }
        }
    }
    //目标信息报文
    public static class taskTargetInfoDDS implements Runnable {

        private HttpHeaders headers;

        private RestTemplate restTemplate;

        public taskTargetInfoDDS(HttpHeaders headers,RestTemplate restTemplate) {
            this.headers = headers;
            this.restTemplate = restTemplate;
        }

        @Override
        public void run() {
            TargetInfo targetInfo = new TargetInfo();
            targetInfo.setSender("自动发送");
            targetInfo.setMsgTime(System.currentTimeMillis());
            targetInfo.setTime(System.currentTimeMillis()-(15*1000));
            targetInfo.setTargetId(String.valueOf(taskTest1to5.random.nextInt(10) + 1));
            targetInfo.setTargetTypeId(String.valueOf(taskTest1to5.random.nextInt(2) + 1));
            targetInfo.setDistance(taskTest1to5.random.nextFloat() * 100);
            targetInfo.setSpeed(taskTest1to5.random.nextFloat() * 100);
            targetInfo.setAzimuth(taskTest1to5.random.nextFloat() * 100);
            targetInfo.setPitchAngle(taskTest1to5.random.nextFloat() * 100);
            targetInfo.setDepth(taskTest1to5.random.nextFloat() * 100);
            restTemplate.postForEntity(Constant.DDS_URL+"TargetInfo", new HttpEntity<>(targetInfo, headers), String.class);
        }
    }
    //目标指示报文
    public static class taskTargetInstructionsInfoDDS implements Runnable {


        private HttpHeaders headers;

        private RedisService redisService;

        private RestTemplate restTemplate;

        public taskTargetInstructionsInfoDDS(HttpHeaders headers, RedisService redisService,RestTemplate restTemplate) {
            this.headers = headers;
            this.redisService = redisService;
            this.restTemplate = restTemplate;
        }

        //传感器ID数组
        private String[] sensorIdS = {"1", "10", "11", "15", "16"};


        @Override
        public void run() {

            String targetId = String.valueOf(taskTest1to5.random.nextInt(10) + 1);

            String key = String.format("%s:%s:%s", Constant.TARGET_INFO_HTTP_KEY, DateParserUtils.getTime(), targetId);

            Set<String> set = redisService.getTemplate().opsForZSet().reverseRange(key, 0, 0);

            if (set == null || set.size() == 0) {
                log.info("目标指示-没找到targetId为%s的目标信息", targetId);
                return;
            }

            String json = set.iterator().next();

            TargetInfo targetInfo = JsonUtils.deserialize(json, TargetInfo.class);
            TargetInstructionsInfo targetInstructionsInfo = new TargetInstructionsInfo();
            targetInstructionsInfo.setSender("自动生成");
            targetInstructionsInfo.setMsgTime(System.currentTimeMillis());
            targetInstructionsInfo.setTime(System.currentTimeMillis()-(13*1000));
            targetInstructionsInfo.setTargetId(targetInfo.getTargetId());
            targetInstructionsInfo.setTargetTypeId(targetInfo.getTargetTypeId());
            targetInstructionsInfo.setEquipmentId(sensorIdS[taskTest1to5.random.nextInt(sensorIdS.length)]);
            targetInstructionsInfo.setEquipmentTypeId("3");
            targetInstructionsInfo.setDistance(taskTest1to5.random.nextFloat() * 100);
            targetInstructionsInfo.setSpeed(taskTest1to5.random.nextFloat() * 100);
            targetInstructionsInfo.setAzimuth(taskTest1to5.random.nextFloat() * 100);
            targetInstructionsInfo.setPitchAngle(taskTest1to5.random.nextFloat() * 100);
            targetInstructionsInfo.setDepth(taskTest1to5.random.nextFloat() * 100);
            restTemplate.postForEntity(Constant.DDS_URL+"TargetInstructionsInfo", new HttpEntity<>(targetInstructionsInfo, headers), String.class);
        }
    }
    //目标火控报文
    public static class taskTargetFireControlInfoDDS implements Runnable{

        private HttpHeaders headers;

        private RedisService redisService;

        private RestTemplate restTemplate;

        public taskTargetFireControlInfoDDS(HttpHeaders headers, RedisService redisService,RestTemplate restTemplate) {
            this.headers = headers;
            this.redisService = redisService;
            this.restTemplate = restTemplate;
        }

        //火控ID数组
        private String[] fireControlSystemIds = {"2", "12", "17", "21", "27"};


        @Override
        public void run() {

            String targetId = String.valueOf(taskTest1to5.random.nextInt(10) + 1);
            String key = String.format("%s:%s:%s", Constant.TARGET_INFO_HTTP_KEY, DateParserUtils.getTime(), targetId);
            Set<String> set = redisService.getTemplate().opsForZSet().reverseRange(key, 0, 0);
            if (set == null || set.size() == 0) {
                log.info("目标火控-没找到targetId为%s的目标信息", targetId);
                return;
            }
            String json = set.iterator().next();
            TargetInfo targetInfo = JsonUtils.deserialize(json, TargetInfo.class);
            TargetFireControlInfo targetFireControlInfo = new TargetFireControlInfo();
            targetFireControlInfo.setSender("自动发送");
            targetFireControlInfo.setMsgTime(System.currentTimeMillis());
            targetFireControlInfo.setTime(System.currentTimeMillis()-(10*1000));
            targetFireControlInfo.setTargetId(targetInfo.getTargetId());
            targetFireControlInfo.setTargetTypeId(targetInfo.getTargetTypeId());
            targetFireControlInfo.setFireControlSystemId(fireControlSystemIds[taskTest1to5.random.nextInt(fireControlSystemIds.length)]);
            targetFireControlInfo.setFireControlSystemTypeId("1");
            targetFireControlInfo.setDistance(taskTest1to5.random.nextFloat()*100);
            targetFireControlInfo.setSpeed(taskTest1to5.random.nextFloat()*100);
            targetFireControlInfo.setAzimuth(taskTest1to5.random.nextFloat()*100);
            targetFireControlInfo.setPitchAngle(taskTest1to5.random.nextFloat()*100);
            targetFireControlInfo.setDepth(taskTest1to5.random.nextFloat()*100);
            restTemplate.postForEntity(Constant.DDS_URL+"TargetFireControlInfo", new HttpEntity<>(targetFireControlInfo, headers), String.class);
        }
    }
    //发射架调转
    public static class taskLauncherRotationInfoDDS implements Runnable{

        private HttpHeaders headers;

        private RedisService redisService;

        private RestTemplate restTemplate;

        public taskLauncherRotationInfoDDS(HttpHeaders headers, RedisService redisService,RestTemplate restTemplate) {
            this.headers = headers;
            this.redisService = redisService;
            this.restTemplate = restTemplate;
        }

        private String[] targetTypeIds = {"3","18","22"};

        @Override
        public void run() {
            String targetId = String.valueOf(taskTest1to5.random.nextInt(10) + 1);
            String key = String.format("%s:%s:%s", Constant.TARGET_INFO_HTTP_KEY, DateParserUtils.getTime(), targetId);
            Set<String> set = redisService.getTemplate().opsForZSet().reverseRange(key, 0, 0);
            if (set == null || set.size() == 0) {
                log.info("发射架-没找到targetId为%s的目标信息", targetId);
                return;
            }
            String json = set.iterator().next();
            TargetInfo targetInfo = JsonUtils.deserialize(json, TargetInfo.class);
            LauncherRotationInfo launcherRotationInfo = new LauncherRotationInfo();
            launcherRotationInfo.setSender("自动生成");
            launcherRotationInfo.setMsgTime(System.currentTimeMillis());
            launcherRotationInfo.setTime(System.currentTimeMillis() - (7*1000));
            launcherRotationInfo.setTargetId(targetInfo.getTargetId());
            launcherRotationInfo.setTargetTypeId(targetInfo.getTargetTypeId());
            launcherRotationInfo.setLauncherId(targetTypeIds[taskTest1to5.random.nextInt(targetTypeIds.length)]);
            launcherRotationInfo.setLauncherTypeId("1");
            launcherRotationInfo.setAzimuth(taskTest1to5.random.nextFloat()*100);
            launcherRotationInfo.setPitchAngle(taskTest1to5.random.nextFloat()*100);
            restTemplate.postForEntity(Constant.DDS_URL+"LauncherRotationInfo", new HttpEntity<>(launcherRotationInfo, headers), String.class);
        }
    }
    //武器发射
    public static class taskEquipmentLaunchStatusDDS implements Runnable{

        private HttpHeaders headers;

        private RedisService redisService;

        private RestTemplate restTemplate;

        public taskEquipmentLaunchStatusDDS(HttpHeaders headers, RedisService redisService,RestTemplate restTemplate) {
            this.headers = headers;
            this.redisService = redisService;
            this.restTemplate = restTemplate;
        }

        //武器ID数组
        private String[] equipmentIds = {"9", "4", "5", "19", "6","7", "14", "13", "8", "28","26", "25", "24", "23", "20"};

        @Override
        public void run() {
            String targetId = String.valueOf(taskTest1to5.random.nextInt(10) + 1);
            String key = String.format("%s:%s:%s", Constant.TARGET_INFO_HTTP_KEY, DateParserUtils.getTime(), targetId);
            Set<String> set = redisService.getTemplate().opsForZSet().reverseRange(key, 0, 0);
            if (set == null || set.size() == 0) {
                log.info("武器发射-没找到targetId为%s的目标信息", targetId);
                return;
            }
            String json = set.iterator().next();
            TargetInfo targetInfo = JsonUtils.deserialize(json, TargetInfo.class);
            EquipmentLaunchStatus equipmentLaunchStatus = new EquipmentLaunchStatus();
            equipmentLaunchStatus.setSender("自动生成");
            equipmentLaunchStatus.setMsgTime(System.currentTimeMillis());
            equipmentLaunchStatus.setTime(System.currentTimeMillis());
            equipmentLaunchStatus.setEquipmentId(equipmentIds[taskTest1to5.random.nextInt(equipmentIds.length)]);
            equipmentLaunchStatus.setEquipmentTypeId("1");
            equipmentLaunchStatus.setEquipmentMode("根据武器id去看");
            equipmentLaunchStatus.setTargetId(targetInfo.getTargetId());
            equipmentLaunchStatus.setTargetTypeId(targetInfo.getTargetTypeId());
            equipmentLaunchStatus.setLaunchAzimuth(taskTest1to5.random.nextFloat()*100);
            equipmentLaunchStatus.setLaunchPitchAngle(taskTest1to5.random.nextFloat()*100);
            restTemplate.postForEntity(Constant.DDS_URL+"EquipmentLaunchStatus", new HttpEntity<>(equipmentLaunchStatus, headers), String.class);
        }
    }

}
