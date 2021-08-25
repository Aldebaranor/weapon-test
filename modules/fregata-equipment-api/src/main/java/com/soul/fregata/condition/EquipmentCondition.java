package com.soul.fregata.condition;

import com.egova.model.annotation.Display;
import com.flagwind.persistent.annotation.Condition;
import com.flagwind.persistent.annotation.ConditionOperator;
import com.flagwind.persistent.model.ClauseOperator;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * created by 迷途小码农
 */
@Data
@Condition
@Display("装备基础表")
public class EquipmentCondition implements Serializable {

    @Display("编码")
    @ConditionOperator(name = "code", operator = ClauseOperator.Equal)
    private String code;

    @Display("名称")
    @ConditionOperator(name = "name", operator = ClauseOperator.Like)
    private String name;

    @Display("数据完整度（字典）")
    @ConditionOperator(name = "dataIntegrity", operator = ClauseOperator.Equal)
    private String dataIntegrity;

    @Display("产地国家（字典）")
    @ConditionOperator(name = "country", operator = ClauseOperator.Equal)
    private String country;

    @Display("产地国家（字典）")
    @ConditionOperator(name = "country", operator = ClauseOperator.In)
    private List<String> countries;

    @Display("平台分类")
    @ConditionOperator(name = "categoryType", operator = ClauseOperator.Equal)
    private String categoryType;

    @ConditionOperator(name = "categoryType", operator = ClauseOperator.In)
    private List<String> categoryTypes;

    @Display("大类")
    @ConditionOperator(name = "mainType", operator = ClauseOperator.Equal)
    private String mainType;

    @Display("大类")
    @ConditionOperator(name = "mainType", operator = ClauseOperator.In)
    private List<String> mainTypes;


    @Display("小类")
    @ConditionOperator(name = "subType", operator = ClauseOperator.Equal)
    private String subType;

    @ConditionOperator(name = "subType", operator = ClauseOperator.In)
    private List<String> subTypes;

    @Display("细类")
    @ConditionOperator(name = "thirdType", operator = ClauseOperator.Equal)
    private String thirdType;

    @ConditionOperator(name = "thirdType", operator = ClauseOperator.In)
    private List<String> thirdTypes;

}
