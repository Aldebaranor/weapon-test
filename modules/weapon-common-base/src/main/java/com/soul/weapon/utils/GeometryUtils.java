package com.soul.weapon.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 距离计算工具类
 *
 * @author 刘龙海
 * @version 1.0.0
 */
public final class GeometryUtils {
    private GeometryUtils() {
        throw new IllegalStateException("Utils");
    }

    private static double EARTH_RADIUS = 6378.137;
    private static Double infinity = 1e10;
    private static Double delta = 1e-10;

    public static double getRadian(double degree) {
        return degree * Math.PI / 180.0;
    };

    // 计算两点之间距离
    public static double getDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = getRadian(lat1);
        double radLat2 = getRadian(lat2);
        // 两点纬度差
        double a = radLat1 - radLat2;
        // 两点的经度差
        double b = getRadian(lon1) - getRadian(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s * 1000;
    }

    // 计算笛卡尔坐标系下两点坐标
    public static double getCartesianDis(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }

    //射线法判断点是否在多边形内
    public static Boolean isInPolygon(List<Point> polygon, Point point) {
        //顶点数
        int n = polygon.size();
        //射线与多边形交点数
        int count = 0;
        //定义射线
        Line radial = new Line(point, new Point(-infinity, point.y));
        for (int i = 0; i < n; i++) {
            Line side = new Line(polygon.get(i), polygon.get((i + 1) % n));
            //点在边上
            if (isOnline(point, side)) {
                return true;
            }
            //如果radial平行x轴则不考虑
            if (Math.abs(side.p1.y - side.p2.y) < delta) {
                continue;
            }
            //计算射线与边相交点数
            if (isOnline(side.p1, radial)) {
                if (side.p1.y > side.p2.y) {
                    count++;
                }
            } else if (isOnline(side.p2, radial)) {
                if (side.p2.y > side.p1.y) {
                    count++;
                }
            } else if (isIntersect(radial, side)) {
                count++;
            }
        }
        //相交点数为奇数，点在多边形内部
        if (count % 2 == 1) {
            return true;
        }
        return false;
    }

    //计算叉乘|P0P1|x|P0P2|
    public static Double multiply(Point p1, Point p2, Point p0) {
        return (p1.x - p0.x) * (p2.y - p0.y) - (p2.x - p0.x) * (p1.y - p0.y);
    }

    //判断线段是否包含点point
    public static Boolean isOnline(Point point, Line line) {
        return Math.abs(multiply(line.p1, line.p2, point)) < delta
                && (point.x - line.p1.x) * (point.x - line.p2.x) <= 0
                && (point.y - line.p1.y) * (point.y - line.p2.y) <= 0;
    }

    //判断线段是否相交
    public static Boolean isIntersect(Line line1, Line line2) {
        return Math.max(line1.p1.x, line1.p2.x) >= Math.min(line2.p1.x, line2.p2.x)
                && Math.max(line2.p1.x, line2.p2.x) >= Math.min(line1.p1.x, line1.p2.x)
                && Math.max(line1.p1.y, line1.p2.y) >= Math.min(line2.p1.y, line2.p2.y)
                && Math.max(line2.p1.y, line2.p2.y) >= Math.min(line1.p1.y, line1.p2.y)
                && multiply(line2.p1, line1.p2, line1.p1) * multiply(line1.p2, line2.p2, line1.p1) >= 0
                && multiply(line1.p1, line2.p2, line2.p1) * multiply(line2.p2, line1.p2, line2.p1) >= 0;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Point implements Serializable {

        private Double x;
        private Double y;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Line {

        private Point p1 = new Point();
        private Point p2 = new Point();

    }
}
