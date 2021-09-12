package com.soul.weapon.entity;

import org.apache.commons.net.ntp.TimeStamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: nash5
 * @Date: 2021/9/12 15:10
 * 存放历史信息的类，每一种历史信息都可以用如下类存放
 */
public class HistoryInfo {
    public String xTitle;
    public String yTitle;
    public String infoTypeName;
    public Float xStart, xEnd;
    public Float yStart, yEnd;

    Map<String , Map<TimeStamp, Float>> dataMaps;

    public HistoryInfo(String xTitle, String yTitle, Float xStart, Float xEnd, Float yStart, Float yEnd, String infoTypeName) {
        this.xTitle = xTitle;
        this.yTitle = yTitle;
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
        this.infoTypeName = infoTypeName;
    }

    public void addData(String type, TimeStamp curTime, Float value) {
        if(dataMaps.containsKey(type)) {
            dataMaps.put(type, new HashMap<TimeStamp, Float>());
        }
        dataMaps.get(type).put(curTime, value);
    }

    public String getAll() {
        return  dataMaps.toString();
    }
}
