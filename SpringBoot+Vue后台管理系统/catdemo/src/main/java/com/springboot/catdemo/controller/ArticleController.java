package com.springboot.catdemo.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.catdemo.common.Constants;
import com.springboot.catdemo.common.Result;
import com.springboot.catdemo.entity.Area;
import com.springboot.catdemo.entity.Article;
import com.springboot.catdemo.entity.City;
import com.springboot.catdemo.entity.Province;
import com.springboot.catdemo.service.IArticleService;
import com.springboot.catdemo.service.ISysService;
import com.springboot.catdemo.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 文章发布 前端控制器
 * </p>
 *
 * @author CAN
 * @since 2022-05-23
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private IArticleService articleService;

    @Resource
    private ISysService sysService;

    // 新增或修改
    @PostMapping("/saveOrUpdate")
    public Result save(@RequestBody Article article) {
        // 新增
        if (article.getId() == null) {
            article.setCreateTime(LocalDateTime.now());
            Objects.requireNonNull(TokenUtils.getCurrentUser(), "当前用户不存在，参数错误").getUsername();
            article.setUserId(TokenUtils.getCurrentUser().getId()); // 用户id
            article.setUserName(TokenUtils.getCurrentUser().getUsername()); // 用户名
            article.setUserNickName(TokenUtils.getCurrentUser().getNickname()); // 用户昵称

            if (article.getZone() != null) {
                String[] split = article.getZone().split(",");
                Province province = sysService.findProvince(Integer.parseInt(split[0]));
                if (split.length == 3) {
                    City city = sysService.findCity(Integer.parseInt(split[1]));
                    Area area = sysService.findArea(Integer.parseInt(split[2]));
                    article.setProvince(province.getLabel());
                    article.setCity(city.getLabel());
                } else if (split.length == 2) {
                    City city = sysService.findCity(Integer.parseInt(split[1]));
                    Area area = sysService.findArea(Integer.parseInt(split[2]));
                    article.setProvince(province.getLabel());
                    article.setCity(city.getLabel());
                } else if (split.length == 1){
                    article.setProvince(province.getLabel());
                }
            }
        }
        return Result.success(articleService.saveOrUpdate(article));
    }

    // 根据Id删除
    @DeleteMapping(value = "/del/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(articleService.removeById(id));
    }

    // 根据id 批量删除
    @PostMapping(value = "/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(articleService.removeBatchByIds(ids));
    }

    // 查询所有数据
    @GetMapping("/findAll")
    public Result findAll() {
        return Result.success(articleService.list());
    }

    // 分页查询 - mybatis-plus的方式
// @RequestParam接受 ?pageNum=1&pageSize=10
// limit (pageNum-1) * pageSize,pageSize   | 页数-1*当页总数,当页总数
    @GetMapping("/page")    // 接口路径 /user/page
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String title,
                           @RequestParam(defaultValue = "") String userName,
                           @RequestParam(defaultValue = "") String zone) {
        IPage<Article> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (StrUtil.isNotBlank(title)) {
            queryWrapper.like("title",title);
        }
        if (StrUtil.isNotBlank(userName)) {
            queryWrapper.like("userNickName",userName);
        }
        if (StrUtil.isNotBlank(zone) && !("全市".equals(zone))) {
            queryWrapper.like("province",zone);
        }
        return Result.success(articleService.page(page, queryWrapper));
    }

    /**
     * 根据文章Id查询文章详情
     * @param id
     * @return 文章详情
     */
    @GetMapping("/articleDetail/{id}")
    public Result findArticleDetail(@PathVariable Integer id) {
        if (id != null) {
            return Result.success(articleService.getById(id));
        }
        return Result.error(Constants.CODE_400,"参数错误");
    }

    /**
     * 获取文章总数
     * @return 文章/帖子 总数
     */
    @GetMapping(value = "/getArticleCount")
    public Result getArticleCount() {
        long count = articleService.count();
        return Result.success(count);
    }
}

