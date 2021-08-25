package com.soul.fregata.entity;

import com.egova.associative.Associative;
import com.egova.associative.DictionaryItemTextProvider;
import com.egova.entity.Person;
import com.egova.model.BaseEntity;
import com.egova.model.annotation.Display;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * created by 迷途小码农
 */
@Data
@Entity
@Table(name = "fregata_experiment_team")
@Display("试验分享")
@EqualsAndHashCode(callSuper = true)
public class ExperimentTeam extends BaseEntity {

    public static final String NAME = "fregata:experiment-team";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("试验ID")
    @Column(name = "experimentId")
    private String experimentId;

    @Display("用户ID")
    @Column(name = "personId")
    private String personId;

    @Display("访客类型【字典】")
    @Column(name = "visitorType")
    @Associative(name = "visitorTypeName",providerClass = DictionaryItemTextProvider.class)
    private String visitorType;

    @Display("创建人")
    @Column(name = "creator")
    private String creator;

    @Display("创建时间")
    @Column(name = "createTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @OneToOne(targetEntity = Person.class, mappedBy = "id")
    @JoinColumn(name = "personId")
    private Person person;

}
