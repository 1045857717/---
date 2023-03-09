package com.springboot.catdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 角色菜单关系
 * @Author: can
 * @Description:
 * @Date: Create in 7:30 2022/3/23
 */
@TableName(value = "sys_role_menu")
@Data
@Accessors(chain = true)
@ApiModel(value = "RoleMenu对象", description = "")
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenu {

    @ApiModelProperty("角色id")
    @TableField("role_id")
    private Integer roleId;

    @ApiModelProperty("菜单id")
    @TableField("menu_id")
    private Integer menuId;
}
