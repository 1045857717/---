package com.springboot.catdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.catdemo.entity.Area;
import com.springboot.catdemo.entity.City;
import com.springboot.catdemo.entity.Province;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: can
 * @Description:
 * @Date: Create in 6:10 2022/6/9
 */
@Mapper
public interface SysMapper extends BaseMapper<Province> {

    /**
     * 获取所有的省市区
     * @return
     */
    List<Province> selectProvinceAll();

    /**
     * 根据省Id获取省名称
     * @param code
     * @return
     */
    Province selectProvince(@Param("code") Integer code);

    /**
     * 根据市Id获取市
     * @param code
     * @return
     */
    City selectCity(@Param("code")Integer code);

    /**
     * 根据区Id获取区名称
     * @param code
     * @return
     */
    Area selectArea(@Param("code")Integer code);
}
