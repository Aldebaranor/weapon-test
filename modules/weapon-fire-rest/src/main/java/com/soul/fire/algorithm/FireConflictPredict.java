package com.soul.fire.algorithm;

import com.soul.weapon.model.ConflictReport;
import com.soul.weapon.model.ScenariosInfo;

/**
 * @Author: XinLai
 * @Date: 2021/10/29 16:20
 */
public interface FireConflictPredict {


    /**
     * 预判作战方案之间是否存在火力冲突
     * @param scenariosA 作战方案A
     * @param scenariosB 作战方案B
     * @return 冲突报告
     */
    ConflictReport conflictPredict(ScenariosInfo scenariosA , ScenariosInfo scenariosB);

}
