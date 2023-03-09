package com.springboot.catdemo.controller.dto;

import com.springboot.catdemo.entity.Menu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 接受前端登录请求的参数
 * @Author: can
 * @Description:
 * @Date: Create in 6:34 2022/3/12
 */
@Data
@ApiModel(value = "UserDTO", description = "用户登录参数映射")
public class UserDTO {
    @ApiModelProperty("用户Id")
    private String id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("昵称")
    private String nickname;
    @ApiModelProperty("头像")
    private String avatarUrl;
    @ApiModelProperty("Token")
    private String token;
    @ApiModelProperty("用户角色")
    private String role;
    @ApiModelProperty("用户菜单")
    private List<Menu> menus;
}
