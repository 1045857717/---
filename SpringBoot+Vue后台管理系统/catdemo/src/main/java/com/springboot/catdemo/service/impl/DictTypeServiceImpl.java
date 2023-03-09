package com.springboot.catdemo.service.impl;

import com.springboot.catdemo.entity.DictType;
import com.springboot.catdemo.mapper.DictTypeMapper;
import com.springboot.catdemo.service.IDictTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 词典类型表 服务实现类
 * </p>
 *
 * @author CAN
 * @since 2022-06-13
 */
@Service
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements IDictTypeService {

}
