package com.soul.weapon.controller.free;

import com.egova.exception.ApiException;
import com.egova.exception.ExceptionUtils;
import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.egova.web.annotation.Api;
import com.flagwind.commons.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @Api
    @PostMapping(value = "/{structName}")
    public Boolean pumpByStruct(@PathVariable String structName,@RequestBody String msg) {
        if(StringUtils.isBlank(msg)){
            throw ExceptionUtils.api("msg can not be null");
        }
        structName = StringUtils.trim(structName);
        String beanName = structName.substring(0, 1).toLowerCase() + structName.substring(1);
        String key = String.format("weapon:pump:%s",beanName);
        RedisUtils.getService().opsForList().leftPush(key,msg);
        return true;
    }
}
