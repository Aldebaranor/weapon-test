package com.soul.weapon.influxdb;

import lombok.Data;
import okhttp3.OkHttpClient;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * @Auther: 码头工人
 * @Date: 2021/09/18/2:29 下午
 * @Description:
 */

@Data
@Configuration
@Component
@ConfigurationProperties(prefix = "influxdb")
@ConditionalOnProperty(prefix = "influxdb",name = "enabled" ,havingValue = "true", matchIfMissing = false)
public class InfluxdbConfig {

    private String userName;

    private String password;

    private String url;

    private String database;

    @Bean
    @ConditionalOnMissingBean
    public InfluxDB influxdb() {
        OkHttpClient.Builder client = new OkHttpClient.Builder().readTimeout(1000, TimeUnit.SECONDS);

        InfluxDB influxDB = InfluxDBFactory.connect(url, userName, password, client);
        influxDB.setDatabase(database);
        influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);
        influxDB.enableBatch();
        return influxDB;
    }

    @Bean
    public InfluxdbTemplate createmplate() {
        return new InfluxdbTemplate(database);
    }
}