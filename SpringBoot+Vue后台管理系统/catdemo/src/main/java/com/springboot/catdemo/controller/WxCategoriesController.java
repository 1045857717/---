package com.springboot.catdemo.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.catdemo.common.Constants;
import com.springboot.catdemo.common.Result;
import com.springboot.catdemo.entity.Categories;
import com.springboot.catdemo.mapper.CategoriesMapper;
import com.springboot.catdemo.service.ICategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 微信九宫格菜单 前端控制器
 * </p>
 *
 * @author CAN
 * @since 2022-07-10
 */
@RestController
@RequestMapping("/wxCategories")
public class WxCategoriesController {

    /**
     * 微信九宫格图标位置
     */
    @Value("${wx.upload.menuIcon.path}")
    private String fileUploadPath;

    @Autowired
    private ICategoriesService categoriesService;

    @Resource
    private CategoriesMapper categoriesMapper;

    /**
     * 文件上传链接
     * @param file 前端传递过来的文件
     * @return
     * @throws IOException
     */
    @PostMapping
    public Result save(@RequestParam MultipartFile file,@RequestParam String name, HttpServletRequest request) throws IOException {
        // 获取url协议(例如:http)
        String serverScheme = request.getScheme();
        // 获取url地址 https://www.jb51.net/article/71693.htm
        String serverName = request.getServerName();
        // 获取url端口
        int serverPort = request.getServerPort();
        // 生成下载文件的接口
        String url = serverScheme + "://" + serverName + ":" + serverPort;
        System.out.println(url);

        // 获取文件的名称
        String originalFilename = file.getOriginalFilename();
        // 获取文件的类型
        String type = FileUtil.extName(originalFilename);
        // 获取文件的字节(b)
        long fileSize = file.getSize();
        // 定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        // 生成的文件名
        String fileUUID = uuid + StrUtil.DOT + type;
        // 文件下载的完整路径
        String fullPath = fileUploadPath + fileUUID;
        // 创建文件信息
        File uploadFile = new File(fullPath);
        // 判断 配置的文件目录 是否存在 并生成文件夹
        File parentFile = uploadFile.getParentFile();
        if (! (parentFile.exists()) ) {
            parentFile.mkdirs();
        }
        // 生成文件的唯一MD5
        String md5 = "";
        // 上传文件到指定目录
        file.transferTo(uploadFile);
        // 上传文件后才能获取md5,通过对比md5避免重复上传相同的文件
        md5 = SecureUtil.md5(uploadFile);
        // 通过md5判断 从数据库查询 是否有相同的md5
        Categories files = getFileByMd5(md5);
        // 如果文件已经存在数据库中，则不需要再保存到磁盘目录
        if (files != null) {
            // 通过文件md5判断已存在 获取文件原本的下载接口
            url = files.getMenuIconUrl();
            // 由于文件已存在，所以删除刚才上传的重复文件
            uploadFile.delete();
        } else { // 数据库中不存在重复文件，则不删除刚才上传的文件
            // 生成下载文件的接口
            url = url + StrPool.C_SLASH + "wxCategories" + StrPool.C_SLASH + "Wxfile" + StrPool.C_SLASH + fileUUID;
        }
        // 相关信息存储到数据库
        Categories saveFile = new Categories()
                .setFileName(originalFilename)
                .setMenuName(name)
                .setType(type)
                .setSize(fileSize/1024) // KB
                .setMenuIconUrl(url)
                .setMd5(md5);
        categoriesMapper.insert(saveFile);
        return Result.success(saveFile);
    }

    /**
     * 文件下载接口  http://localhost:9090/categories/Wxfile/ + {fileUUID}
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/Wxfile/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // 设置输出流格式
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, CharsetUtil.UTF_8));
        response.setContentType("application/octet-stream");

        ServletOutputStream out = response.getOutputStream();
        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        // 读取文件所有数据
        byte[] bytes = FileUtil.readBytes(uploadFile);
        out.write(bytes);
        out.flush();
        out.close();
    }

    /**
     * 修改文件状态(启用：未启用)
     * @param categories
     * @return
     */
    @PostMapping(value = "/updateEnable")
    public Result updateEnable(@RequestBody Categories categories) {
        if (categories != null) {
            return Result.success(categoriesMapper.updateById(categories));
        } else {
            return Result.error(Constants.CODE_400, "参数错误");
        }
    }

    // 根据Id删除
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Integer id) {
        // 逻辑删除
//        Categories categories = categoriesMapper.selectById(id);
//        categories.setIsDelete(true);
//        categoriesMapper.updateById(categories);
        return Result.success(categoriesService.removeById(id));
    }

    // 根据id 批量删除
    @PostMapping(value = "/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        // 逻辑删除
//        List<Categories> categories = categoriesMapper.selectBatchIds(ids).stream()
//                .map(item -> item.setIsDelete(true))
//                .collect(Collectors.toList());
//        categoriesService.updateBatchById(categories);
        return Result.success(categoriesService.removeBatchByIds(ids));
    }

    // 查询所有数据
    @GetMapping
    public Result findAll() {
        return Result.success(categoriesService.list());
    }

// 分页查询 - mybatis-plus的方式
// @RequestParam接受 ?pageNum=1&pageSize=10
// limit (pageNum-1) * pageSize,pageSize   | 页数-1*当页总数,当页总数
    @GetMapping("/page")    // 接口路径 /user/page
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String menuName,
                                @RequestParam(defaultValue = "") String fileName) {
        IPage<Categories> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Categories> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (StrUtil.isNotBlank(menuName)) {
            queryWrapper.like("menuName", menuName);
        }
        if (StrUtil.isNotBlank(fileName)) {
            queryWrapper.like("fileName", fileName);
        }
        return Result.success(categoriesService.page(page, queryWrapper));
    }

    /**
     * 通过文件的md5查询文件
     * @param md5
     * @return
     */
    private Categories getFileByMd5(String md5) {
        QueryWrapper<Categories> qw = new QueryWrapper<>();
        qw.eq("md5", md5);
        // 通过md5判断文件是否存在
        List<Categories> filesList = categoriesMapper.selectList(qw);
        return (filesList != null && filesList.size() > 0) ? filesList.get(0) : null;
    }
}

