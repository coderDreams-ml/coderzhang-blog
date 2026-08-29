package com.coderzhang.blog.controller;

import com.coderzhang.blog.common.Result;
import com.coderzhang.blog.dto.PageResult;
import com.coderzhang.blog.entity.Article;
import com.coderzhang.blog.service.ArticleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/articles")
public class PublicArticleController {

    private final ArticleService articleService;

    public PublicArticleController(ArticleService articleService) { this.articleService = articleService; }

    @GetMapping
    public Result<PageResult<Article>> page(@RequestParam(defaultValue = "1") long page,
                                            @RequestParam(defaultValue = "10") long size,
                                            @RequestParam(required = false) Long categoryId) {
        return Result.ok(articleService.publicPage(page, size, categoryId));
    }

    @GetMapping("/{id}")
    public Result<Article> detail(@PathVariable Long id) {
        return Result.ok(articleService.publicDetail(id));
    }
}
