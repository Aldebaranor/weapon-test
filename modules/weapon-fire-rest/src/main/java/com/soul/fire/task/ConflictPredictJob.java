//package com.soul.fire.task;
//
//import com.soul.fire.service.FireConflictChargeService;
//import com.soul.fire.service.FireConflictPredictService;
//import com.soul.weapon.config.CommonConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
///**
// * @author: nash5
// * @date: 2021-11-09 16:46
// */
//
//@Slf4j
//@Component
//public class ConflictPredictJob  {
//
//    @Autowired
//    public FireConflictPredictService fireConflictPredictService;
//    @Autowired
//    public FireConflictChargeService fireConflictCharge;
//    @Autowired
//    public CommonConfig config;
//
//    //定时任务去对redis里面的数据进行冲突兼容校验
//    @Scheduled(fixedDelay = 1000)
//    public void execute() {
//        try {
//            fireConflictPredictService.predictTest();
//            fireConflictCharge.chargeTest();
//        } catch (Exception e) {
//            log.error("定时轮询火力兼容预判算法失败！", e);
//        }
//    }
//}
