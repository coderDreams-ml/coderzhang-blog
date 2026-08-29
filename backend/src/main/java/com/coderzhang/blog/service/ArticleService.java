package com.coderzhang.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coderzhang.blog.common.BizException;
import com.coderzhang.blog.dto.ArticleSaveRequest;
import com.coderzhang.blog.dto.PageResult;
import com.coderzhang.blog.entity.Article;
import com.coderzhang.blog.entity.ArticleTag;
import com.coderzhang.blog.entity.Category;
import com.coderzhang.blog.entity.Tag;
import com.coderzhang.blog.mapper.ArticleMapper;
import com.coderzhang.blog.mapper.ArticleTagMapper;
import com.coderzhang.blog.mapper.CategoryMapper;
import com.coderzhang.blog.mapper.TagMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleMapper articleMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final ArticleTagMapper articleTagMapper;

    public ArticleService(ArticleMapper articleMapper, CategoryMapper categoryMapper,
                          TagMapper tagMapper, ArticleTagMapper articleTagMapper) {
        this.articleMapper = articleMapper;
        this.categoryMapper = categoryMapper;
        this.tagMapper = tagMapper;
        this.articleTagMapper = articleTagMapper;
    }

    /** 后台分页查询（含草稿），支持关键字/状态过滤 */
    public PageResult<Article> adminPage(long page, long size, String keyword, Integer status) {
        LambdaQueryWrapper<Article> qw = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isBlank()) qw.like(Article::getTitle, keyword);
        if (status != null) qw.eq(Article::getStatus, status);
        qw.orderByDesc(Article::getUpdatedAt);
        Page<Article> p = articleMapper.selectPage(new Page<>(page, size), qw);
        p.getRecords().forEach(this::fill);
        return new PageResult<>(p.getTotal(), p.getRecords());
    }

    /** 前台分页查询（仅已发布） */
    public PageResult<Article> publicPage(long page, long size, Long categoryId) {
        LambdaQueryWrapper<Article> qw = new LambdaQueryWrapper<>();
        qw.eq(Article::getStatus, 1);
        if (categoryId != null) qw.eq(Article::getCategoryId, categoryId);
        qw.orderByDesc(Article::getCreatedAt);
        Page<Article> p = articleMapper.selectPage(new Page<>(page, size), qw);
        p.getRecords().forEach(a -> {
            fill(a);
            a.setContent(null);   // 列表不返回正文，减小体积
        });
        return new PageResult<>(p.getTotal(), p.getRecords());
    }

    /** 文章详情，浏览量 +1 */
    public Article publicDetail(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null || article.getStatus() != 1) throw new BizException("文章不存在或未发布");
        articleMapper.incrViews(id);
        article.setViews(article.getViews() + 1);
        fill(article);
        return article;
    }

    @Transactional
    public Article create(ArticleSaveRequest req) {
        Article article = new Article();
        apply(article, req);
        article.setViews(0);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        articleMapper.insert(article);
        saveTags(article.getId(), req.getTags());
        return article;
    }

    @Transactional
    public void update(Long id, ArticleSaveRequest req) {
        Article article = articleMapper.selectById(id);
        if (article == null) throw new BizException("文章不存在");
        apply(article, req);
        article.setUpdatedAt(LocalDateTime.now());
        articleMapper.updateById(article);
        // 重建标签关联
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, id));
        saveTags(id, req.getTags());
    }

    @Transactional
    public void delete(Long id) {
        articleMapper.deleteById(id);
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, id));
    }

    private void apply(Article a, ArticleSaveRequest req) {
        a.setTitle(req.getTitle());
        a.setSummary(req.getSummary());
        a.setContent(req.getContent());
        a.setCover(req.getCover());
        a.setCategoryId(req.getCategoryId());
        a.setStatus(req.getStatus() == null ? 0 : req.getStatus());
    }

    private void saveTags(Long articleId, List<String> tagNames) {
        if (tagNames == null) return;
        for (String name : tagNames.stream().map(String::trim).filter(s -> !s.isEmpty()).distinct().toList()) {
            Tag tag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, name));
            if (tag == null) {
                tag = new Tag();
                tag.setName(name);
                tagMapper.insert(tag);
            }
            ArticleTag at = new ArticleTag();
            at.setArticleId(articleId);
            at.setTagId(tag.getId());
            articleTagMapper.insert(at);
        }
    }

    /** 填充分类名与标签列表 */
    private void fill(Article a) {
        if (a.getCategoryId() != null) {
            Category c = categoryMapper.selectById(a.getCategoryId());
            a.setCategoryName(c == null ? null : c.getName());
        }
        List<ArticleTag> relations = articleTagMapper.selectList(
                new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, a.getId()));
        if (!relations.isEmpty()) {
            List<Long> tagIds = relations.stream().map(ArticleTag::getTagId).collect(Collectors.toList());
            a.setTags(tagMapper.selectBatchIds(tagIds).stream().map(Tag::getName).collect(Collectors.toList()));
        }
    }
}
