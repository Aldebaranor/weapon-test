package com.soul.screen.controller.free;


import com.egova.exception.ExceptionUtils;
import com.egova.json.utils.JsonUtils;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.redis.RedisUtils;
import com.egova.web.annotation.Api;
import com.soul.screen.model.*;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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

    //当前目标ID
    private String SCREEN_TARGETID = "";

    private Boolean TARGETID_START = true;


    /**
     * 获取选中TargetID
     */
    @Api
    @GetMapping("/sendTargetId/{targetId}")
    public void sendTargetId(@PathVariable String targetId){
        this.SCREEN_TARGETID = targetId;
    }

    /**
     * 获取当前选择的TargetId
     */
    @Api
    @GetMapping("/getTargetId")
    public String getTargetId()
    {
        return this.SCREEN_TARGETID;
    }


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
            System.out.println("暂无对空计数数据!");
            return new ArrayList<ScreenUniversalData>();
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
    @GetMapping("/statusreport/watertype")
    public ScreenSrAndRtData getWatertypeStatusReportData() {
        String key1 = Constant.SCREEN_STATUSREPORTDATA_WATERTYPE;
        String key2 = String.format(Constant.SCREEN_STATUSREPORTDATA_WATERTYPE_TARGETID, this.SCREEN_TARGETID);
        if (!RedisUtils.getService(config.getScreenDataBase()).exists(key1) && !RedisUtils.getService(config.getScreenDataBase()).exists(key2)) {
            System.out.println("暂无水下周期报数据!");
            return new ScreenSrAndRtData();
        }
        return getScreenStatusReportData(key1, key2, this.SCREEN_TARGETID);
    }

    /**
     * 获取对空状态周期报
     */
    @Api
    @GetMapping("/statusreport/airtype")
    public ScreenSrAndRtData getAirtypeStatusReportData() {
        String key1 = Constant.SCREEN_STATUSREPORTDATA_AIRTYPE;
        String key2 = String.format(Constant.SCREEN_STATUSREPORTDATA_AIRTYPE_TARGETID, this.SCREEN_TARGETID);
        if (!RedisUtils.getService(config.getScreenDataBase()).exists(key1) && !RedisUtils.getService(config.getScreenDataBase()).exists(key2)) {
            throw ExceptionUtils.api("暂无對空周期报数据!");
        }
        return getScreenStatusReportData(key1, key2, this.SCREEN_TARGETID);
    }

    /**
     * 获取水下通道时间
     */
    @Api
    @GetMapping("/responsetime/underwater")
    public ScreenSrAndRtData getUnderwarterResponseTime(){
        String key = String.format(Constant.SCREEN_RESPONSETIME_WATERTYPE_TARGETID,this.SCREEN_TARGETID);
        if (!RedisUtils.getService(config.getScreenDataBase()).exists(key)) {

            System.out.println("暂无水下响应时间数据!");
            return new ScreenSrAndRtData();
        }
        List<ScreenUniversalData> screenUniversalDataList = RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).values().stream().map(v -> {
            ScreenUniversalData deserialize = JsonUtils.deserialize(v, ScreenUniversalData.class);
            return deserialize;
        }).collect(Collectors.toList());

        screenUniversalDataList.sort(new Comparator<ScreenUniversalData>() {
            @Override
            public int compare(ScreenUniversalData o1, ScreenUniversalData o2) {
                return Integer.valueOf(o1.getType()) - Integer.valueOf(o2.getType());
            }
        });
        ScreenSrAndRtData screenSrAndRtData = new ScreenSrAndRtData();
        screenSrAndRtData.setTargetId(this.SCREEN_TARGETID);
        screenSrAndRtData.setReportData(screenUniversalDataList);
        return screenSrAndRtData;
    }

    /**
     * 获取对空通道时间
     */
    @Api
    @GetMapping("/responsetime/air")
    public ScreenSrAndRtData getAirtypeResponseTime(){
        String key = String.format(Constant.SCREEN_RESPONSETIME_AIRTYPE_TARGETID,this.SCREEN_TARGETID);
        if (!RedisUtils.getService(config.getScreenDataBase()).exists(key)) {
            throw ExceptionUtils.api("暂无对空响应时间数据!");
        }
        List<ScreenUniversalData> screenUniversalDataList = RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).values().stream().map(v -> {
            ScreenUniversalData deserialize = JsonUtils.deserialize(v, ScreenUniversalData.class);
            return deserialize;
        }).collect(Collectors.toList());
        ScreenSrAndRtData screenSrAndRtData = new ScreenSrAndRtData();
        screenSrAndRtData.setTargetId(this.SCREEN_TARGETID);
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
        if (!RedisUtils.getService(config.getScreenDataBase()).exists(key)) {
            System.out.println("暂无水下任务通道数据!");
            return new ArrayList<ScreenTctData>();
        }
        List<ScreenTctData> screenTctDataList = RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).values().stream().map(v -> {
            ScreenTctData objects = JsonUtils.deserialize(v, ScreenTctData.class);
            return objects;
        }).collect(Collectors.toList());

        screenTctDataList.sort(new Comparator<ScreenTctData>() {
            @Override
            public int compare(ScreenTctData o1, ScreenTctData o2) {
                return Integer.valueOf(o1.getTargetId()) - Integer.valueOf(o2.getTargetId());
            }
        });
        return screenTctDataList;
    }

    /**
     * 获取水下任务通道报文
     */
    @Api
    @PostMapping("/page/taskchannelstatus/underwater")
    public PageResult<ScreenTctData> getPageUnberwaterTaskChannelStatus(@RequestBody QueryModel<ScreenTctData> model){
        String key = Constant.SCREEN_TASKCHANNELSTATUS_WATERTYPE;
        if (!RedisUtils.getService(config.getScreenDataBase()).exists(key)) {
            System.out.println("暂无水下任务通道数据!");
            return null;
        }
        List<ScreenTctData> screenTctDataList = RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).values().stream().map(v -> {
            ScreenTctData objects = JsonUtils.deserialize(v, ScreenTctData.class);
            return objects;
        }).collect(Collectors.toList());

        screenTctDataList.sort(new Comparator<ScreenTctData>() {
            @Override
            public int compare(ScreenTctData o1, ScreenTctData o2) {
                return Integer.valueOf(o1.getTargetId()) - Integer.valueOf(o2.getTargetId());
            }
        });

        ArrayList<ScreenTctData> result = new ArrayList<>();
        Long pageIndex = model.getPaging().getPageIndex();
        Long pageSize = model.getPaging().getPageSize();

        if ((pageIndex-1)*pageSize > screenTctDataList.size()) {
            return null;
        }
        if ((screenTctDataList.size() - (pageSize*pageIndex)) < pageSize) {
            for (long i = (pageIndex-1)*pageSize; i < screenTctDataList.size(); i++) {
                result.add(screenTctDataList.get((int) i));
            }
        }else{
            for (long i = (pageIndex-1)*pageSize; i < pageIndex*pageSize; i++) {
                result.add(screenTctDataList.get((int) i));
            }
        }

        return PageResult.of(result,screenTctDataList.size());
    }

    /**
     * 获取对空任务通道报文
     */
    @Api
    @GetMapping("/taskchannelstatus/airtype")
    public List<ScreenTctData> getAirtypeTaskChannelStatus(){
        String key = Constant.SCREEN_TASKCHANNELSTATUS_AIRTYPE;
        if (!RedisUtils.getService(config.getScreenDataBase()).exists(key)) {
            throw ExceptionUtils.api("暂无对空任务通道数据!");
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
    @PostMapping("/page/taskchannelstatus/air")
    public PageResult<ScreenTctData> getPageAirTaskChannelStatus(@RequestBody QueryModel<ScreenTctData> model){
        String key = Constant.SCREEN_TASKCHANNELSTATUS_AIRTYPE;
        if (!RedisUtils.getService(config.getScreenDataBase()).exists(key)) {
            System.out.println("暂无空中任务通道数据!");
            return null;
        }
        List<ScreenTctData> screenTctDataList = RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).values().stream().map(v -> {
            ScreenTctData objects = JsonUtils.deserialize(v, ScreenTctData.class);
            return objects;
        }).collect(Collectors.toList());

        screenTctDataList.sort(new Comparator<ScreenTctData>() {
            @Override
            public int compare(ScreenTctData o1, ScreenTctData o2) {
                return Integer.valueOf(o1.getTargetId()) - Integer.valueOf(o2.getTargetId());
            }
        });

        ArrayList<ScreenTctData> result = new ArrayList<>();
        Long pageIndex = model.getPaging().getPageIndex();
        Long pageSize = model.getPaging().getPageSize();

        if ((pageIndex-1)*pageSize > screenTctDataList.size()) {
            return null;
        }
        if ((screenTctDataList.size() - (pageSize*pageIndex)) < pageSize) {
            for (long i = (pageIndex-1)*pageSize; i < screenTctDataList.size(); i++) {
                result.add(screenTctDataList.get((int) i));
            }
        }else{
            for (long i = (pageIndex-1)*pageSize; i < pageIndex*pageSize; i++) {
                result.add(screenTctDataList.get((int) i));
            }
        }

        return PageResult.of(result,screenTctDataList.size());
    }

    /**
     * 获取水下探测器精度报文
     */
    @Api
    @GetMapping("/detectoraccuracy/underwater/{number}")
    public ScreenAccuracyData getUnderwaterScreenDetectorAccuracyData(@PathVariable Integer number){
        ScreenAccuracyData result = new ScreenAccuracyData();
        String key = String.format(Constant.SCREEN_SENSORACCURACY_WATERTYPE_TARGETID,this.SCREEN_TARGETID);

        if (!RedisUtils.getService(config.getScreenDataBase()).exists(key)) {
            result.setTargetId(this.SCREEN_TARGETID);
            result.setStatus(true);
            result.setAccuracyData(new ArrayList<>(0));
        }
        List<AccuracyData> list = RedisUtils.getService(config.getScreenDataBase()).boundZSetOps(key).range(0, -1).stream().map(v -> {
            AccuracyData deserialize = JsonUtils.deserialize(v, AccuracyData.class);
            return deserialize;
        }).collect(Collectors.toList());

        list.sort(new Comparator<AccuracyData>() {
            @Override
            public int compare(AccuracyData o1, AccuracyData o2) {
                return (int) (o1.getTime() - o2.getTime());
            }
        });
        result.setTargetId(this.SCREEN_TARGETID);
//        result.setStatus(false);
        if (list.size() > number || list.size() == number) {
            result.setAccuracyData(list.subList(number,list.size()));
        }else{
            result.setAccuracyData(list);
        }
        return result;
    }
    /**
     * 获取水下发射架精度报文
     */
    @Api
    @GetMapping("/launcheraccuracy/underwater/{number}")
    public ScreenAccuracyData getUnderwaterScreenLauncherAccuracyData(@PathVariable Integer number){
        ScreenAccuracyData result = new ScreenAccuracyData();
        String key = String.format(Constant.SCREEN_LAUNCHERROTATIONACCURACY_WATERTYPE_TARGETID,this.SCREEN_TARGETID);
        if (!RedisUtils.getService(config.getScreenDataBase()).exists(key)) {
            result.setTargetId(this.SCREEN_TARGETID);
            result.setStatus(true);
            result.setAccuracyData(new ArrayList<>(0));
        }
        List<AccuracyData> list = RedisUtils.getService(config.getScreenDataBase()).boundZSetOps(key).range(0, -1).stream().map(v -> {
            AccuracyData deserialize = JsonUtils.deserialize(v, AccuracyData.class);
            return deserialize;
        }).collect(Collectors.toList());
        result.setTargetId(this.SCREEN_TARGETID);
//        result.setStatus(false);
        if (list.size() > number || list.size() == number) {
            result.setAccuracyData(list.subList(number,list.size()));
        }else{
            result.setAccuracyData(list);
        }
        return result;
    }

    /**
     * 获取空中探测器精度报文
     */
    @Api
    @GetMapping("/detectoraccuracy/air/{number}")
    public ScreenAccuracyData getAirScreenDetectorAccuracyData(@PathVariable Integer number){
        ScreenAccuracyData result = new ScreenAccuracyData();
        String key = String.format(Constant.SCREEN_SENSORACCURACY_AIRTYPE_TARGETID,this.SCREEN_TARGETID);

        if (!RedisUtils.getService(config.getScreenDataBase()).exists(key)) {
            result.setTargetId(this.SCREEN_TARGETID);
            result.setStatus(true);
            result.setAccuracyData(new ArrayList<>(0));
        }
        List<AccuracyData> list = RedisUtils.getService(config.getScreenDataBase()).boundZSetOps(key).range(0, -1).stream().map(v -> {
            AccuracyData deserialize = JsonUtils.deserialize(v, AccuracyData.class);
            return deserialize;
        }).collect(Collectors.toList());

        list.sort(new Comparator<AccuracyData>() {
            @Override
            public int compare(AccuracyData o1, AccuracyData o2) {
                return (int) (o1.getTime() - o2.getTime());
            }
        });
        result.setTargetId(this.SCREEN_TARGETID);
//        result.setStatus(false);
        if (list.size() > number || list.size() == number) {
            result.setAccuracyData(list.subList(number,list.size()));
        }else{
            result.setAccuracyData(list);
        }
        return result;
    }
    /**
     * 获取空中发射架精度报文
     */
    @Api
    @GetMapping("/launcheraccuracy/air/{number}")
    public ScreenAccuracyData getScreenLauncherAccuracyData(@PathVariable Integer number){
        ScreenAccuracyData result = new ScreenAccuracyData();
        String key = String.format(Constant.SCREEN_LAUNCHERROTATIONACCURACY_AIRTYPE_TARGETID,this.SCREEN_TARGETID);
        if (!RedisUtils.getService(config.getScreenDataBase()).exists(key)) {
            result.setTargetId(this.SCREEN_TARGETID);
            result.setStatus(true);
            result.setAccuracyData(new ArrayList<>(0));
        }
        List<AccuracyData> list = RedisUtils.getService(config.getScreenDataBase()).boundZSetOps(key).range(0, -1).stream().map(v -> {
            AccuracyData deserialize = JsonUtils.deserialize(v, AccuracyData.class);
            return deserialize;
        }).collect(Collectors.toList());
        result.setTargetId(this.SCREEN_TARGETID);
//        result.setStatus(false);
        if (list.size() > number || list.size() == number) {
            result.setAccuracyData(list.subList(number,list.size()));
        }else{
            result.setAccuracyData(list);
        }
        return result;
    }


    /**
     * 获取树状图状态报文
     * @return
     */
    @Api
    @GetMapping("/tree/status/underwater")
    public FlowchartStatus getUnderWaterFlowchartStatus(){
        String key = Constant.SCREEN_LIUCHENGTU_WATERTYPE;
        FlowchartStatus flowchartStatus = new FlowchartStatus();
        flowchartStatus.setWeaponType(new HashSet<>(0));
        if (RedisUtils.getService(config.getScreenDataBase()).exists(key)) {
            if (RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).entries().containsKey(this.SCREEN_TARGETID)) {
                String json = RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).entries().get(this.SCREEN_TARGETID);
                FlowchartStatus deserialize = JsonUtils.deserialize(json, FlowchartStatus.class);
                return deserialize;
            }
        }
        return flowchartStatus;
    }

    /**
     * 获取树状图状态报文
     * @return
     */
    @Api
    @GetMapping("/tree/status/air")
    public FlowchartStatus getAirFlowchartStatus(){
        String key = Constant.SCREEN_LIUCHENGTU_AIRTYPE;
        FlowchartStatus flowchartStatus = new FlowchartStatus();
        flowchartStatus.setWeaponType(new HashSet<>(0));
        if (RedisUtils.getService(config.getScreenDataBase()).exists(key)) {
            if (RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).entries().containsKey(this.SCREEN_TARGETID)) {
                String json = RedisUtils.getService(config.getScreenDataBase()).boundHashOps(key).entries().get(this.SCREEN_TARGETID);
                FlowchartStatus deserialize = JsonUtils.deserialize(json, FlowchartStatus.class);
                return deserialize;
            }
        }
        return flowchartStatus;
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
        screenUniversalDataList1.sort(new Comparator<ScreenUniversalData>() {
            @Override
            public int compare(ScreenUniversalData o1, ScreenUniversalData o2) {
                return Integer.valueOf(o1.getType()) - Integer.valueOf(o2.getType());
            }
        });
        ScreenSrAndRtData screenSrAndRtData = new ScreenSrAndRtData();
        screenSrAndRtData.setTargetId(targetId);
        screenSrAndRtData.setReportData(screenUniversalDataList1);
        return screenSrAndRtData;
    }
}
