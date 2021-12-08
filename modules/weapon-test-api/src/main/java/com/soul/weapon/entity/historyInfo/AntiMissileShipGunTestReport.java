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
@Table(name = "his_amsg_report")
@Display("反导舰炮武器通道测试-2")
@EqualsAndHashCode(callSuper = true)
public class AntiMissileShipGunTestReport extends BaseEntity {

    public static final String NAME="weapon-test:AntiMissileShipGunTestReport";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("测试时间")
    @Column(name = "time")
    private Long time;

    @Display("反导舰炮跟踪雷达标识")
    @Column(name = "radarId")
    private String radarId;

    @Display("反导舰炮跟踪雷达自检")
    @Column(name = "radarSelfCheck")
    private Boolean radarSelfCheck;

    @Display("反导舰炮火控系统标识")
    @Column(name = "fireControlId")
    private String fireControlId;

    @Display("反导舰炮火控系统自检")
    @Column(name = "fireControlSelfCheck")
    private Boolean fireControlSelfCheck;

    @Display("反导舰炮标识")
    @Column(name = "shipGunId")
    private String shipGunId;

    @Display("反导舰炮自检")
    @Column(name = "shipGunSelfCheck")
    private Boolean shipGunSelfCheck;

    @Display("反导舰炮武器通道状态")
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
