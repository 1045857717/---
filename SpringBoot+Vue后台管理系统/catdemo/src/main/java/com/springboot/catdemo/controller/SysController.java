package com.springboot.catdemo.controller;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.generator.ImageCaptchaGenerator;
import cloud.tianai.captcha.generator.common.model.dto.ImageCaptchaInfo;
import cloud.tianai.captcha.validator.ImageCaptchaValidator;
import cloud.tianai.captcha.validator.impl.BasicCaptchaTrackValidator;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.springboot.catdemo.CatdemoApplication;
import com.springboot.catdemo.common.Constants;
import com.springboot.catdemo.common.Result;
import com.springboot.catdemo.entity.Province;
import com.springboot.catdemo.service.ISysService;
import com.springboot.catdemo.utils.MyRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统服务
 * @Author: can
 * @Description:
 * @Date: Create in 6:08 2022/6/9
 */
@RestController
@RequestMapping("/sys")
@Slf4j
public class SysController {

    @javax.annotation.Resource
    private ISysService sysService;

    @Autowired
    private ImageCaptchaGenerator myImageCaptchaGenerator;

    private static final Log LOG = Log.get();

    /**
     * 获取验证码
     * @return
     */
    @GetMapping("/getCaptcha")
    public Result getCaptcha() {
        // 生成图片
        ImageCaptchaInfo imageCaptchaInfo = myImageCaptchaGenerator.generateCaptchaImage(CaptchaTypeConstant.SLIDER);
        LOG.info("生成的滑块验证码：{}", imageCaptchaInfo.toString());
        // 生成UUID存入redis
        String uuid = UUID.randomUUID().toString(true);
        HashMap<String, String> data = new HashMap<>();
        data.put("uuid", uuid);
        imageCaptchaInfo.setData(data);
        // 将UUID为key,拼图验证码为value存入redis
        MyRedisUtils.setStringCache(uuid, JSONUtil.toJsonStr(imageCaptchaInfo), 60);
        return Result.success(imageCaptchaInfo);
    }

//    /**
//     * 校验验证码
//     * @param uuid
//     * @param movePercent
//     * @return
//     */
//    @GetMapping("/captchaValid/{uuid}/{movePercent}")
//    public Result validCaptcha(@PathVariable String uuid, @PathVariable Float movePercent) {
//        String jsonStr = MyRedisUtils.getStringCache(uuid);
//        // 清除当前校验的拼图验证
//        MyRedisUtils.stringDeleteKey(uuid);
//        if (jsonStr == null) {
//            return Result.error(Constants.CODE_500, "验证码失效，请重试！");
//        }
//        ImageCaptchaInfo imageCaptchaInfo = JSONUtil.toBean(jsonStr, ImageCaptchaInfo.class);
//        // 负责计算一些数据存到缓存中，用于校验使用
//        // ImageCaptchaValidator负责校验用户滑动滑块是否正确和生成滑块的一些校验数据; 比如滑块到凹槽的百分比值
//        // 2.ImageCaptchaValidator校验器 验证
//        ImageCaptchaValidator imageCaptchaValidator = new BasicCaptchaTrackValidator();
//        // 这个map数据应该存到缓存中，校验的时候需要用到该数据
//        Map<String, Object> map = imageCaptchaValidator.generateImageCaptchaValidData(imageCaptchaInfo);
////        ImageCaptchaTrack imageCaptchaTrack = null;
//        Float percentage = (Float) map.get("percentage");
//        // 用户传来的行为轨迹和进行校验
//        // - imageCaptchaTrack为前端传来的滑动轨迹数据
//        // - map 为生成验证码时缓存的map数据
////        boolean check = imageCaptchaValidator.valid(imageCaptchaTrack, map);
//        // 如果只想校验用户是否滑到指定凹槽即可，也可以使用
//        // - 参数1 用户传来的百分比数据
//        // - 参数2 生成滑块是真实的百分比数据
//        boolean check = imageCaptchaValidator.checkPercentage(movePercent, percentage);
//        if (!check) {
//            return Result.error(Constants.CODE_600, "验证码错误，请重试！");
//        }
//        return Result.success(check);
//    }

    /**
     * 获取所有省市区
     * @return
     */
    @GetMapping("getProvinces")
    public Result getProvinces() {
        List<Province> provinces = sysService.selectProvinces();
        // 获取list指定的元素下标位置
//        IntStream.range(0, provinces.size()).filter(i -> "1".equals(provinces.get(i).getValue().toString())).findFirst();
        // 找到城市code为1的下标（全市下标为1）
        int i = provinces.stream().map(Province::getValue).collect(Collectors.toList()).indexOf(1);
        // 将数组下标第一个和全市(i是全市的下标) 换位置
        Collections.swap(provinces, 0, i);
        return Result.success(provinces);
    }

    /**
     * 重启项目
     */
    @PostMapping("/reboot")
    public Result restart() {
        CatdemoApplication.restart();
        return Result.success();
    }

}
