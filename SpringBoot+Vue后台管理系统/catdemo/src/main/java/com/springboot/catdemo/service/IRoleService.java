package com.springboot.catdemo.service;

import com.springboot.catdemo.entity.Menu;
import com.springboot.catdemo.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author CAN
 * @since 2022-03-21
 */
public interface IRoleService extends IService<Role> {

    void setRoleMenu(Integer roleId, List<Integer> menuIds);

    List<Menu> getMenuByRoleId(Integer roleId);
}
