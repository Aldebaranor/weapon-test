package com.soul.fire.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author: XinLai
 * @Date: 2021/11/1 10:08
 */

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "fire.predict")
public class PredictConfig {

    //TODO:静态变量定义到Constant文件。这个database还要吗？？？
    public int database;

    public static String PREDICT_KEY = "fire:predict";

    public static String PREDICTDETAIL_KEY = "fire:predict_detail";
}
