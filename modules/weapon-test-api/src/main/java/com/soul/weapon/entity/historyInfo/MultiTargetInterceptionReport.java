package com.soul.weapon.entity.historyInfo;

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
 * @author dxq
 * @Date 2021/12/7 11:17
 */

@Data
@Entity
@Table(name = "his_mti_report")
@Display("多目标拦截能力测试-15")
@EqualsAndHashCode(callSuper = true)
public class MultiTargetInterceptionReport extends BaseEntity {

    public static final String NAME="weapon-test:MultiTargetInterceptionReport";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("目标标识")
    @Column(name = "targetId")
    private String targetId;

    @Display("目标类型")
    @Column(name = "targetType")
    private String targetType;

    @Display("测试时间")
    @Column(name = "time")
    private Long time;

    @Display("目标拦截时间")
    @Column(name = "interceptionTime")
    private Long interceptionTime;

    @Display("目标拦截数量")
    @Column(name = "interceptionAccount")
    private Integer interceptionAccount;

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


    public boolean getDisabled() {
        return disabled;
    }
}
