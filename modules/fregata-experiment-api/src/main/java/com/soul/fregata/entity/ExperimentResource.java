package com.soul.fregata.entity;

import com.egova.model.BaseEntity;
import com.egova.model.annotation.Display;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.soul.fregata.entity.enums.ResourceType;
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
@Table(name = "fregata_experiment_resource")
@Display("场景资源")
@EqualsAndHashCode(callSuper = true)
public class ExperimentResource extends BaseEntity {

    public static final String NAME = "fregata:experiment-resource";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("资源名称")
    @Column(name = "name")
    private String name;

    @Display("类型【0区域，1路线】")
    @Column(name = "type")
    private ResourceType type;

    @Display("试验ID")
    @Column(name = "experimentId")
    private String experimentId;

    @Display("内容【区域点位或路线点位】")
    @Column(name = "content")
    private String content;

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
