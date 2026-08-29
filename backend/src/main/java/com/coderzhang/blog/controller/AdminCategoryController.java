package com.coderzhang.blog.controller;

import com.coderzhang.blog.common.Result;
import com.coderzhang.blog.entity.Category;
import com.coderzhang.blog.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) { this.categoryService = categoryService; }

    @GetMapping
    public Result<List<Category>> list() { return Result.ok(categoryService.list()); }

    @PostMapping
    public Result<Category> create(@RequestBody Map<String, Object> body) {
        return Result.ok(categoryService.create((String) body.get("name"),
                body.get("sort") == null ? 0 : ((Number) body.get("sort")).intValue()));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        categoryService.update(id, (String) body.get("name"),
                body.get("sort") == null ? null : ((Number) body.get("sort")).intValue());
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.ok();
    }
}
