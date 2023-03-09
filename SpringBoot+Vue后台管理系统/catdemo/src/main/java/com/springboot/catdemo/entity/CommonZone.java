package com.springboot.catdemo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 共享地区类
 * @Author: can
 * @Description:
 * @Date: Create in 7:03 2022/5/25
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel(value = "CommonZone对象", description = "共享地区类")
public class CommonZone {

    @ApiModelProperty("省份")
    private String province;

    @ApiModelProperty("市区")
    private String city;
}
