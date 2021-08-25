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
@Table(name = "fregata_equipment_click")
@Display("装备浏览表")
@EqualsAndHashCode(callSuper = true)
public class EquipmentHot extends BaseEntity {

    public static final String NAME = "fregata:equipment-hot";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("设备主键")
    @Column(name = "equipmentId")
    private String equipmentId;

    @Display("创建时间")
    @Column(name = "createTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @Display("创建人")
    @Column(name = "creator")
    private String creator;

}
