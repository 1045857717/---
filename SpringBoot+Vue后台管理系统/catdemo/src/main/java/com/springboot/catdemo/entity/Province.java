package com.springboot.catdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author: can
 * @Description:
 * @Date: Create in 6:13 2022/6/9
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel(value = "省对象", description = "")
public class Province {

    @ApiModelProperty("code")
    Integer value;

    @ApiModelProperty("省名称")
    String label;

    @ApiModelProperty("市对象数组")
    List<City> children;

}
