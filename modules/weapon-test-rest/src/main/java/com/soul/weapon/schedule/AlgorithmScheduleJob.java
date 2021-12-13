package com.soul.weapon.schedule;

import com.egova.redis.RedisUtils;
import com.soul.weapon.algorithm.AllAlgorithm;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.entity.PipeTest;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: nash5
 * @date: 2021-11-09 16:46
 */

@Slf4j
@Component
public class AlgorithmScheduleJob  {

    @Autowired
    public CommonConfig config;

    @Autowired
    public AllAlgorithm allAlgorithm;

    @Scheduled(fixedDelay = 1000)
    public void execute() {
        try {
            String currentTask = getCurrentTask();
            Map<String, PipeTest> currentPipeTest = getCurrentPipeTest();

            allAlgorithm.shipToAirMissile(currentTask,getPipeTestByCode(currentPipeTest, Constant.SHIP_AIR_MISSILE));
            allAlgorithm.antiMissileShipGun(currentTask,getPipeTestByCode(currentPipeTest, Constant.ANTI_MISSILE_NAVAL_GUN));
            allAlgorithm.torpedoDefense(currentTask,getPipeTestByCode(currentPipeTest, Constant.TORPEDO_DEFENSE));
            allAlgorithm.electronicCountermeasure(currentTask,getPipeTestByCode(currentPipeTest, Constant.ELECTRONIC_COUNTERMEASURE));
            allAlgorithm.underwaterAcousticCountermeasure(currentTask,getPipeTestByCode(currentPipeTest, Constant.UNDERWATER_ACOUSTIC_COUNTERMEASURE));
            allAlgorithm.infoProcessTest(currentTask,getPipeTestByCode(currentPipeTest, Constant.INFORMATION_FLOW));
            allAlgorithm.threatJudgment(currentTask,getPipeTestByCode(currentPipeTest, Constant.THREAT_JUDGMENT));
            allAlgorithm.indicationAccuracyTest(currentTask,getPipeTestByCode(currentPipeTest, Constant.INDICATES_THE_PROCESSING_ACCURACY));
            allAlgorithm.executionStatusTest(currentTask,getPipeTestByCode(currentPipeTest, Constant.IMPLEMENTATION));
            allAlgorithm.radarTrackTest(currentTask,getPipeTestByCode(currentPipeTest, Constant.RADAR_TRACK));
            allAlgorithm.interceptDistanceTest(currentTask,getPipeTestByCode(currentPipeTest, Constant.INTERCEPT_DISTANCE));
            allAlgorithm.fireControlTest(currentTask,getPipeTestByCode(currentPipeTest, Constant.FIRE_CONTROL_CALCULATION_ACCURACY));
            allAlgorithm.reactionTimeTest(currentTask,getPipeTestByCode(currentPipeTest, Constant.REACTION_TIME));
            allAlgorithm.launcherRotationTest(currentTask,getPipeTestByCode(currentPipeTest, Constant.LAUNCHER_ROTATION_ACCURACY));
            allAlgorithm.multiTargetInterceptionTest(currentTask,getPipeTestByCode(currentPipeTest, Constant.MULTI_TARGET_INTERCEPTION));

        } catch (Exception e) {
            log.error("定时轮询所有算法失败！", e);
        }
    }


    private  Map<String, PipeTest> getCurrentPipeTest(){
        return RedisUtils.getService(config.getPumpDataBase()).extrasForHash().hgetall(Constant.WEAPON_CURRENT_PIPETEST, PipeTest.class);
    }



    private String  getCurrentTask(){
        return RedisUtils.getService(config.getPumpDataBase()).extrasForValue().get(Constant.WEAPON_CURRENT_TASK);
    }

    private PipeTest getPipeTestByCode(Map<String, PipeTest> map ,String code){
        return map.get(code);
    }



}
