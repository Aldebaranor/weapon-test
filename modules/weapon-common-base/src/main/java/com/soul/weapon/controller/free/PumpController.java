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
                // RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate().
                //         boundHashOps(Constant.EQUIPMENT_STATUS_HTTP_KEY).putAll(msg);
                RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate().opsForValue().set(
                        Constant.EQUIPMENT_STATUS_HTTP_KEY, JsonUtils.serialize(msg));
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
                EquipmentStatus equipmentStatus = new EquipmentStatus();
                equipmentStatus.setSender("juntai");
                equipmentStatus.setMsgTime(System.currentTimeMillis());
                equipmentStatus.setEquipmentId("1");
                equipmentStatus.setEquipmentTypeId("1");
                equipmentStatus.setEquipmentMode("远程防空导弹");
                equipmentStatus.setCheckStatus(true);
                equipmentStatus.setTime(System.currentTimeMillis());
                equipmentStatus.setBeWork(true);
                equipmentStatus.setLaunchAzimuth(0.1F);
                equipmentStatus.setLaunchPitchAngle(0.1F);
                equipmentStatus.setElectromagneticFrequency(0.1F);
                equipmentStatus.setMinFrequency(0.1F);
                equipmentStatus.setMaxFrequency(0.1F);
                HttpEntity<Object> request = new HttpEntity<>(equipmentStatus, headers);
                ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://127.0.0.1:8016/free/pump/"+structName,request,String.class);
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


