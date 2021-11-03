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
@Table(name = "fire_conflict_priority")
@Display("冲突类型优先级表")
@EqualsAndHashCode(callSuper = true)
public class FireConflictPriority extends BaseEntity {
    public static final String NAME = "fire-test:conflictPriority";

    @Id
    @Display("主键")
    @Column(name = "ConflictType")
    private String id;

    @Display("冲突优先级")
    @Column(name = "conflictPriority")
    private Integer conflictPriority;
}
