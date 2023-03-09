package com.springboot.catdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.catdemo.entity.Files;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: can
 * @Description:
 * @Date: Create in 7:19 2022/3/16
 */
@Mapper
public interface FileMapper extends BaseMapper<Files> {
}
