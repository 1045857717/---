package com.springboot.catdemo.controller;


import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.catdemo.common.Constants;
import com.springboot.catdemo.common.Result;
import com.springboot.catdemo.entity.DictData;
import com.springboot.catdemo.service.IDictDataService;
import com.springboot.catdemo.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 词典数据表 前端控制器
 * </p>
 *
 * @author CAN
 * @since 2022-06-13
 */
@RestController
@RequestMapping("/dictData")
public class DictDataController {

    @Autowired
    private IDictDataService dictDataService;

    // 新增或修改
    @PostMapping
    public Result save(@RequestBody DictData dictData) {
        if (dictData.getId() != null) {
            dictData.setUpdateBy(
                    Objects.requireNonNull(TokenUtils.getCurrentUser(),"当前用户不存在，参数错误")
                            .getUsername());
            dictData.setUpdateTime(LocalDateTimeUtil.now());
        } else {
            dictData.setCreateBy(
                    Objects.requireNonNull(TokenUtils.getCurrentUser(),"当前用户不存在，参数错误")
                            .getUsername());
            dictData.setCreateTime(LocalDateTimeUtil.now());
        }
        return Result.success(dictDataService.saveOrUpdate(dictData));
    }

    // 根据Id删除
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(dictDataService.removeById(id));
    }

    // 根据id 批量删除
    @PostMapping(value = "/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(dictDataService.removeBatchByIds(ids));
    }

    // 查询所有数据
    @GetMapping
    public Result findAll() {
        return Result.success(dictDataService.list());
    }


    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String dictLabel,
                                @RequestParam(defaultValue = "") String dictType) {
        IPage<DictData> page = new Page<>(pageNum, pageSize);
        QueryWrapper<DictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (dictType != null && !"".equals(dictType))
            queryWrapper.eq("dict_type", dictType);
        if (dictLabel != null && !"".equals(dictLabel))
            queryWrapper.like("dict_label", dictLabel);
        return Result.success(dictDataService.page(page, queryWrapper));
    }

    /**
     * 修改字典状态（0正常 1停用）
     * @param dictData
     * @return
     */
    @PostMapping(value = "/updateStatus")
    public Result updateEnable(@RequestBody DictData dictData) {
        if (dictData != null) {
            return Result.success(dictDataService.updateById(dictData));
        } else {
            return Result.error(Constants.CODE_400, "参数错误");
        }
    }

    /**
     * 字典标签是否存在
     * @param dictLabel 字典标签
     * @return 不存在返回 200:dictLabel,存在返回400：参数错误
     */
    @PostMapping(value = "/existDictLabel/{dictLabel}")
    public Result existDictLabel(@PathVariable String dictLabel) {
        if (StrUtil.isEmptyIfStr(dictLabel)) {
            return Result.error(Constants.CODE_400,"参数错误");
        } else {
            if (dictDataService.isExistUsername(dictLabel)) {
                // 存在则返回code:500,msg:用户已存在
                return Result.error(Constants.CODE_500, "字典标签已存在");
            } else {
                // 不存在则返回code:200,msg:username
                return Result.success(dictLabel);
            }
        }
    }
}

