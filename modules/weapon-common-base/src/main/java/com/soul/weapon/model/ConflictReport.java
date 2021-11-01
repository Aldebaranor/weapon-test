package com.soul.weapon.model;

import com.egova.model.annotation.Display;
import lombok.Data;

/**
 * @Author: XinLai
 * @Date: 2021/10/29 16:22
 */
@Data
@Display("兼容预判结果报文")
public class ConflictReport {

    @Display("冲突序号")
    private String id;

    @Display("预判冲突类型")
    private Integer conflictType;

    @Display("冲突时间")
    private Long time;

    @Display("A装备标识")
    private String equipmentIdA;

    @Display("B装备标识")
    private String equipmentIdB;



}
