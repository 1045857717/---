package com.springboot.catdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * POI的Excel实体类
 * @Author: can
 * @Description:
 * @Date: Create in 2:00 2022/3/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class POIExcelDao<T> {

    // 查询到的数据
    List<T> list;
    // 当前导入是否修改数据(默认为false)
    boolean isUpdate = false;

}
