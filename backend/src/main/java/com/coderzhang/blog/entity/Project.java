package com.coderzhang.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/** 首页展示的项目作品 */
@Data
@TableName("project")
public class Project {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String url;
    /** 分组名，如「AI 探索」「效率工具」 */
    private String groupName;
    private Integer sort;
    private Integer enabled;
    private LocalDateTime createdAt;
}
