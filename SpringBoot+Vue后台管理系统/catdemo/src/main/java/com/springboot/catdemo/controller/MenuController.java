package com.springboot.catdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.catdemo.common.Constants;
import com.springboot.catdemo.common.Result;
import com.springboot.catdemo.entity.Dict;
import com.springboot.catdemo.entity.DictData;
import com.springboot.catdemo.entity.Menu;
import com.springboot.catdemo.mapper.DictDataMapper;
import com.springboot.catdemo.mapper.DictMapper;
import com.springboot.catdemo.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author CAN
 * @since 2022-03-21
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @Resource
    private DictDataMapper dictDataMapper;

    // 新增或修改
    @PostMapping
    public Result save(@RequestBody Menu menu) {
        return Result.success(menuService.saveOrUpdate(menu));
    }

    // 根据Id删除
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(menuService.removeById(id));
    }

    // 根据id 批量删除
    @PostMapping(value = "/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(menuService.removeBatchByIds(ids));
    }


    /**
     * 查询所有菜单
     * @param name
     * @return
     */
    @GetMapping
    public Result findAll(@RequestParam(defaultValue = "") String name) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByAsc("menu_order");
        // 获取所有菜单
        List<Menu> list = menuService.list(queryWrapper);
        // 找出pid为null的一级菜单
        List<Menu> parenMenu = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        // 菜单列表
        List<Menu> MenusNode = new ArrayList<>();
        // 遍历父级菜单添加子级菜单
        for (Menu menu : parenMenu) {
            Menu parenNode = addChildrenMenu(menu, list);
            MenusNode.add(parenNode);
        }
        return Result.success(MenusNode);
    }


    /**
     * 递归 添加子菜单
     * @param pNode 父级菜单
     * @param menus 所有菜单列表
     * @return
     */
    private Menu addChildrenMenu(Menu pNode, List<Menu> menus) {
        List<Menu> childrenMenus = new ArrayList<>();
        for (Menu menu : menus) {
            if (pNode.getId().equals(menu.getPid())) {
                childrenMenus.add(addChildrenMenu(menu, menus));
            }
        }
        return pNode.setChildren(childrenMenus);
    }

// 分页查询 - mybatis-plus的方式
// @RequestParam接受 ?pageNum=1&pageSize=10
// limit (pageNum-1) * pageSize,pageSize   | 页数-1*当页总数,当页总数
    @GetMapping("/page")    // 接口路径 /user/page
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String name) {
        IPage<Menu> page = new Page<>(pageNum, pageSize);
        IPage menus = menuService.findMenusPage(page, name);
        return Result.success(menus);
    }

    /**
     * 词典中获取ICON图标
     * @return
     */
    @GetMapping(value = "/icons")
    public Result getIcons() {
        QueryWrapper<DictData> qw = new QueryWrapper<>();
        qw.eq("dict_type", Constants.DICT_TYPE_ICON);
        return Result.success(dictDataMapper.selectList(qw));
    }
}

