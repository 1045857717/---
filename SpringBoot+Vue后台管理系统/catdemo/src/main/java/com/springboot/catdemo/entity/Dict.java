package com.springboot.catdemo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: can
 * @Description:
 * @Date: Create in 4:08 2022/3/22
 */
@TableName("sys_dict")
@Data
@Accessors(chain = true)
public class Dict {

    @ApiModelProperty("名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("内容")
    @TableField("value")
    private String value;

    @ApiModelProperty("类型")
    @TableField("type")
    private String type;
}
