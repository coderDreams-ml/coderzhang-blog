package com.coderzhang.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coderzhang.blog.entity.Article;
import com.coderzhang.blog.entity.Project;
import com.coderzhang.blog.mapper.ArticleMapper;
import com.coderzhang.blog.mapper.ProjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatsService {

    private final ArticleMapper articleMapper;
    private final ProjectMapper projectMapper;

    public StatsService(ArticleMapper articleMapper, ProjectMapper projectMapper) {
        this.articleMapper = articleMapper;
        this.projectMapper = projectMapper;
    }

    public Map<String, Object> stats() {
        Map<String, Object> m = new HashMap<>();
        m.put("articleTotal", articleMapper.selectCount(null));
        m.put("articlePublished", articleMapper.selectCount(
                new LambdaQueryWrapper<Article>().eq(Article::getStatus, 1)));
        m.put("projectTotal", projectMapper.selectCount(
                new LambdaQueryWrapper<Project>().eq(Project::getEnabled, 1)));
        m.put("totalViews", articleMapper.selectList(null).stream().mapToInt(a -> a.getViews() == null ? 0 : a.getViews()).sum());
        return m;
    }
}
