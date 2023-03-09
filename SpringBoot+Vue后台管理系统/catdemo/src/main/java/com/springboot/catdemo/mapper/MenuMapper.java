package com.springboot.catdemo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.springboot.catdemo.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author CAN
 * @since 2022-03-21
 */
public interface MenuMapper extends BaseMapper<Menu> {

    IPage<Menu> selectPageOrder(@Param("page") IPage<Menu> page,@Param("params") Map<String, Object> params);

}
