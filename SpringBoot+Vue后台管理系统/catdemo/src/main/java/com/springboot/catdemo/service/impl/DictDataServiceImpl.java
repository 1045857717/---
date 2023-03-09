package com.springboot.catdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.catdemo.entity.DictData;
import com.springboot.catdemo.entity.User;
import com.springboot.catdemo.mapper.DictDataMapper;
import com.springboot.catdemo.service.IDictDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 词典数据表 服务实现类
 * </p>
 *
 * @author CAN
 * @since 2022-06-13
 */
@Service
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements IDictDataService {

    @Override
    public boolean isExistUsername(String dictLabel) {
        QueryWrapper<DictData> qw = new QueryWrapper<>();
        qw.eq("dict_label", dictLabel);
        List<DictData> user = list(qw);
        if (user != null && user.size() > 0) {
            return true;
        }
        return false;
    }
}
