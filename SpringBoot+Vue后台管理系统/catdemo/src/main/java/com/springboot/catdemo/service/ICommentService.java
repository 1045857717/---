package com.springboot.catdemo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.catdemo.entity.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章评论表 服务类
 * </p>
 *
 * @author CAN
 * @since 2022-06-05
 */
public interface ICommentService extends IService<Comment> {

    /**
     * 根据文章Id返回文章评论树
     * @param articleId 文章Id
     * @return
     */
    List<Comment> selectCommentTree(Integer articleId);

    /**
     *  查询多级评论(分页)
     */
    List<Comment> selectCommentTree(HashMap<String, Object> params);

    /**
     * 查询最上级评论
     * @param articleId 文章Id
     * @return
     */
    List<Comment> selectParentComment(Integer articleId);

    /**
     * 根据最上级评论Id查询子级评论
     * @param id 最上级评论Id
     * @return 所有的子级评论
     */
    List<Comment> selectChildrenComments(Integer id);
}
