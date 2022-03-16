package com.soul.weapon.service.impl;

import com.egova.exception.ExceptionUtils;
import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisService;
import com.egova.redis.RedisUtils;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.config.WeaponTestConstant;
import com.soul.weapon.model.StatusAnalysisEvaluation;
import com.soul.weapon.model.dds.*;
import com.soul.weapon.service.StatusAnalysisEvaluationService;
import com.soul.weapon.utils.DateParserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName StatusAnalysisEvaluationServiceImpl
 * @Description 获取全部状态分析评估
 * @Author ShiZuan
 * @Date 2022/3/9 15:22
 * @Version
 **/

@Slf4j
@Service
public class StatusAnalysisEvaluationServiceImpl implements StatusAnalysisEvaluationService {

    @Autowired
    private CommonConfig config;


    @Override
    public Map<String, List<StatusAnalysisEvaluation>> getStatusAnglysisAll() {

        RedisService redisTemplate = RedisUtils.getService(config.getPumpDataBase());

        Map<String, List<StatusAnalysisEvaluation>> resultMap = new HashMap<>();

        resultMap.put(Constant.AIRTYPE, new ArrayList<>());
        resultMap.put(Constant.WATERTYPE, new ArrayList<>());


        String targetInfoKey = String.format("%s:%s:*", Constant.TARGET_INFO_HTTP_KEY, DateParserUtils.getTime());

        Set keys = redisTemplate.keys(targetInfoKey);

        if (keys == null || keys.size() == 0) {
            throw ExceptionUtils.api("当前没有目标，无法进行状态分析评估");
        }
        for (Object key : keys) {
            //获取当前目标探测时间最新的数据
            TargetInfo targetInfo = JsonUtils.deserialize(String.valueOf(redisTemplate.getTemplate().opsForZSet().reverseRange(String.valueOf(key), 0, 0).iterator().next()), TargetInfo.class);

            //获取当前目标目指探测时间最新的数据
            String targetInstructionsInfoKey = String.format("%s:%s:%s", Constant.TARGET_INSTRUCTIONS_INFO_HTTP_KEY, DateParserUtils.getTime(), targetInfo.getTargetId());
            Set<String> set = redisTemplate.getTemplate().opsForZSet().reverseRangeByScore(targetInstructionsInfoKey, targetInfo.getTime(), Long.MAX_VALUE, 0, 1);
            if (set == null || set.size() == 0) {
                resultMap.get(targetInfo.getTargetTypeId()).add(new StatusAnalysisEvaluation(targetInfo.getTargetId()));
                continue;

            }

            TargetInstructionsInfo targetInstructionsInfo = JsonUtils.deserialize(set.iterator().next(), TargetInstructionsInfo.class);
            //获取当前目标火控诸元时间最新的数据
            String targetControlInfoKey = String.format("%s:%s:%s", Constant.TARGET_FIRE_CONTROL_INFO_HTTP_KEY, DateParserUtils.getTime(), targetInfo.getTargetId());
            Set<String> set1 = redisTemplate.getTemplate().opsForZSet().reverseRangeByScore(targetControlInfoKey, targetInstructionsInfo.getTime(), Long.MAX_VALUE, 0, 1);
            if (set1 == null || set1.size() == 0) {
                resultMap.get(targetInfo.getTargetTypeId()).add(new StatusAnalysisEvaluation(
                        targetInfo.getTargetId(),
                        WeaponTestConstant.MAP_ID_TO_SHOW.get(targetInstructionsInfo.getEquipmentId()),
                        targetInstructionsInfo.getTime()));
                continue;
            }
            TargetFireControlInfo targetControlInfo = JsonUtils.deserialize(set1.iterator().next(), TargetFireControlInfo.class);

            //获取当前目标发射架调转时间最新的数据
            String launcherRotationInfoKey = String.format("%s:%s:%s", Constant.LAUNCHER_ROTATION_INFO_HTTP_KEY_2, DateParserUtils.getTime(), targetInfo.getTargetId());
            Set<String> set3 = redisTemplate.getTemplate().opsForZSet().reverseRangeByScore(launcherRotationInfoKey, targetControlInfo.getTime(), Long.MAX_VALUE, 0, 1);
            if (set3 == null || set3.size() == 0) {
                resultMap.get(targetInfo.getTargetTypeId()).add(new StatusAnalysisEvaluation(
                        targetInfo.getTargetId(),
                        WeaponTestConstant.MAP_ID_TO_SHOW.get(targetInstructionsInfo.getEquipmentId()),
                        targetInstructionsInfo.getTime(),
                        WeaponTestConstant.MAP_ID_TO_SHOW.get(targetControlInfo.getFireControlSystemId()),
                        targetControlInfo.getTime()));
                continue;
            }
            LauncherRotationInfo launcherRotationInfo = JsonUtils.deserialize(set3.iterator().next(), LauncherRotationInfo.class);

            //获取当前目标武器发射时间最新的数据
            String equipmentLaunchStatusKey = String.format("%s:%s:%s", Constant.EQUIPMENT_LAUNCH_STATUS_HTTP_KEY, DateParserUtils.getTime(), targetInfo.getTargetId());
            Set<String> set4 = redisTemplate.getTemplate().opsForZSet().reverseRangeByScore(equipmentLaunchStatusKey, targetControlInfo.getTime(), Long.MAX_VALUE, 0, 1);
            if (set4 == null || set4.size() == 0) {
                resultMap.get(targetInfo.getTargetTypeId()).add(new StatusAnalysisEvaluation(
                        targetInfo.getTargetId(),
                        WeaponTestConstant.MAP_ID_TO_SHOW.get(targetInstructionsInfo.getEquipmentId()),
                        targetInstructionsInfo.getTime(),
                        WeaponTestConstant.MAP_ID_TO_SHOW.get(targetControlInfo.getFireControlSystemId()),
                        targetControlInfo.getTime(),
                        WeaponTestConstant.MAP_ID_TO_SHOW.get(launcherRotationInfo.getLauncherId()),
                        launcherRotationInfo.getTime()));
                continue;
            }

            EquipmentLaunchStatus equipmentLaunchStatusInfo = JsonUtils.deserialize(set4.iterator().next(), EquipmentLaunchStatus.class);

            resultMap.get(targetInfo.getTargetTypeId()).add(new StatusAnalysisEvaluation(
                    targetInfo.getTargetId(),
                    WeaponTestConstant.MAP_ID_TO_SHOW.get(targetInstructionsInfo.getEquipmentId()),
                    targetInstructionsInfo.getTime(),
                    WeaponTestConstant.MAP_ID_TO_SHOW.get(targetControlInfo.getFireControlSystemId()),
                    targetControlInfo.getTime(),
                    WeaponTestConstant.MAP_ID_TO_SHOW.get(launcherRotationInfo.getLauncherId()),
                    launcherRotationInfo.getTime(),
                    WeaponTestConstant.MAP_ID_TO_SHOW.get(equipmentLaunchStatusInfo.getEquipmentId()),
                    equipmentLaunchStatusInfo.getTime()));
        }
        return resultMap;
    }
}
