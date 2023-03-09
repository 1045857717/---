package com.springboot.catdemo.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.catdemo.common.PageResult;
import com.springboot.catdemo.common.Result;
import com.springboot.catdemo.entity.Comment;
import com.springboot.catdemo.entity.User;
import com.springboot.catdemo.service.ICommentService;
import com.springboot.catdemo.service.IUserService;
import com.springboot.catdemo.utils.TokenUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 文章评论表 前端控制器
 * </p>
 *
 * @author CAN
 * @since 2022-06-05
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @Resource
    private IUserService userService;

    // 新增或修改
    @PostMapping
    public Result save(@RequestBody Comment comment) {
        // 新增评论
        if (comment.getId() == null) {
            comment.setUserId(Objects.requireNonNull(TokenUtils.getCurrentUser(),"当前用户不存在，参数错误").getId());
            comment.setCommentTime(LocalDateTime.now());
            // 回复
            if (comment.getPid() != null) {
                Integer pid = comment.getPid();
                Comment pComment = commentService.getById(pid);
                User pUser = userService.getById(pComment.getUserId());
                comment.setReplyName(pUser.getNickname());
                if (pComment.getOriginalId() != null) {
                    comment.setOriginalId(pComment.getOriginalId());
                } else {
                    comment.setOriginalId(comment.getPid());
                }
            }
        }
        return Result.success(commentService.saveOrUpdate(comment));
    }

    // 根据Id删除
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(commentService.removeById(id));
    }

    // 根据id 批量删除
    @PostMapping(value = "/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(commentService.removeBatchByIds(ids));
    }

    // 查询所有数据
    @GetMapping
    public Result findAll() {
        return Result.success(commentService.list());
    }

    // 查询多级评论
    @GetMapping("/commentTree/{articleId}")
    public Result findCommentTree(@PathVariable(value = "articleId") Integer articleId) {
        List<Comment> articleComments = commentService.selectCommentTree(articleId);
        return Result.success(articleComments);
    }

    /**
     *  查询多级评论(分页)
     */
    @GetMapping("/commentTreePage")
    public PageResult findPageCommentTree(@RequestParam Integer pageNum,
                                          @RequestParam Integer pageSize,
                                          @RequestParam Integer articleId) {
        // 评论列表的第一个下标
        int current =  (pageNum-1) * pageSize;
        // 评论列表的最后一个下标（不包含）
        int end = current + pageSize;
        // https://blog.csdn.net/huang6chen6/article/details/121664393
        // 查询文章的所有的父级评论
        List<Comment> parentComment = commentService.selectParentComment(articleId);
        // 最后的下标大于数组下标
        if (end > parentComment.size()) {
            // 根据每页分页求余，得到多出的数量
            end = current + (parentComment.size()%pageSize);
        }
        // 将用户评论分页(截取)
        List<Comment> resultList = CollectionUtil.sub(parentComment, current, end);
        // 给用户的父级评论添加关联的子级评论
        Iterator<Comment> iterator = resultList.iterator();
        while (iterator.hasNext()) {
            Comment pComment = iterator.next();
            // 根据父级评论Id查询关联所有的子级评论，并将子级评论添加到父级评论的子级对象中去
            pComment.setChildrenComments(commentService.selectChildrenComments(pComment.getId()));
        }
        return PageResult.success(resultList,parentComment.size());
    }

    // 分页查询 - mybatis-plus的方式
// @RequestParam接受 ?pageNum=1&pageSize=10
// limit (pageNum-1) * pageSize,pageSize   | 页数-1*当页总数,当页总数
    @GetMapping("/page")    // 接口路径 /user/page
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        IPage<Comment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(commentService.page(page, queryWrapper));
    }
}

