package com.springboot.catdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 词典类型表
 * </p>
 *
 * @author CAN
 * @since 2022-06-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_dict_type")
@ApiModel(value = "DictType对象", description = "词典类型表")
public class DictType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("字典名称")
    @TableField("dict_name")
    private String dictName;

    @ApiModelProperty("字典类型")
    @TableField("dict_type")
    private String dictType;

    @ApiModelProperty("状态（0正常 1停用）")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("创建者")
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty("更新者")
    @TableField("update_by")
    private String updateBy;

    @ApiModelProperty("更新时间")
    @TableField("update_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;
}
