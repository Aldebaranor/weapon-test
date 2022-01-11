//package com.soul.fire.task;
//
//import com.egova.json.utils.JsonUtils;
//import com.egova.model.OperateResult;
//import com.soul.weapon.config.CommonConfig;
//import com.soul.weapon.config.Constant;
//import com.soul.weapon.model.ChargeReport;
//import com.soul.weapon.model.ConflictReport;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.quartz.DisallowConcurrentExecution;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static com.egova.redis.RedisUtils.getService;
//
///**
// * @Author: XinLai
// * @Date: 2022/1/4 17:38
// */
//
//@Slf4j
//@Component("ChargeDistributeJob")
//@com.egova.quartz.annotation.Job(name="ChargeDistributeJob",group = "fire",cron = "1000")
//@DisallowConcurrentExecution
//@RequiredArgsConstructor
//public class ChargeDistributeJob implements Job {
//
//    @Autowired(required=false)
//    public SimpMessagingTemplate messageTemplate;
//
//    private final CommonConfig config;
//
//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) {
//        try {
//
//            OperateResult result = new OperateResult();
//            Map<String, String> map = getService(config.getFireDataBase()).extrasForHash().hgetall(Constant.CHARGE_KEY);
//            Map<String, List<ChargeReport>> lineMap = new HashMap<>();
//            map.forEach((k, v)->{
//                try {
//                    List<ChargeReport> lines = JsonUtils.deserializeList(v, ChargeReport.class);
//                    lineMap.put(k,lines);
//                } catch (Exception e) {
//                    return;
//                }
//            });
//            result.setResult(lineMap);
//            messageTemplate.convertAndSend("/topic/free/fire/charge" , result);
//        } catch (Exception e) {
//            log.error("定时推送态势数据失败", e);
//        }
//    }
//
//}
