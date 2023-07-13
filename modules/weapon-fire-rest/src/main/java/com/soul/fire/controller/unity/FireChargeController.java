package com.soul.fire.controller.unity;

import com.egova.redis.RedisUtils;
import com.egova.web.annotation.Api;
import com.soul.fire.service.FireConflictChargeService;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import com.soul.weapon.model.ChargeReport;
import com.soul.weapon.model.ReportDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    public FireConflictChargeService fireConflictCharge;
    /**
     * 管控结果查询
     *
     * @return List<ChargeReport>
     */
    @Api
    @GetMapping("/result")
    public List<ChargeReport> chargeResult() {
        fireConflictCharge.chargeTest();
        List<ChargeReport> results = new ArrayList<>();
        Map<String, ChargeReport> chargeResults = RedisUtils.getService(config.
                getFireDataBase()).extrasForHash().hgetall(Constant.CHARGE_KEY, ChargeReport.class);
        if (chargeResults == null) {
            return results;
        }
        return chargeResults.entrySet().stream().map(map -> map.getValue()).collect(Collectors.toList());
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
        Map<String, ReportDetail> detailResults = RedisUtils.getService(config.
                getFireDataBase()).extrasForHash().hgetall(Constant.CHARGEDETAIL_KEY, ReportDetail.class);
        if (detailResults == null) {
            return results;
        }
        return detailResults.entrySet().stream().map(map -> map.getValue()).collect(Collectors.toList());
    }
}
