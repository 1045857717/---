package com.springboot.catdemo.config.interceptor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.springboot.catdemo.common.Constants;
import com.springboot.catdemo.entity.User;
import com.springboot.catdemo.exception.ServiceException;
import com.springboot.catdemo.service.IUserService;
import com.springboot.catdemo.utils.MyRedisUtils;
import com.springboot.catdemo.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Map;

/**
 * JWT拦截器
 * @Author: can
 * @Description:
 * @Date: Create in 0:41 2022/3/16
 */
public class JwtInterceptor implements HandlerInterceptor {

    private static final Log LOG = Log.get();


    @Autowired
    private IUserService userService;

    /**
     * 方法执行之前就开始执行
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 打印请求信息
        LOG.info("------------- LogInterceptor 开始 -------------");
        LOG.info("请求地址: {} {}", request.getRequestURL().toString(), request.getMethod());
        LOG.info("远程地址: {}", request.getRemoteAddr());
        LOG.info("请求参数: {}", request.getQueryString());
        long startTime = System.currentTimeMillis();
        request.setAttribute("requestStartTime", startTime);

        // 如果不是映射的方法则 直接通过
        if ( !(handler instanceof HandlerMethod) ) {
            return true;
        }
        // 执行认证
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            throw new ServiceException(Constants.CODE_401, "无token，请重新登录");
        }
        // 获取token 中的 user id
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException e) {
            throw new ServiceException(Constants.CODE_401, "token验证失败");
        }
        // 通过token中的userId查询用户是否存在
        User user = userService.getById(userId);
        if (user == null) {
            throw new ServiceException(Constants.CODE_401, "用户不存在，请重新登录");
        }
        // 用户密码加签 验证token
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
            jwtVerifier.verify(token); // 验证token
        } catch (JWTVerificationException e) {
            throw new ServiceException(Constants.CODE_401, "token验证错误，请重新登录");
        }
        // 将盐值和密码清空.
        user.setSalt(null).setPassword(null);
        // 用户只要有操作便存入redis记录在线人数(60秒过期),超时就算不在线 (活跃人数统计)
        MyRedisUtils.zsAdd(Constants.USERONLINE_KEY, user.getId(),Double.parseDouble( DateUtil.current() + ""), null);
        // 只有返回true才会继续向下执行，返回false取消当前请求
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (Long) request.getAttribute("requestStartTime");
        LOG.info("------------- LogInterceptor 结束 耗时：{} ms -------------", System.currentTimeMillis() - startTime);
    }
}
