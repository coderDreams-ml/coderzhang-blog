package com.coderzhang.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coderzhang.blog.entity.Article;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface ArticleMapper extends BaseMapper<Article> {

    /** 浏览量 +1 */
    @Update("UPDATE article SET views = views + 1 WHERE id = #{id}")
    int incrViews(@Param("id") Long id);
}
