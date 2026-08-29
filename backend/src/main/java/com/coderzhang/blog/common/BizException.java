package com.coderzhang.blog.common;

/** 业务异常 */
public class BizException extends RuntimeException {
    public BizException(String message) { super(message); }
}
