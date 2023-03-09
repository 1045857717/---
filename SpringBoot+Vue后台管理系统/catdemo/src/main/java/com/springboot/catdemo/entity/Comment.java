package com.springboot.catdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文章评论表
 * </p>
 *
 * @author CAN
 * @since 2022-06-05
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("cat_comment")
@ApiModel(value = "Comment对象", description = "文章评论表")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("评论内容")
    @TableField("comment_content")
    private String commentContent;

    @ApiModelProperty("评论时间")
    @TableField("comment_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime commentTime;

    @ApiModelProperty("评论人的用户Id")
    @TableField("user_id")
    private Integer userId;

    @ApiModelProperty("回复人的昵称")
    @TableField("reply_name")
    private String replyName;

    @ApiModelProperty("评论的文章Id")
    @TableField("article_id")
    private Integer articleId;

    @ApiModelProperty("评论的父Id")
    @TableField("pid")
    private Integer pid;

    @ApiModelProperty("最上级评论Id")
    @TableField("original_id")
    private Integer originalId;

    @TableField(exist = false)
    @ApiModelProperty("评论的用户名")
    private String username;

    @TableField(exist = false)
    @ApiModelProperty("评论的用户昵称")
    private String nickname;

    @TableField(exist = false)
    @ApiModelProperty("评论人的性别")
    private String gender;

    @TableField(exist = false)
    @ApiModelProperty("评论的邮箱")
    private String email;

    @TableField(exist = false)
    @ApiModelProperty("评论的电话")
    private String phone;

    @TableField(exist = false)
    @ApiModelProperty("评论的用户头像")
    private String avatarUrl;

    @TableField(exist = false)
    @ApiModelProperty("评论的子列联")
    private List<Comment> childrenComments;

}
