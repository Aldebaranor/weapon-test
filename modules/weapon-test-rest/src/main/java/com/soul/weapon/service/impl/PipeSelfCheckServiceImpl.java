package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.generic.service.DictionaryService;
import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.soul.weapon.condition.PipeSelfCheckCondition;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.config.WeaponTestConstant;
import com.soul.weapon.domain.PipeSelfCheckRepository;
import com.soul.weapon.entity.PipeSelfCheck;
import com.soul.weapon.model.dds.EquipmentStatus;
import com.soul.weapon.service.PipeSelfCheckService;
import com.soul.weapon.utils.DateParserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Priority(5)
@CacheConfig(cacheNames = PipeSelfCheck.NAME)
public class PipeSelfCheckServiceImpl extends TemplateService<PipeSelfCheck,String> implements PipeSelfCheckService {

    private final PipeSelfCheckRepository pipeSelfCheckRepository;
    private final DictionaryService dictionaryService;
    private final CommonConfig config;

    
    @Override
    public void insertList(List<PipeSelfCheck> entities) {
        super.insertList(entities);
    }

    @Override
    protected AbstractRepositoryBase<PipeSelfCheck,String> getRepository(){
        return pipeSelfCheckRepository;
    }


    @Override
    public PipeSelfCheck getByName(String name)
    {
        PipeSelfCheckCondition pipeSelfCheckCondition = new PipeSelfCheckCondition();
        pipeSelfCheckCondition.setName(name);
        dictionaryService.getItemTreeByType("");
        return super.query(pipeSelfCheckCondition).stream().findFirst().orElse(null);
    }


    @Override
    public String insert( PipeSelfCheck pipeSelfCheck){
        pipeSelfCheck.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(pipeSelfCheck);
    }

    @Override
    public void update(PipeSelfCheck pipeSelfCheck){
        pipeSelfCheck.setModifyTime(new Timestamp(System.currentTimeMillis()));
        super.update(pipeSelfCheck);
    }

    @Override
    public ArrayList<ArrayList<String>> getPipeShow() {
        Map<String, Boolean> res = new HashMap<>();
        ArrayList<ArrayList<String>> ret = new ArrayList<>();

        // 初始化每个类型装备的检测结果
        for(String equipShow : WeaponTestConstant.MAP_ID_TO_SHOW.values()) {
            res.put(equipShow, true);
        }

        // 获取所有装备的状态
        String key = String.format("%s:%s", Constant.EQUIPMENT_STATUS_HTTP_KEY, DateParserUtils.getTime());
        if (!Boolean.TRUE.equals(RedisUtils.getService(config.getPumpDataBase()).getTemplate()
                .hasKey(key))) {
            log.error("从Redis中获取装备信息失败！-2");
            return ret;
        }
        Map<String, String> tmpEquipments = RedisUtils.getService(config.getPumpDataBase()).
                boundHashOps(key).entries();
        assert tmpEquipments != null;
        Map<String, EquipmentStatus> allEquipmentStatus = tmpEquipments.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                pair -> JsonUtils.deserialize(pair.getValue(), EquipmentStatus.class))
        );

        for(String equipId : allEquipmentStatus.keySet()) {
            // 检测redis中目前所有装备的最新状态返回给前端
            res.put(
                    WeaponTestConstant.MAP_ID_TO_SHOW.get(equipId),
                    res.get(WeaponTestConstant.MAP_ID_TO_SHOW.get(equipId)) && allEquipmentStatus.get(equipId).getCheckStatus()
            );
        }

        ret.add(new ArrayList<>(res.keySet()));
        ret.add(new ArrayList<>(res.values().stream().map(String::valueOf).collect(Collectors.toList())));
        return ret;
    }
}
