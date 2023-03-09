package com.springboot.catdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.catdemo.entity.Menu;
import com.springboot.catdemo.entity.RoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: can
 * @Description:
 * @Date: Create in 7:33 2022/3/23
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    @Delete("delete from sys_role_menu where role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") Integer roleId);

    /**
     * 通过角色id查询对应的菜单
     * @param roleId
     * @return
     */
    @Select("select m.* from sys_menu as m " +
            "left join sys_role_menu as rm on m.id = rm.menu_id " +
            "where rm.role_id = #{roleId} " +
            "ORDER BY is_parent desc, if(isnull(menu_order), 999, menu_order) asc ")
    List<Menu> selectByRoleMenu(@Param("roleId")Integer roleId);
}
