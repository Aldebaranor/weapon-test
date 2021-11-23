package com.soul.weapon.influxdb;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Auther: 码头工人
 * @Date: 2021/09/18/2:34 下午
 * @Description:
 */
@Slf4j
public class InfluxdbTemplate {

    @Autowired
    private InfluxDB influxDB;

    private String database;


    public InfluxdbTemplate(String database) {
        this.database = database;
    }

    public InfluxdbTemplate(String database,InfluxDB influxDB) {
        this.database = database;
        this.influxDB = influxDB;
    }


    public List<String> tables()  {
        QueryResult result = influxDB.query(new Query("SHOW MEASUREMENTS"));
        if (StringUtils.isNotBlank(result.getError())) {
            throw new RuntimeException("influxdb操作错误: " + result.getError());
        }
        // 过滤出表
        return result.getResults().stream()
                .findFirst()
                .flatMap(res -> {
                    if (res.getSeries() == null) {
                        return Optional.empty();
                    }
                    return res.getSeries().stream()
                            .findFirst()
                            .map(series -> series.getValues().stream()
                                    .map(values -> values.stream().findFirst().orElse(null))
                                    .filter(Objects::nonNull).map(Object::toString)
                                    .collect(Collectors.toList())
                            );
                }).orElse(Collections.emptyList());
    }

    public Table tableStructure(String tableName)  {
        Table table = new Table();
        table.setName(tableName);
        QueryResult result = influxDB.query(new Query("SHOW FIELD KEYS FROM " + tableName));
        if (StringUtils.isNotBlank(result.getError())) {
            throw new RuntimeException("influxdb操作错误: " + result.getError());
        }
        // 过滤出field
        result.getResults().stream()
                .findFirst()
                .flatMap(res -> {
                            if (res.getSeries() == null) {
                                return Optional.empty();
                            }
                            return res.getSeries().stream()
                                    .findFirst()
                                    .map(series -> series.getValues().stream()
                                            .map(values -> {
                                                String name = values.get(0).toString();
                                                String rawType = values.get(1).toString();
                                                Column.Type type;
                                                switch (rawType) {
                                                    case "string":
                                                        type = Column.Type.TEXT;
                                                        break;
                                                    case "integer":
                                                        type = Column.Type.INT;
                                                        break;
                                                    case "float":
                                                        type = Column.Type.FLOAT;
                                                        break;
                                                    case "boolean":
                                                        type = Column.Type.BOOLEAN;
                                                        break;
                                                    default:
                                                        type = Column.Type.OTHER;
                                                }
                                                return Column.builder()
                                                        .name(name)
                                                        .type(type)
                                                        .rawType(rawType)
                                                        .build();
                                            })
                                            .collect(Collectors.toList())
                                    );
                        }
                ).orElse(Collections.emptyList())
                .forEach(table::addColumn);
        result = influxDB.query(new Query("SHOW TAG KEYS FROM " + tableName));
        if (StringUtils.isNotBlank(result.getError())) {
            throw new RuntimeException("influxdb操作错误: " + result.getError());
        }
        // 过滤出tag
        result.getResults().stream()
                .findFirst()
                .flatMap(res -> {
                            if (res.getSeries() == null) {
                                return Optional.empty();
                            }
                            return res.getSeries().stream()
                                    .findFirst()
                                    .map(series -> series.getValues().stream()
                                            .map(values -> values.stream().findFirst().orElse(null))
                                            .filter(Objects::nonNull).map(Object::toString)
                                            .collect(Collectors.toList())
                                    );
                        }
                ).orElse(Collections.emptyList())
                .forEach(tag -> {
                    Column column = Column.builder()
                            .name(tag)
                            .type(Column.Type.VARCHAR)
                            .rawType("tag")
                            .build();
                    table.addColumn(column);
                });
        // 时间字段
        if (!CollectionUtils.isEmpty(table.getColumns())) {
            table.addColumn(Column.builder()
                    .name("time")
                    .type(Column.Type.DATETIME)
                    .rawType("time")
                    .build());
        }
        return table;
    }

