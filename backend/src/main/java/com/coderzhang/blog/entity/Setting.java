package com.coderzhang.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/** 站点设置，key-value */
@Data
@TableName("setting")
public class Setting {
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 列名 k（key 是 MySQL 保留字，字段名避开） */
    @TableField("k")
    private String settingKey;
    @TableField("v")
    private String settingValue;
}
