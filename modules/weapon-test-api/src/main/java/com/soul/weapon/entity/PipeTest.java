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
@Table(name = "pipe_test")
@Display("测试表")
@EqualsAndHashCode(callSuper = true)
public class PipeTest extends BaseEntity {

    public static final String NAME="weapon-test:test";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("测试名称")
    @Column(name = "name",unique = true)
    private String name;

    @Display("测试编号")
    @Column(name = "code")
    private String code;

    @Display("测试类别")
    @Column(name = "type")
    private String type;

    @Display("测试阈值")
    @Column(name = "threshold")
    private String threshold;

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

    @Display("排序字段")
    @Column(name = "sortKey")
    private Integer sortKey;

    @Display("创建人")
    @Column(name="creator")
    private String creator;

    @Display("废弃标志")
    @Column(name = "disabled")
    private boolean disabled;

    @Display("运行状态")
    @Column(name="status")
    private Integer status;

    @Display("关联任务id")
    @Column(name = "taskId")
    private String taskId;

}
