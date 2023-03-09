package com.springboot.catdemo.controller;

import com.springboot.catdemo.entity.Menu;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 树形结构菜单
 * @Author can
 * @ClassName MenuTree
 * @Description
 * @Date 2022/03/24 17:54
 * @Version 1.0
 */
@Component
public class MenuTree {

    /**
     * 所有的菜单数据
     */
    private List<Menu> menuList = new ArrayList<>();

    public MenuTree(List<Menu> menuList) {
        this.menuList = menuList;
    }

    /**
     * 建立树形结构
     * @return
     */
    public List<Menu> buildTree() {
//        List<Menu> treeMenus = new ArrayList<>();
//        for (Menu menuNode : getRootNode()) {
//            menuNode = buildChildTree(menuNode);
//            treeMenus.add(menuNode);
//        }
//        return treeMenus;

        return getRootNode().stream().map(this::buildChildTree).collect(Collectors.toList());


    }

    /**
     * 递归，建立子树形结构
     * @param pNode
     * @return
     */
    private Menu buildChildTree(Menu pNode) {
/*        List<Menu> childMenus = new ArrayList<>();
        for (Menu menuNode : menuList) {
            if (pNode.getId().equals(menuNode.getPid())) {
                childMenus.add(buildChildTree(menuNode));
            }
        }
        pNode.setChildren(childMenus);*/
        /* stream流表达式 */
        pNode.setChildren(menuList.stream()
                .filter(menu -> pNode.getId().equals(menu.getPid()))
                .map(this::buildChildTree)
                .collect(Collectors.toList()));
        return pNode;
    }

    /**
     * 获取根节点
     *
     * @return
     */
    private List<Menu> getRootNode() {
        List<Menu> rootMenuLists = menuList.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        return rootMenuLists;
    }
}