package com.springboot.catdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: can
 * @Description:
 * @Date: Create in 7:15 2022/3/16
 */
@Data
@TableName("sys_file")
@Accessors(chain = true)
public class Files {

    @ApiModelProperty("id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("文件名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("文件后缀类型")
    @TableField("type")
    private String type;

    @ApiModelProperty("文件大小(KB)")
    @TableField("size")
    private Long size;

    @ApiModelProperty("下载链接")
    @TableField("url")
    private String url;

    @ApiModelProperty("文件md5")
    @TableField("md5")
    private String md5;

    @ApiModelProperty("上传文件的用户名")
    @TableField("create_username")
    private String createUsername;

    @ApiModelProperty("上传文件的时间")
    @TableField("create_time")
    private String createTime;

    @ApiModelProperty("是否删除(0:未删除,1:删除)")
    @TableField("is_delete")
    private Boolean isDelete;

    @ApiModelProperty("是否禁用链接(0:禁用,1:可用)")
    @TableField("enable")
    private Boolean enable;
}
