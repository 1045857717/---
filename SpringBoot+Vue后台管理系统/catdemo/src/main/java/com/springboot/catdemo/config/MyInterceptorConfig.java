package com.springboot.catdemo.config;

import cn.hutool.log.Log;
import com.springboot.catdemo.config.interceptor.JwtInterceptor;
import com.springboot.catdemo.entity.ExcludeInterUrl;
import com.springboot.catdemo.service.IExcludeInterUrlService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 拦截器注册
 * @Author: can
 * @Description:
 * @Date: Create in 1:26 2022/3/16
 */
@Configuration()
public class MyInterceptorConfig implements WebMvcConfigurer {

    private static final Log LOG = Log.get();

    private List<String> excludePath;

    /**
     * jwt验证方案
     * @return
     */
    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }

    /**
     * 放行url的服务
     */
    @Resource
    private IExcludeInterUrlService excludeInterUrlService;

    /**
     * 需要放行的请求url
     **/
    public List<ExcludeInterUrl> excludeInterUrlList;


    /** 添加拦截器 **/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        addInterceptorsByExcludeInterUrl(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

    /**
     * 数据库中查找需要放行的URL,并添加到拦截器当中
     * @return String[]
     */
    public InterceptorRegistry addInterceptorsByExcludeInterUrl(InterceptorRegistry registry) {
        getExcludeInterUrlList();
        // 获取要放行的URL
        excludePath = excludeInterUrlList.stream()
                .map(ExcludeInterUrl::getExcludeInterUrl).collect(Collectors.toList());
        LOG.info("加载放行的URL: {}", excludePath.toString());
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**") // 拦截所有请求，通过token判断是否合法 来决定是否需要登录
                // 放行(以下url无需token验证,直接放行)
                .excludePathPatterns(
                        // 动态获取放行URL
                        excludePath
//                        "/user/login",
//                        "/user/register",
//                        "/user/register/**",
//                        "/sys/**",
//                        "/article/**", // 发表文章
//                        "/comment/**", // 评论文章
//                        "/**/export",
//                        "/**/import",
//                        "/file/**",
//                        "/user/downloadImage/**",
//                        "/ws",
//                        "/ws/**",
//                        "/wss",
//                        "/wss/**",
//                        "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/api", "/api-docs", "/api-docs/**"
                ) // 排除拦截，直接放行
                .excludePathPatterns( "/**/*.html", "/**/*.js", "/**/*.css", "/**/*.woff", "/**/*.ttf");  // 放行静态文件
        return registry;
    }

    /**
     * 获取需要放行的请求url
     * @return
     */
    public List<ExcludeInterUrl> getExcludeInterUrlList() {
        // 获取放行的URL
        excludeInterUrlList = excludeInterUrlService.list();
        return excludeInterUrlList;
    }

}
