package com.springboot.catdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.springboot.catdemo.entity.Comment;
import com.springboot.catdemo.mapper.CommentMapper;
import com.springboot.catdemo.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章评论表 服务实现类
 * </p>
 *
 * @author CAN
 * @since 2022-06-05
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Resource
    private CommentMapper commentMapper;

    @Override
    public List<Comment> selectCommentTree(Integer articleId) {
        return commentMapper.selectCommentDetail(articleId);
    }

    @Override
    public List<Comment> selectCommentTree(HashMap<String, Object> params) {
        return commentMapper.selectPageCommentDetail(params);
    }

    @Override
    public List<Comment> selectParentComment(Integer articleId) {
        return commentMapper.selectParentCommentDetail(articleId);
    }

    @Override
    public List<Comment> selectChildrenComments(Integer id) {
        return commentMapper.selectChildrenCommentsDetail(id);
    }
}
