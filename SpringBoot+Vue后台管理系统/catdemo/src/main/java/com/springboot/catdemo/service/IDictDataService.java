package com.springboot.catdemo.service;

import com.springboot.catdemo.entity.DictData;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 词典数据表 服务类
 * </p>
 *
 * @author CAN
 * @since 2022-06-13
 */
public interface IDictDataService extends IService<DictData> {

    /**
     * 查看字典标签是否存在
     * @param dictLabel
     * @return 存在则返回true否则false
     */
    boolean isExistUsername(String dictLabel);
}
