package com.soul.fregata.entity;

import com.egova.associative.Associative;
import com.egova.model.BaseEntity;
import com.egova.model.annotation.Display;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.soul.fregata.associative.EquipmentTypeNameProvider;
import com.soul.fregata.entity.codes.Country;
import com.soul.fregata.entity.codes.EquipmentTypeIntro;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * created by 迷途小码农
 */
@Data
@Entity
@Table(name = "fregata_equipment")
@Display("装备基础表")
@EqualsAndHashCode(callSuper = true)
public class Equipment extends BaseEntity {

    public static final String NAME = "fregata:equipment";

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

    @Display("描述")
    @Column(name = "description")
    private String description;

    @Display("照片")
    @Column(name = "photo")
    private String photo;

    @Display("数据完整度（字典）")
    @Column(name = "dataIntegrity")
    private String dataIntegrity;

    @Display("产地国家（字典）")
    @Column(name = "country")
    private Country country;

    @Display("平台分类")
    @Column(name = "categoryType")
    // @Associative(name = "categoryTypeName", providerClass = EquipmentTypeNameProvider.class)
    private EquipmentTypeIntro categoryType;

    @Display("大类")
    @Column(name = "mainType")
    // @Associative(name = "mainTypeName", providerClass = EquipmentTypeNameProvider.class)
    private EquipmentTypeIntro mainType;

    @Display("小类")
    @Column(name = "subType")
    // @Associative(name = "subTypeName", providerClass = EquipmentTypeNameProvider.class)
    private EquipmentTypeIntro subType;

    @Display("细类")
    @Column(name = "thirdType")
    // @Associative(name = "thirdTypeName", providerClass = EquipmentTypeNameProvider.class)
    private EquipmentTypeIntro thirdType;

    @Display("军标")
    @Column(name = "armyIcon")
    private String armyIcon;

    @Display("图标")
    @Column(name = "icon")
    private String icon;

    @Display("模型")
    @Column(name = "model")
    private String model;

    @Display("禁用标识")
    @Column(name = "disabled")
    private Boolean disabled;

    @Display("备注")
    @Column(name = "remark")
    private String remark;

    @Display("发布日期")
    @Column(name = "releaseDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Timestamp releaseDate;

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

    @OneToMany(targetEntity = EquipmentAsset.class, mappedBy = "equipmentId")
    @JoinColumn(name = "id")
    private List<EquipmentAsset> assets;

}
