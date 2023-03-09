package com.springboot.catdemo.service.impl;

import com.springboot.catdemo.entity.Categories;
import com.springboot.catdemo.mapper.CategoriesMapper;
import com.springboot.catdemo.service.ICategoriesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 微信九宫格菜单 服务实现类
 * </p>
 *
 * @author CAN
 * @since 2022-07-10
 */
@Service
public class CategoriesServiceImpl extends ServiceImpl<CategoriesMapper, Categories> implements ICategoriesService {

}
