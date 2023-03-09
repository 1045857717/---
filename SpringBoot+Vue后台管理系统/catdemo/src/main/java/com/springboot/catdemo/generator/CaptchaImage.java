package com.springboot.catdemo.generator;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.generator.ImageCaptchaGenerator;
import cloud.tianai.captcha.generator.common.constant.SliderCaptchaConstant;
import cloud.tianai.captcha.generator.common.model.dto.ImageCaptchaInfo;
import cloud.tianai.captcha.generator.impl.MultiImageCaptchaGenerator;
import cloud.tianai.captcha.resource.ImageCaptchaResourceManager;
import cloud.tianai.captcha.resource.ResourceStore;
import cloud.tianai.captcha.resource.common.model.dto.Resource;
import cloud.tianai.captcha.resource.impl.DefaultImageCaptchaResourceManager;
import cloud.tianai.captcha.resource.impl.provider.ClassPathResourceProvider;
import cloud.tianai.captcha.resource.impl.provider.URLResourceProvider;
import cloud.tianai.captcha.validator.ImageCaptchaValidator;
import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import cloud.tianai.captcha.validator.impl.BasicCaptchaTrackValidator;

import java.util.HashMap;
import java.util.Map;

/**
 * 前端登录验证码生成|校验
 * cloud.tianai.captcha.common.constant.CaptchaTypeConstant
 * @Author: can
 * @Description:
 * @Date: Create in 10:21 2022/6/16
 */
public class CaptchaImage {
    public static void main(String[] args) {
        // region
        // 1.ImageCaptchaGenerator生成器生成验证码
        ImageCaptchaResourceManager imageCaptchaResourceManager = new DefaultImageCaptchaResourceManager();
        // 通过资源管理器或者资源存储器
        ResourceStore resourceStore = imageCaptchaResourceManager.getResourceStore();
        // 添加classpath目录下的 aa.jpg 图片
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource(ClassPathResourceProvider.NAME, "static/imageCaptcha/test2022.jpeg"));

        // 添加远程url图片资源
//        resourceStore.addResource(CaptchaTypeConstant.SLIDER,new Resource(URLResourceProvider.NAME, "http://www.xx.com/aa.jpg"));
        // 内置了通过url 和 classpath读取图片资源，如果想扩展可实现 ResourceProvider 接口，进行自定义扩展
        // endregion

        // region
        // 添加滑块验证码模板.模板图片由三张图片组成
        // 模板与三张图片组成 滑块、凹槽、背景图
//        Map<String, Resource> template1 = new HashMap<>(4);
//        template1.put(SliderCaptchaConstant.TEMPLATE_ACTIVE_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, "/active.png"));
//        template1.put(SliderCaptchaConstant.TEMPLATE_FIXED_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, "/fixed.png"));
//        template1.put(SliderCaptchaConstant.TEMPLATE_MATRIX_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, "/matrix.png"));
//        resourceStore.addTemplate(CaptchaTypeConstant.SLIDER,template1);
        // 同样默认支持 classpath 和 url 两种获取图片资源， 如果想扩展可实现 ResourceProvider 接口，进行自定义扩展
        // endregion

        // region
        // 为方便快速上手 系统本身自带了一张图片和两套滑块模板，如果不想用系统自带的可以不让它加载系统自带的
        // 第二个构造参数设置为false时将不加载默认的图片和模板
        ImageCaptchaGenerator imageCaptchaGenerator
                = new MultiImageCaptchaGenerator(imageCaptchaResourceManager).init(true);
        // endregion

        /*
                生成滑块验证码图片, 可选项
                SLIDER (滑块验证码)
                ROTATE (旋转验证码)
                CONCAT (滑动还原验证码)
                WORD_IMAGE_CLICK (文字点选验证码)

                更多验证码支持 详见 cloud.tianai.captcha.common.constant.CaptchaTypeConstant
         */
        ImageCaptchaInfo imageCaptchaInfo = imageCaptchaGenerator.generateCaptchaImage(CaptchaTypeConstant.SLIDER);
        System.out.println(imageCaptchaInfo);

        // 负责计算一些数据存到缓存中，用于校验使用
        // ImageCaptchaValidator负责校验用户滑动滑块是否正确和生成滑块的一些校验数据; 比如滑块到凹槽的百分比值
        // 2.ImageCaptchaValidator校验器 验证
        ImageCaptchaValidator imageCaptchaValidator = new BasicCaptchaTrackValidator();
        // 这个map数据应该存到缓存中，校验的时候需要用到该数据
        Map<String, Object> map = imageCaptchaValidator.generateImageCaptchaValidData(imageCaptchaInfo);
        ImageCaptchaTrack imageCaptchaTrack = null;
        Float percentage = null;
        // 用户传来的行为轨迹和进行校验
        // - imageCaptchaTrack为前端传来的滑动轨迹数据
        // - map 为生成验证码时缓存的map数据
        boolean check = imageCaptchaValidator.valid(imageCaptchaTrack, map);
        // 如果只想校验用户是否滑到指定凹槽即可，也可以使用
        // - 参数1 用户传来的百分比数据
        // - 参数2 生成滑块是真实的百分比数据
        check = imageCaptchaValidator.checkPercentage(0.2f, percentage);
    }
}
