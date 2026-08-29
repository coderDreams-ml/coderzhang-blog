package com.coderzhang.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coderzhang.blog.entity.Category;
import com.coderzhang.blog.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryMapper categoryMapper) { this.categoryMapper = categoryMapper; }

    public List<Category> list() {
        return categoryMapper.selectList(new LambdaQueryWrapper<Category>().orderByAsc(Category::getSort));
    }

    public Category create(String name, Integer sort) {
        Category c = new Category();
        c.setName(name);
        c.setSort(sort == null ? 0 : sort);
        c.setCreatedAt(LocalDateTime.now());
        categoryMapper.insert(c);
        return c;
    }

    public void update(Long id, String name, Integer sort) {
        Category c = categoryMapper.selectById(id);
        if (c == null) throw new com.coderzhang.blog.common.BizException("分类不存在");
        if (name != null) c.setName(name);
        if (sort != null) c.setSort(sort);
        categoryMapper.updateById(c);
    }

    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }
}
