package com.soul.fire.task;

import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.flagwind.commons.StringUtils;
import com.soul.fire.service.FireConflictPredictService;
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
@Component("ConflictPredictJob")
@com.egova.quartz.annotation.Job(name="ConflictPredictJob",group = "weapon",cron = "1000")
@DisallowConcurrentExecution
public class ConflictPredictJob implements Job {

    @Autowired
    public FireConflictPredictService fireConflictPredictService;

    @Autowired
    public CommonRedisConfig commonRedisConfig;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException{
        try {
            String combatScenariosInfoStr = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).extrasForValue().
                    get(Constant.COMBAT_SCENARIOS_INFO_HTTP_KEY);
            if(StringUtils.isBlank(combatScenariosInfoStr)) {
                log.error("从Redis中获取冲突预判信息失败！");
                return ;
            }
            CombatScenariosInfo test = JsonUtils.deserialize(combatScenariosInfoStr, CombatScenariosInfo.class);
            ScenariosInfo scenariosInfoA = test.getScenariosList().get(0);
            ScenariosInfo scenariosInfoB = test.getScenariosList().get(0);

            fireConflictPredictService.conflictPredict(scenariosInfoA, scenariosInfoB);
        } catch (Exception e) {
            log.error("定时计算预判火力兼容预判任务失败！", e);
        }
    }
}
