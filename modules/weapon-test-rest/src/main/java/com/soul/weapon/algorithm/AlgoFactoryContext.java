package com.soul.weapon.algorithm;
import com.soul.weapon.algorithm.annotation.WeaponAlgorithm;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: nash5
 * @Date: 2021/9/12 16:23
 */

public class AlgoFactoryContext {
    private static final Logger LOG = LoggerFactory.getLogger(AlgoFactoryContext.class);
    private static Map<String, Class> allStrategies;

    static {
        Reflections reflections = new Reflections("com.soul.weapon.algorithm.impl",
                new SubTypesScanner(),
                new TypeAnnotationsScanner(),
                new FieldAnnotationsScanner());
        Set<Class<?>> annotatedClasses =
                reflections.getTypesAnnotatedWith(WeaponAlgorithm.class);
        allStrategies = new ConcurrentHashMap<String, Class>();
        for (Class<?> classObject : annotatedClasses) {
            WeaponAlgorithm annotatedAlgo = (WeaponAlgorithm) classObject
                    .getAnnotation(WeaponAlgorithm.class);
            allStrategies.put(annotatedAlgo.algoName(), classObject);
        }
        allStrategies = Collections.unmodifiableMap(allStrategies);
    }

    private Algorithm algoExecutor;

    public AlgoFactoryContext (String requiredAlgoName){
        if(allStrategies.containsKey(requiredAlgoName)) {
            LOG.info("algo name is {}", requiredAlgoName);
            try {
                algoExecutor =  (Algorithm) allStrategies.get(requiredAlgoName).getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException | InstantiationException
                    | InvocationTargetException | IllegalAccessException ex) {
                LOG.error("Instantiate algo Failed: ", ex);
            }
        } else {
            LOG.error("algo with name: {} not exist!", requiredAlgoName);
        }
    }

    public void execAlgo(String dataString) {
        algoExecutor.exAlgo(dataString);
    }
}
