package com.soul.fire.entity;

import com.egova.model.BaseEntity;
import com.egova.model.annotation.Display;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.soul.fire.entity.enums.ConflictSourceType;
import com.soul.fire.entity.enums.ConflictType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * @Auther: 码头工人
 * @Date: 2021/11/01/2:10 下午
 * @Description:
 */

@Data
@Entity
@Table(name = "fire_conflict")
@Display("冲突表")
@EqualsAndHashCode(callSuper = true)
public class FireConflict extends BaseEntity {
    public static final String NAME = "fire-test:conflict";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("任务编号")
    @Column(name = "code")
    private String code;

    @Display("兼容预判任务名称")
    @Column(name = "name")
    private String name;

    @Display("冲突类型")
    @Column(name = "ConflictType")
    private ConflictType conflictType;

    @Display("管控，预判")
    @Column(name = "conflictSource")
    private ConflictSourceType conflictSource;

    @Display("外键-任务")
    @Column(name = "taskId")
    private String taskId;

    @Display("冲突发生时间")
    @Column(name = "conflictTime")
    private Long conflictTime;

    @Display("冲突实体")
    @Column(name = "conflictObjects")
    private String conflictObjects;

    @Display("冲突解决")
    @Column(name = "conflictSolution")
    private String conflictSolution;

    @Display("创建人")
    @Column(name = "creator")
    private String creator;

    @Display("废弃标志")
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
}
