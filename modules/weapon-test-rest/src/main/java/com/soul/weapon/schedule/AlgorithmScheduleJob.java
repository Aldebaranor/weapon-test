package com.soul.weapon.schedule;

import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.flagwind.commons.StringUtils;
import com.soul.weapon.config.CommonRedisConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.model.ScenariosInfo;
import com.soul.weapon.model.dds.CombatScenariosInfo;
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
@com.egova.quartz.annotation.Job(name="ConflictPredictJob",group = "weapon",cron = "1000")
@DisallowConcurrentExecution
public class AlgorithmScheduleJob implements Job {
    @Autowired
    public CommonRedisConfig commonRedisConfig;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException{
        try {
            // TODO: 待所有算法实现后，该定时任务轮询所有算法即可！
            log.info("所有算法轮询计算中！");

        } catch (Exception e) {
            log.error("定时轮询所有算法失败！", e);
        }
    }
}
