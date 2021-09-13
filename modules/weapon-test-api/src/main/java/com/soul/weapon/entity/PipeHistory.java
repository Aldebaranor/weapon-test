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

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
@Data
@Entity
@Table(name = "pipe_history")
@Display("历史表")
@EqualsAndHashCode(callSuper = true)
public class PipeHistory extends BaseEntity {

    public static final String NAME="weapon-test:history";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("测试值")
    @Column(name = "res",unique = true)
    private String res;

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

    @Display("关联测试项id,外键")
    @Column(name = "pipeTestId")
    private String pipeTestId;

}
