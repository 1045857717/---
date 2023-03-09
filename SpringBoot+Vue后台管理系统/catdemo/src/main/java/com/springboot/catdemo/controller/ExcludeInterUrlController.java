package com.springboot.catdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.catdemo.common.Constants;
import com.springboot.catdemo.common.Result;
import com.springboot.catdemo.entity.DictData;
import com.springboot.catdemo.entity.ExcludeInterUrl;
import com.springboot.catdemo.mapper.DictDataMapper;
import com.springboot.catdemo.service.IExcludeInterUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  需要放行的请求url
 * </p>
 *
 * @author CAN
 * @since 2022-06-15
 */
@RestController
@RequestMapping("/excludeInterUrl")
public class ExcludeInterUrlController {

    @Resource
    private IExcludeInterUrlService excludeInterUrlService;

    @Resource
    private DictDataMapper dictDataMapper;

    // 新增或修改
    @PostMapping
    public Result save(@RequestBody ExcludeInterUrl excludeInterUrl) {
        return Result.success(excludeInterUrlService.saveOrUpdate(excludeInterUrl));
    }

    // 根据Id删除
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(excludeInterUrlService.removeById(id));
    }

    // 根据id 批量删除
    @PostMapping(value = "/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(excludeInterUrlService.removeBatchByIds(ids));
    }

    // 查询所有数据
    @GetMapping
    public Result findAll() {
        return Result.success(excludeInterUrlService.list());
    }

// 分页查询 - mybatis-plus的方式
// @RequestParam接受 ?pageNum=1&pageSize=10
// limit (pageNum-1) * pageSize,pageSize   | 页数-1*当页总数,当页总数
    @GetMapping("/page")    // 接口路径 /user/page
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String excludeInterName,
                                @RequestParam(defaultValue = "") String excludeInterURL
                           ) {
        IPage<ExcludeInterUrl> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ExcludeInterUrl> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (excludeInterName != null && !"".equals(excludeInterName))
            queryWrapper.like("exclude_inter_name", excludeInterName);
        if (excludeInterURL != null && !"".equals(excludeInterURL))
            queryWrapper.like("exclude_inter_url", excludeInterURL);
        return Result.success(excludeInterUrlService.page(page, queryWrapper));
    }

    /**
     * 词典中获取URL类型
     * @return
     */
    @GetMapping(value = "/urlType")
    public Result getUrlType() {
        QueryWrapper<DictData> qw = new QueryWrapper<>();
        qw.eq("dict_type", Constants.DICT_SYS_INTER_URL);
        return Result.success(dictDataMapper.selectList(qw));
    }
}

