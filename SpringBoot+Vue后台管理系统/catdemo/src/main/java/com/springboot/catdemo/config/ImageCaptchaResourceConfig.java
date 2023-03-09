package com.springboot.catdemo.config;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.generator.ImageCaptchaGenerator;
import cloud.tianai.captcha.generator.common.model.dto.ImageCaptchaInfo;
import cloud.tianai.captcha.generator.impl.MultiImageCaptchaGenerator;
import cloud.tianai.captcha.resource.ImageCaptchaResourceManager;
import cloud.tianai.captcha.resource.ResourceStore;
import cloud.tianai.captcha.resource.common.model.dto.Resource;
import cloud.tianai.captcha.resource.impl.DefaultImageCaptchaResourceManager;
import cloud.tianai.captcha.resource.impl.provider.ClassPathResourceProvider;
import cn.hutool.log.Log;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;

/**
 * 拼图图形验证码资源存储器
 * @Author: can
 * @Description:
 * @Date: Create in 14:16 2022/6/25
 */
@Configuration
@Log4j
public class ImageCaptchaResourceConfig {

    @Value("${static.captchaImage.path}")
    private String captchaImagePath;

    // 拼图验证码静态文件的文件夹
    private final String CLASS_PATH_STATIC_CAPTCHA = "static" + File.separator + "captchaImage" + File.separator;

    private static final Log LOG = Log.get();

    /**
     * 图片验证码生成器
     * @return
     */
    @Bean
    public ImageCaptchaGenerator myImageCaptchaGenerator() {
        ImageCaptchaResourceManager imageCaptchaResourceManager = new DefaultImageCaptchaResourceManager();
        // 通过资源管理器或者资源存储器
        ResourceStore resourceStore = imageCaptchaResourceManager.getResourceStore();
        File file = new File(captchaImagePath);
        if (file.exists()) {
            // file.list()返回没有完整路径的文件名|listFiles()返回完整路径的文件名
//                file.listFiles();
            // 获取拼图验证码文件
            String[] captchaImages = file.list();
            if (captchaImages != null) {
                // 添加classpath目录下的 .jpg 图片 (自定义图片资源大小为 590*360 格式为jpg)
                for (String imageCaptcha : captchaImages) {
                    // 添加自定义图片资源
                    resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource(ClassPathResourceProvider.NAME, CLASS_PATH_STATIC_CAPTCHA + imageCaptcha));
                    LOG.info("添加的拼图验证码文件：{}"
                            , CLASS_PATH_STATIC_CAPTCHA + imageCaptcha);
                }
            }
        }
        ImageCaptchaGenerator imageCaptchaGenerator
                = new MultiImageCaptchaGenerator(imageCaptchaResourceManager).init(true);
        return imageCaptchaGenerator;
    }
}
