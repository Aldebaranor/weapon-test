package com.soul.weapon.model.dds;

import com.egova.model.annotation.Display;
import com.soul.weapon.model.ScenariosInfo;
import lombok.Data;

import java.util.List;

/**
 * @Author: XinLai
 * @Date: 2021/10/28 15:27
 * 作战方案信息
 */
@Data
@Display("作战方案信息")
public class CombatScenariosInfo {
    @Display("发送方编号")
    private String sender;

    @Display("信息发送时间")
    private Long msgTime;

    @Display("方案生成时间")
    private Long time;

    @Display("作战方案信息实体")
    private String scenarios;

    private List<ScenariosInfo> scenariosList;

}