    public Long count(String sql, Object... values)  {
        String countSql = sql.replaceAll("(?i)SELECT.+FROM", "SELECT COUNT(*) FROM");
        log.debug("count sql: {}", countSql);
        QueryResult result = influxDB.query(new Query(countSql));
        if (StringUtils.isNotBlank(result.getError())) {
            throw new RuntimeException("influxdb操作错误: " + result.getError());
        }
        return result.getResults().stream()
                .findFirst()
                .map(res -> Optional.ofNullable(res.getSeries())
                        .map(list -> list.stream()
                                .findFirst()
                                .map(series -> series.getValues().stream()
                                        .map(vs ->
                                                vs.stream().filter(Objects::nonNull).mapToLong(value -> {
                                                    try {
                                                        double v = Double.parseDouble(value.toString());
                                                        return (long) v;
                                                    } catch (NumberFormatException e) {
                                                        return 0;
                                                    }
                                                }).max().orElse(0L)
                                        ).findFirst().orElse(0L)
                                ).orElse(0L)).orElse(0L)
                ).orElse(0L);
    }

    public Page<Row> select(String sql, Page<Row> page, Object... values)  {
        page = Optional.ofNullable(page).orElseGet(Page::new);
        Long count = count(sql);
        if (count == null || count == 0) {
            page.setTotalCount(0L);
            return page;
        }
        List<Row> rows = doSelect(sql, page);
        page.setTotalCount(count);
        page.setData(rows);
        return page;
    }

    public List<Row> select(String sql)  {
        log.debug("page sql: {}", sql);
        QueryResult result = influxDB.query(new Query(sql));
        if (StringUtils.isNotBlank(result.getError())) {
            throw new RuntimeException("influxdb操作错误: " + result.getError());
        }
        return result.getResults().stream()
                .findFirst()
                .map(res -> res.getSeries().stream()
                        .findFirst()
                        .map(e -> {
                            List<String> columns = e.getColumns();
                            int size = columns.size();
                            return e.getValues().stream().map(row -> {
                                Map<String, Object> map = new HashMap<>(size);
                                for (int i = 0; i < size; i++) {
                                    Object value = row.get(i);
                                    map.put(columns.get(i), value);
                                }
                                return new Row(map);
                            }).collect(Collectors.toList());
                        }).orElse(Collections.emptyList())
                ).orElse(Collections.emptyList());
    }

    private List<Row> doSelect(String sql, Page<Row> page)  {
        String pageSql;
        if (StringUtils.containsIgnoreCase(sql, "limit")) {
            pageSql = String.format("SELECT * FROM (%s) ORDER BY time desc LIMIT %d OFFSET %d", sql, page.getPageSize(), page.getOffset());
        } else {
            pageSql = String.format("%s ORDER BY time desc LIMIT %d OFFSET %d", sql, page.getPageSize(), page.getOffset());
        }
        log.debug("page sql: {}", pageSql);
        QueryResult result = influxDB.query(new Query(pageSql));
        if (StringUtils.isNotBlank(result.getError())) {
            throw new RuntimeException("influxdb操作错误: " + result.getError());
        }
        return result.getResults().stream()
                .findFirst()
                .map(res -> res.getSeries().stream()
                        .findFirst()
                        .map(e -> {
                            List<String> columns = e.getColumns();
                            int size = columns.size();
                            return e.getValues().stream().map(row -> {
                                Map<String, Object> map = new HashMap<>(size);
                                for (int i = 0; i < size; i++) {
                                    Object value = row.get(i);
                                    map.put(columns.get(i), value);
                                }
                                return new Row(map);
                            }).collect(Collectors.toList());
                        }).orElse(Collections.emptyList())
                ).orElse(Collections.emptyList());
    }

    public Page<Row> select(String tableName, PageQuery<Row> pageQuery)  {
        Page<Row> page = Optional.ofNullable(pageQuery.getPaging()).orElseGet(Page::new);
        String sql = parseQuerySql(tableName, pageQuery);
        return select(sql, page);
    }

    public void drop(String tableName)  {
        QueryResult result = influxDB.query(new Query("DROP MEASUREMENT " + tableName));
        if (StringUtils.isNotBlank(result.getError())) {
            throw new RuntimeException("influxdb操作错误: " + result.getError());
        }
    }

