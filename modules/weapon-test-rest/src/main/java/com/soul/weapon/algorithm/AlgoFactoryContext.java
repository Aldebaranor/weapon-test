//package com.soul.weapon.algorithm;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
///**
// * @Author: nash5
// * @Date: 2021/9/12 16:23
// */
//@Service
//public class AlgoFactoryContext {
//    private static final Logger LOG = LoggerFactory.getLogger(AlgoFactoryContext.class);
//
//    @Autowired(required = true)
//    private Map<String, Algorithm> allStrategies;
//
//    private Algorithm algoExecutor;
//
//    public void execAlgo(String requiredAlgoName, String dataString) {
//        if(allStrategies.containsKey(requiredAlgoName)) {
//            LOG.info("algo name is {}", requiredAlgoName);
//            algoExecutor =  (Algorithm) allStrategies.get(requiredAlgoName);
//        } else {
//            LOG.error("algo with name: {} not exist!", requiredAlgoName);
//        }
//        algoExecutor.exAlgo(dataString);
//    }
//}
