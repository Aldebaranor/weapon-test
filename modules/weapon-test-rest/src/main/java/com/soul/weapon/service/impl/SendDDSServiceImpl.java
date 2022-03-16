package com.soul.weapon.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.egova.utils.FileUtils;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.model.dds.EquipmentStatus;
import com.soul.weapon.schedule.DDSMessageSending;
import com.soul.weapon.service.SendDDSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

/**
 * @ClassName SendDDSServiceImpl
 * @Description
 * @Author ShiZuan
 * @Date 2022/3/14 15:02
 * @Version
 **/
@Slf4j
@Service
public class SendDDSServiceImpl implements SendDDSService {

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    private CommonConfig config;

    @Autowired
    private RestTemplate restTemplate;



    @Override
    public String sendDDSBytestCode(String testCode)    {

        ClassPathResource classPathResource = new ClassPathResource("DDSTest.json");

        Map map = JSONArray.parseObject(FileUtils.readFile(classPathResource), Map.class);

        if (map.get(testCode) == null) {
            return "请输入正确任务编号";
        }
        ScheduledFuture future = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = String.valueOf(map.get(testCode));

        if (testCode.equals("1") || testCode.equals("2") || testCode.equals("3") || testCode.equals("4") || testCode.equals("5")) {
            if (Constant.threadMap.get(testCode) != null) {
                return "当前测试报文任务已开启，请先停止";
            }
            future = threadPoolTaskScheduler.schedule(new DDSMessageSending.taskTest1to5(headers, JsonUtils.deserializeList(json, EquipmentStatus.class), restTemplate), new CronTrigger("0/3 * * * * ?"));
            Constant.threadMap.put(testCode, future);
        }

        //目标信息
        if (testCode.equals("TargetInfo")) {
            if (Constant.threadMap.get(testCode) != null) {
                return "当前测试报文任务已开启，请先停止";
            }
            future = threadPoolTaskScheduler.schedule(new DDSMessageSending.taskTargetInfoDDS(headers,restTemplate), new CronTrigger("0/3 * * * * ?"));
            Constant.threadMap.put(testCode, future);
        }

        //目标指示
        if (testCode.equals("TargetInstructionsInfo")) {
            if (Constant.threadMap.get(testCode) != null) {
                return "当前测试报文任务已开启，请先停止";
            }
            future = threadPoolTaskScheduler.schedule(new DDSMessageSending.taskTargetInstructionsInfoDDS(headers, RedisUtils.getService(config.getPumpDataBase()),restTemplate), new CronTrigger("0/3 * * * * ?"));
            Constant.threadMap.put(testCode, future);
        }

        //目标火控
        if (testCode.equals("TargetFireControlInfo")) {
            if (Constant.threadMap.get(testCode) != null) {
                return "当前测试报文任务已开启，请先停止";
            }
            future = threadPoolTaskScheduler.schedule(new DDSMessageSending.taskTargetFireControlInfoDDS(headers, RedisUtils.getService(config.getPumpDataBase()),restTemplate), new CronTrigger("0/3 * * * * ?"));
            Constant.threadMap.put(testCode, future);
        }
        //发射架调转
        if (testCode.equals("LauncherRotationInfo")) {
            if (Constant.threadMap.get(testCode) != null) {
                return "当前测试报文任务已开启，请先停止";
            }
            future = threadPoolTaskScheduler.schedule(new DDSMessageSending.taskLauncherRotationInfoDDS(headers, RedisUtils.getService(config.getPumpDataBase()),restTemplate), new CronTrigger("0/3 * * * * ?"));
            Constant.threadMap.put(testCode, future);
        }

        //武器发射
        if (testCode.equals("EquipmentLaunchStatus")) {
            if (Constant.threadMap.get(testCode) != null) {
                return "当前测试报文任务已开启，请先停止";
            }
            future = threadPoolTaskScheduler.schedule(new DDSMessageSending.taskEquipmentLaunchStatusDDS(headers, RedisUtils.getService(config.getPumpDataBase()),restTemplate), new CronTrigger("0/3 * * * * ?"));
            Constant.threadMap.put(testCode, future);
        }
        return "DDS报文发送任务已启动，发送的任务编号为：" + testCode;
    }


    @Override
    public String stopSendDDSBytestCode(String testCode) {
        ScheduledFuture scheduledFuture = Constant.threadMap.get(testCode);
        if (scheduledFuture == null) {
            return "没有编号为：" + testCode + "的任务！";
        }
        scheduledFuture.cancel(true);

        while (!scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(true);
        }
        Constant.threadMap.remove(testCode);
        return "停止编号为：" + testCode + "的任务成功！";
    }

    @Override
    public String stopAll() {
        List resultList = new ArrayList();
        for (String key : Constant.threadMap.keySet()) {
            Constant.threadMap.get(key).cancel(true);
            while (!Constant.threadMap.get(key).isCancelled()) {
                Constant.threadMap.get(key).cancel(true);
            }
            Constant.threadMap.remove(key);
            resultList.add(key);
        }
        return "关闭任务成功，任务列表："+resultList.toString();
    }

    @Override
    public String startAllList(){
        List<String> keys = Constant.threadMap.keySet().stream().collect(Collectors.toList());
        return keys.toString();
    }

}