    private String parseQuerySql(String tableName, PageQuery<Row> pageQuery) {
        List<com.soul.weapon.influxdb.Query> queries = pageQuery.getQueries();
        if (CollectionUtils.isEmpty(queries)) {
            return "SELECT * FROM " + tableName;
        }
        List<String> where = new ArrayList<>();
        queries.forEach(query -> {
            Object value = query.getValue();
            if (value == null) {
                return;
            }
            query.setName("\"" + query.getName() + "\"");
            String opName = null;
            switch (query.getOperate()) {
                case LIKE:
                    String sql = query.getName() + " =~ /" + query.getValue().toString() + "/";
                    where.add(sql);
                    return;
                case IS_NULL:
                        throw new RuntimeException("InfluxDB不支持为空(IS NULL)操作符");
                case IS_NOT_NULL:
                        throw new RuntimeException("InfluxDB不支持不为空(IS NOT NULL)操作符");
                case IN:
                        throw new RuntimeException("InfluxDB不支持(IN)操作符");
                case EQ:
                    opName = "=";
                case GT:
                    if (opName == null) {
                        opName = ">";
                    }
                case GTE:
                    if (opName == null) {
                        opName = ">=";
                    }
                case LT:
                    if (opName == null) {
                        opName = "<";
                    }
                case LTE:
                    if (opName == null) {
                        opName = "<=";
                    }
                    if (value instanceof String) {
                        String v = value.toString();
                        if (NumberUtils.isCreatable(v)) {
                            if (v.contains(".")) {
                                try {
                                    value = Double.parseDouble(v);
                                    where.add(query.getName() + " " + opName + " " + v);
                                    return;
                                } catch (NumberFormatException e) {
                                    // ignore
                                }
                            } else {
                                try {
                                    value = Long.parseLong(v);
                                    where.add(query.getName() + " " + opName + " " + v);
                                    return;
                                } catch (NumberFormatException e) {
                                    // ignore
                                }
                            }
                        }
                        where.add(query.getName() + " " + opName + " '" + v + "'");
                        return;
                    }
                default:
                    try {
                        throw new Exception("不支持的操作符: " + query.getOperate());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        });
        String sql = "SELECT * FROM " + tableName;
        if (!CollectionUtils.isEmpty(where)) {
            sql = sql + " WHERE " + String.join(" AND ", where);
        }
        return sql;
    }


    public void insert(String table, Map<String, Object> row) {
        if (CollectionUtils.isEmpty(row)) {
            return;
        }
        insert(table, Collections.singletonList(row));
    }


    public void insert(String table, List<Map<String, Object>> rows) {
        if (CollectionUtils.isEmpty(rows)) {
            return;
        }
        List<Point> points = rows.stream()
                .filter(e -> !CollectionUtils.isEmpty(e))
                .map(e -> {
                        return to(table, e);

                })
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(points)) {
            return;
        }
        BatchPoints batchPoints = BatchPoints.builder()
                .points(points)
                .build();
        influxDB.write(batchPoints);
        influxDB.flush();
    }

    private Point to(String table, Map<String, Object> row)  {
        Point.Builder builder = Point.measurement(table).time(getTime(row), TimeUnit.MILLISECONDS);
        row.remove("time");
        row.forEach((k, v) -> {
            if (v == null) {
                return;
            }
            if (StringUtils.startsWith(k, "_")) {
                String key = StringUtils.removeStart(k, "_");
                if (v.getClass().getName().equals(boolean.class.getName())) {
                    builder.addField(key, (boolean) v);
                } else if (v.getClass().getName().equals(short.class.getName())) {
                    builder.addField(key, (short) v);
                } else if (v.getClass().getName().equals(int.class.getName())) {
                    builder.addField(key, (int) v);
                } else if (v.getClass().getName().equals(long.class.getName())) {
                    builder.addField(key, (long) v);
                } else if (v.getClass().getName().equals(float.class.getName())) {
                    builder.addField(key, (float) v);
                } else if (v.getClass().getName().equals(double.class.getName())) {
                    builder.addField(key, (double) v);
                } else if (v instanceof Boolean) {
                    builder.addField(key, (Boolean) v);
                } else if (v instanceof Number) {
                    builder.addField(key, (Number) v);
                } else if (v instanceof String) {
                    builder.addField(key, (String) v);
                } else {
                    builder.addField(key, v.toString());
                }
            } else {
                builder.tag(k, v.toString());
            }
        });
        return builder.build();
    }

    private Long getTime(Map<String, Object> row)  {
        Object time = row.get("time");
        if (time == null) {
            return System.currentTimeMillis();
        }
        if (time instanceof Number) {
            return ((Number) time).longValue();
        }
        if (time instanceof Date) {
            return ((Date) time).getTime();
        }
        throw new RuntimeException("influxdb time字段类型不合法: " + time);
    }


}
