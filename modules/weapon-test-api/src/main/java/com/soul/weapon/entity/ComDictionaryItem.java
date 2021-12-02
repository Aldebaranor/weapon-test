package com.soul.weapon.entity;

import com.egova.model.annotation.Display;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Author: dxq
 * @Date: 2021/12/2 15:10
 */
@Data
@Entity
@Table(name = "com_dictionary_item")
@Display("字典表")
public class ComDictionaryItem implements Serializable, com.egova.model.Entity {
    public static final String NAME="weapon-test:pipe-test-type";

    @Id
    @Display("主键")
    @Column(name = "id")
    private String id;

    @Display("字段标识")
    @Column(name = "code")
    private String code;

    @Display("字体值")
    @Column(name = "value")
    private String value;

    @Display("描述")
    @Column(name = "text")
    private String text;

    @Display("图标")
    @Column(name = "icon")
    private String icon;

    @Display("字典类型")
    @Column(name = "type")
    private String type;

    @Display("拼音")
    @Column(name = "phonetic")
    private String phonetic;

    @Display("上级节点Id")
    @Column(name = "parentId")
    private String parentId;

    @Display("分组")
    @Column(name = "groupId")
    private String groupId;

    @Display("排序")
    @Column(name = "sort")
    private String sort;
}
