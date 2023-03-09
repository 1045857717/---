package com.springboot.catdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.catdemo.entity.Area;
import com.springboot.catdemo.entity.City;
import com.springboot.catdemo.entity.Province;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: can
 * @Description:
 * @Date: Create in 6:09 2022/6/9
 */
@Service
public interface ISysService extends IService<Province> {

    /**
     * 获取所有的省市区
     * @return
     */
    List<Province> selectProvinces();

    /**
     * 根据省Id获取省名称
     * @param code
     * @return
     */
    Province findProvince(Integer code);

    /**
     * 根据市Id获取市名称
     * @param code
     * @return
     */
    City findCity(Integer code);

    /**
     * 根据区Id获取区名称
     * @param code
     * @return
     */
    Area findArea(Integer code);
}
