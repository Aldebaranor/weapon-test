package com.soul.weapon.controller.free;
import com.google.common.collect.Lists;
import com.egova.exception.ExceptionUtils;
import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.egova.web.annotation.Api;
import com.flagwind.commons.StringUtils;
import com.soul.weapon.config.CommonRedisConfig;
import com.soul.weapon.influxdb.InfluxdbTemplate;
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

import java.util.List;
import java.util.Map;
import com.soul.weapon.config.Constant;

/**
 * @Auther: 码头工人
 * @Date: 2021/11/01/2:10 下午
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/free/pump")
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "influxdb",name = "enabled" ,havingValue = "true", matchIfMissing = false)
public class PumpController {
    private final InfluxdbTemplate influxdbTemplate;
    private final RestTemplate restTemplate;
    private final CommonRedisConfig commonRedisConfig;

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

                RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate().opsForValue().set(
                        Constant.COMBAT_SCENARIOS_INFO_HTTP_KEY, JsonUtils.serialize(combatScenariosInfo));
            }break;
            case "EnvironmentInfo": {
                RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate().opsForValue().set(
                        Constant.ENVIRONMENT_INFO_HTTP_KEY, JsonUtils.serialize(msg));
            }break;
            case "EquipmentLaunchStatus": {
                RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate().opsForValue().set(
                        Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, JsonUtils.serialize(msg));
            }break;
            case "EquipmentStatus": {
                EquipmentStatus equipmentStatus = JsonUtils.deserialize(JsonUtils.serialize(msg), EquipmentStatus.class);
                RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate().
                        boundHashOps(Constant.EQUIPMENT_STATUS_HTTP_KEY).put(
                                equipmentStatus.getEquipmentId(), JsonUtils.serialize(equipmentStatus));
            }break;
            case "LauncherRotationInfo": {
                RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate().opsForValue().set(
                        Constant.LAUNCHER_ROTATION_INFO_HTTP_KEY, JsonUtils.serialize(msg));
            }break;
            case "TargetFireControlInfo": {
                RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate().opsForValue().set(
                        Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, JsonUtils.serialize(msg));
            }break;
            case "TargetInfo": {
                TargetInfo tmpTargetInfo = JsonUtils.deserialize(JsonUtils.serialize(msg), TargetInfo.class);
                RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate().boundHashOps(
                        Constant.TARGET_INFO_HTTP_KEY).put(tmpTargetInfo.getTargetId(), JsonUtils.serialize(tmpTargetInfo));
                // 添加一个field字段使得能够成功插入influxdb
                msg.put("_uselessFiled", "useless");
                influxdbTemplate.insert(Constant.TARGET_INFO_INFLUX_MEASURMENT_NAME, msg);
            }break;
            case "TargetInstructionsInfo": {
                RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate().opsForValue().set(
                        Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, JsonUtils.serialize(msg));
            }break;
            default: {
                log.error("unrecognized struct name for http telegram to redis");
            }
        }


        String key = String.format("weapon:pump:%s",structName.toLowerCase());
        RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).opsForList().leftPush(key,JsonUtils.serialize(msg));
        RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate().opsForValue().set(key, JsonUtils.serialize(msg));
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
                return JsonUtils.serialize(new EquipmentLaunchStatus());
            }
            case "EquipmentStatus": {
                EquipmentStatus equipmentStatus1 = new EquipmentStatus();
                equipmentStatus1.setSender("1");
                equipmentStatus1.setMsgTime(System.currentTimeMillis());
                equipmentStatus1.setEquipmentId("1");
                equipmentStatus1.setEquipmentTypeId("1");
                equipmentStatus1.setEquipmentMode("跟踪雷达");
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
                equipmentStatus3.setEquipmentId("3");
                equipmentStatus3.setEquipmentTypeId("3");
                equipmentStatus3.setEquipmentMode("反导舰炮");
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
                HttpEntity<Object> request1 = new HttpEntity<>(equipmentStatus1, headers);
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
                targetFireControlInfo.setSender("1");
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
                TargetInfo targetInfo=new TargetInfo();
                targetInfo.setTargetId("1");
                targetInfo.setSender("1");
                targetInfo.setTargetTypeId("1");
                targetInfo.setMsgTime(System.currentTimeMillis());
                targetInfo.setDistance(1f);
                targetInfo.setSpeed(2f);
                targetInfo.setPitchAngle(2f);
                targetInfo.setTime(1637643392281L);

                HttpEntity<Object> request1 = new HttpEntity<>(targetInfo, headers);
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                        "http://127.0.0.1:8016/free/pump/" + structName, request1, String.class);

                TargetInfo tarInfo2 = new TargetInfo();
                tarInfo2.setSender("xxx");
                tarInfo2.setMsgTime(System.currentTimeMillis());
                tarInfo2.setTime(System.currentTimeMillis());
                tarInfo2.setTargetId("6");
                tarInfo2.setTargetTypeId("6");
                tarInfo2.setDistance(6.0F);
                tarInfo2.setSpeed(6.0F);
                tarInfo2.setAzimuth(6.0F);
                tarInfo2.setPitchAngle(6.0F);
                tarInfo2.setDepth(6.0F);
                HttpEntity<Object> request2 = new HttpEntity<>(tarInfo2, headers);
                return restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/" + structName,
                        request2, String.class).toString();
            }
            case "TargetInstructionsInfo": {
                TargetInstructionsInfo targetInstructionsInfo=new TargetInstructionsInfo();
                targetInstructionsInfo.setSender("1");
                targetInstructionsInfo.setMsgTime(System.currentTimeMillis());
                targetInstructionsInfo.setTargetId("1");
                targetInstructionsInfo.setTargetTypeId("1");
                targetInstructionsInfo.setDistance(1f);
                targetInstructionsInfo.setSpeed(1f);
                targetInstructionsInfo.setPitchAngle(1f);
                targetInstructionsInfo.setTime(1637643392280L);

                HttpEntity<Object> request2 = new HttpEntity<>(targetInstructionsInfo, headers);
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                        "http://127.0.0.1:8016/free/pump/" + structName, request2, String.class);

                return responseEntity.toString();
            }
            default: {
                   log.error("unrecognized struct name for http telegram test!");
            }
        }
        return "";
    }

    /**
     * 查询test数据库下的所有表名，仅供测试使用
     */
    @GetMapping("/table")
    public void publish(){

        List<String> tables = influxdbTemplate.tables();
        for (String s:tables
        ) {
            log.info(s);
        }
    }
}


