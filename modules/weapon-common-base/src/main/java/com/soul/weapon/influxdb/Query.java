package com.soul.weapon.influxdb;

import com.soul.weapon.utils.EscapeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 查询
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Query {

    /**
     * 字段名称
     */
    private String name;

    /**
     * 操作符
     */
    private Operate operate;

    /**
     * 字段值
     */
    private Object value;

    public enum Operate {

        /**
         * 等于
         */
        EQ,

        /**
         * 大于
         */
        GT,

        /**
         * 大于等于
         */
        GTE,

        /**
         * 小于
         */
        LT,

        /**
         * 小于等于
         */
        LTE,

        /**
         * 模糊
         */
        LIKE,

        /**
         * 不为空
         */
        IS_NOT_NULL,

        /**
         * 为空
         */
        IS_NULL,

        /**
         * 数组
         */
        IN;

    }

    public String toParameterSql() {
        switch (operate) {
            case EQ:
                return name + " = ?";
            case GT:
                return name + " > ?";
            case GTE:
                return name + " >= ?";
            case LT:
                return name + " < ?";
            case LTE:
                return name + " <= ?";
            case LIKE:
                return name + " LIKE ?";
            case IS_NULL:
                return name + " IS NULL";
            case IS_NOT_NULL:
                return name + " IS NOT NULL";
            case IN:
                if (this.value instanceof List) {
                    List value = (List) this.value;
                    return name + " IN (" + join(", ", value.size()) + ")";
                }
                if (this.value instanceof Object[]) {
                    Object[] value = (Object[]) this.value;
                    return name + " IN (" + join(", ", value.length) + ")";
                }
                return "";
            default:
                return "";
        }
    }

    public String toSql() {
        switch (operate) {
            case EQ:
                if (this.value == null) {
                    return "";
                }
                return name + " = " + parseSqlValue(this.value);
            case GT:
                if (this.value == null) {
                    return "";
                }
                return name + " > " + parseSqlValue(this.value);
            case GTE:
                if (this.value == null) {
                    return "";
                }
                return name + " >= " + parseSqlValue(this.value);
            case LT:
                if (this.value == null) {
                    return "";
                }
                return name + " < " + parseSqlValue(this.value);
            case LTE:
                if (this.value == null) {
                    return "";
                }
                return name + " <= " + parseSqlValue(this.value);
            case LIKE:
                if (this.value == null) {
                    return "";
                }
                return name + " LIKE '%" + this.value + "%'";
            case IS_NULL:
                return name + " IS NULL";
            case IS_NOT_NULL:
                return name + " IS NOT NULL";
            case IN:
                if (this.value == null) {
                    return "";
                }
                if (this.value instanceof List) {
                    List<?> value = (List) this.value;
                    String in = value.stream().filter(Objects::nonNull).map(this::parseSqlValue).collect(Collectors.joining(", ", "(", ")"));
                    return name + " IN " + in;
                }
                if (this.value instanceof Object[]) {
                    Object[] value = (Object[]) this.value;
                    String in = Arrays.stream(value).filter(Objects::nonNull).map(this::parseSqlValue).collect(Collectors.joining(", ", "(", ")"));
                    return name + " IN " + in;
                }
                return "";
            default:
                return "";
        }
    }

    public boolean requireValue() {
        if (operate == Operate.IS_NULL) {
            return false;
        }
        if (operate == Operate.IS_NOT_NULL) {
            return false;
        }
        return true;
    }

    private String join(String delimiter, int len) {
        StringBuilder join = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (i == 0) {
                join.append("?");
            } else {
                join.append(delimiter).append("?");
            }
        }
        return join.toString();
    }

    private String parseSqlValue(Object value) {
        if (value instanceof Number) {
            return value.toString();
        }
        return "'" + EscapeUtils.sql(value.toString()) + "'";
    }

}
