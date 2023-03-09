package com.springboot.catdemo.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.springboot.catdemo.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章评论表 Mapper 接口
 * </p>
 *
 * @author CAN
 * @since 2022-06-05
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 根据文章Id返回文章的树状评论信息
     * @param articleId
     * @return
     */
    List<Comment> selectCommentDetail(@Param("articleId") Integer articleId);

    /**
     * 根据文章Id返回文章的树状评论信息(分页)
     *
     * @param params
     * @return
     */
    List<Comment> selectPageCommentDetail(@Param("params") Map<String, Object> params);

    /**
     * 查询最上级评论
     * @param articleId
     * @return
     */
    List<Comment> selectParentCommentDetail(@Param("articleId")Integer articleId);

    /**
     * 根据最上级评论Id查询子级评论
     * @param id 最上级评论Id
     * @return 所有的子级评论
     */
    List<Comment> selectChildrenCommentsDetail(@Param("id") Integer id);
}
