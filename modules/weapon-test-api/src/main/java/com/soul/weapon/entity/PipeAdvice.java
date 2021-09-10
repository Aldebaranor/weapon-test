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
@Table(name = "test_res_advice")
@Display("自检表")
@EqualsAndHashCode(callSuper = true)
public class PipeAdvice extends BaseEntity {

    public static final String NAME="weapon-test:advice";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("测试项(1级)名称")
    @Column(name = "l1Name",unique = true)
    private String l1Name;

    @Display("测试项子项(2级)名称")
    @Column(name = "l2Name",unique = true)
    private String l2Name;

    @Display("l2是否有Err")
    @Column(name = "l2HasErr")
    private boolean l2HasErr;

    @Display("对策建议")
    @Column(name = "advice")
    private String advice;

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
