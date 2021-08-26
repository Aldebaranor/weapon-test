package com.soul.weapon.entity;

import com.egova.model.BaseEntity;
import com.egova.model.annotation.Display;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * created by nash5
 */
@Data
@Entity
@Table(name = "task")
@Display("任务表")
@EqualsAndHashCode(callSuper = true)
public class Task extends BaseEntity {

    public static final String NAME = "weapon:task";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("任务名称")
    @Column(name = "taskTitle")
    private String name;

    @Display("描述")
    @Column(name = "taskDescription")
    private String description;

    @Display("创建人")
    @Column(name = "owner")
    private String owner;

    @Display("创建时间")
    @Column(name = "createTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

}
