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

    public static Integer[] AIR_MISSILE_WEAPON_PIPE_TEST_INDICES = {1,2,3,4};
}
