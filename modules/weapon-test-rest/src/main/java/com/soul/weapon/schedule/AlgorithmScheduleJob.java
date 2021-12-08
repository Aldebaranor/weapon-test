package com.soul.weapon.schedule;

import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.soul.weapon.algorithm.AllAlgorithm;
import com.soul.weapon.config.CommonRedisConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.config.TaskTypeConstant;
import com.soul.weapon.entity.PipeTest;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author: nash5
 * @date: 2021-11-09 16:46
 */

@Slf4j
@Component("AlgorithmScheduleJob")
@com.egova.quartz.annotation.Job(name="AlgorithmScheduleJob",group = "weapon3",cron = "1000")
@DisallowConcurrentExecution
public class AlgorithmScheduleJob implements Job {

    @Autowired
    public CommonRedisConfig commonRedisConfig;

    @Autowired
    public AllAlgorithm allAlgorithm;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException{
        try {
            // TODO: 待所有算法实现后，该定时任务轮询所有算法即可！
            String currentTask = getCurrentTask();
            Map<String, PipeTest> currentPipeTest = getCurrentPipeTest();

            allAlgorithm.shipToAirMissile(currentTask,getPipeTestByCode(currentPipeTest, TaskTypeConstant.SHIP_AIR_MISSILE));
            allAlgorithm.antiMissileShipGun(currentTask,getPipeTestByCode(currentPipeTest, TaskTypeConstant.ANTI_MISSILE_NAVAL_GUN));
            allAlgorithm.torpedoDefense(currentTask,getPipeTestByCode(currentPipeTest, TaskTypeConstant.TORPEDO_DEFENSE));
            allAlgorithm.electronicCountermeasure(currentTask,getPipeTestByCode(currentPipeTest, TaskTypeConstant.ELECTRONIC_COUNTERMEASURE));
            allAlgorithm.underwaterAcousticCountermeasure(currentTask,getPipeTestByCode(currentPipeTest, TaskTypeConstant.UNDERWATER_ACOUSTIC_COUNTERMEASURE));

            allAlgorithm.executionStatusTest(currentTask,getPipeTestByCode(currentPipeTest, TaskTypeConstant.INFORMATION_FLOW));

            allAlgorithm.threatenEstimation(currentTask,getPipeTestByCode(currentPipeTest, TaskTypeConstant.THREAT_JUDGMENT));
            allAlgorithm.threatenEstimation(currentTask,getPipeTestByCode(currentPipeTest, TaskTypeConstant.INDICATES_THE_PROCESSING_ACCURACY));

            allAlgorithm.executionStatusTest(currentTask,getPipeTestByCode(currentPipeTest, TaskTypeConstant.IMPLEMENTATION));

            allAlgorithm.threatenEstimation(currentTask,getPipeTestByCode(currentPipeTest, TaskTypeConstant.RADAR_TRACK));

            allAlgorithm.interceptDistanceTest(currentTask,getPipeTestByCode(currentPipeTest, TaskTypeConstant.INTERCEPT_DISTANCE));
            allAlgorithm.fireControlTest(currentTask,getPipeTestByCode(currentPipeTest, TaskTypeConstant.FIRE_CONTROL_CALCULATION_ACCURACY));
            allAlgorithm.reactionTimeTest(currentTask,getPipeTestByCode(currentPipeTest, TaskTypeConstant.REACTION_TIME));
            allAlgorithm.launcherRotationTest(currentTask,getPipeTestByCode(currentPipeTest, TaskTypeConstant.LAUNCHER_ROTATION_ACCURACY));
            allAlgorithm.multiTargetInterceptionTest(currentTask,getPipeTestByCode(currentPipeTest, TaskTypeConstant.MULTI_TARGET_INTERCEPTION));

        } catch (Exception e) {
            log.error("定时轮询所有算法失败！", e);
        }
    }






    private  Map<String, PipeTest> getCurrentPipeTest(){
        return RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).extrasForHash().hgetall(Constant.WEAPON_CURRENT_PIPETEST, PipeTest.class);
    }



    private String  getCurrentTask(){
        return RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).extrasForValue().get(Constant.WEAPON_CURRENT_TASK);
    }

    private PipeTest getPipeTestByCode(Map<String, PipeTest> map ,String code){
        return map.get(code);
    }



}
