package com.soul.weapon.controller.free;

import com.egova.exception.ApiException;
import com.egova.exception.ExceptionUtils;
import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.egova.web.annotation.Api;
import com.flagwind.commons.StringUtils;
import com.soul.weapon.model.dds.EquipmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.JsonNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.websocket.server.PathParam;
import java.util.Map;

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

    @Api
    @PostMapping(value = "/{structName}")
    public Boolean pumpByStruct(@PathVariable String structName,@RequestBody Map<String,String> msg) {

        if(StringUtils.isBlank(structName)){
            throw ExceptionUtils.api("structName can not be null");
        }
        if(msg == null){
            throw ExceptionUtils.api("msg can not be null");
        }
        structName = StringUtils.trim(structName);
        String key = String.format("weapon:pump:%s",structName.toLowerCase());
        RedisUtils.getService().opsForList().leftPush(key,JsonUtils.serialize(msg));
        return true;
    }

    @Api
    @GetMapping(value = "/demo/{structName}")
    public Boolean pumpTest(@PathVariable String structName) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if(StringUtils.equalsIgnoreCase("EquipmentStatus",structName)){
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




        }
        return true;
    }


}


