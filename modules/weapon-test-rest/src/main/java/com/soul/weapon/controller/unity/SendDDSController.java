package com.soul.weapon.controller.unity;

import com.egova.web.annotation.Api;
import com.soul.weapon.service.SendDDSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SendDDSController
 * @Description
 * @Author ShiZuan
 * @Date 2022/3/14 14:39
 * @Version
 **/

@Slf4j
@RestController
@RequestMapping("/unity/sendDDS")
public class SendDDSController {

    @Autowired
    private SendDDSService sendDDSService;

    @Api
    @GetMapping(value = "/start/{testCode}")
    public String sendDDSBytestCode(@PathVariable("testCode") String testCode){
        return sendDDSService.sendDDSBytestCode(testCode);
    }

    @Api
    @GetMapping(value = "/stop/{testCode}")
    public String stopSendDDSBytestCode(@PathVariable("testCode") String testCode){
        return sendDDSService.stopSendDDSBytestCode(testCode);
    }

    @Api
    @GetMapping("/stopAll")
    public String stopAll(){
        return sendDDSService.stopAll();
    }

    @Api
    @GetMapping("/startAllList")
    public String startAllList(){
        return sendDDSService.startAllList();
    }
}
