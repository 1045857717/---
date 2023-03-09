package com.springboot.catdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.catdemo.entity.Menu;
import com.springboot.catdemo.entity.Role;
import com.springboot.catdemo.entity.RoleMenu;
import com.springboot.catdemo.mapper.RoleMapper;
import com.springboot.catdemo.mapper.RoleMenuMapper;
import com.springboot.catdemo.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CAN
 * @since 2022-03-21
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource // 先通过Name注入再类型
    private RoleMenuMapper roleMenuMapper;

    @Transactional
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
        // 先删除所有的绑定关系，再绑定关系
        roleMenuMapper.deleteByRoleId(roleId);
        for (Integer menuId : menuIds) {
            roleMenuMapper.insert(new RoleMenu(roleId, menuId));
        }
    }

    @Override
    public List<Menu> getMenuByRoleId(Integer roleId) {
        return roleMenuMapper.selectByRoleMenu(roleId);
    }
}
