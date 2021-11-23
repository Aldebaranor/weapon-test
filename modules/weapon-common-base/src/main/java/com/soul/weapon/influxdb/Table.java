package com.soul.weapon.influxdb;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 表
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
@Data
public class Table {

    private String catalog;

    private String schema;

    private String name;

    private List<Column> columns;

    public Table() {
    }

    public Table(String name) {
        this.name = name;
    }

    public void addColumn(Column column) {
        if (CollectionUtils.isEmpty(this.columns)) {
            this.columns = new ArrayList<>();
        }
        this.columns.add(column);
    }

}
