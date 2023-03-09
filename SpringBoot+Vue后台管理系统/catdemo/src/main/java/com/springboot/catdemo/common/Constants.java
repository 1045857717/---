package com.springboot.catdemo.common;

/**
 * @Author: can
 * @Description:
 * @Date: Create in 23:28 2022/3/12
 */
public interface Constants {

    String CODE_200 = "200"; // 成功
    String CODE_400 = "400"; // 参数错误
    String CODE_401 = "401"; // 权限不足
    String CODE_500 = "500"; // 系统错误
    String CODE_600 = "600"; // 业务错误

    // 业务代码

    /** 单位小时 用户令牌超时时间2小时*/
    int USER_TOKEN_EXPIRE_DATE_HOUR_2 = 2;

    /** 单位秒 用户状态超市时间 */
    int USER_STATUS_EXPIRE_DATE_SECONDS = 60;

    // 词典类型
    String DICT_TYPE_ICON = "icon_type";
    String DICT_SYS_INTER_URL = "sys_inter_url_type";

    // redis常量key

    // UserController.java
    String FINDALL_KEY = "FINDALL";
    String FINDPAGE_KEY = "FINDPAGE";
    /** 登录用户菜单 */
    String USERMENU_KEY = "USERMENU";
    /** 登录用户信息 */
    String USERINFO_KEY = "USERINFO";
    /** 用户在线状态 */
    String USERONLINE_KEY = "USERONLINE";

    // FileController.java
    /** 宠物视频数据缓存 */
    String FILES_KEY = "FILES_FRONT_ALL";

    /** 寻宠系统文件缓存 */
    String Cat_FILES_KEY = "Cat_FILES_ALL";
}
