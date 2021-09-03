package com.soul.weapon.entity;

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

@Data
@Entity
@Table(name = "self_check")
@Display("自检表")
@EqualsAndHashCode(callSuper = true)
public class PipeSelfCheck extends BaseEntity {

    public static final String NAME="weapon-test:selfCheck";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("装备名称")
    @Column(name = "name",unique = true)
    private String name;

    @Display("装备编号")
    @Column(name = "code")
    private String code;

    @Display("显示名称, 供前端调用(所属装备类)")
    @Column(name = "showName")
    private String showName;

    @Display("该装备对应的显示名称是否有Err")
    @Column(name = "hasErr")
    private boolean hasErr;

    @Display("创建时间")
    @Column(name = "createTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp createTime;

    @Display("修改时间")
    @Column(name = "modifyTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp modifyTime;

    @Display("创建人")
    @Column(name="creator")
    private String creator;

    @Display("排序字段")
    @Column(name = "sortKey")
    private Integer sortKey;

    @Display("废弃标志")
    @Column(name = "disabled")
    private boolean disabled;

    @Display("关联任务id,外键")
    @Column(name = "taskId")
    private String taskId;
}
