package com.springboot.catdemo.mapper;

import com.springboot.catdemo.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author CAN
 * @since 2022-03-21
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 通过角色唯一标识查找角色id
     * @param roleKey
     * @return
     */
    @Select("select id from sys_role where role_key = #{roleKey}")
    Integer selectByRoleKey(@Param("roleKey") String roleKey);
}
