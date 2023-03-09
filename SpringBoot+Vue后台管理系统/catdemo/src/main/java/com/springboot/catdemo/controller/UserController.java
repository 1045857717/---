package com.springboot.catdemo.controller;


import cloud.tianai.captcha.generator.common.model.dto.ImageCaptchaInfo;
import cloud.tianai.captcha.validator.ImageCaptchaValidator;
import cloud.tianai.captcha.validator.impl.BasicCaptchaTrackValidator;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.catdemo.common.Constants;
import com.springboot.catdemo.common.Result;
import com.springboot.catdemo.controller.dto.UserDTO;
import com.springboot.catdemo.entity.POIExcelDao;
import com.springboot.catdemo.entity.User;
import com.springboot.catdemo.exception.ServiceException;
import com.springboot.catdemo.service.IUserService;
import com.springboot.catdemo.utils.ExcelPoiUtil;
import com.springboot.catdemo.utils.MyRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author CAN
 * @since 2022-03-06
 */
@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {

    @Value("${files.upload.avatar.path}")
    private String uploadAvatarPath;

    @Autowired
    private IUserService userService;

    /* https://hutool.cn/docs/#/ */
    private static final Log LOG = Log.get();

    /**
     * 用户登录
     *
     * @param uuid        验证码UUID
     * @param movePercent 验证码用户滑动的百分比
     * @param userDTO
     * @return
     */
    @PostMapping(value = "/login/{uuid}/{movePercent}")
    public Result login(@PathVariable String uuid, @PathVariable Float movePercent, @RequestBody UserDTO userDTO) {
        boolean valid = this.validCaptcha(uuid, movePercent);
        if (!valid) {
            return Result.error(Constants.CODE_600, "验证码错误");
        }
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        UserDTO data = userService.login(userDTO);
        return Result.success(data);
    }

    /**
     * 用户注册
     *
     * @param userDTO
     * @return
     */
    @PostMapping(value = "/register")

    public Result register(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return Result.success(userService.register(userDTO));
    }

    /**
     * 用户是否存在
     *
     * @param username 用户名
     * @return 不存在返回 200:username,存在返回400：参数错误
     */
    @PostMapping(value = "/register/username/{username}")
    public Result existUsername(@PathVariable String username) {
        if (StrUtil.isEmptyIfStr(username)) {
            return Result.error(Constants.CODE_400, "参数错误");
        } else {
            if (userService.isExistUsername(username)) {
                // 存在则返回code:500,msg:用户已存在
                return Result.error(Constants.CODE_500, "用户名已存在");
            } else {
                // 不存在则返回code:200,msg:username
                return Result.success(username);
            }
        }
    }

    /**
     * 新增用户或修改用户
     *
     * @param user
     * @return
     */
    @PostMapping
    public Result save(@RequestBody User user) {
        if (null == user.getPassword()) {
            userService.saveOrUpdate(user);
        } else {
            User resultUser = userService.getMd5PasswordUser(user);
            userService.saveOrUpdate(resultUser);
        }
        // 删除查找方法的缓存
        MyRedisUtils.deleteKeys(Constants.FINDPAGE_KEY, Constants.FINDALL_KEY);
        return Result.success();
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
//    @CacheEvict(value = "delete", key = "#id") // 清楚一条缓存 key为要更新的数据
    public Result delete(@PathVariable Integer id) {
        boolean result = userService.removeById(id);
        if (result) {
            // 删除查找方法的缓存
            MyRedisUtils.deleteKeys(Constants.FINDPAGE_KEY, Constants.FINDALL_KEY);
        }
        return Result.success();
    }

    /**
     * 批量删除用户
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/del/batch")
//    @CacheEvict(value = "deleteBatch", allEntries = true) // 清空所有缓存
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        boolean result = userService.removeBatchByIds(ids);
        if (result) {
            // 删除查找方法的缓存
            MyRedisUtils.deleteKeys(Constants.FINDPAGE_KEY, Constants.FINDALL_KEY);
        }
        return Result.success();
    }

    /**
     * 修改用户密码
     * @param oldPass 原密码
     * @param confirmNewPass 新密码
     * @return
     */
    @PostMapping(value = "/updatePassword/{oldPass}/{confirmNewPass}")
    public Result UpdatePassword(@PathVariable String oldPass,@PathVariable String confirmNewPass) {
        if (StrUtil.isBlank(oldPass) || StrUtil.isBlank(confirmNewPass)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return userService.updatePasswordByoldPass(oldPass, confirmNewPass) ? Result.success() : Result.error(Constants.CODE_600, "更新错误请重试！");
    }

    /**
     * 查询所有用户信息
     * @return
     */
    @GetMapping
    public Result findAll() {
        String jsonStr = (String) MyRedisUtils.getStringCache(Constants.FINDALL_KEY);
        List<User> userList;
        // 如果为空则查询数据库数据，并将数据存入缓存 用Hutool工具类
        if (StrUtil.isBlank(jsonStr)) {
            userList = userService.list();
            // 将数据存入缓存
            MyRedisUtils.setStringCache(Constants.FINDALL_KEY, JSONUtil.toJsonStr(userList));
        } else {
            // 如果缓存在则从缓存中取出数据
            userList = JSONUtil.toList(jsonStr, User.class);
        }
        return Result.success(userList);
    }

    /**
     * 通过用户名查询用户信息
     *
     * @return
     */
    @GetMapping("/getUserInfoByUsername/{username}")
    public Result getUserInfoByUsername(@PathVariable(value = "username") String username) {
        QueryWrapper<User> userQuery = new QueryWrapper<>();
        userQuery.eq("username", username);
        try {
            User user = userService.getOne(userQuery);
            user.setPassword(null);
            return Result.success(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error(Constants.CODE_500, "系统错误");
    }

    // 分页查询 - mybatis-plus的方式
// @RequestParam接受 ?pageNum=1&pageSize=10
// limit (pageNum-1) * pageSize,pageSize   | 页数-1*当页总数,当页总数
    @GetMapping(value = "/page")    // 接口路径 /user/page
//    @Cacheable(value = "findPage", key = "#pageNum + '-' + #pageSize")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") String address) {
        // 用hutool日志打印执行方法类+方法名称+开始时间
        LOG.info("start Class: {}, start MethodName: {}, start time: {}",
                this.getClass().getName(), "findPage", DateUtil.now());
        IPage<User> resultPage;
        IPage<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (username != null && !"".equals(username))
            queryWrapper.like("username", username);
        if (username != null && !"".equals(email))
            queryWrapper.like("email", email);
        if (username != null && !"".equals(address))
            queryWrapper.like("address", address);
        queryWrapper.orderByDesc("id");
        resultPage = userService.page(page, queryWrapper);
        resultPage.getRecords().forEach(user -> user.setPassword(null));
        // 用hutool日志打印类名+方法名+结果集
        LOG.info("end Class: {}, end MethodName: {}, resultPage: {}", this.getClass().getName(), "findPage", resultPage.getRecords().toString());
        // 用hutool日志打印执行方法类+方法名称+结束时间
        LOG.info("end Class: {}, end MethodName: {}, end time: {}", this.getClass().getName(), "findPage", DateUtil.now());
        return Result.success(resultPage);
    }

    /**
     * POI导出EXCEL
     *
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询所有用户数据
        List<User> list = userService.list();
        // 原标题:别名
        HashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("id", "ID");
        titleMap.put("username", "用户名");
        titleMap.put("nickname", "昵称");
        titleMap.put("email", "邮箱");
        titleMap.put("phone", "电话");
        titleMap.put("address", "地址");
        titleMap.put("createTime", "创建时间");
        titleMap.put("avatarUrl", "头像");
        ExcelPoiUtil.excelExport("用户信息表", list, titleMap, response);
    }

    /**
     * POI导入 保存和修改
     *
     * @param file
     * @throws Exception
     */
    @PostMapping(value = "/import")
    public Result importExcel(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        // 原标题:别名
        HashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("用户名", "username");
        titleMap.put("昵称", "nickname");
        titleMap.put("邮箱", "email");
        titleMap.put("电话", "phone");
        titleMap.put("地址", "address");
        titleMap.put("创建时间", "createTime");
        titleMap.put("头像", "avatarUrl");
        // 返回list对象
        POIExcelDao<User> userDao = ExcelPoiUtil.importExcel(inputStream, titleMap, User.class);
        List<User> users = userDao.getList();
        // 更新操作
        if (userDao.isUpdate()) {
            return Result.success(userService.updateBatchById(users));
        }
        users.stream().forEach(System.out::println);
        return Result.success(userService.saveBatch(users));// 保存入库
    }

    /**
     * 用户个人信息头像上传
     *
     * @param file 前端传递过来的文件
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadImage")
    public Result uploadImage(@RequestParam MultipartFile file, @RequestParam String username) throws IOException {
        // 获取文件的名称
        String originalFilename = file.getOriginalFilename();
        // 获取文件的类型
        String type = FileUtil.extName(originalFilename);
        // 获取文件的字节(b)
        long fileSize = file.getSize();
        // 定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        // 生成的文件名
        String fileName = uuid + StrUtil.DOT + type;
        // 生成用户头像url( 用户名/文件名 )
        String userAvatarUrl = username + StrUtil.SLASH + fileName;
        // 文件下载的完整路径( xxxxxxx/用户名/文件名 )
        String fullPath = uploadAvatarPath + userAvatarUrl;
        // 创建文件信息
        File uploadFile = new File(fullPath);
        // 判断 配置的文件目录 是否存在 并生成文件夹
        File parentFile = uploadFile.getParentFile();
        if (!(parentFile.exists())) {
            parentFile.mkdirs();
        }
        // 上传头像到指定目录
        file.transferTo(uploadFile);
        // 数据库更新用户头像url
        boolean result = userService.updateAvatarUrlByUsername(fileName, username);
        if (result) {
            return Result.success(userAvatarUrl);
        } else {
            return Result.error();
        }
    }

    /**
     * 通过用户名 获取获取个人头像
     *
     * @param username 用户名
     * @param filename 文件名
     * @param response
     * @throws IOException
     */
    @GetMapping("/downloadImage/{username}/{filename}")
    public void downloadImage(@PathVariable String username, @PathVariable String filename, HttpServletResponse response) throws IOException {

        // 设置输出流格式
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, CharsetUtil.UTF_8));

        response.setContentType("application/octet-stream");

        ServletOutputStream out = response.getOutputStream();
        // 生成用户头像url
        String userAvatarUrl = username + StrUtil.SLASH + filename;
        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(uploadAvatarPath + userAvatarUrl);
        // 读取文件所有数据
        byte[] bytes = FileUtil.readBytes(uploadFile);
        out.write(bytes);
        out.flush();
        out.close();
    }

    /**
     * 通过用户角色唯一标识获取菜单列表
     *
     * @param roleKey 角色唯一标识
     * @return
     */
    @GetMapping(value = "/getRoleMenusByRoleKey/{roleKey}")
    public Result getRoleMenusByRoleKey(@PathVariable(value = "roleKey") String roleKey) {
        return Result.success(userService.getMenusByRoleKey(roleKey));
    }

    /**
     * 校验验证码
     *
     * @param uuid        验证码UUID
     * @param movePercent 用户滑动的百分比
     * @return
     */
    private boolean validCaptcha(String uuid, Float movePercent) {
        String jsonStr = (String) MyRedisUtils.getStringCache(uuid);
        // 清除当前校验的拼图验证
        MyRedisUtils.deleteKey(uuid);
        if (jsonStr == null) {
            throw new ServiceException(Constants.CODE_500, "验证码失效，请重试！");
        }
        ImageCaptchaInfo imageCaptchaInfo = JSONUtil.toBean(jsonStr, ImageCaptchaInfo.class);
        // 负责计算一些数据存到缓存中，用于校验使用
        // ImageCaptchaValidator负责校验用户滑动滑块是否正确和生成滑块的一些校验数据; 比如滑块到凹槽的百分比值
        // 2.ImageCaptchaValidator校验器 验证
        ImageCaptchaValidator imageCaptchaValidator = new BasicCaptchaTrackValidator();
        // 这个map数据应该存到缓存中，校验的时候需要用到该数据
        Map<String, Object> map = imageCaptchaValidator.generateImageCaptchaValidData(imageCaptchaInfo);
//        ImageCaptchaTrack imageCaptchaTrack = null;
        Float percentage = (Float) map.get("percentage");
        // 用户传来的行为轨迹和进行校验
        // - imageCaptchaTrack为前端传来的滑动轨迹数据
        // - map 为生成验证码时缓存的map数据
//        boolean check = imageCaptchaValidator.valid(imageCaptchaTrack, map);
        // 如果只想校验用户是否滑到指定凹槽即可，也可以使用
        // - 参数1 用户传来的百分比数据
        // - 参数2 生成滑块是真实的百分比数据
        boolean check = imageCaptchaValidator.checkPercentage(movePercent, percentage);
        if (!check) {
            throw new ServiceException(Constants.CODE_600, "验证码错误，请重试！");
        }
        return true;
    }

    /**
     * 用户心跳检测活跃用户在线，并返回当前活跃用户数
     *
     * @param id 用户Id
     * @return 活跃用户数量
     */
    @GetMapping(value = "/getOnlineCount/{id}")
    public Result getRealOnlineCount(@PathVariable String id) {
        long beforeOnlineCount = MyRedisUtils.zsSize(Constants.USERONLINE_KEY);
        LOG.info("时间：{}, Before心跳检测当前活跃用户数:{}", DateUtil.now(), beforeOnlineCount);
        // 更新用户心跳
        MyRedisUtils.zsAdd(Constants.USERONLINE_KEY, Integer.valueOf(id), Double.parseDouble(DateUtil.current() + ""), null);
        // 每次心跳检测非活跃用户，并删掉不活跃的用户
        long current = DateUtil.current();
        long expireCount = MyRedisUtils.zsRemoveRangeByScore(
                Constants.USERONLINE_KEY,
                0,
                Double.parseDouble(current - (60 * 4 * 1000) + "")); // 4分钟没更新心跳的移除用户
        LOG.info("时间：{}, 淘汰用户数：{}", DateUtil.now(), expireCount);

        long afterOnlineCount = MyRedisUtils.zsSize(Constants.USERONLINE_KEY);

        LOG.info("时间：{}, After心跳检测当前活跃用户数:{}", DateUtil.now(), afterOnlineCount);
        return Result.success(afterOnlineCount);
    }

    /**
     * 查询用户总数
     *
     * @return
     */
    @GetMapping(value = "/getUserCount")
    public Result getUserCount() {
        long count = userService.count();
        return Result.success(count);
    }
}

