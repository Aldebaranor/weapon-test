package com.soul.weapon.algorithm.impl;

import com.soul.weapon.algorithm.AlgoFactoryContext;
import com.soul.weapon.algorithm.Algorithm;
import com.soul.weapon.algorithm.annotation.WeaponAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author: nash5
 * @Date: 2021/9/12 16:18
 */
@Service(value = "airMissilePipeTest")
public class AirMissilePipeTest implements Algorithm {

    @Override
    public String exAlgo(String input) {
        Logger LOG = LoggerFactory.getLogger(AirMissilePipeTest.class);
        LOG.info("airMissilePipeTest algo executing!");
        return input;
    }
}
