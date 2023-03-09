package com.springboot.catdemo.controller;


import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.catdemo.common.Constants;
import com.springboot.catdemo.common.Result;
import com.springboot.catdemo.entity.DictType;
import com.springboot.catdemo.service.IDictTypeService;
import com.springboot.catdemo.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 词典类型表 前端控制器
 * </p>
 *
 * @author CAN
 * @since 2022-06-13
 */
@RestController
@RequestMapping("/dictType")
public class DictTypeController {

    @Autowired
    private IDictTypeService dictTypeService;

    // 新增或修改
    @PostMapping
    public Result save(@RequestBody DictType dictType) {
        if (dictType.getId() != null) {
            dictType.setUpdateBy(
                    Objects.requireNonNull(TokenUtils.getCurrentUser(),"当前用户不存在，参数错误")
                            .getUsername());
            dictType.setUpdateTime(LocalDateTimeUtil.now());
        } else {
            dictType.setCreateBy(
                    Objects.requireNonNull(TokenUtils.getCurrentUser(),"当前用户不存在，参数错误")
                            .getUsername());
            dictType.setCreateTime(LocalDateTimeUtil.now());
        }
        return Result.success(dictTypeService.saveOrUpdate(dictType));
    }

    // 根据Id删除
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(dictTypeService.removeById(id));
    }

    // 根据id 批量删除
    @PostMapping(value = "/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(dictTypeService.removeBatchByIds(ids));
    }

    // 查询所有数据
    @GetMapping
    public Result findAll() {
        return Result.success(dictTypeService.list());
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String dictName) {
        IPage<DictType> page = new Page<>(pageNum, pageSize);
        QueryWrapper<DictType> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (dictName != null && !"".equals(dictName))
            queryWrapper.like("dict_name", dictName);
        return Result.success(dictTypeService.page(page, queryWrapper));
    }

    /**
     * 修改字典状态（0正常 1停用）
     * @param dictType
     * @return
     */
    @PostMapping(value = "/updateStatus")
    public Result updateEnable(@RequestBody DictType dictType) {
        if (dictType != null) {
            return Result.success(dictTypeService.updateById(dictType));
        } else {
            return Result.error(Constants.CODE_400, "参数错误");
        }
    }
}

