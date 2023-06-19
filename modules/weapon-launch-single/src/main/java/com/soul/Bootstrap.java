package com.soul;

import com.egova.EgovaPlatform;
import com.egova.beans.PackageBeanNameGenerator;
import com.egova.model.PropertyDescriptorScan;
import com.egova.security.EgovaAuthorizationServer;
import com.egova.security.EgovaResourceServer;
import com.flagwind.mybatis.spring.annotation.MapperScan;
import com.flagwind.mybatis.spring.autoconfigure.FlagwindAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
@EgovaAuthorizationServer
@EgovaResourceServer
@EgovaPlatform
@EnableScheduling
@EnableAsync
@ComponentScan({
        "com.soul.*.*"
})
@MapperScan(
        nameGenerator = PackageBeanNameGenerator.class,
        basePackages = {"com.soul.**.domain"}
)
@EnableAspectJAutoProxy
@PropertyDescriptorScan
@EnableCaching
public class Bootstrap {

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }

    @Bean
    public ErrorPageFilter errorPageFilter() {
        return new ErrorPageFilter();
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    public FilterRegistrationBean<ErrorPageFilter> disableSpringBootErrorFilter(ErrorPageFilter filter) {
        FilterRegistrationBean<ErrorPageFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
