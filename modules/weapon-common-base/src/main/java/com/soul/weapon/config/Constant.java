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

}
