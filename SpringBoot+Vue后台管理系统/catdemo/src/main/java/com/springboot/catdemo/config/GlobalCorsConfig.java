package com.springboot.catdemo.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 全局解决跨域
 */
@Configuration
public class GlobalCorsConfig {

    // 当前跨域请求最大有效时长。这里默认1天
    private static final long MAX_AGE = 24 * 60 * 60;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 设置允许跨域请求的域名
        // 如果setAllowCredentials为true，corsConfiguration.addAllowedOrigin("*")的参数就不能是*，必须指明，这里直接注释掉，使用addAllowedOriginPattern
//        corsConfiguration.addAllowedOrigin("*");
        //允许所有域名进行跨域调用
        corsConfiguration.addAllowedOriginPattern("*");
        // 允许跨越发送cookie
        corsConfiguration.setAllowCredentials(true);
        // 放行全部原始头信息
        corsConfiguration.addAllowedHeader("*");
        // 允许所有请求方法跨域调用
        corsConfiguration.addAllowedMethod("*");
        // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        corsConfiguration.setMaxAge(MAX_AGE);
        // 对所有接口都有效
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        // 优先级最高
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return new CorsFilter(source);
    }
}
