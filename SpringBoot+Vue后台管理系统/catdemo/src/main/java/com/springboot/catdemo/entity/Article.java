package com.springboot.catdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 文章发布
 * </p>
 *
 * @author CAN
 * @since 2022-05-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "cat_article")
@ApiModel(value = "Article对象", description = "文章发布")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("标题")
    @TableField("title")
    private String title;

    @ApiModelProperty("内容")
    @TableField("content")
    private String content;

    @ApiModelProperty("发布人id")
    @TableField("userId")
    private Integer userId;

    @ApiModelProperty("用户名")
    @TableField("userName")
    private String userName;

    @ApiModelProperty("用户昵称")
    @TableField("userNickName")
    private String userNickName;

    @ApiModelProperty("省份")
    @TableField("province")
    private String province;

    @ApiModelProperty("市区")
    @TableField("city")
    private String city;

    @ApiModelProperty("地区(省份+市区)")
    @TableField("zone")
    private String zone;

    @ApiModelProperty("详细位置")
    @TableField("address")
    private String address;

    @ApiModelProperty("发布时间")
    @TableField("createTime")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty("用户的回复帖子数量")
    @TableField("replyTotal")
    private int replyTotal;

    @ApiModelProperty("用户查看文章的次数")
    @TableField("lookTotal")
    private int lookTotal;

}
