package com.soul.weapon.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author: nash5
 * @date: 2021-11-11 9:14
 */
public class Constant {

    /**
     * http 报文存入redis方法
     */
    //作战方案
    public static String COMBAT_SCENARIOS_INFO_HTTP_KEY = "weapon:combat_scenarios_info";
    //战场环境
    public static String ENVIRONMENT_INFO_HTTP_KEY = "weapon:environment_info";
    //武器发射
    public static String EQUIPMENT_LAUNCH_STATUS_HTTP_KEY = "weapon:equipment_launch_status:targetId";
    //设备状态
    public static String EQUIPMENT_STATUS_HTTP_KEY = "weapon:equipment_status";
    //发射器架调转
    public static String LAUNCHER_ROTATION_INFO_HTTP_KEY = "weapon:launcher_rotation_info:equipmentId";
    //发射器调转2
    public static String LAUNCHER_ROTATION_INFO_HTTP_KEY_2 = "weapon:launcher_rotation_info:targetId";
    //目标火控
    public static String TARGET_FIRE_CONTROL_INFO_HTTP_KEY = "weapon:target_fire_control_info:targetId";
    //目标火控2
    public static String TARGET_FIRE_CONTROL_INFO_HTTP_KEY_2 = "weapon:target_fire_control_info:equipmentId";
    //目标信息
    public static String TARGET_INFO_HTTP_KEY = "weapon:target_info:targetId";
    //目标指示
    public static String TARGET_INSTRUCTIONS_INFO_HTTP_KEY = "weapon:target_instructions_info:targetId";
    //目标指示2
    public static String TARGET_INSTRUCTIONS_INFO_HTTP_KEY_2 = "weapon:target_instructions_info";

    //目标Id
    public static String TARGET_ID = ":targetId";
    //武器Id
    public static String EQUIPMENT_ID = ":equipmentId";


    //线程存储器
    public static ConcurrentHashMap<String, ScheduledFuture> threadMap = new ConcurrentHashMap<String,ScheduledFuture>();
    /**
     * 目标信息(雷达探测到的目标信息)于influxdb中的表名
     */
    public static String TARGET_INFO_INFLUX_MEASURMENT_NAME = "target_info";

    /**
     * 火力兼容结果报文
     */
    public static String CONFLICT_PREDICT_REPORT = "fire:pump:conflict_predict_report";
    public static String CONFLICT_CHARGE_REPORT = "fire:pump:conflict_charge_report";

    public static String CHARGE_KEY = "fire:pump:charge";
    public static String CHARGEDETAIL_KEY = "fire:pump:charge_detail";

    public static String PREDICT_KEY = "fire:pump:predict";
    public static String PREDICTDETAIL_KEY = "fire:pump:predict_detail";


    public static String WEAPON_CURRENT_TASK = "weapon:current:task";
    public static String WEAPON_CURRENT_PIPETEST = "weapon:current:pipeTest";

    //舰空导弹武器通道测试
    public static String SHIP_AIR_MISSILE="test1";
    //反导舰炮武器通道测试
    public static String ANTI_MISSILE_NAVAL_GUN="test2";
    //鱼雷防御武器通道测试
    public static String TORPEDO_DEFENSE="test3";
    //电子对抗武器通道测试
    public static String ELECTRONIC_COUNTERMEASURE="test4";
    //水声对抗武器测试
    public static String UNDERWATER_ACOUSTIC_COUNTERMEASURE="test5";
    //信息流程测试
    public static String INFORMATION_FLOW="test6";
    //威胁判断测试
    public static String THREAT_JUDGMENT="test7";
    //指示处理精度测试
    public static String INDICATES_THE_PROCESSING_ACCURACY="test8";
    //执行情况测试
    public static String IMPLEMENTATION="test9";
    //雷达航迹测试
    public static String RADAR_TRACK="test10";
    //拦截距离测试
    public static String INTERCEPT_DISTANCE="test11";
    //火控解算精度测试
    public static String FIRE_CONTROL_CALCULATION_ACCURACY="test12";
    //反应时间测试
    public static String REACTION_TIME="test13";
    //发射架调转精度测试
    public static String LAUNCHER_ROTATION_ACCURACY="test14";
    //多目标拦截能力测试
    public static String MULTI_TARGET_INTERCEPTION="test15";

    //空中目标
    public static String AIRTYPE = "1";
    //水下目标
    public static String WATERTYPE = "2";

