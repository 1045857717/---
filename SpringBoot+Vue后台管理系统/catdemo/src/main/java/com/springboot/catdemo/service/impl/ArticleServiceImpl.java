package com.springboot.catdemo.service.impl;

import com.springboot.catdemo.entity.Article;
import com.springboot.catdemo.mapper.ArticleMapper;
import com.springboot.catdemo.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章发布 服务实现类
 * </p>
 *
 * @author CAN
 * @since 2022-05-23
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
