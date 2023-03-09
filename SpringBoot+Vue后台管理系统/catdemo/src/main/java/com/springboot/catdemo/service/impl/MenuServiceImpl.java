package com.springboot.catdemo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.springboot.catdemo.controller.MenuTree;
import com.springboot.catdemo.entity.Menu;
import com.springboot.catdemo.mapper.MenuMapper;
import com.springboot.catdemo.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.catdemo.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CAN
 * @since 2022-03-21
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    public IPage findMenusPage(IPage<Menu> page, String name) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", name);
        IPage<Menu> menusPage = menuMapper.selectPageOrder(page, params);
        // 判断菜单名称搜索后的菜单，没有父级菜单则添加父级菜单，以防前端不展示子级菜单(只有子级菜单没有父级菜单 不会展示菜单)
        List<Menu> records = menusPage.getRecords();
        // 获取所有的菜单
        List<Menu> menuAll = menuMapper.selectList(null);
        // 获取当前菜单的所有id
        List<Integer> currentMenuAllId = records.stream().map(Menu::getId).collect(Collectors.toList());
        List<Menu> tempMenus = new ArrayList<>();
        for (Menu record : records) {
            List<Menu> parentNode = this.getParentNode(new ArrayList<>(), record, menuAll, currentMenuAllId);
            if (parentNode != null) {
                // 获取到的父节点添加到搜索到的子节点
                tempMenus.addAll(parentNode);
            }
        }
        records.addAll(tempMenus);
        // 菜单去重
        List<Menu> menusNode = records.stream().distinct().collect(Collectors.toList());
        // 父子关联菜单
        List<Menu> menuNode = new MenuTree(menusNode).buildTree();
        menuNode.stream().forEach(System.out::println);
        // 将筛选后的菜单重新赋值给page结果
        menusPage.setRecords(menuNode);
        return menusPage;
    }

    /**
     * 获取节点的父级节点
     * @param menus 递归传递
     * @param child 要获取父级节点的子节点
     * @param menuAll 所有菜单列表
     * @param currentMenuAllId 当前菜单的id
     * @return 父级节点
     */
    private List<Menu> getParentNode(List<Menu> menus, Menu child, List<Menu> menuAll, List<Integer> currentMenuAllId) {
        List<Menu> menusNode = menus;
        if (null != child.getPid() && !currentMenuAllId.contains(child.getPid())) {
            for (Menu parentMenu : menuAll) {
                if (child.getPid().equals(parentMenu.getId())) {
                    menusNode.add(parentMenu);
                    getParentNode(menusNode, parentMenu, menuAll, currentMenuAllId);
                }
            }
            return menusNode.stream().distinct().collect(Collectors.toList());
        } else {
            return null;
        }
    }
}