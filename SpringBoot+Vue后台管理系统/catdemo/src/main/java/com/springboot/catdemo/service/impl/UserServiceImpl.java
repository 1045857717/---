package com.springboot.catdemo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.catdemo.common.Constants;
import com.springboot.catdemo.common.RoleEnum;
import com.springboot.catdemo.controller.MenuTree;
import com.springboot.catdemo.controller.dto.UserDTO;
import com.springboot.catdemo.entity.Menu;
import com.springboot.catdemo.entity.User;
import com.springboot.catdemo.exception.ServiceException;
import com.springboot.catdemo.mapper.MenuMapper;
import com.springboot.catdemo.mapper.RoleMapper;
import com.springboot.catdemo.mapper.RoleMenuMapper;
import com.springboot.catdemo.mapper.UserMapper;
import com.springboot.catdemo.service.IUserService;
import com.springboot.catdemo.utils.MyRedisUtils;
import com.springboot.catdemo.utils.TokenUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author CAN
 * @since 2022-03-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    private static final Log LOG = Log.get();

    @Override
    public UserDTO login(UserDTO userDTO) {
        User user = getUserInfo(userDTO);
        if (user != null) {
            // 从查询结果中获取盐值
            String salt = user.getSalt();
            // 将参数password和盐值进行加密然后进行判断
            String md5Password = getMd5Password(userDTO.getPassword(), salt);
            // 判断加密后得到的密码与查询结果中的密码是否不匹配
            if (!user.getPassword().equals(md5Password)) {
                throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
            }
            // 将user对象复制给userDTO（ignoreCase：true忽略大小写） 浅拷贝
            BeanUtil.copyProperties(user, userDTO, true);
            // 设置Token
            String token = TokenUtils.genToken(user.getId().toString(), user.getPassword());
            userDTO.setToken(token);
            // 设置密码为Null
            userDTO.setPassword(null);
            String roleKey = user.getRole();
            if (roleKey != null) {
                // 通过角色唯一标识 获取角色id
                Integer roleId = roleMapper.selectByRoleKey(roleKey);
                // 通过角色id查询对应的菜单
                List<Menu> menus = getRoleMenus(roleId);
                // 父子关联菜单
                List<Menu> roleMenus = new MenuTree(menus).buildTree();
                // 设置用户的菜单列表
                userDTO.setMenus(roleMenus);
                roleMenus.forEach(System.err::println);
            }
            // 将用户信息存入redis缓存
            boolean cacheFlag = MyRedisUtils.setHashCache(Constants.USERINFO_KEY, userDTO.getId(), userDTO);
            if (!cacheFlag) {
                throw new ServiceException(Constants.CODE_600, "登录错误，请联系管理员！");
            }
            return userDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "数据异常");
        }
    }

    /**
     * 根据角色id查询角色的菜单列表|查询全部菜单列表
     *
     * @param roleId 角色不传则查全部菜单
     * @return
     */
    public List<Menu> getRoleMenus(Integer roleId) {
        List<Menu> menus = null;
        // 通过角色id查询对应的菜单
        if ("".equals(roleId) || roleId == null) {
            menus = menuMapper.selectList(null);
        } else {
            menus = roleMenuMapper.selectByRoleMenu(roleId);
        }
        return menus;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public User register(UserDTO userDTO) {
        User user = getUserInfo(userDTO);
        if (user != null) { // 用户不存在则保存入库
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        } else {
            user = new User();
            BeanUtil.copyProperties(userDTO, user, true);
            // 对参数user中的password执行加密
            user = getMd5PasswordUser(user);
            // 设置默认的昵称
            user.setNickname(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10));
            // 设置默认的角色
            user.setRole(RoleEnum.ROLE_USER.name());
            // 入库
            save(user);
        }
        return user;
    }

    /**
     * 通过用户名判断用户是否存在
     *
     * @param username 用户名
     * @return 存在返回true，不存在返回false
     */
    @Override
    public boolean isExistUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<User> user = list(queryWrapper);
        if (user != null && user.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据用户名称 (用户名称唯一)判断用户是否存在
     *
     * @param userDTO
     * @return 数据库中存在两个以上则抛出异常 500
     */
    private User getUserInfo(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        User user;
        try {
            user = getOne(queryWrapper);
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return user;
    }

    /**
     * 更新用户头像
     * @param fileName 头像路径
     * @param username 要更新的用户名
     * @return true更新成功|false更新失败
     */
    @Override
    public boolean updateAvatarUrlByUsername(String fileName, String username) {
        Integer res = 0;
        if (!StrUtil.isBlank(fileName)) {
            res = userMapper.updateAvatarUrlByUsername(fileName, username);
        }
        return res != null && res > 0;
    }

    /**
     * 通过用户角色唯一标识获取菜单列表
     *
     * @param roleKey
     * @return
     */
    @Override
    public List<Menu> getMenusByRoleKey(String roleKey) {
        // 通过角色唯一标识 获取角色id
        Integer roleId = roleMapper.selectByRoleKey(roleKey);
        // 通过角色id查询对应的菜单
        List<Menu> menus = getRoleMenus(roleId);
        // 父子关联菜单
        List<Menu> roleMenus = new MenuTree(menus).buildTree();
        return roleMenus;
    }

    /**
     * 判断旧密码是否正确，更新新密码
     *
     * @param oldPass        旧密码
     * @param confirmNewPass 要更新的密码
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean updatePasswordByoldPass(String oldPass, String confirmNewPass) {
        // 从JWT从获取当前用户信息
        User currentUser = TokenUtils.getCurrentUser();
        // 将用户输入的原密码，加入盐判断密码是否正确。
        String md5OldPass = getMd5Password(oldPass, currentUser.getSalt());
        // 判断加密后得到的密码与查询结果中的密码是否不匹配
        if (!currentUser.getPassword().equals(md5OldPass)) {
            return false;
        } else {
            currentUser.setPassword(confirmNewPass);
            User newMd5PasswordUser = getMd5PasswordUser(currentUser);
            return userMapper.updateById(newMd5PasswordUser) > 0;
        }
    }

    /**
     * 注册用户，生产新的MD5
     * 获取使用MD5摘要算法加密后的密码
     *
     * @return 加密后的密码 返回user对象
     */
    @Override
    public User getMd5PasswordUser(User user) {
        // 生成盐值
        String salt = UUID.randomUUID().toString().toUpperCase();
        // 对参数user中的password执行加密
        String password = user.getPassword();
        // 加密规则：原密码的左右两侧都拼接盐值，并循环加密5次
        String str = salt + password + salt;
        for (int i = 0; i < 5; i++) {
            str = DigestUtils.md5Hex(str).toUpperCase();
        }
        // 封装盐值和加密后的密码
        user.setSalt(salt);
        user.setPassword(str);
        return user;
    }

    public static void main(String[] args) {
//        String salt = "5B401F08-CA17-44FB-9441-C8081C13D348";
//        String pass = "admin";
//
//        // 加密规则：原密码的左右两侧都拼接盐值，并循环加密5次
//        String str = salt + pass + salt;
//        for (int i = 0; i < 5; i++) {
//            str = DigestUtils.md5Hex(str).toUpperCase();
//        }
//        System.out.println(str); // admin AB07310580D9DC941733207CC041F0C1
    }

    /**
     * 获取使用MD5摘要算法加密后的密码
     *
     * @param password 原密码
     * @param salt     盐值
     * @return 加密后的密码
     */
    private String getMd5Password(String password, String salt) {
        // 加密规则：原密码的左右两侧都拼接盐值，并循环加密5次
        String str = salt + password + salt;
        for (int i = 0; i < 5; i++) {
            str = DigestUtils.md5Hex(str).toUpperCase();
        }
        return str;
    }

}
