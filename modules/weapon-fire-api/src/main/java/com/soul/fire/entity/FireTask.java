package com.soul.fire.entity;

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
@Table(name = "fire_task")
@Display("任务表")
@EqualsAndHashCode(callSuper = true)
public class FireTask extends BaseEntity{

    public static final String NAME = "fire-test:task";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("任务序号")
    @Column(name = "code")
    private String code;

    @Display("兼容预判任务名称")
    @Column(name = "name")
    private String name;


    @Display("开始时间")
    @Column(name = "startTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp startTime;

    @Display("结束时间")
    @Column(name = "endTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp endTime;

    @Display("修改时间")
    @Column(name = "modifyTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp modifyTime;

    @Display("创建时间")
    @Column(name = "createTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @Display("创建人")
    @Column(name = "creator")
    private String creator;

    @Display("运行标志")
    @Column(name = "running")
    private boolean running;

    @Display("废弃标志")
    @Column(name = "disabled")
    private boolean disabled;
}
