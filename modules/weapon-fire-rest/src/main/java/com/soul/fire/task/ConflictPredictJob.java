package com.soul.fire.task;

import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.flagwind.commons.StringUtils;
import com.soul.fire.service.FireConflictService;
import com.soul.weapon.model.ScenariosInfo;
import com.soul.weapon.model.dds.EquipmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * @author: nash5
 * @date: 2021-11-09 16:46
 */

@Slf4j
@Component("ConflictPredictJob")
@com.egova.quartz.annotation.Job(name="ConflictPredictJob",group = "weapon",cron = "1000")
@RequiredArgsConstructor
public class ConflictPredictJob implements Job {

    private final FireConflictService fireConflictService;

    private final Integer DBINDEX = 1;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException{
        try {
            // part1: 从redis中拿从报文解析的供计算的数据
            String scenariosStrA = RedisUtils.getService(DBINDEX).extrasForValue().get("weapon:pump:testkey1");
            String scenariosStrB = RedisUtils.getService(DBINDEX).extrasForValue().get("weapon:pump:testkey1");

            if(StringUtils.isBlank(scenariosStrA) || StringUtils.isBlank(scenariosStrB)) {
                log.error("从Redis中获取冲突预判信息失败！");
                return ;
            }
            ScenariosInfo scenariosInfoA = JsonUtils.deserialize(scenariosStrA, ScenariosInfo.class);
            ScenariosInfo scenariosInfoB = JsonUtils.deserialize(scenariosStrB, ScenariosInfo.class);
            // fireConflictService.conflictPredict(scenariosInfoA, scenariosInfoB);

        } catch (Exception e) {
            log.error("定时计算预判火力兼容预判任务失败！", e);
        }
    }
}
