package com.soul.fregata.entity;

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
@Table(name = "fregata_experiment_attribute")
@Display("试验属性")
@EqualsAndHashCode(callSuper = true)
public class ExperimentAttribute extends BaseEntity {

    public static final String NAME = "fregata:experiment-attribute";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("试验ID")
    @Column(name = "experimentId")
    private String experimentId;

    @Display("兵力ID")
    @Column(name = "armyId")
    private String armyId;

    @Display("名称")
    @Column(name = "name")
    private String name;

    @Display("经度")
    @Column(name = "longitude")
    private Double longitude;

    @Display("纬度")
    @Column(name = "latitude")
    private Double latitude;

    @Display("高度（米）")
    @Column(name = "height")
    private Double height;

    @Display("速度（m/s）")
    @Column(name = "speed")
    private Double speed;

    @Display("方向角")
    @Column(name = "directionAngle")
    private Double directionAngle;

    @Display("航向角")
    @Column(name = "courseAngle")
    private Double courseAngle;

    @Display("滚转角")
    @Column(name = "rollAngle")
    private Double rollAngle;

    @Display("俯仰角")
    @Column(name = "pitchAngle")
    private Double pitchAngle;

    @Display("决策模型")
    @Column(name = "model")
    private String model;

    @Display("作战计划")
    @Column(name = "plan")
    private String plan;

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
