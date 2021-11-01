package com.soul.fire.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author: XinLai
 * @Date: 2021/11/1 11:01
 */
@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "fire.charge")
public class ChargeConfig {
    private int database;
}
