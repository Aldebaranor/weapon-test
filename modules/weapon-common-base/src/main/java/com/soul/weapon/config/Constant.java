package com.soul.weapon.config;

/**
 * @author: nash5
 * @date: 2021-11-11 9:14
 */
public class Constant {

    /**
     * http 报文存入redis方法
     */
    //作战方案
    public static String COMBAT_SCENARIOS_INFO_HTTP_KEY = "weapon:pump:combat_scenarios_info";
    //战场环境
    public static String ENVIRONMENT_INFO_HTTP_KEY = "weapon:pump:environment_info";
    //武器发射
    public static String EQUIPMENT_LAUNCH_STATUS_HTTP_KEY = "weapon:pump:equipment_launch_status";
    //设备状态
    public static String EQUIPMENT_STATUS_HTTP_KEY = "weapon:pump:equipment_status";
    //发射器架跳转
    public static String LAUNCHER_ROTATION_INFO_HTTP_KEY = "weapon:pump:launcher_rotation_info";
    //目标火控
    public static String TARGET_FIRE_CONTROL_INFO_HTTP_KEY = "weapon:pump:target_fire_control_info";
    //目标信息
    public static String TARGET_INFO_HTTP_KEY = "weapon:pump:target_info";
    //目标指示
    public static String TARGET_INSTRUCTIONS_INFO_HTTP_KEY = "weapon:pump:target_instructions_info";

    public static String TARGET_ID = ":targetId";
    public static String EQUIPMENT_ID = ":equipmentId";

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
    public static String SHIP_TO_AIR_MISSILE_FIRE_CONTROL_DEVICE = "003";
    //反导舰炮火控装置
    public static String ANTI_MISSILE_NAVAL_GUN_FIRE_CONTROL_DEVICE = "013";
    //鱼雷防御武器火控装置
    public static String TORPEDO_DEFENSE_WEAPON_FIRE_CONTROL_DEVICE = "018";
    //电子对抗武器火控装置
    public static String FIRE_CONTROL_DEVICE_OF_ELECTRONIC_COUNTERMEASURE_WEAPON = "022";
    //水声对抗武器火控装置
    public static String WATER_ACOUSTIC_COUNTERMEASURE_WEAPON_FIRE_CONTROL_DEVICE = "028";


}
