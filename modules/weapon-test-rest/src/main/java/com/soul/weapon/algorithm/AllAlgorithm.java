package com.soul.weapon.algorithm;

import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.soul.weapon.config.CommonRedisConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.entity.HistoryInfo;
import com.soul.weapon.entity.PipeHistory;
import com.soul.weapon.model.dds.*;
import com.soul.weapon.service.PipeHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: nash5
 * @date: 2021-11-15 16:08
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AllAlgorithm {
    private final CommonRedisConfig commonRedisConfig;


    /** 威胁判断-传感器探测时间阈值 **/

    private Long DetectorTimeThreshold = 2L;

    private Long ExecutionTimeThreshold = 60L;

    private final PipeHistoryService pipeHistoryService;

    /**
     * 反导舰炮算法实现
     */
    public void antiMissileShipGun() {
        if(!Boolean.TRUE.equals(RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate()
                .hasKey(Constant.EQUIPMENT_STATUS_HTTP_KEY))) {
            log.error("从Redis中获取装备信息失败！");
            return ;
        }

        Map<String, String> tmpEquipments = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).
                boundHashOps(Constant.EQUIPMENT_STATUS_HTTP_KEY).entries();
        assert tmpEquipments != null;
        Map<String, EquipmentStatus> allEquipmentStatus = tmpEquipments.entrySet().stream().collect(Collectors.toMap(
                    Map.Entry::getKey,
                    pair -> JsonUtils.deserialize(pair.getValue(), EquipmentStatus.class))
                );

        // TODO: 写一个处理通道测试的算法，主要就是传入不同通道的id即可，返回的是<时间 long, 0/1>;

        return ;
    }

    /**
     * 威胁判断算法-7
     */
    public void threatenEstimation(){
        StringRedisTemplate template = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate();
        if(!template.hasKey(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY)||!template.hasKey(Constant.TARGET_INFO_HTTP_KEY)){
            log.error("从Redis中获取目标指示信息或目标真值信息失败！");
            return ;
        }

        Map<String,String> tmpTargetInfos = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).boundHashOps(Constant.TARGET_INFO_HTTP_KEY).entries();
        assert  tmpTargetInfos != null;
        Map<String, TargetInfo> allTargetInfos = tmpTargetInfos.entrySet().stream().collect(Collectors.toMap(
                    Map.Entry::getKey,
                    pair->JsonUtils.deserialize(pair.getValue(),TargetInfo.class))
        );

        Map<String,String> tmpInstructionInfos = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).boundHashOps(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY).entries();
        assert  tmpInstructionInfos != null;
        Map<String, TargetInstructionsInfo> allInstructionInfos = tmpInstructionInfos.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair->JsonUtils.deserialize(pair.getValue(),TargetInstructionsInfo.class))
        );

        // 用双重循环做的两两匹配测试，估计得优化
        for(String key:allTargetInfos.keySet()){

             TargetInfo targetInfo = allTargetInfos.get(key);

             for(String key2:allInstructionInfos.keySet()){

                 TargetInstructionsInfo targetInstructionsInfo = allInstructionInfos.get(key2);

                 if(targetInfo.getTargetId()==targetInstructionsInfo.getTargetId()){

                     if(Math.abs(targetInfo.getTime()-targetInstructionsInfo.getTime())<DetectorTimeThreshold){

                         HistoryInfo.ThreatenReport threatenReport = new HistoryInfo.ThreatenReport();
                         threatenReport.setId(targetInfo.getTargetId());
                         threatenReport.setType(targetInfo.getTargetTypeId());
                         threatenReport.setDistanceOffset(Math.abs(targetInfo.getDistance()-targetInstructionsInfo.getDistance()));
                         threatenReport.setTime(targetInstructionsInfo.getTime());
                         threatenReport.setPitchOffset(Math.abs(targetInfo.getPitchAngle()-targetInstructionsInfo.getPitchAngle()));
                         threatenReport.setSpeedOffset(Math.abs(targetInfo.getSpeed()-targetInstructionsInfo.getSpeed()));

                         PipeHistory pipeHistory = new PipeHistory();
                         pipeHistory.setId(targetInfo.getTargetId()+"_"+targetInfo.getTime());
                         pipeHistory.setCreateTime(new Timestamp(System.currentTimeMillis()));
                         pipeHistory.setType("7");
                         pipeHistory.setDisabled(false);
                         pipeHistory.setRes(JsonUtils.serialize(threatenReport));
                         pipeHistoryService.insert(pipeHistory);

                     }
                     break;
                 }
             }
        }

    }

    /**
     * 执行情况测试-9
     */
    public void executionStatusTest(){

        StringRedisTemplate template = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate();
        if(!template.hasKey(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY)||!template.hasKey(Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY)||!template.hasKey(Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY)){
            log.error("从Redis中获取信息失败！");
            return ;
        }

        Map<String,String> tmpFireControlInfos = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).boundHashOps(Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY).entries();
        assert  tmpFireControlInfos != null;
        Map<String, TargetFireControlInfo> allFireControlInfos = tmpFireControlInfos.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair->JsonUtils.deserialize(pair.getValue(),TargetFireControlInfo.class))
        );

        Map<String,String> tmpInstructionInfos = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).boundHashOps(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY).entries();
        assert  tmpInstructionInfos != null;
        Map<String, TargetInstructionsInfo> allInstructionInfos = tmpInstructionInfos.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair->JsonUtils.deserialize(pair.getValue(),TargetInstructionsInfo.class))
        );

        Map<String,String> tmpEquipmentLaunchInfos = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).boundHashOps(Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY).entries();
        assert  tmpEquipmentLaunchInfos != null;
        Map<String, EquipmentLaunchStatus> allEquipmentLaunchInfos = tmpEquipmentLaunchInfos.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair->JsonUtils.deserialize(pair.getValue(),EquipmentLaunchStatus.class))
        );

        for(String key:allFireControlInfos.keySet()) {

            TargetFireControlInfo targetFireControlInfo = allFireControlInfos.get(key);

            for(String key2:allInstructionInfos.keySet()){

                TargetInstructionsInfo targetInstructionsInfo = allInstructionInfos.get(key2);

                for(String key3:allEquipmentLaunchInfos.keySet()){

                    EquipmentLaunchStatus equipmentLaunchStatus = allEquipmentLaunchInfos.get(key3);

                    if(targetFireControlInfo.getTargetId() == targetInstructionsInfo.getTargetId() && targetFireControlInfo.getTargetId() == equipmentLaunchStatus.getTargetId()){

                        HistoryInfo.ExecutionStatusReport executionStatusReport = new HistoryInfo.ExecutionStatusReport();

                        boolean b = equipmentLaunchStatus.getTime()>targetFireControlInfo.getTime() && targetFireControlInfo.getTime()>targetInstructionsInfo.getTime();
                        boolean c = equipmentLaunchStatus.getTime()-targetInstructionsInfo.getTime()<ExecutionTimeThreshold;

                        executionStatusReport.setTargetId(equipmentLaunchStatus.getTargetId());
                        executionStatusReport.setTime(equipmentLaunchStatus.getTime());
                        executionStatusReport.setTargetType(equipmentLaunchStatus.getTargetTypeId());

                        if(b&&c){
                            executionStatusReport.setStatus(true);
                        }else{
                            executionStatusReport.setStatus(false);
                        }

                        PipeHistory pipeHistory = new PipeHistory();
                        pipeHistory.setId("executionStatus_"+equipmentLaunchStatus.getTargetId()+"_"+equipmentLaunchStatus.getTime());
                        pipeHistory.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        pipeHistory.setType("8");
                        pipeHistory.setDisabled(false);
                        pipeHistory.setRes(JsonUtils.serialize(executionStatusReport));
                        pipeHistoryService.insert(pipeHistory);


                    }
                }

            }

        }


        }


    public void interceptionDistanceTest(){

        if(!Boolean.TRUE.equals(RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate()
                .hasKey(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY))) {
            log.error("从Redis中获取目标指示信息失败！");
            return ;
        }

        Map<String, String> tmpInstructions = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).boundHashOps(Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY).entries();
        assert tmpInstructions != null;

        Map<String, EquipmentStatus> allEquipmentStatus = tmpInstructions.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), EquipmentStatus.class))
        );

    }

}
