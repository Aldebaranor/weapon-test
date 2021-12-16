package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.generic.service.DictionaryService;
import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.soul.weapon.condition.PipeSelfCheckCondition;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.domain.PipeSelfCheckRepository;
import com.soul.weapon.entity.PipeSelfCheck;
import com.soul.weapon.entity.enums.PipeWeaponIndices;
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

import static java.util.Map.entry;

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

    /** 设备id -> 前端显示名称 from 0 to 19 **/
    private static final Map<String, String> MAP_ID_TO_SHOW = Map.ofEntries(
           entry(PipeWeaponIndices.AirMissileRadar.getValue(), "跟踪雷达"),
           entry(PipeWeaponIndices.AirMissileFireControl.getValue(), "对空防御指控设备"),
           entry(PipeWeaponIndices.AirMissileLauncher.getValue(), "航空导弹发射装置"),
           entry(PipeWeaponIndices.AirMissileShortRange.getValue(), "航空导弹武器"),
           entry(PipeWeaponIndices.AirMissileMediumRange.getValue(), "航空导弹武器"),
           entry(PipeWeaponIndices.AirMissileLongRange.getValue(), "航空导弹武器"),
           entry(PipeWeaponIndices.AntiMissileShipGunRadar.getValue(), "跟踪雷达"),
           entry(PipeWeaponIndices.AntiMissileShipGunControl.getValue(), "对空防御指挥设备"),
           entry(PipeWeaponIndices.AntiMissileShipGun.getValue(), "反导舰炮"),
           entry(PipeWeaponIndices.Sonar.getValue(), "舰壳声呐"),
           entry(PipeWeaponIndices.TorpedoFireControl.getValue(), "水下防御指控设备"),
           entry(PipeWeaponIndices.TorpedoLauncher.getValue(), "鱼雷房屋武器发射装备"),
           entry(PipeWeaponIndices.Torpedo.getValue(), "鱼雷防御武器"),
           //  entry(PipeWeaponIndices.ElectronicDetection.getValue(), ""), 不参与显示
           entry(PipeWeaponIndices.ElectronicCountermeasure.getValue(), "对空防御指控设备"),
           entry(PipeWeaponIndices.MultiUsageLaunch.getValue(), "多功能发射装置"),
           entry(PipeWeaponIndices.OutBoardElectronicCountermeasure.getValue(), "电子对抗器材"),
           entry(PipeWeaponIndices.InBoardElectronicCountermeasure.getValue(), "电子对抗器材"),
           entry(PipeWeaponIndices.UnderwaterAcousticCountermeasureControl.getValue(), "水下防御指控设备"),
           entry(PipeWeaponIndices.UnderwaterAcousticCountermeasure.getValue(), "水声对抗器材")
           // TODO:
           // 搜索雷达和拖曳声呐单独从dds总线采集，等真实报文，将其存入redis，而后修改getPipeShow即可
    );
    
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
        for(String equipShow : MAP_ID_TO_SHOW.values()) {
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
                    MAP_ID_TO_SHOW.get(equipId),
                    res.get(MAP_ID_TO_SHOW.get(equipId)) && allEquipmentStatus.get(equipId).getCheckStatus()
            );
        }

        ret.add(new ArrayList<>(res.keySet()));
        ret.add(new ArrayList<>(res.values().stream().map(String::valueOf).collect(Collectors.toList())));
        return ret;
    }
}
