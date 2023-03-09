package com.springboot.catdemo.controller.wx;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.catdemo.common.Result;
import com.springboot.catdemo.entity.Categories;
import com.springboot.catdemo.entity.Files;
import com.springboot.catdemo.service.ICategoriesService;
import com.springboot.catdemo.service.IFileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信首页
 * @Author: can
 * @Description:
 * @Date: Create in 15:31 2022/7/7
 */
@RestController
@RequestMapping("/wxHome")
public class WxHomeController {

    @Resource
    private IFileService fileService;

    @Resource
    private ICategoriesService categoriesService;

    /**
     * 获取轮播图
     * @return
     */
    @GetMapping("/getSlides")
    public Result getSlides() {
        QueryWrapper<Files> qw = new QueryWrapper<>();
        qw.eq("type","png").or().eq("type","jpg");
        List<Files> slidesList = fileService.list(qw);
        return Result.success(slidesList);
    }

    /**
     * 获取九宫格菜单
     * @return
     */
    @GetMapping("/getCategories")
    public Result getCategories() {
        List<Categories> list = categoriesService.list();
        return Result.success(list);
    }
}
