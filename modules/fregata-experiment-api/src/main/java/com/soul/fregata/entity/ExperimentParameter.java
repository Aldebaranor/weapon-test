package com.soul.fregata.entity;

import com.egova.associative.Associative;
import com.egova.associative.DictionaryItemTextProvider;
import com.egova.model.BaseEntity;
import com.egova.model.annotation.Display;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.soul.fregata.associative.DictionaryItemsProvider;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * created by yangL
 */
@Data
@Entity
@Table(name = "fregata_experiment_parameter")
@Display("试验参数")
@EqualsAndHashCode(callSuper = true)
public class ExperimentParameter extends BaseEntity {

    public static final String NAME = "fregata:experiment-parameter";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("试验ID")
    @Column(name = "experimentId")
    private String experimentId;

    @Display("步长")
    @Column(name = "step")
    private String step;

    @Display("模型粒度【字典】")
    @Column(name = "modelGranularity")
    @Associative(name = "modelGranularityName", providerClass = DictionaryItemTextProvider.class)
    private String modelGranularity;

    @Display("支撑服务【字典】")
    @Column(name = "supportService")
    @Associative(name = "supportServiceItems", providerClass = DictionaryItemsProvider.class)
    private String supportService;

    @Display("计算节点")
    @Column(name = "calculateNode")
    private String calculateNode;

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


}
