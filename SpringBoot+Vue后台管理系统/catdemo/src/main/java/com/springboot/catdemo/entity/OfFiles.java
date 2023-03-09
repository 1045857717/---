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
 *
 * </p>
 *
 * @author CAN
 * @since 2023-02-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("cat_of_files")
@ApiModel(value = "OfFiles对象", description = "")
public class OfFiles implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("文件名称")
    @TableField("file_name")
    private String fileName;

    @ApiModelProperty("文件类型")
    @TableField("file_type")
    private String fileType;

    @ApiModelProperty("文件划分(使用场景)")
    @TableField("file_rank")
    private String fileRank;

    @ApiModelProperty("文件大小")
    @TableField("file_size")
    private Long fileSize;

    @ApiModelProperty("文件下载链接")
    @TableField("file_url")
    private String fileUrl;

    @ApiModelProperty("文件MD5")
    @TableField("file_md5")
    private String fileMd5;

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
