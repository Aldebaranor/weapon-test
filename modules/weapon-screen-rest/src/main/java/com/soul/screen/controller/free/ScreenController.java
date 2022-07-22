package com.soul.screen.controller.free;


import com.egova.exception.ExceptionUtils;
import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.egova.web.annotation.Api;
import com.soul.screen.model.ScreenDetectorAccuracyData;
import com.soul.screen.model.ScreenSrAndRtData;
import com.soul.screen.model.ScreenTctData;
import com.soul.screen.model.ScreenUniversalData;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ScreenController
 * @Description 大屏接口层
 * @Author ShiZuan
 * @Date 2022/7/20 14:11
 * @Version
 **/
@Slf4j
@RestController
@RequestMapping("/free/screen")
public class ScreenController {


    @Autowired
    private CommonConfig config;


    /**
     * 获取水下数量统计
     *
     * @return
     */
    @Api
    @GetMapping("/count/watertype")
    public List<ScreenUniversalData> getWatertypeCountData() {


        String key = Constant.SCREEN_COUNT_WATERTYPE;

        if (!RedisUtils.getService(config.getScreenDataBase()).exists(key)) {
            throw ExceptionUtils.api("暂无水下计数数据!");
        }
        List<ScreenUniversalData> screenUniversalDataList = RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).values().stream().map(v -> {
            ScreenUniversalData deserialize = JsonUtils.deserialize(v, ScreenUniversalData.class);
            return deserialize;
        }).collect(Collectors.toList());
        return screenUniversalDataList;
    }

    /**
     * 获取对空数量统计
     *
     * @return
     */
    @Api
    @GetMapping("/count/airtype")
    public List<ScreenUniversalData> getAirtypeCountData() {

        String key = Constant.SCREEN_COUNT_AIRTYPE;

        if (!RedisUtils.getService(config.getScreenDataBase()).exists(key)) {
            throw ExceptionUtils.api("暂无对空计数数据!");
        }
        List<ScreenUniversalData> screenUniversalDataList = RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).values().stream().map(v -> {
            ScreenUniversalData deserialize = JsonUtils.deserialize(v, ScreenUniversalData.class);
            return deserialize;
        }).collect(Collectors.toList());
        return screenUniversalDataList;
    }

    /**
     * 获取水下状态周期报
     */
    @Api
    @GetMapping("/statusreport/watertype/{targetId}")
    public ScreenSrAndRtData getWatertypeStatusReportData(@PathVariable String targetId) {
        String key1 = Constant.SCREEN_STATUSREPORTDATA_WATERTYPE;
        String key2 = String.format(Constant.SCREEN_STATUSREPORTDATA_WATERTYPE_TARGETID, targetId);
        if (!RedisUtils.getService(config.getScreenDataBase()).exists(key1) && !RedisUtils.getService(config.getScreenDataBase()).exists(key2)) {
            throw ExceptionUtils.api("暂无水下周期报数据!");
        }
        return getScreenStatusReportData(key1, key2, targetId);
    }

    /**
     * 获取对空状态周期报
     */
    @Api
    @GetMapping("/statusreport/airtype/{targetId}")
    public ScreenSrAndRtData getAirtypeStatusReportData(@PathVariable String targetId) {
        String key1 = Constant.SCREEN_STATUSREPORTDATA_AIRTYPE;
        String key2 = String.format(Constant.SCREEN_STATUSREPORTDATA_AIRTYPE_TARGETID, targetId);
        if (!RedisUtils.getService(config.getScreenDataBase()).exists(key1) && !RedisUtils.getService(config.getScreenDataBase()).exists(key2)) {
            throw ExceptionUtils.api("暂无對空周期报数据!");
        }
        return getScreenStatusReportData(key1, key2, targetId);
    }

    /**
     * 获取水下状态周期报
     */
    @Api
    @GetMapping("/responsetime/underwater/{targetId}")
    public ScreenSrAndRtData getUnderwarterResponseTime(@PathVariable String targetId){
        String key = String.format(Constant.SCREEN_RESPONSETIME_WATERTYPE_TARGETID,targetId);
        if (RedisUtils.getService(config.getScreenDataBase()).exists(key)) {
            throw ExceptionUtils.api("暂无水下响应时间数据!");
        }
        List<ScreenUniversalData> screenUniversalDataList = RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).values().stream().map(v -> {
            ScreenUniversalData deserialize = JsonUtils.deserialize(v, ScreenUniversalData.class);
            return deserialize;
        }).collect(Collectors.toList());
        ScreenSrAndRtData screenSrAndRtData = new ScreenSrAndRtData();
        screenSrAndRtData.setTargetId(targetId);
        screenSrAndRtData.setReportData(screenUniversalDataList);
        return screenSrAndRtData;
    }


    /**
     * 获取对空状态周期报
     */
    @Api
    @GetMapping("/responsetime/airtype/{targetId}")
    public ScreenSrAndRtData getAirtypeResponseTime(@PathVariable String targetId){
        String key = String.format(Constant.SCREEN_RESPONSETIME_AIRTYPE_TARGETID,targetId);
        if (RedisUtils.getService(config.getScreenDataBase()).exists(key)) {
            throw ExceptionUtils.api("暂无对空响应时间数据!");
        }
        List<ScreenUniversalData> screenUniversalDataList = RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).values().stream().map(v -> {
            ScreenUniversalData deserialize = JsonUtils.deserialize(v, ScreenUniversalData.class);
            return deserialize;
        }).collect(Collectors.toList());
        ScreenSrAndRtData screenSrAndRtData = new ScreenSrAndRtData();
        screenSrAndRtData.setTargetId(targetId);
        screenSrAndRtData.setReportData(screenUniversalDataList);
        return screenSrAndRtData;
    }

    /**
     * 获取水下任务通道报文
     */
    @Api
    @GetMapping("/taskchannelstatus/underwater")
    public List<ScreenTctData> getUnberwaterTaskChannelStatus(){
        String key = Constant.SCREEN_TASKCHANNELSTATUS_WATERTYPE;
        if (RedisUtils.getService(config.getScreenDataBase()).exists(key)) {
            throw ExceptionUtils.api("暂无水下任务通道数据!");
        }
        List<ScreenTctData> screenTctDataList = RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).values().stream().map(v -> {
            ScreenTctData objects = JsonUtils.deserialize(v, ScreenTctData.class);
            return objects;
        }).collect(Collectors.toList());
        return screenTctDataList;
    }

    /**
     * 获取对空任务通道报文
     */
    @Api
    @GetMapping("/taskchannelstatus/airtype")
    public List<ScreenTctData> getAirtypeTaskChannelStatus(){
        String key = Constant.SCREEN_TASKCHANNELSTATUS_AIRTYPE;
        if (RedisUtils.getService(config.getScreenDataBase()).exists(key)) {
            throw ExceptionUtils.api("暂无对空任务通道数据!");
        }
        List<ScreenTctData> screenTctDataList = RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).values().stream().map(v -> {
            ScreenTctData objects = JsonUtils.deserialize(v, ScreenTctData.class);
            return objects;
        }).collect(Collectors.toList());
        return screenTctDataList;
    }

    /**
     * 获取探测器精度报文
     */
    @Api
    @GetMapping("/detectoraccuracy/underwater/{targetId}")
    public ScreenDetectorAccuracyData  getScreenDetectorAccuracyData(@PathVariable String targetId){
        return null;
    }




    /**
     * 获取周期报
     * @param key1
     * @param key2
     * @param targetId
     * @return
     */
    private ScreenSrAndRtData getScreenStatusReportData(String key1, String key2 , String targetId) {
        List<ScreenUniversalData> screenUniversalDataList1 = RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key1).values().stream().map(v -> {
            ScreenUniversalData deserialize = JsonUtils.deserialize(v, ScreenUniversalData.class);
            return deserialize;
        }).collect(Collectors.toList());
        List<ScreenUniversalData> screenUniversalDataList2 = RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key2).values().stream().map(v -> {
            ScreenUniversalData deserialize = JsonUtils.deserialize(v, ScreenUniversalData.class);
            return deserialize;
        }).collect(Collectors.toList());

        screenUniversalDataList1.addAll(screenUniversalDataList2);

        ScreenSrAndRtData screenSrAndRtData = new ScreenSrAndRtData();
        screenSrAndRtData.setTargetId(targetId);
        screenSrAndRtData.setReportData(screenUniversalDataList1);
        return screenSrAndRtData;
    }
}
