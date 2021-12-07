package com.soul.weapon.entity.HistoryInfo;

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
@Table(name = "his_sam_report")
@Display("舰空导弹武器通道测试-1")
@EqualsAndHashCode(callSuper = true)
public class ShipToAirMissileTestReport extends BaseEntity {

    public static final String NAME="weapon-test:ShipToAirMissileTestReport";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("测试时间")
    @Column(name = "time")
    private Long time;

    @Display("舰空导弹跟踪雷达标识")
    @Column(name = "radarId")
    private String radarId;

    @Display("舰空导弹跟踪雷达自检")
    @Column(name = "radarSelfCheck")
    private Boolean radarSelfCheck;

    @Display("舰空导弹火控系统标识")
    @Column(name = "fireControlId")
    private String fireControlId;

    @Display("舰空导弹火控系统自检")
    @Column(name = "fireControlSelfCheck")
    private Boolean fireControlSelfCheck;

    @Display("舰空导弹发射系统标识")
    @Column(name = "launcherId")
    private String launcherId;

    @Display("舰空导弹发射系统自检")
    @Column(name = "launcherSelfCheck")
    private Boolean launcherSelfCheck;

    @Display("舰空导弹近程标识")
    @Column(name = "missileShortId")
    private String missileShortId;

    @Display("舰空导弹近程自检")
    @Column(name = "missileSelfShortCheck")
    private Boolean missileSelfShortCheck;

    @Display("舰空导弹中程标识")
    @Column(name = "missileMediumId")
    private String missileMediumId;

    @Display("舰空导弹中程自检")
    @Column(name = "missileSelfMediumCheck")
    private Boolean missileSelfMediumCheck;

    @Display("舰空导弹远程标识")
    @Column(name = "missileLongId")
    private String missileLongId;

    @Display("舰空导弹远程自检")
    @Column(name = "missileSelfLongCheck")
    private Boolean missileSelfLongCheck;

    @Display("舰空导弹武器通道状态")
    @Column(name = "status")
    private Boolean status;

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
