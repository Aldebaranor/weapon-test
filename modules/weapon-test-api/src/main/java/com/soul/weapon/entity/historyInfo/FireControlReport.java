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
@Table(name = "his_fire_control_report")
@Display("火控解算精度测试-12")
@EqualsAndHashCode(callSuper = true)
public class FireControlReport extends BaseEntity {

    public static final String NAME="weapon-test:FireControlReport";

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

    @Display("火控系统标识")
    @Column(name = "fireControlId")
    private String fireControlId;

    @Display("火控系统类型")
    @Column(name = "fireControlType")
    private String fireControlType;

    @Display("测试时间")
    @Column(name = "time")
    private Long time;

    @Display("目标距离火控解算精度")
    @Column(name = "targetDistance")
    private Float targetDistance;

    @Display("目标方位角火控解算精度")
    @Column(name = "targetPitch")
    private Float targetPitch;

    @Display("目标俯仰角火控解算精度")
    @Column(name = "targetAzimuth")
    private Float targetAzimuth;

    @Display("目标深度火控解算精度")
    @Column(name = "targetDepth")
    private Float targetDepth;

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
