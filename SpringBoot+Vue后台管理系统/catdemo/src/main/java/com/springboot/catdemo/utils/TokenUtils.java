package com.springboot.catdemo.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.springboot.catdemo.common.Constants;
import com.springboot.catdemo.entity.User;
import com.springboot.catdemo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * iss: 该JWT的签发者
 * sub: 该JWT所面向的用户
 * aud: 接收该JWT的一方
 * exp(expires): 什么时候过期，这里是一个Unix时间戳
 * iat(issued at): 在什么时候签发的
 * @Author: can
 * @Description:
 * @Date: Create in 0:14 2022/3/16
 */
@Component
public class TokenUtils {

    private static IUserService staticUserService;

    @Autowired
    private IUserService userService;

    @PostConstruct // 在构造函数执行完后再执行
    public void setUserService() {
        staticUserService = userService;
    }

    /**
     * 生成Token
     * @return
     */
    public static String genToken(String userId, String sign) {
        return JWT.create()
                .withAudience(userId) // 将 UserId 保存到 token中，作为载荷
                .withIssuedAt(new Date()) // 签发时间
                .withExpiresAt(DateUtil.offsetHour(new Date(), Constants.USER_TOKEN_EXPIRE_DATE_HOUR_2)) // 设置 2小时后token过期
                .sign(Algorithm.HMAC256(sign)); // 以password 作为 token 密钥
    }

    /**
     * 获取当前登录的用户信息
     * @return user对象
     */
    public static User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes())
                    .getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserService.getById(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
