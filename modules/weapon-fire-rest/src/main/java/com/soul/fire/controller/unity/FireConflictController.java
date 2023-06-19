package com.soul.fire.controller.unity;

import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.egova.web.annotation.Api;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.model.ConflictReport;
import com.soul.weapon.model.ReportDetail;
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
@RequestMapping("/unity/conflict")
@RequiredArgsConstructor
public class FireConflictController {

    private final CommonConfig config;


    /**
     * 冲突结果查询
     *
     * @return List<ConflictReport>
     */
    @Api
    @GetMapping("/result")
    public List<ConflictReport> chargeResult() {
        List<ConflictReport> results = new ArrayList<>();
        Map<String, String> conflictResults = RedisUtils.getService(config.

                getFireDataBase()).boundHashOps(Constant.PREDICT_KEY).entries();

        if (conflictResults == null) {
            return results;
        }

        Map<String, ConflictReport> chargeResultsMap = conflictResults.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        pair -> JsonUtils.deserialize(pair.getValue(), ConflictReport.class)
                )
        );
        return new ArrayList<>(chargeResultsMap.values());
    }


    /**
     * 冲突装备详细信息查询
     *
     * @return List<ReportDetail>
     */
    @Api
    @PostMapping("/detail/{id}")
    public List<ReportDetail> getDetails(@PathVariable String id) {

        List<ReportDetail> results = new ArrayList<>();
        Map<String, String> detailResults = RedisUtils.getService(config.
                getFireDataBase()).boundHashOps(Constant.PREDICTDETAIL_KEY).entries();
        if (detailResults == null) {
            return results;
        }

        Map<String, ReportDetail> detailResultsMap = detailResults.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        pair -> JsonUtils.deserialize(pair.getValue(), ReportDetail.class)
                )
        );
        for (String key : detailResultsMap.keySet()) {
            if (key.equals(id)) {
                results.add(detailResultsMap.get(key));
            }
        }
        return results;
    }
}
