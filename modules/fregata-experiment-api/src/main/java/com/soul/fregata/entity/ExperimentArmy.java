package com.soul.fregata.entity;

import com.egova.associative.Associative;
import com.egova.model.BaseEntity;
import com.egova.model.annotation.Display;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.soul.fregata.associative.EquipmentNameProvider;
import com.soul.fregata.entity.enums.ArmyType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.List;

/**
 * created by 迷途小码农
 */
@Data
@Entity
@Table(name = "fregata_experiment_army")
@Display("我的试验兵力")
@EqualsAndHashCode(callSuper = true)
public class ExperimentArmy extends BaseEntity {

    public static final String NAME = "fregata:experiment-army";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("试验ID")
    @Column(name = "experimentId")
    private String experimentId;

    @Display("上级ID")
    @Column(name = "parentId")
    private String parentId;

    @Display("分组名")
    @Column(name = "name")
    private String name;

    @Display("装备ID")
    @Column(name = "equipmentId")
    @Associative(name = "equipmentName", providerClass = EquipmentNameProvider.class)
    private String equipmentId;

    @Display("数量")
    @Column(name = "number")
    private Integer number;

    @Display("类型【0红、1蓝】")
    @Column(name = "type")
    private ArmyType type;

    @Display("属性")
    @Column(name = "attribute")
    private String attribute;

    @Display("创建时间")
    @Column(name = "createTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @Display("修改时间")
    @Column(name = "modifyTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp modifyTime;

    @Display("创建人")
    @Column(name = "creator")
    private String creator;

    @Display("修改人")
    @Column(name = "modifier")
    private String modifier;

    @Transient
    private List<ExperimentArmy> children;

}
