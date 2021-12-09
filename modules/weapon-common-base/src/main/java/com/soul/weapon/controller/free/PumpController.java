package com.soul.weapon.controller.free;

import com.google.common.collect.Lists;
import com.egova.exception.ExceptionUtils;
import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.egova.web.annotation.Api;
import com.flagwind.commons.StringUtils;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.model.ScenariosInfo;
import com.soul.weapon.model.dds.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
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
    private final int ONE_DAY=24*3600;

    @Api
    @PostMapping(value = "/{structName}")
    public Boolean pumpByStruct(@PathVariable String structName,@RequestBody Map<String, Object> msg) {

        if(StringUtils.isBlank(structName)){
            throw ExceptionUtils.api("structName can not be null");
        }
        if(msg == null){
            throw ExceptionUtils.api("msg can not be null");
        }
        structName = StringUtils.trim(structName);
        switch (structName) {
            case "CombatScenariosInfo": {
                CombatScenariosInfo combatScenariosInfo = JsonUtils.deserialize(JsonUtils.serialize(msg), CombatScenariosInfo.class);
                combatScenariosInfo.setScenariosList(JsonUtils.deserializeList(
                        combatScenariosInfo.getScenarios(), ScenariosInfo.class));

                String key=String.format("%s:%s",Constant.COMBAT_SCENARIOS_INFO_HTTP_KEY,getTime());
                if(RedisUtils.getService(config.getPumpDataBase()).exists(key)){
                    RedisUtils.getService(config.getPumpDataBase()).extrasForValue().set(
                            key, JsonUtils.serialize(combatScenariosInfo));
                }else{
                    RedisUtils.getService(config.getPumpDataBase()).extrasForValue().set(
                            key, JsonUtils.serialize(combatScenariosInfo));
                    RedisUtils.getService(config.getPumpDataBase()).expire(key,ONE_DAY);
                }

            }break;
            case "EnvironmentInfo": {

                String key=String.format("%s:%s", Constant.ENVIRONMENT_INFO_HTTP_KEY,getTime());
                if (RedisUtils.getService(config.getPumpDataBase()).exists(key)){
                    RedisUtils.getService(config.getPumpDataBase()).extrasForValue().set(
                            key, JsonUtils.serialize(msg));
                }else{
                    RedisUtils.getService(config.getPumpDataBase()).extrasForValue().set(
                            key, JsonUtils.serialize(msg));
                    RedisUtils.getService(config.getPumpDataBase()).expire(key,ONE_DAY);
                }
            }break;
            case "EquipmentLaunchStatus": {
                EquipmentLaunchStatus equipmentLaunchStatus = JsonUtils.deserialize(JsonUtils.serialize(msg), EquipmentLaunchStatus.class);

                String keyAll = String.format("%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY,getTime());
                if (RedisUtils.getService(config.getPumpDataBase()).exists(keyAll)){
                    RedisUtils.getService(config.getPumpDataBase()).
                            boundHashOps(keyAll).put(
                            equipmentLaunchStatus.getEquipmentId(), JsonUtils.serialize(equipmentLaunchStatus));
                }else{
                    RedisUtils.getService(config.getPumpDataBase()).
                            boundHashOps(keyAll).put(
                            equipmentLaunchStatus.getEquipmentId(), JsonUtils.serialize(equipmentLaunchStatus));
                    RedisUtils.getService(config.getPumpDataBase()).expire(keyAll,ONE_DAY);
                }

                String key = String.format("%s_%s:%s",Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY,equipmentLaunchStatus.getEquipmentId(),getTime());
                if ( RedisUtils.getService(config.getPumpDataBase()).exists(key)){
                    RedisUtils.getService(config.getPumpDataBase()).boundListOps(key).
                            leftPush(JsonUtils.serialize(equipmentLaunchStatus));
                }else {
                    RedisUtils.getService(config.getPumpDataBase()).boundListOps(key).
                            leftPush(JsonUtils.serialize(equipmentLaunchStatus));
                    RedisUtils.getService(config.getPumpDataBase()).expire(key,ONE_DAY);
                }
            }break;
            case "EquipmentStatus": {
                EquipmentStatus equipmentStatus = JsonUtils.deserialize(JsonUtils.serialize(msg), EquipmentStatus.class);

                RedisUtils.getService(config.getPumpDataBase()).
                        boundHashOps(Constant.EQUIPMENT_STATUS_HTTP_KEY).put(
                                equipmentStatus.getEquipmentId(), JsonUtils.serialize(equipmentStatus));

                RedisUtils.getService(config.getPumpDataBase()).boundListOps(
                        Constant.EQUIPMENT_STATUS_HTTP_KEY+"_"+ equipmentStatus.getEquipmentId()).leftPush(JsonUtils.serialize(equipmentStatus));

                String keyAll = String.format("%s:%s", Constant.EQUIPMENT_STATUS_HTTP_KEY,getTime());
                if (RedisUtils.getService(config.getPumpDataBase()).exists(keyAll)){
                    RedisUtils.getService(config.getPumpDataBase()).
                            boundHashOps(keyAll).put(
                            equipmentStatus.getEquipmentId(), JsonUtils.serialize(equipmentStatus));
                } else {
                    RedisUtils.getService(config.getPumpDataBase()).
                            boundHashOps(keyAll).put(
                            equipmentStatus.getEquipmentId(), JsonUtils.serialize(equipmentStatus));
                    RedisUtils.getService(config.getPumpDataBase()).expire(keyAll,ONE_DAY);
                }

                String key = String.format("%s_%s:%s",Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY,equipmentStatus.getEquipmentId(),getTime());
                if (RedisUtils.getService(config.getPumpDataBase()).exists(key)){
                    RedisUtils.getService(config.getPumpDataBase()).boundListOps(key).
                            leftPush(JsonUtils.serialize(equipmentStatus));
                }else {
                    RedisUtils.getService(config.getPumpDataBase()).boundListOps(key).
                            leftPush(JsonUtils.serialize(equipmentStatus));
                    RedisUtils.getService(config.getPumpDataBase()).expire(key,ONE_DAY);
                }
            }break;
            case "LauncherRotationInfo": {

                String key=String.format("%s:%s",Constant.LAUNCHER_ROTATION_INFO_HTTP_KEY,getTime());
                if(RedisUtils.getService(config.getPumpDataBase()).exists(key)){
                    RedisUtils.getService(config.getPumpDataBase()).extrasForValue().set(
                            key, JsonUtils.serialize(msg));
                }else{
                    RedisUtils.getService(config.getPumpDataBase()).extrasForValue().set(
                            key, JsonUtils.serialize(msg));
                    RedisUtils.getService(config.getPumpDataBase()).expire(key,ONE_DAY);
                }

            }break;
            case "TargetFireControlInfo": {
                TargetFireControlInfo tmpTargetFireControlInfo=JsonUtils.deserialize(JsonUtils.serialize(msg),TargetFireControlInfo.class);
                RedisUtils.getService(config.getPumpDataBase()).boundHashOps(
                        Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY).put(tmpTargetFireControlInfo.getTargetId(),JsonUtils.serialize(tmpTargetFireControlInfo));

                String key=String.format("%s:%s",Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY,getTime());
                if(RedisUtils.getService(config.getPumpDataBase()).exists(key)){
                    RedisUtils.getService(config.getPumpDataBase()).boundHashOps(key).
                            put(tmpTargetFireControlInfo.getTargetId(),JsonUtils.serialize(tmpTargetFireControlInfo));

                }else{
                    RedisUtils.getService(config.getPumpDataBase()).boundHashOps(key).
                            put(tmpTargetFireControlInfo.getTargetId(),JsonUtils.serialize(tmpTargetFireControlInfo));
                    RedisUtils.getService(config.getPumpDataBase()).expire(key,ONE_DAY);
                }

            }break;
            case "TargetInfo": {
                TargetInfo tmpTargetInfo = JsonUtils.deserialize(JsonUtils.serialize(msg), TargetInfo.class);

                // 目标真值历史报文
                String keyAll = String.format("%s:%s", Constant.TARGET_INFO_HTTP_KEY,getTime());
                if (RedisUtils.getService(config.getPumpDataBase()).exists(keyAll)){
                    RedisUtils.getService(config.getPumpDataBase()).boundHashOps(keyAll).
                            put(tmpTargetInfo.getTargetId(), JsonUtils.serialize(tmpTargetInfo));
                }else{
                    RedisUtils.getService(config.getPumpDataBase()).boundHashOps(keyAll).
                            put(tmpTargetInfo.getTargetId(), JsonUtils.serialize(tmpTargetInfo));
                    RedisUtils.getService(config.getPumpDataBase()).expire(keyAll,ONE_DAY);
                }

                // 根据每个目标id存的目标真值历史报文
                String key = String.format("%s_%s:%s",Constant.TARGET_INFO_HTTP_KEY,tmpTargetInfo.getTargetId(),getTime());
                if ( RedisUtils.getService(config.getPumpDataBase()).exists(key)){
                    RedisUtils.getService(config.getPumpDataBase()).boundListOps(key).
                            leftPush(JsonUtils.serialize(tmpTargetInfo));
                }else {
                    RedisUtils.getService(config.getPumpDataBase()).boundListOps(key).
                            leftPush(JsonUtils.serialize(tmpTargetInfo));
                    RedisUtils.getService(config.getPumpDataBase()).expire(key,ONE_DAY);
                }

//                // 添加一个field字段使得能够成功插入influxdb
//                msg.put("_uselessFiled", "useless");
//                influxdbTemplate.insert(Constant.TARGET_INFO_INFLUX_MEASURMENT_NAME, msg);
            }break;
            case "TargetInstructionsInfo": {
                TargetInstructionsInfo targetInstructionsInfo = JsonUtils.deserialize(JsonUtils.serialize(msg),
                        TargetInstructionsInfo.class);
                // 目标指示历史报文
                RedisUtils.getService(config.getPumpDataBase()).getTemplate().boundHashOps(
                        Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY).put(targetInstructionsInfo.getTargetId(),
                        JsonUtils.serialize(targetInstructionsInfo));

                // 根据每个目标id存的目标指示历史报文
                RedisUtils.getService(config.getPumpDataBase()).getTemplate().boundListOps(
                        Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY+"_"+targetInstructionsInfo.getTargetId()).
                        leftPush(JsonUtils.serialize(targetInstructionsInfo));

                // 目标真值历史报文
                String keyAll = String.format("%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY,getTime());
                if (RedisUtils.getService(config.getPumpDataBase()).exists(keyAll)){
                    RedisUtils.getService(config.getPumpDataBase()).boundHashOps(keyAll).
                            put(targetInstructionsInfo.getTargetId(),
                            JsonUtils.serialize(targetInstructionsInfo));
                }else{
                    RedisUtils.getService(config.getPumpDataBase()).boundHashOps(keyAll).
                            put(targetInstructionsInfo.getTargetId(),
                            JsonUtils.serialize(targetInstructionsInfo));
                    RedisUtils.getService(config.getPumpDataBase()).expire(keyAll,ONE_DAY);
                }

                // 根据每个目标id存的目标真值历史报文
                String key = String.format("%s_%s:%s",Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY,
                        targetInstructionsInfo.getTargetId(),getTime());
                if ( RedisUtils.getService(config.getPumpDataBase()).exists(key)){
                    RedisUtils.getService(config.getPumpDataBase()).getTemplate().boundListOps(key).
                            leftPush(JsonUtils.serialize(targetInstructionsInfo));
                }else {
                    RedisUtils.getService(config.getPumpDataBase()).getTemplate().boundListOps(key).
                            leftPush(JsonUtils.serialize(targetInstructionsInfo));
                    RedisUtils.getService(config.getPumpDataBase()).expire(key,ONE_DAY);
                }

            }break;
            default: {
                log.error("unrecognized struct name for http telegram to redis");
            }
        }


//        String key = String.format("weapon:pump:%s",structName.toLowerCase());
//        RedisUtils.getService(config.getPumpDataBase()).opsForList().leftPush(key,JsonUtils.serialize(msg));
//        RedisUtils.getService(config.getPumpDataBase()).getTemplate().opsForValue().set(key, JsonUtils.serialize(msg));
        return true;
    }

    @Api
    @GetMapping(value = "/demo/{structName}")
    public String pumpTest(@PathVariable String structName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if(StringUtils.isBlank(structName)){
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
                ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/"+structName,request,String.class);
                return responseEntity.toString();
            }
            case "EnvironmentInfo": {
                return JsonUtils.serialize(new EnvironmentInfo());
            }
            case "EquipmentLaunchStatus": {
                EquipmentLaunchStatus equipmentLaunchStatus1=new EquipmentLaunchStatus();
                equipmentLaunchStatus1.setSender("1");
                equipmentLaunchStatus1.setMsgTime(System.currentTimeMillis());
                equipmentLaunchStatus1.setEquipmentId("6");
                equipmentLaunchStatus1.setEquipmentMode("6");
                equipmentLaunchStatus1.setTime(System.currentTimeMillis());
                equipmentLaunchStatus1.setTargetId("6");
                equipmentLaunchStatus1.setTargetTypeId("6");
                equipmentLaunchStatus1.setLaunchAzimuth(0.1f);
                equipmentLaunchStatus1.setLaunchPitchAngle(0.1f);

                EquipmentLaunchStatus equipmentLaunchStatus2=new EquipmentLaunchStatus();
                equipmentLaunchStatus2.setSender("2");
                equipmentLaunchStatus2.setMsgTime(System.currentTimeMillis());
                equipmentLaunchStatus2.setEquipmentId("2");
                equipmentLaunchStatus2.setEquipmentMode("2");
                equipmentLaunchStatus2.setTime(111L);
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
                return JsonUtils.serialize(new LauncherRotationInfo());
            }
            case "TargetFireControlInfo": {
                TargetFireControlInfo targetFireControlInfo=new TargetFireControlInfo();
                targetFireControlInfo.setTargetId("2");
                targetFireControlInfo.setSender("2");
                targetFireControlInfo.setTime(111L);
                targetFireControlInfo.setTargetTypeId("2");
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
                tarInfo.setTime(100L);
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
                tarInfo2.setTime(111L);
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
                tarInfo3.setTime(108L);
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
                targetInstructionsInfo.setTime(100L);
                targetInstructionsInfo.setTargetId("6");
                targetInstructionsInfo.setTargetTypeId("6");
                targetInstructionsInfo.setDistance(8.8F);
                targetInstructionsInfo.setSpeed(9.1F);
                targetInstructionsInfo.setAzimuth(9.2F);
                targetInstructionsInfo.setPitchAngle(8.5F);
                targetInstructionsInfo.setDepth(8.7F);

                TargetInstructionsInfo targetInstructionsInfo2 = new TargetInstructionsInfo();
                targetInstructionsInfo2.setSender("xxx");
                targetInstructionsInfo2.setMsgTime(103L);
                targetInstructionsInfo2.setTime(103L);
                targetInstructionsInfo2.setTargetId("6");
                targetInstructionsInfo2.setTargetTypeId("6");
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

                HttpEntity<Object> request1 = new HttpEntity<>(targetInstructionsInfo2, headers);
                restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/" + structName,
                        request1, String.class).toString();

                HttpEntity<Object> request2 = new HttpEntity<>(targetInstructionsInfo3, headers);
                return restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/" + structName,
                        request2, String.class).toString();

            }
            default: {
                   log.error("unrecognized struct name for http telegram test!");
            }
        }
        return "";
    }



    private String getTime(){
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(System.currentTimeMillis());
    }
}


