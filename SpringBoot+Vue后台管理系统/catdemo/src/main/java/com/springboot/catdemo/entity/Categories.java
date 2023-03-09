package com.springboot.catdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 微信九宫格菜单
 * </p>
 *
 * @author CAN
 * @since 2022-07-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("wx_categories")
@ApiModel(value = "Categories对象", description = "微信九宫格菜单")
public class Categories implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("微信菜单名称")
    @TableField("menuName")
    private String menuName;

    @ApiModelProperty("微信菜单图标url")
    @TableField("menuIconUrl")
    private String menuIconUrl;

    @ApiModelProperty("文件名称")
    @TableField("fileName")
    private String fileName;

    @ApiModelProperty("文件类型")
    @TableField("type")
    private String type;

    @ApiModelProperty("文件大小(KB)")
    @TableField("size")
    private Long size;

    @ApiModelProperty("文件md5")
    @TableField("md5")
    private String md5;

    @ApiModelProperty("是否删除(0:未删除,1:删除)")
    @TableField("is_delete")
    private Boolean isDelete;

    @ApiModelProperty("是否禁用链接(0:禁用,1:可用)")
    @TableField("enable")
    private Boolean enable;


}
