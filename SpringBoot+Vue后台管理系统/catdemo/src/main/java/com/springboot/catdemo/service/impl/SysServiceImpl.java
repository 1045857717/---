package com.springboot.catdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.catdemo.entity.Area;
import com.springboot.catdemo.entity.City;
import com.springboot.catdemo.entity.Province;
import com.springboot.catdemo.mapper.CommentMapper;
import com.springboot.catdemo.mapper.SysMapper;
import com.springboot.catdemo.service.ISysService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: can
 * @Description:
 * @Date: Create in 6:09 2022/6/9
 */
@Service
public class SysServiceImpl extends ServiceImpl<SysMapper, Province> implements ISysService {

    @Resource
    private SysMapper sysMapper;

    @Override
    public List<Province> selectProvinces() {
        return sysMapper.selectProvinceAll();
    }

    @Override
    public Province findProvince(Integer code) {
        return sysMapper.selectProvince(code);
    }

    @Override
    public City findCity(Integer code) {
        return sysMapper.selectCity(code);
    }

    @Override
    public Area findArea(Integer code) {
        return sysMapper.selectArea(code);
    }
}
