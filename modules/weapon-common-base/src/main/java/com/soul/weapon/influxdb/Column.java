package com.soul.weapon.influxdb;


import com.soul.weapon.utils.DateParserUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.JDBCType;
import java.util.Date;

/**
 * 列
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Column {

    /**
     * 列名
     */
    private String name;

    /**
     * 类型
     */
    private Type type;

    /**
     * 数据库原生类型
     */
    private String rawType;

    /**
     * 列大小，字符串长度或者数值总长度
     */
    private Integer size;

    /**
     * 小数部分的位数，如果不是小数，值为0
     */
    private Integer decimalDigits;

    /**
     * 是否允许为null
     */
    private Boolean nullable;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否为主键
     */
    private Boolean primaryKey;

    @Slf4j
    public enum Type {

        /**
         * 布尔
         */
        BOOLEAN,

        /**
         * 整型
         */
        INT,

        /**
         * 长整型
         */
        BIGINT,

        /**
         * 浮点数
         */
        FLOAT,

        /**
         * 双精度浮点数
         */
        DOUBLE,

        /**
         * 日期
         */
        DATE,

        /**
         * 日期时间
         */
        DATETIME,

        /**
         * 字符串
         */
        CHAR,

        /**
         * 字符串
         */
        VARCHAR,

        /**
         * 文本
         */
        TEXT,

        /**
         * 点
         */
        POINT,

        /**
         * 线
         */
        LINESTRING,

        /**
         * 面
         */
        POLYGON,

        /**
         * 多点
         */
        MULTIPOINT,

        /**
         * 多线
         */
        MULTILINESTRING,

        /**
         * 多面
         */
        MULTIPOLYGON,

        /**
         * 空间集合
         */
        GEOMETRYCOLLECTION,

        /**
         * 空间对象
         */
        GEOMETRY,

        /**
         * 其他
         */
        OTHER,

        ;

        public static Type parse(String type) {
            Type[] values = Type.values();
            for (Type value : values) {
                if (value.name().equalsIgnoreCase(type)) {
                    return value;
                }
            }
            return Type.OTHER;
        }

        public static Type parse(int type) {
            JDBCType jdbcType;
            try {
                jdbcType = JDBCType.valueOf(type);
            } catch (IllegalArgumentException e) {
                log.error("不是一个合法的JDBCType", e);
                return Type.OTHER;
            }
            return parse(jdbcType);
        }

        public static Type parse(JDBCType type) {
            switch (type) {
                case BIT:
                    return Type.BOOLEAN;
                case TINYINT:
                case SMALLINT:
                case INTEGER:
                    return Type.INT;
                case BIGINT:
                    return Type.BIGINT;
                case FLOAT:
                case REAL:
                    return Type.FLOAT;
                case DOUBLE:
                case NUMERIC:
                case DECIMAL:
                    return Type.DOUBLE;
                case TIME:
                case TIME_WITH_TIMEZONE:
                case DATE:
                    return Type.DATE;
                case TIMESTAMP:
                case TIMESTAMP_WITH_TIMEZONE:
                    return Type.DATETIME;
                case CHAR:
                case VARCHAR:
                case LONGVARCHAR:
                    return Type.VARCHAR;
                case BINARY:
                case VARBINARY:
                case LONGVARBINARY:
                case BLOB:
                case CLOB:
                    return Type.TEXT;
                default:
                    return Type.OTHER;
            }
        }

        public Object to(Object value) throws Exception {
            if (value == null) {
                return null;
            }
            switch (this) {
                case BOOLEAN:
                    if (value instanceof Boolean) {
                        return value;
                    }
                    return BooleanUtils.toBoolean(value.toString());
                case INT:
                case BIGINT:
                    if (value instanceof Number) {
                        return value;
                    }
                    if (!NumberUtils.isCreatable(value.toString())) {
                        // 不合法的数值
                        throw new Exception("字段值 " + value + " 不是一个正确的类型: " + this);
                    }
                    try {
                        return Long.parseLong(value.toString());
                    } catch (Exception e) {
                        // 不合法的时间
                        throw new Exception("字段值 " + value + " 不是一个正确的类型: " + this);
                    }
                case FLOAT:
                case DOUBLE:
                    if (value instanceof Number) {
                        return value;
                    }
                    if (!NumberUtils.isCreatable(value.toString())) {
                        // 不合法的数值
                        throw new Exception("字段值 " + value + " 不是一个正确的类型: " + this);
                    }
                    try {
                        return Double.parseDouble(value.toString());
                    } catch (Exception e) {
                        // 不合法的时间
                        throw new Exception("字段值 " + value + " 不是一个正确的类型: " + this);
                    }
                case DATE:
                case DATETIME:
                    if (value instanceof Date) {
                        return value;
                    }
                    String v = value.toString();
                    // 时间撮
                    if (NumberUtils.isCreatable(v)) {
                        try {
                            long time = Long.parseLong(v);
                            return new Date(time);
                        } catch (Exception e) {
                            throw new Exception("字段值 " + value + " 不是一个正确的类型: " + this);
                        }
                    }
                    // 时间字符串
                    try {
                        return DateUtils.parseDate(value.toString(), DateParserUtils.FORMATS);
                    } catch (Exception e) {
                        // 不合法的时间
                        throw new Exception("字段值 " + value + " 不是一个正确的类型: " + this);
                    }
                case VARCHAR:
                case TEXT:
                    return value.toString();
                case OTHER:
                    return value;
                default:
                    throw new Exception("字段值 " + value + " 不是一个正确的类型: " + this);
            }
        }

    }

    public Integer getSize() {
        return size == null ? 0 : size;
    }

    public Integer getDecimalDigits() {
        return decimalDigits == null ? 0 : decimalDigits;
    }

    public Boolean getNullable() {
        return nullable == null ? true : nullable;
    }

}
