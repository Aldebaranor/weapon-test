package com.soul.weapon.schedule;

import com.soul.weapon.algorithm.AllAlgorithm;
import com.soul.weapon.config.CommonRedisConfig;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
          /*  allAlgorithm.shipToAirMissile();
            allAlgorithm.antiMissileShipGun();
            allAlgorithm.threatenEstimation();
            allAlgorithm.torpedoDefense();
            allAlgorithm.electronicCountermeasure();
            allAlgorithm.underwaterAcousticCountermeasure();
            allAlgorithm.executionStatusTest();
            allAlgorithm.interceptDistanceTest();*/
           // allAlgorithm.fireControlTest();
           // allAlgorithm.reactionTimeTest();
            allAlgorithm.launcherRotationTest();
           // allAlgorithm.multiTargetInterceptionTest();
        } catch (Exception e) {
            log.error("定时轮询所有算法失败！", e);
        }
    }
}
