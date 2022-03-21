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
@Table(name = "his_torpedo_report")
@Display("鱼雷防御武器通道测试-3")
@EqualsAndHashCode(callSuper = true)
public class TorpedoTestReport extends BaseEntity {

    public static final String NAME="weapon-test:TorpedoTestReport";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("测试时间")
    @Column(name = "time")
    private Long time;

    @Display("声呐标识")
    @Column(name = "sonarId")
    private String sonarId;

    @Display("声呐自检")
    @Column(name = "sonarSelfCheck")
    private Boolean sonarSelfCheck;

    @Display("鱼雷防御武器火控系统标识")
    @Column(name = "fireControlId")
    private String fireControlId;

    @Display("鱼雷防御武器火控系统自检")
    @Column(name = "fireControlSelfCheck")
    private Boolean fireControlSelfCheck;

    @Display("鱼雷防御武器发射系统标识")
    @Column(name = "launcherId")
    private String launcherId;

    @Display("鱼雷防御武器发射系统自检")
    @Column(name = "launcherSelfCheck")
    private Boolean launcherSelfCheck;

    @Display("鱼雷防御武器标识")
    @Column(name = "torpedoId")
    private String torpedoId;

    @Display("鱼雷防御武器自检")
    @Column(name = "torpedoSelfCheck")
    private Boolean torpedoSelfCheck;

    @Display("鱼雷防御武器通道状态")
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

    public boolean getDisabled() {
        return disabled;
    }

}
