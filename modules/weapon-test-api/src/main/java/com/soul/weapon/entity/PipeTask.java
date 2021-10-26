package com.soul.weapon.entity;

import com.egova.model.BaseEntity;
import com.egova.model.annotation.Display;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.soul.weapon.entity.codes.PipeState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
@Data
@Entity
@Table(name = "pipe_task")
@Display("任务表")
@EqualsAndHashCode(callSuper = true)
public class PipeTask extends BaseEntity {

    public static final String NAME="weapon-test:task";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("任务名称")
    @Column(name = "name",unique = true)
    private String name;

    @Display("任务编号")
    @Column(name = "code")
    private String code;

    @Display("任务内容")
    @Column(name = "description")
    private String description;

    @Display("开始时间")
    @Column(name = "startTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp startTime;

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

    @Display("结束时间")
    @Column(name = "endTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp endTime;

    @Display("创建人")
    @Column(name="creator")
    private String creator;

    @Display("废弃标志")
    @Column(name = "disabled")
    private boolean disabled;

    @Display("运行状态")
    @Column(name="status")
    private PipeState status;
}
