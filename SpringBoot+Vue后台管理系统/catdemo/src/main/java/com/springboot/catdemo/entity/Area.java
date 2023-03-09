package com.springboot.catdemo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Author: can
 * @Description:
 * @Date: Create in 6:21 2022/6/9
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel(value = "区对象", description = "")
public class Area {

    @ApiModelProperty("code")
    Integer value;
    
    @ApiModelProperty("关联省Id")
    Integer areaCityCode;
    
    @ApiModelProperty("关联市Id")
    Integer areaProvinceCode;
    
    @ApiModelProperty("区名称")
    String label;
}
