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
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.List;

/**
 * created by 迷途小码农
 */
@Data
@Entity
@Table(name = "fregata_equipment_type")
@Display("装备大小类表")
@EqualsAndHashCode(callSuper = true)
public class EquipmentType extends BaseEntity {

    public static final String NAME = "fregata:equipment-type";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("编码")
    @Column(name = "code")
    private String code;

    @Display("名称")
    @Column(name = "name")
    private String name;

    @Display("等级")
    @Column(name = "grade")
    private Integer grade;

    @Display("上级ID")
    @Column(name = "parentId")
    private String parentId;

    @Display("描述")
    @Column(name = "description")
    private String description;

    @Display("是否禁用")
    @Column(name = "disabled")
    private Boolean disabled;

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
    private List<EquipmentType> children;

}
