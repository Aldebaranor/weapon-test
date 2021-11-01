package com.soul.weapon.model;

import com.egova.model.annotation.Display;
import lombok.Data;

/**
 * @Author: XinLai
 * @Date: 2021/11/1 11:04
 */
@Data
@Display("兼容管控结果报文")
public class ChargeReport {

    @Display("管控序号")
    private String id;

    @Display("管控冲突类型")
    private Integer chargeType;

    @Display("管控时间")
    private Long time;

    @Display("管控措施")
    private String chargeMethod;

    @Display("管控装备标识")
    private String chargeEquipId;

    @Display("未管控装备标识")
    private String freeEquipId;
}
