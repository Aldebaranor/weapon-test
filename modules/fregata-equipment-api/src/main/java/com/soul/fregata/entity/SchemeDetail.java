package com.soul.fregata.entity;

import com.egova.model.BaseEntity;
import com.egova.model.annotation.Display;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "fregata_scheme_detail")
@Display("装备方案与装备关联表")
@EqualsAndHashCode(callSuper = true)
public class SchemeDetail extends BaseEntity {

    public static final String NAME = "fregata:scheme-detail";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("方案ID")
    @Column(name = "schemeId")
    private String schemeId;

    @Display("方案搭载的设备ID")
    @Column(name = "equipmentId")
    private String equipmentId;

    @Display("该型设备的数量")
    @Column(name = "count")
    private Integer count;

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

    @OneToOne(targetEntity = Equipment.class, mappedBy = "id")
    @JoinColumn(name = "equipmentId")
    private Equipment equipment;


}
