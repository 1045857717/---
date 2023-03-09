package com.springboot.catdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.catdemo.controller.dto.UserDTO;
import com.springboot.catdemo.entity.Menu;
import com.springboot.catdemo.entity.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author CAN
 * @since 2022-03-06
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    User register(UserDTO userDTO);

    boolean isExistUsername(String username);

    boolean updateAvatarUrlByUsername(String fileUrl, String username);

    List<Menu> getMenusByRoleKey(String roleKey);

    User getMd5PasswordUser(User user);

    boolean updatePasswordByoldPass(String oldPass, String confirmNewPass);
}
