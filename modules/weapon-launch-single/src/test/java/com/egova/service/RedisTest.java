package com.egova.service;

import com.egova.BootstrapTest;
import com.egova.entity.DictionaryGroup;
import com.egova.redis.RedisUtils;
import com.soul.screen.model.TctStatus;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
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

    @Test
    public void compare(){

        List<TctStatus> values = new ArrayList<>();
        TctStatus t1 = new TctStatus();
        t1.setType("1");
        TctStatus t2 = new TctStatus();
        t2.setType("10");
        TctStatus t3 = new TctStatus();
        t3.setType("11");
        values.sort(new Comparator<TctStatus>() {
            @Override
            public int compare(TctStatus o1, TctStatus o2) {
                return Integer.parseInt(o1.getType())-Integer.parseInt(o2.getType());
            }
        });
        System.out.println(values);
    }
}
