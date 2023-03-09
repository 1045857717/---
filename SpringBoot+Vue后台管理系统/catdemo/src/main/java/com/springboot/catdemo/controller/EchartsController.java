package com.springboot.catdemo.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.springboot.catdemo.common.Result;
import com.springboot.catdemo.entity.User;
import com.springboot.catdemo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: can
 * @Description:
 * @Date: Create in 20:25 2022/3/19
 */
@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private IUserService userService;

    @GetMapping("/example")
    public Result get() {
        Map<String, Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("星期一","星期二","星期三","星期四","星期五","星期六", "星期天"));
        map.put("y", CollUtil.newArrayList(111, 222, 333, 444, 555, 666, 777));
        return Result .success(map);
    }

    @GetMapping("/members")
    public Result getMembers() {
        List<User> users = userService.list();
        int q1 = 0; // 第一季度
        int q2 = 0; // 第二季度
        int q3 = 0; // 第三季度
        int q4 = 0; // 第四季度
        for (User user : users) {
            Quarter quarter = DateUtil.quarterEnum(user.getCreateTime());
            switch (quarter) {
                case Q1: q1 += 1; break;
                case Q2: q2 += 1; break;
                case Q3: q3 += 1; break;
                case Q4: q4 += 1; break;
                default:break;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("第一季度", "第二季度", "第三季度", "第四季度"));
        map.put("y", CollUtil.newArrayList(q1, q2, q3, q4));
        return Result.success(map);
    }

}
