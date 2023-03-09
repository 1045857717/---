package com.springboot.catdemo.mapper;

import com.springboot.catdemo.entity.Menu;
import com.springboot.catdemo.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author CAN
 * @since 2022-03-06
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名更新头像url
     * @param fileUrl
     * @param username
     * @return
     */
    Integer updateAvatarUrlByUsername(@Param("fileUrl") String fileUrl, @Param("username") String username);
}
