package com.egova.service;

import com.egova.BootstrapTest;
import com.egova.entity.DictionaryGroup;
import com.egova.redis.RedisUtils;
import org.junit.Test;

import java.util.List;

/**
 * @auther: 码头工人
 * @date: 2022/02/13/9:53 下午
 * @description:
 */
public class RedisTest extends BootstrapTest {
    @Test
    public void list() {
        RedisUtils.getService(0).boundListOps("test").leftPush("1");
        RedisUtils.getService(0).boundListOps("test").leftPush("2");
        RedisUtils.getService(0).boundListOps("test").leftPush("3");
        RedisUtils.getService(0).boundListOps("test").leftPush("4");
        RedisUtils.getService(0).boundListOps("test").leftPush("5");
        List<String> test = RedisUtils.getService(0).boundListOps("test").range(0, 1);
        Long size1 = RedisUtils.getService(0).boundListOps("test").size();
        RedisUtils.getService(0).boundListOps("test").leftPush("6");
        Long size2 = RedisUtils.getService(0).boundListOps("test").size();
        String test1 = RedisUtils.getService(0).boundListOps("test").index(0);
        RedisUtils.getService(0).boundListOps("test").leftPush("7");
        String test2 = RedisUtils.getService(0).boundListOps("test").index(0);


    }
}
