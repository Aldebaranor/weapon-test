package com.soul.weapon.model;

import com.egova.model.annotation.Display;
import lombok.Data;

/**
 * @Author: XinLai
 * @Date: 2021/10/28 15:27
 */
@Data
public class CombatScenariosInfo {
    @Display("发送方编号")
    private String sender;

    @Display("信息发送时间")
    private Long msgTime;

    @Display("方案生成时间")
    private Long time;

    @Display("作战方案")
    private String scenarios;

}
