package com.springboot.catdemo.controller.wx;

import com.springboot.catdemo.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * 微信小程序测试接口ssserver --version
 * @Author: can
 * @Description:
 * @Date: Create in 19:39 2022/6/28
 */
@RestController
@RequestMapping("/wx")
public class WxTestController {

    @GetMapping("/get")
    public Result getInfo(@RequestParam(required = false) String param1, @RequestParam(required = false) String param2) {
        HashMap<String, String> map = new HashMap<>();
        map.put("param1", param1);
        map.put("param2", param2);
        return Result.success(map);
    }

    @PostMapping("/post")
    public Result postInfo(@RequestBody HashMap<String, String> map) {
        return Result.success(map);
    }
}
