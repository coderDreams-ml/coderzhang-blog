package com.coderzhang.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/** 社交链接 */
@Data
@TableName("social_link")
public class SocialLink {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String platform;
    private String url;
    private Integer sort;
    private Integer enabled;
}
