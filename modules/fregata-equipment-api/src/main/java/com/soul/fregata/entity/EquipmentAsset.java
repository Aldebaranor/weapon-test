package com.soul.fregata.entity;

import com.egova.model.BaseEntity;
import com.egova.model.annotation.Display;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.soul.fregata.entity.enums.AssetType;
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
@Table(name = "fregata_equipment_asset")
@Display("设备资源")
@EqualsAndHashCode(callSuper = true)
public class EquipmentAsset extends BaseEntity {

    public static final String NAME = "fregata:equipment-asset";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("设备主键")
    @Column(name = "equipmentId")
    private String equipmentId;

    @Display("资产类型【0:扩展字段，1：技术参数】")
    @Column(name = "type")
    private AssetType type;

    @Display("内容")
    @Column(name = "content")
    private String content;

    @Display("创建时间")
    @Column(name = "createTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @Display("创建人")
    @Column(name = "creator")
    private String creator;

    @Display("修改时间")
    @Column(name = "modifyTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp modifyTime;

    @Display("修改人")
    @Column(name = "modifier")
    private String modifier;

}
