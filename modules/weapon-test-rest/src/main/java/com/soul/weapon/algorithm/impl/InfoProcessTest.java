package com.soul.weapon.algorithm.impl;

import com.soul.weapon.algorithm.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author: XinLai
 * @Date: 2021/9/15 15:07
 */
@Service(value = "infoProcessTest")
public class InfoProcessTest implements Algorithm {

    @Override
    public String exAlgo(String input)
    {
        Logger LOG = LoggerFactory.getLogger(InfoProcessTest.class);
        LOG.info("InfoProcessTest algo executing!");
        return input;
    }
}
