package com.coderzhang.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

/** 后台保存文章的请求体 */
@Data
public class ArticleSaveRequest {
    @NotBlank(message = "标题不能为空")
    private String title;
    private String summary;
    private String content;
    private String cover;
    private Long categoryId;
    /** 0=草稿 1=发布 */
    private Integer status;
    private List<String> tags;
}
