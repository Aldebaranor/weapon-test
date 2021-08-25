package com.soul.fregata.entity;

import com.egova.associative.Associative;
import com.egova.associative.DictionaryItemTextProvider;
import com.egova.model.BaseEntity;
import com.egova.model.annotation.Display;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "fregata_experiment")
@Display("我的试验")
@EqualsAndHashCode(callSuper = true)
public class Experiment extends BaseEntity {

    public static final String NAME = "fregata:experiment";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("试验名称")
    @Column(name = "name")
    private String name;

    @Display("描述")
    @Column(name = "description")
    private String description;

    @Display("密级【字典】")
    @Column(name = "secret")
    @Associative(name = "secretName", providerClass = DictionaryItemTextProvider.class)
    private String secret;

    @Display("任务类型【字典】")
    @Column(name = "taskType")
    @Associative(name = "taskTypeName", providerClass = DictionaryItemTextProvider.class)
    private String taskType;

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

    /**
     * 红方
     */
    @Transient
    private List<ExperimentArmy> redArmies;

    /**
     * 蓝方
     */
    @Transient
    private List<ExperimentArmy> blueArmies;

}
