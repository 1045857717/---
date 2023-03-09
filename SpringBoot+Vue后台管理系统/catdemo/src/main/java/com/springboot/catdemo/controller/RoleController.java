package com.springboot.catdemo.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.catdemo.common.Result;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import com.springboot.catdemo.service.IRoleService;
import com.springboot.catdemo.entity.Role;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author CAN
 * @since 2022-03-21
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    // 新增或修改
    @PostMapping
    public Result save(@RequestBody Role role) {
        return Result.success(roleService.saveOrUpdate(role));
    }

    // 根据Id删除
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(roleService.removeById(id));
    }

    // 根据id 批量删除
    @PostMapping(value = "/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(roleService.removeBatchByIds(ids));
    }

    // 查询所有数据
    @GetMapping
    public Result findAll() {
        return Result.success(roleService.list());
    }

    // 分页查询 - mybatis-plus的方式
// @RequestParam接受 ?pageNum=1&pageSize=10
// limit (pageNum-1) * pageSize,pageSize   | 页数-1*当页总数,当页总数
    @GetMapping("/page")    // 接口路径 /user/page
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {
        IPage<Role> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        return Result.success(roleService.page(page, queryWrapper));
    }

    /**
     * 通过角色id绑定角色和菜单
     * @param roleId 角色id
     * @param menuIds 菜单id 数组
     * @return
     */
    @PostMapping("/saveRoleMenu/{roleId}")
    public Result saveRoleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        roleService.setRoleMenu(roleId, menuIds);
        return Result.success();
    }

    /**
     * 通过角色id获取菜单列表
     * @param roleId
     * @return
     */
    @GetMapping("/getMenuByRoleId/{roleId}")
    public Result getMenuByRoleId(@PathVariable Integer roleId) {
        return Result.success(roleService.getMenuByRoleId(roleId));
    }

}

