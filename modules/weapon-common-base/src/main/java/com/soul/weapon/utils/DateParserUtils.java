package com.soul.weapon.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;

/**
 * @author 奔波儿灞
 * @since 1.0.0
 */
public final class DateParserUtils {

    public static final String[] FORMATS = new String[]{
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd",
            "EEE, dd MMM yyyy HH:mm:ss Z",
            "yyyy-MM-dd HH::mm:ss:SSS"
    };


    private DateParserUtils() {
        throw new IllegalStateException("Utils");
    }

    public static boolean canParseDate(String value) {
        // 时间撮
        if (NumberUtils.isCreatable(value)) {
            try {
                Long.parseLong(value);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        // 时间字符串
        try {
            DateUtils.parseDate(value, FORMATS);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Date parseDate(String value) {
        // 时间撮
        if (NumberUtils.isCreatable(value)) {
            try {
                long time = Long.parseLong(value);
                return new Date(time);
            } catch (Exception e) {
                throw new IllegalArgumentException("不合法的时间撮: " + value);
            }
        }
        // 时间字符串
        try {
            return DateUtils.parseDate(value, FORMATS);
        } catch (Exception e) {
            throw new IllegalArgumentException("不合法的时间字符串: " + value);
        }
    }

    public static Long getSecsTimeDiff(String start, String end){

        try {
            return getMillsTimeDiff(start, end, "HH:mm:ss:SSS");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Long.MAX_VALUE;
    }

    public static Long getSecsTimeDiff(String start, String end, String pattern){

        try {
            LocalTime timeStart = LocalTime.parse(start, DateTimeFormatter.ofPattern(pattern));
            LocalTime timeEnd = LocalTime.parse(end, DateTimeFormatter.ofPattern(pattern));
            return timeStart.until(timeEnd, ChronoUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Long.MAX_VALUE;
    }

    public static Long getMillsTimeDiff(String start, String end){

        try {
            return getMillsTimeDiff(start, end, "HH:mm:ss:SSS");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Long.MAX_VALUE;
    }

    public static Long getMillsTimeDiff(String start, String end, String pattern){

        try {
            LocalTime timeStart = LocalTime.parse(start, DateTimeFormatter.ofPattern(pattern));
            LocalTime timeEnd = LocalTime.parse(end, DateTimeFormatter.ofPattern(pattern));
            return timeStart.until(timeEnd, ChronoUnit.MILLIS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Long.MAX_VALUE;
    }

    /**
     * 将非标准时间字符串转为标准字符串
     * @param src HH:mm:ss.SSS, 冒号和.隔开的子字符串长度可为1到3，需要将其转化到HH:mm:ss.SSS
     * @return HH:mm:ss.SSS格式的字符串
     */
    public static String convertToStdTimeStr(String src) {
        String res = "";
        String[] hourMin = src.split(":", 3);
        String[] secS = hourMin[2].replace(".", ",").split(",", 2);
        String[] hM = Arrays.copyOfRange(hourMin, 0, 2);
        String[] allParts = new String[4];
        System.arraycopy(hM, 0, allParts, 0, hM.length);
        System.arraycopy(secS, 0, allParts, hM.length, secS.length);

        for(int i = 0; i < 4; ++i) {
            allParts[i] = String.format("%02d", Integer.parseInt(allParts[i]));
            if(i == 3) {
                allParts[i] = String.format("%03d", Integer.parseInt(allParts[i]));
            }
        }
        res = StringUtils.join(allParts[0], ":", allParts[1], ":", allParts[2], ".", allParts[3]);
        return res;
    }

    /**
     * 转换时间日期格式字串为long型
     * @param time 格式为：yyyy-MM-dd HH:mm:ss的时间日期类型
     */
    public static Long convertTimeStrToLong(String time) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 获取当前天的时间
     *
     * @return
     */
    public static String getTime() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(System.currentTimeMillis());
    }
}
