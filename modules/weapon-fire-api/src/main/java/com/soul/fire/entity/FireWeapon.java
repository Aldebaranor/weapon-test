package com.soul.fire.entity;

import com.egova.model.BaseEntity;
import com.egova.model.annotation.Display;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.soul.fire.entity.codes.WeaponType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "fire_weapon")
@Display("武器表")
@EqualsAndHashCode(callSuper = true)
public class FireWeapon extends BaseEntity{

    public static final String NAME = "fire-test:weapon";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("武器编号")
    @Column(name = "code")
    private String code;

    @Display("武器名称")
    @Column(name = "name")
    private String name;

    @Display("武器类型")
    @Column(name = "type")
    private WeaponType type;

    @Display("创建人")
    @Column(name = "creator")
    private String creator;

    @Display("排序字段")
    @Column(name = "sortKey")
    private Integer sortKey;

    @Display("管控标识")
    @Column(name = "controlled")
    private Boolean controlled;

    @Display("自检字段")
    @Column(name = "selfCheck")
    private Boolean selfCheck;

    @Display("废弃标志")
    @Column(name = "disabled")
    private Boolean disabled;

    @Display("装备位置")
    @Column(name = "x")
    private Float x;

    @Display("装备位置")
    @Column(name = "y")
    private Float y;

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
}