    //舰空导弹火控装置
    public static String SHIP_TO_AIR_MISSILE_FIRE_CONTROL_DEVICE = "2";
    //反导舰炮火控装置
    public static String ANTI_MISSILE_NAVAL_GUN_FIRE_CONTROL_DEVICE = "12";
    //鱼雷防御武器火控装置
    public static String TORPEDO_DEFENSE_WEAPON_FIRE_CONTROL_DEVICE = "17";
    //电子对抗武器火控装置
    public static String FIRE_CONTROL_DEVICE_OF_ELECTRONIC_COUNTERMEASURE_WEAPON = "21";
    //水声对抗武器火控装置
    public static String WATER_ACOUSTIC_COUNTERMEASURE_WEAPON_FIRE_CONTROL_DEVICE = "27";

    //dds报文发送路径
    public static String DDS_URL = "http://127.0.0.1:8016/free/pump/";

    /**
     * 大屏redis数据key
     */
    //水下计数
    public static String SCREEN_COUNT_WATERTYPE = "SCREEN:WATERTYPE:COUNT";
    //对空计数
    public static String SCREEN_COUNT_AIRTYPE = "SCREEN:AIRTYPE:COUNT";
    //水下无目标报文周期
    public static String SCREEN_STATUSREPORTDATA_WATERTYPE = "SCREEN:WATERTYPE:STATUSREPORTDATA";
    //对空无目标报文周期
    public static String SCREEN_STATUSREPORTDATA_AIRTYPE = "SCREEN:AIRTYPE:STATUSREPORTDATA";
    //水下有目标报文周期
    public static String SCREEN_STATUSREPORTDATA_WATERTYPE_TARGETID = "SCREEN:WATERTYPE:%s:STATUSREPORTDATA";
    //对空有目标报文周期
    public static String SCREEN_STATUSREPORTDATA_AIRTYPE_TARGETID = "SCREEN:AIRTYPE:%s:STATUSREPORTDATA";
    //水下通道时间报文
    public static String SCREEN_RESPONSETIME_WATERTYPE_TARGETID = "SCREEN:WATERTYPE:%s:RESPONSETIME";
    //对空通道时间报文
    public static String SCREEN_RESPONSETIME_AIRTYPE_TARGETID = "SCREEN:AIRTYPE:%s:RESPONSETIME";
    //水下任务通道状态-任务
    public static String SCREEN_TASKCHANNELSTATUS_WATERTYPE_TARGETID = "SCREEN:WATERTYPE:%s:TASKCHANNELSTATUS";
    //对空任务通道状态-任务
    public static String SCREEN_TASKCHANNELSTATUS_AIRTYPE_TARGETID = "SCREEN:AIRTYPE:%s:TASKCHANNELSTATUS";
    //水下任务通道状态
    public static String SCREEN_TASKCHANNELSTATUS_WATERTYPE = "SCREEN:WATERTYPE:TASKCHANNELSTATUS";
    //对空任务通道状态
    public static String SCREEN_TASKCHANNELSTATUS_AIRTYPE = "SCREEN:AIRTYPE:TASKCHANNELSTATUS";
    //水下传感器精度
    public static String SCREEN_SENSORACCURACY_WATERTYPE_TARGETID = "SCREEN:WATERTYPE:%s:SENSORACCURACY";
    //对空传感器精度
    public static String SCREEN_SENSORACCURACY_AIRTYPE_TARGETID = "SCREEN:AIRTYPE:%s:SENSORACCURACY";
    //水下发射架调转精度
    public static String SCREEN_LAUNCHERROTATIONACCURACY_WATERTYPE_TARGETID = "SCREEN:WATERTYPE:%s:LAUNCHERROTATIONACCURACY";
    //对空发射架调转精度
    public static String SCREEN_LAUNCHERROTATIONACCURACY_AIRTYPE_TARGETID = "SCREEN:AIRTYPE:%s:LAUNCHERROTATIONACCURACY";
    //水下确认诸元参数
    public static String SCREEN_QUERENZHUYUAN_WATERTYPE_TARGETID = "SCREEN:WATERTYPE:%s:QUERENZHUYUAN:%s";
    //对空确认诸元参数
    public static String SCREEN_QUERENZHUYUAN_AIRTYPE_TARGETID = "SCREEN:AIRTYPE:%s:QUERENZHUYUAN:%s";
    //水下防御流程图
    public static String SCREEN_LIUCHENGTU_WATERTYPE = "SCREEN:WATERTYPE:LIUCHENGTU";
    //空中防御流程图
    public static String SCREEN_LIUCHENGTU_AIRTYPE = "SCREEN:AIRTYPE:LIUCHENGTU";



}
