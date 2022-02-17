package com.soul.weapon.service;

import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.flagwind.commons.StringUtils;
import com.soul.weapon.config.Constant;
import com.soul.weapon.entity.PipeTest;
import com.soul.weapon.entity.enums.PipeWeaponIndices;
import com.soul.weapon.entity.historyInfo.*;
import com.soul.weapon.model.dds.*;
import com.soul.weapon.utils.DateParserUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: 码头工人
 * @date: 2022/02/11/4:42 下午
 * @description:
 */
public interface AllAlgorithmService {


    /**
     * 航空导弹-1
     */
    void shipToAirMissile(String taskId, PipeTest pipeTest);


    /**
     * 反导舰炮算法-2
     */
    void antiMissileShipGun(String taskId, PipeTest pipeTest);

    /**
     * 鱼类防御武器测试-3
     */
    void torpedoDefense(String taskId, PipeTest pipeTest) ;

    /**
     * 电子对抗武器测试-4
     */
    void electronicCountermeasure(String taskId, PipeTest pipeTest);


    /**
     * 水声对抗武器测试 -5
     */
    void underwaterAcousticCountermeasure(String taskId, PipeTest pipeTest) ;

    /**
     * 信息流程测试-6
     */
    void infoProcessTest(String taskId, PipeTest pipeTest);

    /**
     * 威胁判断算法-7
     */
    void threatJudgment(String taskId, PipeTest pipeTest);

    /**
     * 指示处理精度测试-8
     */
    void indicationAccuracyTest(String taskId, PipeTest pipeTest) ;

    /**
     * 执行情况测试-9
     */
    void executionStatusTest(String taskId, PipeTest pipeTest);

    /**
     * 雷达航迹测试-10
     */
    void radarTrackTest(String taskId, PipeTest pipeTest);

    /**
     * 拦截距离测试-11
     */
    void interceptDistanceTest(String taskId, PipeTest pipeTest);

    /**
     * 火控解算精度测试-12
     */
    void fireControlTest(String taskId, PipeTest pipeTest);


    /**
     * 反应时间测试-13
     */
    void reactionTimeTest(String taskId, PipeTest pipeTest) ;


    /**
     * 发射架调转精度测试-14
     */
    void launcherRotationTest(String taskId, PipeTest pipeTest) ;


    /**
     * 多目标拦截能力测试-15
     */
    void multiTargetInterceptionTest(String taskId, PipeTest pipeTest) ;
}
