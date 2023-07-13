package com.soul.fire.entity;

import com.egova.model.BaseEntity;
import com.egova.model.annotation.Display;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.soul.fire.entity.enums.ConflictType;
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
@Table(name = "fire_priority")
@Display("武器优先级表")
@EqualsAndHashCode(callSuper = true)
public class FirePriority extends BaseEntity{

    public static final String NAME = "fire-test:priority";
    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("规则序号")
    @Column(name = "code")
    private String code;

    @Display("冲突类型")
    @Column(name = "ConflictType")
    private ConflictType conflictType;

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

    @Display("废弃标志")
    @Column(name = "disabled")
    private boolean disabled;

    @Display("A对B的优先级")
    @Column(name = "aBetterThanB")
    private boolean aBetterThanB;

    @Display("外键-武器Aid")
    @Column(name = "weaponAId")
    private String weaponAId;

    @Display("外键-武器Bid")
    @Column(name = "weaponBId")
    private String weaponBId;
}
