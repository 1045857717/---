package com.springboot.catdemo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author: can
 * @Description:
 * @Date: Create in 6:20 2022/6/9
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel(value = "城对象", description = "")
public class City {

    @ApiModelProperty("code")
    Integer value;

    @ApiModelProperty("关联省Id")
    Integer cityProvinceCode;

    @ApiModelProperty("市名称")
    String label;

    @ApiModelProperty("区对象数组")
    List<Area> children;

}
