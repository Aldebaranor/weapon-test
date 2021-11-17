package com.soul.weapon.algorithm;

import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.soul.weapon.config.CommonRedisConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.model.dds.EquipmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: nash5
 * @date: 2021-11-15 16:08
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AllAlgorithm {
    private final CommonRedisConfig commonRedisConfig;

    /**
     * 反导舰炮算法实现
     */
    public void antiMissileShipGun() {
        if(!Boolean.TRUE.equals(RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).getTemplate()
                .hasKey(Constant.EQUIPMENT_STATUS_HTTP_KEY))) {
            log.error("从Redis中获取装备信息失败！");
            return ;
        }

        Map<String, String> tmpEquipments = RedisUtils.getService(commonRedisConfig.getHttpDataBaseIdx()).
                boundHashOps(Constant.EQUIPMENT_STATUS_HTTP_KEY).entries();
        assert tmpEquipments != null;
        Map<String, EquipmentStatus> allEquipmentStatus = tmpEquipments.entrySet().stream().collect(Collectors.toMap(
                    Map.Entry::getKey,
                    pair -> JsonUtils.deserialize(pair.getValue(), EquipmentStatus.class))
                );

        // TODO: 写一个处理通道测试的算法，主要就是传入不同通道的id即可，返回的是<时间 long, 0/1>;





        return ;
    }
}
