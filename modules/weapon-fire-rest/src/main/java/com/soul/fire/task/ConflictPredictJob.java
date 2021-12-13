package com.soul.fire.task;

import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.flagwind.commons.StringUtils;
import com.soul.fire.service.FireConflictCharge;
import com.soul.fire.service.FireConflictPredictService;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.model.ScenariosInfo;
import com.soul.weapon.model.dds.CombatScenariosInfo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: nash5
 * @date: 2021-11-09 16:46
 */

@Slf4j
@Component
//@Component("ConflictPredictJob")
//@com.egova.quartz.annotation.Job(name="ConflictPredictJob",group = "weapon",cron = "1000")
//@DisallowConcurrentExecution
public class ConflictPredictJob  {

    @Autowired
    public FireConflictPredictService fireConflictPredictService;
    @Autowired
    public FireConflictCharge fireConflictCharge;

    @Autowired
    public CommonConfig config;


    @Scheduled(fixedDelay = 1000)
    public void execute() {
        try {
            fireConflictPredictService.predictTest();
            fireConflictCharge.chargeTest();
        } catch (Exception e) {
            log.error("定时轮询火力兼容预判算法失败！", e);
        }
    }
}
