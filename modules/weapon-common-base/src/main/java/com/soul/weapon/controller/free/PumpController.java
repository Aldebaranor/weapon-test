package com.soul.weapon.controller.free;
import com.google.common.collect.Lists;
import com.egova.exception.ExceptionUtils;
import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.egova.web.annotation.Api;
import com.flagwind.commons.StringUtils;
import com.soul.weapon.config.CommonRedisConfig;
import com.soul.weapon.model.ScenariosInfo;
import com.soul.weapon.model.dds.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
public class PumpController {

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
                RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate().opsForValue().set(
                        Constant.TARGET_INFO_HTTP_KEY, JsonUtils.serialize(msg));
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

                HttpEntity<Object> request1 = new HttpEntity<>(equipmentStatus1, headers);
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                        "http://127.0.0.1:8016/free/pump/" + structName, request1, String.class);

                HttpEntity<Object> request2 = new HttpEntity<>(equipmentStatus2, headers);
                restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/" + structName, request2, String.class);

                HttpEntity<Object> request3 = new HttpEntity<>(equipmentStatus3, headers);
                restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/" + structName, request3, String.class);
                return responseEntity.toString();
            }
            case "LauncherRotationInfo": {
                return JsonUtils.serialize(new LauncherRotationInfo());
            }
            case "TargetFireControlInfo": {
                return JsonUtils.serialize(new TargetFireControlInfo());
            }
            case "TargetInfo": {


                return JsonUtils.serialize(new TargetInfo());
            }
            case "TargetInstructionsInfo": {

                return JsonUtils.serialize(new TargetInstructionsInfo());
            }
            default: {
                log.error("unrecognized struct name for http telegram test!");
            }
        }
        return "";
    }
}


