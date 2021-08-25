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
@Table(name = "fregata_scheme")
@Display("装备方案表")
@EqualsAndHashCode(callSuper = true)
public class Scheme extends BaseEntity {

    public static final String NAME = "fregata:scheme";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("方案名称")
    @Column(name = "name")
    private String name;

    @Display("方案编号")
    @Column(name = "code")
    private String code;

    @Display("是否默认方案（我的试验中可以编辑非默认方案）")
    @Column(name = "beDefault")
    private Boolean beDefault;

    @Display("是否禁用")
    @Column(name = "disabled")
    private Boolean disabled;

    @Display("设备")
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

    @Display("修改时间")
    @Column(name = "modifyTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp modifyTime;

    @Display("修改人")
    @Column(name = "modifier")
    private String modifier;

    @OneToOne(targetEntity = Equipment.class, mappedBy = "id")
    @JoinColumn(name = "equipmentId")
    private Equipment equipment;

    //    @OneToMany(targetEntity = SchemeDetail.class, mappedBy = "schemeId")
    //    @JoinColumn(name = "id")
    @Transient
    private List<SchemeDetail> details;

}
