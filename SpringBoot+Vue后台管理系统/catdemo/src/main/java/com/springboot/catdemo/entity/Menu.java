package com.springboot.catdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author CAN
 * @since 2022-03-21
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_menu")
@ApiModel(value = "Menu对象", description = "")
@ToString
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("国际化词典")
    @TableField("dict_name")
    private String dictName;

    @ApiModelProperty("路由名称")
    @TableField("router_name")
    private String routerName;

    @ApiModelProperty("路由路径")
    @TableField("path")
    private String path;

    @ApiModelProperty("页面路径")
    @TableField("view_path")
    private String viewPath;

    @ApiModelProperty("菜单顺序")
    @TableField("menu_order")
    private Integer menuOrder;

    @ApiModelProperty("图标")
    @TableField("icon")
    private String icon;

    @ApiModelProperty("描述")
    @TableField("description")
    private String description;

    // exist是否为数据库表字段
    @TableField(exist = false)
    private List<Menu> children;

    @ApiModelProperty("父级菜单id")
    @TableField("pid")
    private Integer pid;

    @ApiModelProperty("是否为父级菜单（0:否/1:是）")
    @TableField("is_parent")
    private Integer isParent;

    @ApiModelProperty("是否在菜单中显示（0:否/1:是）默认为是")
    @TableField("is_show")
    private Integer isShow;
}
