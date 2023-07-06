package com.soul.weapon.controller.free;

import com.egova.json.utils.JsonUtils;
import com.egova.web.annotation.Api;
import com.soul.weapon.model.dds.CombatScenariosInfo;
import com.soul.weapon.schedule.UdpClientTest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Song
 * @Date 2023/7/6 14:10
 */
@RestController
@RequestMapping("/free/test")
@RequiredArgsConstructor
public class FreeFireSendUdpController {

    @Autowired
    UdpClientTest udpClientTest;
    @Api
    @PostMapping("/udp")
    public void sendUdp(@RequestBody CombatScenariosInfo combatScenariosInfo) throws InterruptedException {
        UdpClientTest client = new UdpClientTest();
        client.startClient(JsonUtils.serialize(combatScenariosInfo));
    }
}
