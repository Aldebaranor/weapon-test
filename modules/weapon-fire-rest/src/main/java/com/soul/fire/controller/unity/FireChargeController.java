package com.soul.fire.controller.unity;

import com.egova.json.utils.JsonUtils;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.redis.RedisUtils;
import com.egova.web.annotation.Api;
import com.soul.fire.condition.FireConflictCondition;
import com.soul.fire.entity.FireConflict;
import com.soul.fire.service.FireConflictService;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.model.ChargeReport;
import com.soul.weapon.model.ReportDetail;
import com.soul.weapon.model.dds.EquipmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xinl
 */
@Slf4j
@RestController
@RequestMapping("/unity/charge")
@RequiredArgsConstructor
public class FireChargeController {

    private final CommonConfig config;

    /**
     * 管控结果查询
     * @return List<ChargeReport>
     */
    @Api
    @GetMapping("/result")
    public List<ChargeReport> chargeResult() {
        List<ChargeReport> results = new ArrayList<>();
        Map<String,String> chargeResults = RedisUtils.getService(config.
                getFireDataBase()).boundHashOps(Constant.CHARGE_KEY).entries();
        if(chargeResults==null) {
            return results;
        }

        Map<String, ChargeReport> chargeResultsMap = chargeResults.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        pair -> JsonUtils.deserialize(pair.getValue(), ChargeReport.class)
                )
        );
        return new ArrayList<>(chargeResultsMap.values());
    }

    /**
     * 冲突装备详细信息查询
     * @return List<ReportDetail>
     */
    @Api
    @PostMapping("/detail/{id}")
    public List<ReportDetail> getDetails(@PathVariable String id) {

        List<ReportDetail> results = new ArrayList<>();
        Map<String,String> detailResults = RedisUtils.getService(config.
                getFireDataBase()).boundHashOps(Constant.CHARGEDETAIL_KEY).entries();
        if(detailResults==null) {
            return results;
        }

        Map<String, ReportDetail> detailResultsMap = detailResults.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        pair -> JsonUtils.deserialize(pair.getValue(), ReportDetail.class)
                )
        );
        for(String key:detailResultsMap.keySet()){
            if(key.equals(id)){
                results.add(detailResultsMap.get(key));
            }
        }
        return results;
    }


}
