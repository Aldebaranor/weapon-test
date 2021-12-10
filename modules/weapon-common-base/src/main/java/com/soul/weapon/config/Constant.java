package com.soul.weapon.config;

/**
 * @author: nash5
 * @date: 2021-11-11 9:14
 */
public class Constant {

    /**
     * http 报文存入redis方法
     */
    public static String COMBAT_SCENARIOS_INFO_HTTP_KEY = "weapon:pump:combat_scenarios_info";
    public static String ENVIRONMENT_INFO_HTTP_KEY = "weapon:pump:environment_info";
    public static String EQUIPMENT_LAUNCH_STATUS_HTTP_KEY = "weapon:pump:equipment_launch_status";
    public static String EQUIPMENT_STATUS_HTTP_KEY = "weapon:pump:equipment_status";
    public static String LAUNCHER_ROTATION_INFO_HTTP_KEY = "weapon:pump:launcher_rotation_info";
    public static String TARGET_FIRE_CONTROL_INFO_HTTP_KEY = "weapon:pump:target_fire_control_info";
    public static String TARGET_INFO_HTTP_KEY = "weapon:pump:target_info";
    public static String TARGET_INSTRUCTIONS_INFO_HTTP_KEY = "weapon:pump:target_instructions_info";

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

    public static String PREDICT_KEY = "fire:pump:predict";
    public static String PREDICTDETAIL_KEY = "fire:pump:predict_detail";


    public static String WEAPON_CURRENT_TASK = "weapon:current:task";
    public static String WEAPON_CURRENT_PIPETEST = "weapon:current:pipeTest";


    public static String SHIP_AIR_MISSILE="舰空导弹武器通道测试-1";
    public static String ANTI_MISSILE_NAVAL_GUN="反导舰炮武器通道测试-2";
    public static String TORPEDO_DEFENSE="鱼雷防御武器通道测试-3";
    public static String ELECTRONIC_COUNTERMEASURE="电子对抗武器通道测试-4";
    public static String UNDERWATER_ACOUSTIC_COUNTERMEASURE="水声对抗武器测试-5";
    public static String INFORMATION_FLOW="信息流程测试-6";
    public static String THREAT_JUDGMENT="威胁判断测试-7";
    public static String INDICATES_THE_PROCESSING_ACCURACY="指示处理精度测试-8";
    public static String IMPLEMENTATION="执行情况测试-9";
    public static String RADAR_TRACK="雷达航迹测试-10";
    public static String INTERCEPT_DISTANCE="拦截距离测试-11";
    public static String FIRE_CONTROL_CALCULATION_ACCURACY="火控解算精度测试-12";
    public static String REACTION_TIME="反应时间测试-13";
    public static String LAUNCHER_ROTATION_ACCURACY="发射架调转精度测试-14";
    public static String MULTI_TARGET_INTERCEPTION="多目标拦截能力测试-15";



}
