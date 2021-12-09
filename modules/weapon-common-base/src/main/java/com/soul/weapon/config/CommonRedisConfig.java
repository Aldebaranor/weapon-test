package com.soul.weapon.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author: nash5
 * @date: 2021-11-11 12:41
 */
@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "common.redis")
public class CommonRedisConfig {
    /** 火力和通道项目公用的http报文存储数据库配置 **/
    //TODO:不要用一些看不懂的简写
    private int httpDataBaseIdx;

    /** 火力兼容项目redis数据库配置 **/
    private int fireDataBaseIdx;
}
