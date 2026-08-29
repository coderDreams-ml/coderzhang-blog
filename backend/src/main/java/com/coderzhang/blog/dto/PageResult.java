package com.coderzhang.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

/** 统一分页结果 */
@Data
@AllArgsConstructor
public class PageResult<T> {
    private long total;
    private List<T> list;
}
