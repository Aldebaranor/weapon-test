package com.soul.weapon.controller.free;

import com.egova.cache.RedisEhcacheProperties;
import com.egova.redis.RedisService;
import com.egova.utils.TimeUtils;
import com.google.common.collect.Lists;
import com.egova.exception.ExceptionUtils;
import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.egova.web.annotation.Api;
import com.flagwind.commons.StringUtils;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.model.ChargeReport;
import com.soul.weapon.model.ConflictReport;
import com.soul.weapon.model.ScenariosInfo;
import com.soul.weapon.model.dds.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.soul.weapon.config.Constant;

/**
 * @author: 码头工人
 * @Date: 2021/11/01/2:10 下午
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/free/pump")
@RequiredArgsConstructor
public class PumpController {

    private final RestTemplate restTemplate;
    private final CommonConfig config;
    private final int ONE_DAY = 24 * 3600;

    @Api
    @PostMapping(value = "/{structName}")
    public Boolean pumpByStruct(@PathVariable String structName, @RequestBody Map<String, Object> msg) {

        RedisService redisService = RedisUtils.getService(config.getPumpDataBase());

        if (StringUtils.isBlank(structName)) {
            throw ExceptionUtils.api("structName can not be null");
        }
        if (msg == null) {
            throw ExceptionUtils.api("msg can not be null");
        }
        structName = StringUtils.trim(structName);
        switch (structName) {
            //String 作战方案
            //key:[weapon:pump:combat_scenarios_info]
            //value:[combatScenariosInfo]
            case "CombatScenariosInfo": {
                CombatScenariosInfo combatScenariosInfo = JsonUtils.deserialize(JsonUtils.serialize(msg), CombatScenariosInfo.class);
                combatScenariosInfo.setScenariosList(JsonUtils.deserializeList(
                        combatScenariosInfo.getScenarios(), ScenariosInfo.class));

                String key = String.format("%s:%s", Constant.COMBAT_SCENARIOS_INFO_HTTP_KEY, getTime());
                if (RedisUtils.getService(config.getPumpDataBase()).exists(key)) {
                    RedisUtils.getService(config.getPumpDataBase()).extrasForValue().set(
                            key, JsonUtils.serialize(combatScenariosInfo));
                } else {
                    RedisUtils.getService(config.getPumpDataBase()).extrasForValue().set(
                            key, JsonUtils.serialize(combatScenariosInfo));
                    RedisUtils.getService(config.getPumpDataBase()).expire(key, ONE_DAY);
                }

            }
            break;
            //String 战场环境
            //key:[weapon:pump:environment_info]
            //value:[EnvironmentInfo]
            case "EnvironmentInfo": {
                String key = String.format("%s:%s", Constant.ENVIRONMENT_INFO_HTTP_KEY, getTime());
                if (RedisUtils.getService(config.getPumpDataBase()).exists(key)) {
                    RedisUtils.getService(config.getPumpDataBase()).extrasForValue().set(
                            key, JsonUtils.serialize(msg));
                } else {
                    RedisUtils.getService(config.getPumpDataBase()).extrasForValue().set(
                            key, JsonUtils.serialize(msg));
                    RedisUtils.getService(config.getPumpDataBase()).expire(key, ONE_DAY);
                }
            }
            break;
            //map 武器发射
            //key:[weapon:equipment_launch_status:targetId:yyyymmdd:{targetId}]
            //value:[Zset<time,EquipmentLaunchStatus>]
            case "EquipmentLaunchStatus": {
                //对报文数据进行反序列化
                EquipmentLaunchStatus equipmentLaunchStatus = JsonUtils.deserialize(JsonUtils.serialize(msg), EquipmentLaunchStatus.class);
                //拼接redis中当天的武器发射dds的key
                String key = String.format("%s:%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, getTime(),equipmentLaunchStatus.getTargetId());
                redisService.getTemplate().opsForZSet().add(key,JsonUtils.serialize(equipmentLaunchStatus), equipmentLaunchStatus.getTime());
            }
            break;
            //map 装备状态
            //key:[weapon:equipment_status:yyyymmdd]
            //value:[Map(equipmentId,EquipmentStatus)
            case "EquipmentStatus": {
                //反序列化
                EquipmentStatus equipmentStatus = JsonUtils.deserialize(JsonUtils.serialize(msg), EquipmentStatus.class);
                //拼接redis中当天的key
                String key = String.format("%s:%s", Constant.EQUIPMENT_STATUS_HTTP_KEY, getTime());
                //判断是否存在key
                if (RedisUtils.getService(config.getPumpDataBase()).exists(key)) {
                    //如果存在进行添加或者更新
                    RedisUtils.getService(config.getPumpDataBase()).
                            boundHashOps(key).put(
                                    equipmentStatus.getEquipmentId(), JsonUtils.serialize(equipmentStatus));
                } else {
                    //如果不存在存入对应结构并设置过期时间
                    RedisUtils.getService(config.getPumpDataBase()).
                            boundHashOps(key).put(
                                    equipmentStatus.getEquipmentId(), JsonUtils.serialize(equipmentStatus));
                    RedisUtils.getService(config.getPumpDataBase()).expire(key, ONE_DAY);
                }

            }
            break;
            //map 发射架调转
            //key:[weapon:launcher_rotation_info:equipmentId:yyyymmdd:{equipmentId}]
            //value:[Map<targetId,LauncherRotationInfo>]
            //key:[weapon:launcher_rotation_info:targetId:yyyymmdd:{targetId}]
            //value:[Zset<time,LauncherRotationInfo>]
            case "LauncherRotationInfo": {
                //反序列化
                LauncherRotationInfo launcherRotationInfo = JsonUtils.deserialize(JsonUtils.serialize(msg), LauncherRotationInfo.class);
                //拼接redis中当天的key
                String key = String.format("%s:%s:%s", Constant.LAUNCHER_ROTATION_INFO_HTTP_KEY, getTime(),launcherRotationInfo.getLauncherId());
                redisService.boundHashOps(key).put(launcherRotationInfo.getTargetId(),JsonUtils.serialize(launcherRotationInfo));
                //拼接redis中当天的key
                String key1 = String.format("%s:%s:%s", Constant.LAUNCHER_ROTATION_INFO_HTTP_KEY_2, getTime(),launcherRotationInfo.getTargetId());
                redisService.getTemplate().opsForZSet().add(key1,JsonUtils.serialize(launcherRotationInfo), launcherRotationInfo.getTime());
            }
            break;
            //map 目标火控
            //key:[weapon:target_fire_control_info:targetId:yyyymmdd:{targetId}]
            //value:[Zset<time,TargetFireControlInfo>]
            //key2:[weapon:target_fire_control_info:equipmentId:yyyymmdd:{equipmentId}](12算法)
            //value2:[Map<targetId,TargetFireControlInfo>]
            case "TargetFireControlInfo": {
                //反序列化
                TargetFireControlInfo tmpTargetFireControlInfo = JsonUtils.deserialize(JsonUtils.serialize(msg), TargetFireControlInfo.class);
                //拼接redis中当天的key
                String key = String.format("%s:%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, getTime(),tmpTargetFireControlInfo.getTargetId());
                String key2 = String.format("%s:%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY_2, getTime(),tmpTargetFireControlInfo.getFireControlSystemId());
                redisService.getTemplate().opsForZSet().add(key,JsonUtils.serialize(tmpTargetFireControlInfo), tmpTargetFireControlInfo.getTime());
                redisService.boundHashOps(key2).put(tmpTargetFireControlInfo.getTargetId(),JsonUtils.serialize(tmpTargetFireControlInfo));
            }
            break;
            //map 目标信息
            //key:[weapon:target_info:targetId:yyyymmdd:{targetId}]
            //value:[Zset<time,TargetInfo>]
            case "TargetInfo": {
                //反序列化
                TargetInfo tmpTargetInfo = JsonUtils.deserialize(JsonUtils.serialize(msg), TargetInfo.class);
                // 拼接redis中当天的key
                String key = String.format("%s:%s:%s", Constant.TARGET_INFO_HTTP_KEY, getTime(),tmpTargetInfo.getTargetId());
                redisService.getTemplate().opsForZSet().add(key,JsonUtils.serialize(tmpTargetInfo), tmpTargetInfo.getTime());
            }
            break;
            //map 目标指示
            //key:[weapon:target_instructions_info:targetId:yyyymmdd:{targetId}]
            //value:[Zset<time,TargetInstructionsInfo>]
            //key2:[weapon:target_instructions_info:yyyymmdd](7.8.10算法)
            //value2:[Map<targetId,TargetInstructionsInfo>]
            case "TargetInstructionsInfo": {
                //反序列化
                TargetInstructionsInfo targetInstructionsInfo = JsonUtils.deserialize(JsonUtils.serialize(msg), TargetInstructionsInfo.class);
                //目标指示历史报文
                String key = String.format("%s:%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, getTime(),targetInstructionsInfo.getTargetId());
                String key2 = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY_2, getTime());
                redisService.getTemplate().opsForZSet().add(key,JsonUtils.serialize(targetInstructionsInfo), targetInstructionsInfo.getTime());
                redisService.boundHashOps(key2).put(targetInstructionsInfo.getTargetId(),JsonUtils.serialize(targetInstructionsInfo));
            }
            break;
            case "Report": {
                ConflictReport conflictReport = JsonUtils.deserialize(JsonUtils.serialize(msg),
                        ChargeReport.class);

                if (RedisUtils.getService(config.getFireDataBase()).exists(Constant.PREDICT_KEY)) {
                    RedisUtils.getService(config.getFireDataBase()).boundHashOps(Constant.PREDICT_KEY).put(
                            conflictReport.getId(), JsonUtils.serialize(conflictReport));
                } else {
                    RedisUtils.getService(config.getFireDataBase()).boundHashOps(Constant.PREDICT_KEY).put(
                            conflictReport.getId(), JsonUtils.serialize(conflictReport));
                    RedisUtils.getService(config.getFireDataBase()).expire(Constant.PREDICT_KEY, 3600);
                }

            }
            break;
            default: {
                log.error("unrecognized struct name for http telegram to redis");
            }
        }
        return true;
    }

    @Api
    @GetMapping(value = "/demo/{structName}")
    public String pumpTest(@PathVariable String structName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (StringUtils.isBlank(structName)) {
            throw ExceptionUtils.api("structName can not be null");
        }

        structName = StringUtils.trim(structName);
        switch (structName) {
            case "CombatScenariosInfo": {
                CombatScenariosInfo combatScenariosInfo = new CombatScenariosInfo();
                combatScenariosInfo.setSender("");
                combatScenariosInfo.setMsgTime(0L);
                combatScenariosInfo.setTime(0L);
                combatScenariosInfo.setScenarios("[{\"equipmentId\":\"1\",\"equipmentTypeId\":\"1\",\"equipmentMode\":\"1\",\"beginTime\":1636581,\"duration\":5,\"launchAzimuth\":0.1,\"launchPitchAngle\":0.1,\"electromagneticFrequency\":0.1,\"minHydroacousticFrequency\":0.1,\"maxHydroacousticFrequency\":0.1}]");
                combatScenariosInfo.setScenariosList(Lists.newArrayList());
                HttpEntity<Object> request = new HttpEntity<>(combatScenariosInfo, headers);
                ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/" + structName, request, String.class);
                return responseEntity.toString();
            }
            case "EnvironmentInfo": {
                return JsonUtils.serialize(new EnvironmentInfo());
            }
            case "EquipmentLaunchStatus": {
                EquipmentLaunchStatus equipmentLaunchStatus1 = new EquipmentLaunchStatus();
                equipmentLaunchStatus1.setSender("1");
                equipmentLaunchStatus1.setMsgTime(System.currentTimeMillis());
                equipmentLaunchStatus1.setEquipmentId("2");
                equipmentLaunchStatus1.setEquipmentMode("6");
                equipmentLaunchStatus1.setTime(System.currentTimeMillis());
                equipmentLaunchStatus1.setTargetId("6");
                equipmentLaunchStatus1.setTargetTypeId("6");
                equipmentLaunchStatus1.setLaunchAzimuth(0.1f);
                equipmentLaunchStatus1.setLaunchPitchAngle(0.1f);

                EquipmentLaunchStatus equipmentLaunchStatus2 = new EquipmentLaunchStatus();
                equipmentLaunchStatus2.setSender("2");
                equipmentLaunchStatus2.setMsgTime(System.currentTimeMillis());
                equipmentLaunchStatus2.setEquipmentId("1");
                equipmentLaunchStatus2.setEquipmentMode("2");
                equipmentLaunchStatus2.setTime(System.currentTimeMillis());
                equipmentLaunchStatus2.setTargetId("2");
                equipmentLaunchStatus2.setTargetTypeId("6");
                equipmentLaunchStatus2.setLaunchAzimuth(0.1f);
                equipmentLaunchStatus2.setLaunchPitchAngle(0.1f);

                HttpEntity<Object> request = new HttpEntity<>(equipmentLaunchStatus1, headers);
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                        "http://127.0.0.1:8016/free/pump/" + structName, request, String.class);
                HttpEntity<Object> request2 = new HttpEntity<>(equipmentLaunchStatus2, headers);
                ResponseEntity<String> responseEntity2 = restTemplate.postForEntity(
                        "http://127.0.0.1:8016/free/pump/" + structName, request2, String.class);
                return responseEntity.toString();
            }
            case "EquipmentStatus": {
                EquipmentStatus equipmentStatus1 = new EquipmentStatus();
                equipmentStatus1.setSender("1");
                equipmentStatus1.setMsgTime(System.currentTimeMillis());
                equipmentStatus1.setEquipmentId("6");
                equipmentStatus1.setEquipmentTypeId("6");
                equipmentStatus1.setEquipmentMode("反导舰炮跟踪雷达");
                equipmentStatus1.setCheckStatus(true);
                equipmentStatus1.setTime(System.currentTimeMillis());
                equipmentStatus1.setBeWork(true);
                equipmentStatus1.setLaunchAzimuth(0.1F);
                equipmentStatus1.setLaunchPitchAngle(0.1F);
                equipmentStatus1.setElectromagneticFrequency(0.1F);
                equipmentStatus1.setMinFrequency(0.1F);
                equipmentStatus1.setMaxFrequency(0.1F);
                EquipmentStatus equipmentStatus2 = new EquipmentStatus();
                equipmentStatus2.setSender("2");
                equipmentStatus2.setMsgTime(System.currentTimeMillis());
                equipmentStatus2.setEquipmentId("2");
                equipmentStatus2.setEquipmentTypeId("2");
                equipmentStatus2.setEquipmentMode("反导舰炮武器火控系统");
                equipmentStatus2.setCheckStatus(true);
                equipmentStatus2.setTime(System.currentTimeMillis());
                equipmentStatus2.setBeWork(true);
                equipmentStatus2.setLaunchAzimuth(0.2F);
                equipmentStatus2.setLaunchPitchAngle(0.2F);
                equipmentStatus2.setElectromagneticFrequency(0.2F);
                equipmentStatus2.setMinFrequency(0.2F);
                equipmentStatus2.setMaxFrequency(0.2F);
                EquipmentStatus equipmentStatus3 = new EquipmentStatus();
                equipmentStatus3.setSender("3");
                equipmentStatus3.setMsgTime(System.currentTimeMillis());
                equipmentStatus3.setEquipmentId("8");
                equipmentStatus3.setEquipmentTypeId("8");
                equipmentStatus3.setEquipmentMode("反导舰炮武器");
                equipmentStatus3.setCheckStatus(true);
                equipmentStatus3.setTime(System.currentTimeMillis());
                equipmentStatus3.setBeWork(true);
                equipmentStatus3.setLaunchAzimuth(0.3F);
                equipmentStatus3.setLaunchPitchAngle(0.3F);
                equipmentStatus3.setElectromagneticFrequency(0.3F);
                equipmentStatus3.setMinFrequency(0.3F);
                equipmentStatus3.setMaxFrequency(0.3F);

                HttpEntity<Object> request = new HttpEntity<>(equipmentStatus1, headers);
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                        "http://127.0.0.1:8016/free/pump/" + structName, request, String.class);
                HttpEntity<Object> request1 = new HttpEntity<>(equipmentStatus2, headers);
                restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/" + structName, request1, String.class);
                HttpEntity<Object> request2 = new HttpEntity<>(equipmentStatus3, headers);
                restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/" + structName, request2, String.class);
                return responseEntity.toString();
            }
            case "LauncherRotationInfo": {
                LauncherRotationInfo launcherRotationInfo = new LauncherRotationInfo();
                launcherRotationInfo.setLauncherId("1");
                launcherRotationInfo.setAzimuth(1.0f);
                launcherRotationInfo.setPitchAngle(2.0f);
                launcherRotationInfo.setLauncherTypeId("0");
                launcherRotationInfo.setTargetId("2");
                launcherRotationInfo.setTargetTypeId("2");
                launcherRotationInfo.setTime(System.currentTimeMillis());
                launcherRotationInfo.setMsgTime(System.currentTimeMillis());

                LauncherRotationInfo launcherRotationInfo2 = new LauncherRotationInfo();
                launcherRotationInfo2.setLauncherId("1");
                launcherRotationInfo2.setAzimuth(3.0f);
                launcherRotationInfo2.setPitchAngle(2.2f);
                launcherRotationInfo2.setLauncherTypeId("0");
                launcherRotationInfo2.setTargetId("6");
                launcherRotationInfo2.setTargetTypeId("1");
                launcherRotationInfo2.setTime(System.currentTimeMillis());
                launcherRotationInfo2.setMsgTime(System.currentTimeMillis());

                HttpEntity<Object> request = new HttpEntity<>(launcherRotationInfo, headers);
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                        "http://127.0.0.1:8016/free/pump/" + structName, request, String.class);

                HttpEntity<Object> request2 = new HttpEntity<>(launcherRotationInfo2, headers);
                ResponseEntity<String> responseEntity2 = restTemplate.postForEntity(
                        "http://127.0.0.1:8016/free/pump/" + structName, request2, String.class);

                return responseEntity2.toString();
            }
            case "TargetFireControlInfo": {
                TargetFireControlInfo targetFireControlInfo = new TargetFireControlInfo();
                targetFireControlInfo.setTargetId("2");
                targetFireControlInfo.setSender("1");
                targetFireControlInfo.setTime(System.currentTimeMillis());
                targetFireControlInfo.setTargetTypeId("1");
                targetFireControlInfo.setMsgTime(System.currentTimeMillis());
                targetFireControlInfo.setDistance(1f);
                targetFireControlInfo.setSpeed(2f);
                targetFireControlInfo.setPitchAngle(2f);


                HttpEntity<Object> request2 = new HttpEntity<>(targetFireControlInfo, headers);
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                        "http://127.0.0.1:8016/free/pump/" + structName, request2, String.class);

                return responseEntity.toString();
            }
            case "TargetInfo": {
                TargetInfo tarInfo = new TargetInfo();
                tarInfo.setSender("xxx");
                tarInfo.setMsgTime(100L);
                tarInfo.setTime(System.currentTimeMillis());
                tarInfo.setTargetId("6");
                tarInfo.setTargetTypeId("6");
                tarInfo.setDistance(9.0F);
                tarInfo.setSpeed(9.0F);
                tarInfo.setAzimuth(9.0F);
                tarInfo.setPitchAngle(9.0F);
                tarInfo.setDepth(9.0F);

                TargetInfo tarInfo2 = new TargetInfo();
                tarInfo2.setSender("xxx");
                tarInfo2.setMsgTime(104L);
                tarInfo2.setTime(System.currentTimeMillis());
                tarInfo2.setTargetId("2");
                tarInfo2.setTargetTypeId("2");
                tarInfo2.setDistance(6.0F);
                tarInfo2.setSpeed(6.0F);
                tarInfo2.setAzimuth(6.0F);
                tarInfo2.setPitchAngle(6.0F);
                tarInfo2.setDepth(6.0F);

                TargetInfo tarInfo3 = new TargetInfo();
                tarInfo3.setSender("xxx");
                tarInfo3.setMsgTime(104L);
                tarInfo3.setTime(System.currentTimeMillis());
                tarInfo3.setTargetId("6");
                tarInfo3.setTargetTypeId("6");
                tarInfo3.setDistance(3.0F);
                tarInfo3.setSpeed(3.0F);
                tarInfo3.setAzimuth(3.0F);
                tarInfo3.setPitchAngle(3.0F);
                tarInfo3.setDepth(3.0F);

                HttpEntity<Object> request = new HttpEntity<>(tarInfo, headers);
                restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/" + structName,
                        request, String.class).toString();

                HttpEntity<Object> request1 = new HttpEntity<>(tarInfo2, headers);
                restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/" + structName,
                        request1, String.class).toString();

                HttpEntity<Object> request2 = new HttpEntity<>(tarInfo3, headers);
                return restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/" + structName,
                        request2, String.class).toString();
            }
            case "TargetInstructionsInfo": {

                TargetInstructionsInfo targetInstructionsInfo = new TargetInstructionsInfo();
                targetInstructionsInfo.setSender("xxx");
                targetInstructionsInfo.setMsgTime(100L);
                targetInstructionsInfo.setTime(System.currentTimeMillis());
                targetInstructionsInfo.setTargetId("6");
                targetInstructionsInfo.setTargetTypeId("对空目标");
                targetInstructionsInfo.setDistance(8.8F);
                targetInstructionsInfo.setSpeed(9.1F);
                targetInstructionsInfo.setAzimuth(9.2F);
                targetInstructionsInfo.setPitchAngle(8.5F);
                targetInstructionsInfo.setDepth(8.7F);

                TargetInstructionsInfo targetInstructionsInfo2 = new TargetInstructionsInfo();
                targetInstructionsInfo2.setSender("xxx");
                targetInstructionsInfo2.setMsgTime(103L);
                targetInstructionsInfo2.setTime(System.currentTimeMillis());
                targetInstructionsInfo2.setTargetId("2");
                targetInstructionsInfo2.setTargetTypeId("对空目标");
                targetInstructionsInfo2.setDistance(6.8F);
                targetInstructionsInfo2.setSpeed(6.2F);
                targetInstructionsInfo2.setAzimuth(5.7F);
                targetInstructionsInfo2.setPitchAngle(6.1F);
                targetInstructionsInfo2.setDepth(6.15F);

                TargetInstructionsInfo targetInstructionsInfo3 = new TargetInstructionsInfo();
                targetInstructionsInfo3.setSender("xxx");
                targetInstructionsInfo3.setMsgTime(104L);
                targetInstructionsInfo3.setTime(111L);
                targetInstructionsInfo3.setTargetId("2");
                targetInstructionsInfo3.setTargetTypeId("2");
                targetInstructionsInfo3.setDistance(2.88F);
                targetInstructionsInfo3.setSpeed(3.15F);
                targetInstructionsInfo3.setAzimuth(3.22F);
                targetInstructionsInfo3.setPitchAngle(2.78F);
                targetInstructionsInfo3.setDepth(3.17F);

                HttpEntity<Object> request = new HttpEntity<>(targetInstructionsInfo, headers);
                restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/" + structName,
                        request, String.class).toString();
                HttpEntity<Object> request2 = new HttpEntity<>(targetInstructionsInfo3, headers);
                restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/" + structName,
                        request2, String.class).toString();

                HttpEntity<Object> request1 = new HttpEntity<>(targetInstructionsInfo2, headers);
                return restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/" + structName,
                        request1, String.class).toString();

            }
            case "Report": {
//                ChargeReport chargeReport = new ChargeReport();
//                chargeReport.setId("1111");
//                chargeReport.setChargeType(0);
//                chargeReport.setChargeEquipId("11");
//                chargeReport.setFreeEquipId("22");
//                chargeReport.setTime(System.currentTimeMillis());

                ConflictReport conflictReport = new ConflictReport();
                conflictReport.setId("2131221456547");
                conflictReport.setEquipmentIdB("3");
                conflictReport.setEquipmentIdA("7");
                conflictReport.setConflictType(1);
                conflictReport.setTime(System.currentTimeMillis());

                ConflictReport conflictReport2 = new ConflictReport();
                conflictReport2.setId("423425322342352");
                conflictReport2.setEquipmentIdB("3");
                conflictReport2.setEquipmentIdA("7");
                conflictReport2.setConflictType(1);
                conflictReport2.setTime(System.currentTimeMillis());

                HttpEntity<Object> request = new HttpEntity<>(conflictReport, headers);

                restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/" + structName,
                        request, String.class).toString();


                HttpEntity<Object> request2 = new HttpEntity<>(conflictReport, headers);
                return restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/" + structName,
                        request2, String.class).toString();
            }
            default: {
                log.error("unrecognized struct name for http telegram test!");
            }
        }
        return "";
    }

    private String getTime() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(System.currentTimeMillis());
    }
    //生成Map<String,Map<String,List>>结构
    //element:存储的元素,key:redis中的key,id1:外层key,id2:里层key
    private void structureGeneration(Object element, String key, String id1, String id2) {
        if (RedisUtils.getService(config.getPumpDataBase()).exists(key)) {
            //获取对应目标的发射架调转报文
            Map<String, List> map = RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(key, Map.class).get(id1);
            if (map == null) {
                map = new HashMap<>();
                List list = new ArrayList<>();
                list.add(element);
                map.put(id2, list);
            } else {
                //新增或更新
                if (map.get(id2) == null) {
                    List list = new ArrayList<>();
                    list.add(element);
                    map.put(id2, list);
                } else {
                    map.get(id2).add(element);
                }
            }
            //更新redis
            RedisUtils.getService(config.getPumpDataBase()).boundHashOps(key).put(id1, JsonUtils.serialize(map));
        } else {
            //如果不存在创建对应结构进行存储
            Map<String, List> map = new HashMap<>();
            List list = new ArrayList<>();
            list.add(element);
            map.put(id2, list);
            RedisUtils.getService(config.getPumpDataBase()).boundHashOps(key).put(id1, JsonUtils.serialize(map));
            RedisUtils.getService(config.getPumpDataBase()).expire(key, ONE_DAY);
        }
    }
}


