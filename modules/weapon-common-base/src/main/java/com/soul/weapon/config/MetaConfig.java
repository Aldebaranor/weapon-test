package com.soul.weapon.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "meta.code")
public class MetaConfig {

    private String unpackServiceCode;
    private String unpackServiceCodeTest;
}