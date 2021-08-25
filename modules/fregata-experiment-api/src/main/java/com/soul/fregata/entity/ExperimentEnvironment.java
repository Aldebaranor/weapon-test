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
import java.sql.Timestamp;

/**
 * created by 迷途小码农
 */
@Data
@Entity
@Table(name = "fregata_experiment_environment")
@Display("试验环境")
@EqualsAndHashCode(callSuper = true)
public class ExperimentEnvironment extends BaseEntity {

    public static final String NAME = "fregata:experiment-environment";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("试验ID")
    @Column(name = "experimentId")
    private String experimentId;

    @Display("作战区域")
    @Column(name = "area")
    private String area;

    @Display("起始时间")
    @Column(name = "startTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp startTime;

    @Display("结束时间")
    @Column(name = "endTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp endTime;

    @Display("地形")
    @Column(name = "topography")
    @Associative(name = "topographyName", providerClass = DictionaryItemTextProvider.class)
    private String topography;

    @Display("天气")
    @Column(name = "weather")
    @Associative(name = "weatherName", providerClass = DictionaryItemTextProvider.class)
    private String weather;

    @Display("风级别")
    @Column(name = "windLevel")
    @Associative(name = "windLevelName", providerClass = DictionaryItemTextProvider.class)
    private String windLevel;

    @Display("风速")
    @Column(name = "windSpeed")
    @Associative(name = "windSpeedName", providerClass = DictionaryItemTextProvider.class)
    private String windSpeed;

    @Display("风向")
    @Column(name = "windDirection")
    @Associative(name = "windDirectionName", providerClass = DictionaryItemTextProvider.class)
    private String windDirection;

    @Display("低云族")
    @Column(name = "lowCloud")
    @Associative(name = "lowCloudName", providerClass = DictionaryItemTextProvider.class)
    private String lowCloud;

    @Display("中云族")
    @Column(name = "middleCloud")
    @Associative(name = "middleCloudName", providerClass = DictionaryItemTextProvider.class)
    private String middleCloud;

    @Display("高云族")
    @Column(name = "highCloud")
    @Associative(name = "highCloudName", providerClass = DictionaryItemTextProvider.class)
    private String highCloud;

    @Display("直展云族")
    @Column(name = "verticalCloud")
    @Associative(name = "verticalCloudName", providerClass = DictionaryItemTextProvider.class)
    private String verticalCloud;

    @Display("雷电")
    @Column(name = "thunder")
    @Associative(name = "thunderName", providerClass = DictionaryItemTextProvider.class)
    private String thunder;

    @Display("海况")
    @Column(name = "seaState")
    @Associative(name = "seaStateName", providerClass = DictionaryItemTextProvider.class)
    private String seaState;

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
