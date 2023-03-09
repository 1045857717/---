package com.springboot.catdemo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.springboot.catdemo.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author CAN
 * @since 2022-03-21
 */
public interface IMenuService extends IService<Menu> {

    IPage findMenusPage(IPage<Menu> page, String name);
}
